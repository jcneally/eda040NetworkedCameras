package client;

import se.lth.cs.fakecamera.Axis211A;
import se.lth.cs.realtime.RTError;

class JPEGBuffer {
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
}
