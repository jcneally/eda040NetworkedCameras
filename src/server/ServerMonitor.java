package server;

import se.lth.cs.fakecamera.Axis211A;
import java.io.IOException;
import java.io.OutputStream;
import java.net.*;

public class ServerMonitor {
	private byte [] jpeg = new byte[Axis211A.IMAGE_BUFFER_SIZE];
	private int mode;
	private Socket clientSocket = null;
	private boolean commandSent = true;
	
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
	
	/**
	 * Gets outputstream and then writes the jpeg byte array to it.
	 */
	public synchronized void send(OutputStream os, byte[] jpeg, int len) {
		// write        
		try {
			// Create header
			byte hi = (byte)(len / 255);
			byte lo = (byte)(len % 255);

			// Send header
			os.write(hi);
			os.write(lo);

			byte zero = (byte) 0;

			os.write(zero);
			os.write(jpeg,0,len);
			os.flush();
		} catch (IOException e) {
			System.out.println("Server: Failed to write");
			e.printStackTrace();
		}        
	}
	
	public synchronized void sendCommand(OutputStream os) {
		while(!commandSent) {

			try {

				// Create header
				byte hi = (byte)(1 / 255);
				byte lo = (byte)(1 % 255);

				// Send header
				os.write(hi);
				os.write(lo);

				byte one = (byte) 1;


				os.write(one);
				os.write((byte) mode);
				os.flush();			

			} catch (IOException e) {
				System.out.println("Server: Failed to write");
				e.printStackTrace();
			}
			commandSent = true;
			notifyAll();
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
