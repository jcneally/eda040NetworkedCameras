package server;



import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import se.lth.cs.fakecamera.Axis211A;


public class TestConnection extends Thread{
	Socket socket;
	InputStream is;
	int port;

	public TestConnection(int port) {
		this.port = port;
	}

	public void run() {
		//clientconnect--------------------------------------------------

		byte[] jpeg = new byte[Axis211A.IMAGE_BUFFER_SIZE];
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		try {
			socket  = new Socket("localhost", port);
			System.out.println("Client: socket created");
			is = socket.getInputStream();
			System.out.println("Client: inputstream got");
		} catch (UnknownHostException e) {
			e.printStackTrace();
			System.out.println("Client: Catched unknown host exc..");
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Client: cached IOException..");
		}

		while(socket.isConnected()) {
			try {
				is.read(jpeg, 0, 15);
//				System.out.print("Client read: ");
				for(int i = 0; i < 15 ; i++) {
//					System.out.print(jpeg[i] + " ");
				}
//				System.out.println();

			} catch (IOException e) {
				System.out.println("Client: Failed to read bytes");
				//e.printStackTrace();
			}
		}

		// end of clentConnect-----------------------------------
	}

	public void closeSocket() {
		try {
			socket.close();
		} catch (IOException e) {
			System.out.println("Client: Failed to close socket");
			e.printStackTrace();
		}
	}
}
