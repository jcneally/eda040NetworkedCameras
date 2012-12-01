package server;

import java.io.IOException;
import java.io.OutputStream;
import java.net.*;

public class ServerMonitor {
	private int mode;
	private Socket clientSocket = null;
	boolean connected = false;
	
	final static int IDLE_MODE = 0;
	final static int MOVIE_MODE = 1;
	final static int AUTO_MODE = 2;
	
	public ServerMonitor() {
		mode = IDLE_MODE;	
	}
	
	
	public synchronized int getMode() {
		return mode;
	}
	
	public synchronized void setMode(int mode) {
		this.mode = mode;
	}
	
	/**
	 * Gets outputstream and then writes the jpeg byte array to it.
	 */
	public synchronized boolean send(OutputStream os, byte[] jpeg, int len) {	
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
			return true;
		} catch (IOException e) {
		    // This means they stopped the connection.
		    connected = false;
			//System.out.println("Server: Failed to write");
            //e.printStackTrace();
			return false;
		}        
	}

	public synchronized boolean sendCommand(OutputStream os) {
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
			return true;
		} catch (IOException e) {
			System.out.println("Server: Failed to write");
			//e.printStackTrace();
			return false;
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
