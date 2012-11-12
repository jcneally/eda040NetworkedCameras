
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
	
	
	public CaptureAndSend() {
		// TODO Auto-generated constructor stub
		camera = new Axis211A()
	}
	
	public void run() {
		camera.
	}
	
	
	private boolean detect() {
		// TODO: Implement this method..
		motionDetector.detect();
		return false;
	}
}
