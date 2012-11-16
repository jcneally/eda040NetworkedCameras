
import se.lth.cs.realtime.event.Buffer;


public class ImageDispatcher extends Thread{
	
	JPEGBuffer bufferCamera1;
	JPEGBuffer bufferCamera2;
	
	public ImageDispatcher(JPEGBuffer buffer1, JPEGBuffer buffer2){
		bufferCamera1 = buffer1;
		bufferCamera2 = buffer2;
	}
	
	public void getImage(int SynchronizationMode){
		switch (SynchronizationMode){
		case SYNC:
			//return the most delayed image and the other with an added delay equal to the difference between both images delays
			break;
		case ASYNC:
			//return the current image
			break;
		}
	}
	
	public void skipImages(){
		//If the buffer allocate too many images, jump to the one according to the current time, and skip the old ones.
		//Test it with the delay of the last image
	}
	
	public int controlSynchronization(){
		//If the delay is bigger than a bound, switch to asynchronous and viceversa
		//Do it with hysteresis to avoid switch continuously
	}
	
	public void refreshBuffer(Buffer buffer){}
	//Delete the images not used;
}
