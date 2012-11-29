package server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import se.lth.cs.fakecamera.Axis211A;


public class TestConnection extends Thread{
	Socket socket;
	InputStream is;
	OutputStream os;
	int port;

	public TestConnection(int port) {
		this.port = port;
	}

	public void run() {
		//clientconnect--------------------------------------------------

		try {
			socket  = new Socket("localhost", port);
			System.out.println("Client: socket created");
			is = socket.getInputStream();
			System.out.println("Client: inputstream got");
			os = socket.getOutputStream();
			System.out.println("Client: outputstream got");
		} catch (UnknownHostException e) {
			e.printStackTrace();
			System.out.println("Client: Catched unknown host exc..");
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Client: cached IOException..");
		}

		while(socket.isConnected()) {
			readIs();
			
			//writeOs();
		}

		// end of clentConnect-----------------------------------
	}

	private void readIs() {
		
		byte[] message = new byte[Axis211A.IMAGE_BUFFER_SIZE];
		
		try {
			is.read(message, 0, 1);
			if(message[1] != 0) {
				System.out.print("Client read: ");
				System.out.print(message[1]);
				System.out.println();
			}
		} catch (IOException e) {
			System.out.println("Client: Failed to read bytes");
			//e.printStackTrace();
		}
	}

	private void writeOs() {
		int message = 0;
		for (int i = 0; i < 3; i++) {
			message = (byte) i;

			try {
				os.write(message);
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				sleep(10);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
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
