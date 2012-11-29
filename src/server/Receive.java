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
	private byte[] receivedMode;

	public Receive(ServerMonitor serverMonitor) {
		this.serverMonitor = serverMonitor;
		receivedMode = new byte[1];
	}

	public void run() {


		while(true) {

			// Wait for client to accept.
			clientSocket = serverMonitor.waitForClientSocket();
			//			System.out.println("Receive: clientSocket = " + serverMonitor.getClientSocket());

			// Get input stream.
			try {
				is = clientSocket.getInputStream();
			} catch (IOException e) {
				System.out.println("Receive: Failed to get input stream. Get new connection.");
				continue;
			} 

			// Read
			while(clientSocket.isConnected()){
				try {
					if(1 == is.read(receivedMode,0,1)) {
						serverMonitor.setMode(receivedMode[0]);
						System.out.println("Receive: read mode " + receivedMode[0]);	
					}				
				} catch (IOException e) {
					System.out.println("Receive: Failed to get input stream. Get new connection.");
					continue;
				}
			}

		}	

	}


}
