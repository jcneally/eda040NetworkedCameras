package server;

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
	private ServerMonitor monitor;
	private Axis211A camera;
	private MotionDetector motionDetector;
	private byte [] jpeg = new byte[Axis211A.IMAGE_BUFFER_SIZE];
	private int len;												// length of the (filled slots of) byte array.
	private ServerSocket serverSocket;
	private Socket clientSocket;
	private OutputStream os;
	private int port;
	private int mode;												// 0 for IDLE_MODE and 1 for MOVIE_MODE.
	private int idleSleepTime;										// Time to sleep when in idle mode is idleSleepTime * detectSleepTime.
	private long detectSleepTime;
	
	/**
	 * Constructor, creates a fake camera and gets port nbr.
	 * @param port
	 */
	public CaptureAndSend(int port, ServerMonitor monitor) {
		camera = new Axis211A();
		this.port = port;
		detectSleepTime = 1000; // 1 sec.
		idleSleepTime = 5;		// 5 detectSleepTimes.
		
		this.monitor = monitor;
		motionDetector = new MotionDetector();
	}

	/**
	 * Run method. Connects to camera and client, 
	 * then while connected, captures images, 
	 * detects motion and send to client.
	 */
	public void run(){
		long t = System.currentTimeMillis();
		long diff;

		while(true){

		connectCamera();
		connectClient();
		
		while(clientSocket.isConnected()) {
			capture();			
			send();				
			
			// Handle mode. Sleep and detect appropriately. 
			switch (monitor.getMode()) {
			case ServerMonitor.IDLE_MODE:

				for( int i = 0; i < idleSleepTime; i++) {
					System.out.println("Server: motion = " + motionDetector.getLevel());
					if(motionDetector.detect()) {
						System.out.println("Server: Motion detected!");
						// TODO: handle motion i.e. send message to client.
					}
					t += detectSleepTime;
					diff = t - System.currentTimeMillis();
					if(diff > 0) {
						try {
							sleep(diff);
						} catch (InterruptedException e) {
							e.printStackTrace();						
						}
					}
				}

				break;
			case ServerMonitor.MOVIE_MODE:
				System.out.println("Server: motion = " + motionDetector.getLevel());
				if(motionDetector.detect()) {	// TODO: possibly add something so that detect does not run every time.
					System.out.println("Server: Motion detected!");
					// TODO: handle motion i.e. send message to client.
				}
				t = System.currentTimeMillis();
				break;
			case ServerMonitor.AUTO_MODE:
				// TODO: Handle AUTO_MODE...
				for( int i = 0; i < idleSleepTime; i++) {
					System.out.println("Server: motion = " + motionDetector.getLevel());
					if(motionDetector.detect()) {
						System.out.println("Server: Motion detected!");
						// TODO: handle motion i.e. send message to client.
						monitor.setMode(ServerMonitor.MOVIE_MODE);
					}
					t += detectSleepTime;
					diff = t - System.currentTimeMillis();
					if(diff > 0) {
						try {
							sleep(diff);
						} catch (InterruptedException e) {
							e.printStackTrace();						
						}
					}
				}

				
				break;

			default:
				System.out.println("int does not correspond to a mode!");
				System.exit(1);
				break;
			}
			// end of sleep/detect handling
		}
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

	/**
	 * Get jpeg from camera. Prints first bytes for test.
	 */
	private void capture() {
		len = camera.getJPEG(jpeg, 0);		
//		System.out.println("-----------");
//		printByte();
//		System.out.println("-----------");		
	}

	/**
	 * Gets outputstream and then writes the jpeg byte array to it.
	 */
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

	/**
	 * Creates ServerSocket and waits for client to connect. Also sets TcpNoDelay to true.
	 */
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
			System.out.println("Server: Socket accepted!");

			// Set the clientSocket to monitor (and notify in monitor) so that receive thread can access it.
			monitor.setClientSocket(clientSocket);
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
