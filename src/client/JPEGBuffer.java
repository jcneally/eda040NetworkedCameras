package client;

import se.lth.cs.fakecamera.Axis211A;
import se.lth.cs.realtime.RTError;

class JPEGBuffer {
	//Prove push
	///////////////////////////////////////////////////////////////////////////////////
	//	This is a monitor which implements a synchronized image buffer to ensure     //
	//	concurrency and provide safe and full images to the other threads. It has    //
	//	also a method to skip images in case are not relevant (due to a high delay)  //
	///////////////////////////////////////////////////////////////////////////////////
	
	int available = 0;		// Number of lines that are available. 
	final int size=24;		// The max number of buffered lines.
	final int jpegsize = Axis211A.IMAGE_BUFFER_SIZE;
	byte[][] buffData;	// The actual buffer.
	int [] buffDelay;
	int nextToPut = 0;		// Writers index.
	int nextToGet = 0;		// Readers index.
	long offset = 0l;
	int images = 0;


	public JPEGBuffer() {
		buffData = new byte[size][jpegsize];
		buffDelay = new int[size];
	}

	synchronized void putJPEG(byte[] data) {
		try {
			while (available==size) wait();
		} catch (InterruptedException exc) {
			throw new RTError("Buffer.putJPEG interrupted: "+exc);
		};
		buffData[nextToPut] = data;
		long time = 1000L*(((data[25]<0?256+data[25]:data[25])<<24)+((data[26]<0?256+data[26]:data[26])<<16)+((data[27]<0?256+data[27]:data[27])<<8)+(data[28]<0?256+data[28]:data[28]))+10L*(data[29]<0?256+data[29]:data[29]);
		buffDelay[nextToPut]= (int) (System.currentTimeMillis()-time);
		buffData[nextToPut] = data;
		if (++nextToPut == size) nextToPut = 0;
		available++;
		notifyAll(); // Only notify() could wake up another producer.
	}

	synchronized byte[] getJPEG() {
		try {
			while(available == 0) wait();
		} catch (InterruptedException e) {
			throw new RTError("Buffer.getJPEG interrupted:" + e);
		}
		byte [] ans = buffData[nextToGet];
		buffData[nextToGet] = null;
		buffDelay[nextToGet] = -1;
		if(++nextToGet == size) nextToGet = 0;
		available--;
		images++;
		notifyAll();
		return ans;
	}
	

	synchronized int getNumOfImg(){
		int ans = images;
		images = 0;
		return ans;
	}
	
	synchronized int getDelay(){
		//return the delay of the current image of 'camera'
		
		try {
			while(available == 0) wait();
		} catch (InterruptedException e) {
			throw new RTError("Buffer.getDelay interrupted:" + e);
		}
		
		notifyAll();
		return (buffDelay[nextToGet]);
	}

}