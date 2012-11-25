package server;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

import se.lth.cs.fakecamera.Axis211A;


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
		len = 15;	// TODO: change to something appropriately
		message = new byte[15];	// TODO: Change size!!!!
	}
	
	public void run() {

		// Gets IllegalMonitorException if sleep less than 3 sec??? If no connection -> exception?
		try {
			sleep(3000);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		int test = 0;
		while(true) {
			test++;
			// Wait for client to accept.
			while((clientSocket = serverMonitor.getClientSocket()) == null) {
				try {
					wait();
				} catch (InterruptedException e) {
					System.out.println("Receive: Faild waiting for socket.");
					e.printStackTrace();
				}
			}
			
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
		}	
		
	}
	
	
}
