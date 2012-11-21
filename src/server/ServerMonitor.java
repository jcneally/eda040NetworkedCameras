package server;

import se.lth.cs.fakecamera.Axis211A;

/**
 * 
 * @author Viktor Andersson
 *
 * Monitor for images. (and requests?)
 */

public class ServerMonitor {
	byte [] jpeg = new byte[Axis211A.IMAGE_BUFFER_SIZE];
	int mode;
	
	final static int IDLE_MODE = 0;
	final static int MOVIE_MODE = 1;
	final static int AUTO_MODE = 2;
	
	public ServerMonitor() {
		mode = IDLE_MODE;
	}
	
	public synchronized byte[] getImage() {
		return jpeg;
	}
	
	public synchronized void setImage(byte[] jpeg) {
		for(int i = 0; i < Axis211A.IMAGE_BUFFER_SIZE; i++) {
			this.jpeg[i] = jpeg[i];
		}
	}
	
	public synchronized int getMode() {
		return mode;
	}
	
	public synchronized void setMode(int mode) {
		this.mode = mode;
	}
}
