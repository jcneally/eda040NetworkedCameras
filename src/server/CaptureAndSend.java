import java.net.*;
import java.io.*;

import se.lth.cs.fakecamera.Axis211A;
import se.lth.cs.fakecamera.MotionDetector;

/**
 * 
 * @author Viktor Andersson
 *
 * Gets as many images from the camera as possible. 
 * Sends some or all of them depending on mode. 
 * Regardless of mode, do motion detection frequently 
 * (maybe not on all images but on many).
 *
 */

public class CaptureAndSend extends Thread{
	private Axis211A camera;
	private MotionDetector motionDetector;
	private byte [] jpeg = new byte[Axis211A.IMAGE_BUFFER_SIZE];
	private ServerSocket serverSocket;
	private Socket clientSocket;
	private InputStream is;
	private OutputStream os;
	private int port;
	private int len;
	
	/**
	 * Constructor, connects to the camera and socket. Waits for client to connect.
	 * @param port
	 * @param host //Needed and if needed where????
	 * @throws IOException
	 */
	public CaptureAndSend(int port) {
		camera = new Axis211A();
		this.port = port;
	}
	
	
	public void run(){
		while(true){
			connectCamera();
			connectClient();
			capture();
			send();
		}
	}
	
	/**
	 * For testing...
	 */
	private void printByte() {
		for(int i = 0; i < 20; i++) {
			System.out.print(jpeg[i] + " ");
		}
		System.out.println();
	}
	
	private void capture() {
		len = camera.getJPEG(jpeg, 0);		
		System.out.println("-----------");
		printByte();
		System.out.println("-----------");
		
	}
	
	private void send() {
		// Get outputstream
		try {
			os = clientSocket.getOutputStream();
		} catch (IOException e) {
			System.out.println("Server: /capture() Failed to get Outputstream");
			e.printStackTrace();
		}
		// write        
        try {
			os.write(jpeg,0,len);
		} catch (IOException e) {
			System.out.println("Server: /capture() Failed to write");
			e.printStackTrace();
		}        
	}
	
	
	private void createConnection() throws IOException {
		if(!camera.connect()) {
			System.out.println("Server: Camera failed to connect!");
			System.exit(1);
		}
		System.out.println("Server: camera connected!");		
		serverSocket = new ServerSocket(port);
		System.out.println("Server: serverSocket created");
		clientSocket = serverSocket.accept();
		System.out.println("Server: Socket accepted!");	// never prints this.. why?
		clientSocket.setTcpNoDelay(true);
	}
	
	/**
	 * Tries to connect to camera. If not possible, exit system.
	 * TODO: Handle connection problem.
	 */
	private void connectCamera() {
		if(!camera.connect()) {
			System.out.println("Server: Camera failed to connect!");
			System.exit(1);
		}
	}
	
	private void connectClient() {
		// create ServerSocket
					try {
						serverSocket = new ServerSocket(port);
						System.out.println("Server:serverSocket created");
					} catch (IOException e) {
						e.printStackTrace();
						System.out.println("Server: Failed to create socket with port: " + port);
					}
					
					// wait for accept
					try {
						clientSocket = serverSocket.accept();
						System.out.println("Server: Socket accepted!");	// never prints this.. why?
					} catch (IOException e) {
						e.printStackTrace();
						System.out.println("Server; Failed to accept socket!");
					}
					
					// Set TcpNoDelay
					try {
						clientSocket.setTcpNoDelay(true);
					} catch (SocketException e) {
						e.printStackTrace();
						System.out.println("Server: Failed to set TcpNoDelay");
					}
	}
}