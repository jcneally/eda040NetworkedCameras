package server;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

/**
 * 
 * @author Viktor Andersson
 *	Receives input stream.
 */

public class Receive extends Thread{
		
	private Socket clientSocket;
	private ServerMonitor serverMonitor;
	private InputStream is;
	private byte[] message;
	private int len;
	
	public Receive(ServerMonitor serverMonitor) {
		this.serverMonitor = serverMonitor;
		len = 10;	// TODO: change to something appropriately
		message = new byte[10];	// TODO: Change size!!!!
	}
	
	public void run() {

		
		while(true) {
			
			// Wait for client to accept.
			clientSocket = serverMonitor.waitForClientSocket();
			System.out.println("Receive: clientSocket = " + serverMonitor.getClientSocket());
			
			// Get input stream.
			try {
				is = clientSocket.getInputStream();
			} catch (IOException e) {
				System.out.println("Receive: Failed to get input stream. Get new connection.");
				continue;
			} 
			
			// Read
			try {
				is.read(message, 0, len);
			} catch (IOException e) {
				System.out.println("Receive: Failed to get input stream. Get new connection.");
				continue;
			}
			
			System.out.print("Receive: read ");			
			for (int i = 0; i < 10; i++) {
				System.out.print(message[i] + " ");
			}
			System.out.println();
			
			handleMessage();
		}	
		
	}
	
	private void handleMessage() {
		// TODO: Implement message handling.
	}
}
