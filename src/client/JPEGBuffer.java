package client;

import se.lth.cs.fakecamera.Axis211A;
import se.lth.cs.realtime.RTError;

class JPEGBuffer {
	
	///////////////////////////////////////////////////////////////////////////////////
	//	This is a monitor which implements a synchronized image buffer to ensure     //
	//	concurrency and provide safe and full images to the other threads. It has    //
	//	also a method to skip images in case are not relevant (due to a high delay)  //
	///////////////////////////////////////////////////////////////////////////////////
	
	int available;		// Number of lines that are available. 
	final int size=10;		// The max number of buffered lines.
	final int jpegsize = Axis211A.IMAGE_BUFFER_SIZE;
	byte[][] buffData;	// The actual buffer.
	int nextToPut;		// Writers index.
	int nextToGet;		// Readers index.
	long offset = 0l;

	public JPEGBuffer() {
		buffData = new byte[size][jpegsize];
	}

	synchronized void putJPEG(byte[] inp) {
		try {
			while (available==size) wait();
		} catch (InterruptedException exc) {
			throw new RTError("Buffer.putJPEG interrupted: "+exc);
		};
		buffData[nextToPut] = inp;
		if (++nextToPut >= size) nextToPut = 0;
		available++;
		notifyAll(); // Only notify() could wake up another producer.
	}

	synchronized byte[] getJPEG() {
		try {
			while(available == 0) wait();
		} catch (InterruptedException e) {
			throw new RTError("Buffer.getJPEG interrupted:" + e);
		}
		byte[] ans = buffData[nextToGet];
		buffData[nextToGet] = null;
		if(++nextToGet >= size) nextToGet = 0;
		available--;
		notifyAll();
		return ans;
	}
	
	synchronized long getCurrentDelay(){
		//return the delay of the current image of 'camera'
		try {
			while(available == 0) wait();
		} catch (InterruptedException e) {
			throw new RTError("Buffer.getCurrentDelay interrupted:" + e);
		}
		byte[] data = buffData[nextToGet];
		notifyAll();
		long time = 1000L*(((data[25]<0?256+data[25]:data[25])<<24)+((data[26]<0?256+data[26]:data[26])<<16)+((data[27]<0?256+data[27]:data[27])<<8)+(data[28]<0?256+data[28]:data[28]))+10L*(data[29]<0?256+data[29]:data[29]);
		return  (System.currentTimeMillis()-time-offset);
	}
	
	synchronized void skipJPEG(){
		//Erase unused images due to its high delay and jumps to the next read position
		try {
			while(available == 0) wait();
		} catch (InterruptedException e) {
			throw new RTError("Buffer.skipJPEG interrupted:" + e);
		}
		buffData[nextToGet] = null;
		if(++nextToGet >= size) nextToGet = 0;
		available--;
		notifyAll();
	}
}