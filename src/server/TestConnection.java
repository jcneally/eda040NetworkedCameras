package server;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.net.UnknownHostException;


public class TestConnection extends Thread{
	Socket socket;
	InputStream is;
	int port;
	
	public TestConnection(int port) {
		// TODO Auto-generated constructor stub
		this.port = port;
	}
	
	public void run() {
		//clientconnect--------------------------------------------------
		
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				try {
					socket  = new Socket("localhost", port);
					System.out.println("Client: socket created");
					is = socket.getInputStream();
					System.out.println("Client: inputstream got");
				} catch (UnknownHostException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					System.out.println("Client: Catched unknown host exc..");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					System.out.println("Client: cached IOException..");
				}
				
				// end of clentConnect-----------------------------------
	}
	
	public void closeSocket() {
		try {
			socket.close();
		} catch (IOException e) {
			// TODO: handle exception
			System.out.println("Client: Failed to close socket");
			e.printStackTrace();
		}
	}
}
