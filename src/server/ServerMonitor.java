import se.lth.cs.fakecamera.Axis211A;

/**
 * 
 * @author Viktor Andersson
 *
 * Monitor for images. (and requests?)
 */

public class ServerMonitor {
	byte [] jpeg = new byte[Axis211A.IMAGE_BUFFER_SIZE];
	
	public ServerMonitor() {
		
	}
	
	public synchronized byte[] getImage() {
		return jpeg;
	}
	
	public synchronized void setImage(byte[] jpeg) {
		for(int i = 0; i < Axis211A.IMAGE_BUFFER_SIZE; i++) {
			this.jpeg[i] = jpeg[i];
		}
	}
}
