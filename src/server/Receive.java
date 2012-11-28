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
	private int receivedMode;
	
	public Receive(ServerMonitor serverMonitor) {
		this.serverMonitor = serverMonitor;
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
				serverMonitor.setMode(is.read());
			} catch (IOException e) {
				System.out.println("Receive: Failed to get input stream. Get new connection.");
				continue;
			}
						
		}	
		
	}
	
	
}
