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
	
	public CaptureAndSend(int port, String host) throws IOException {
		camera = new Axis211A();
		camera.connect();
		serverSocket = new ServerSocket(port);
		clientSocket = serverSocket.accept();
	}
	
	public void run() {
		camera.getJPEG(jpeg, 0);
	}
	
	
}