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
			System.out.print("1: ");
			printByte();
			System.out.println(camera.getJPEG(jpeg, 0));
			printByte();

			try {
				createConnection();
				capture();
				sleep(0);
			} catch (Exception e) {
				// TODO: handle exception
				System.out.println("Cached exception..");
			}
			System.out.print("2: ");
			printByte();
			camera.close();
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
	
	private void capture() throws IOException {
		camera.getJPEG(jpeg, 0);		
		System.out.println("-----------");
		printByte();
		System.out.println("-----------");
		os = clientSocket.getOutputStream();
				
        int len = camera.getJPEG(jpeg,0);
        os.write(jpeg,0,len);        
	}
	
	
	private void createConnection() throws IOException {
		if(!camera.connect()) {
			System.out.println("Camera failed to connect!");
			System.exit(1);
		}
		System.out.println("camera connected!");		
		serverSocket = new ServerSocket(port);
		System.out.println("serverSocket created");
		clientSocket = serverSocket.accept();
		System.out.println("Socket accepted!");	// never prints this.. why?
		clientSocket.setTcpNoDelay(true);
	}
}