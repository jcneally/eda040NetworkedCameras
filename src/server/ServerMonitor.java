package server;

import se.lth.cs.fakecamera.Axis211A;

import java.io.IOException;
import java.io.OutputStream;
import java.net.*;

/**
 * 
 * @author Viktor Andersson
 *
 * Monitor for images. (and requests?)
 */

public class ServerMonitor {
	byte [] jpeg = new byte[Axis211A.IMAGE_BUFFER_SIZE];
	int mode;
	Socket clientSocket = null;
	boolean commandSent = true;
	
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
		commandSent = false;
		notifyAll();
	}
	
	public synchronized void sendCommand(OutputStream os) {
		while(!commandSent) {
			byte one = (byte) 1;
								
			try {
				os.write(one);
				os.write(mode);
				os.flush();			
				
			} catch (IOException e) {
				System.out.println("Server: Failed to write");
				e.printStackTrace();
			}
		}
	}
	
	public synchronized Socket getClientSocket() {
		return clientSocket;
	}
	
	public synchronized void setClientSocket(Socket clientSocket) {
		this.clientSocket = clientSocket;
		notifyAll();
	}
	
	public synchronized Socket waitForClientSocket() {
		while(clientSocket == null) {
			try {
				wait();
			} catch (InterruptedException e) {
				System.out.println("Receive: Faild waiting for socket.");
				e.printStackTrace();
			}
		}
		
		return clientSocket;
	}
}
