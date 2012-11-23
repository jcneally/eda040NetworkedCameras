package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;


/**
 * 
 * @author Viktor Andersson
 *	Receives input stream.
 */

public class Receive extends Thread{
		
	private Socket clientSocket;
	
	public Receive() {
		
	}
	
	public void run() {
		while(clientSocket == null) {
			try {
				wait();
			} catch (InterruptedException e) {
				System.out.println("Recive: Faild waiting for socket.");
				e.printStackTrace();
			}
		}
	}
	
	public void setSocket(Socket socket) {
		clientSocket = socket;
	}
	
	
}
