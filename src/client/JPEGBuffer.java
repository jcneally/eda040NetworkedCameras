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
	
	synchronized void skipJPEG(){
		//Erase unused images due to its high delay and jumps to the next read position
		buffData[nextToGet] = null;
		if(++nextToGet >= size) nextToGet = 0;
		available--;
		notifyAll();
	}
}
