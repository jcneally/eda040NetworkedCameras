package client;

import se.lth.cs.realtime.event.Buffer;


public class ImageDispatcher extends Thread{
	
	JPEGBuffer bufferCamera1;
	JPEGBuffer bufferCamera2;
	
	int delayCamera1;
	int delayCamera2;
	
	private final int delayBound = 10; //Change this delay bound when test the program
	private final int SYNC = 1;
	private final int ASYNC  = 2;
	private final int AUTO = 0;
	
	public ImageDispatcher(JPEGBuffer buffer1, JPEGBuffer buffer2){
		bufferCamera1 = buffer1;
		bufferCamera2 = buffer2;
	}
	
	public void getImage(int SynchronizationMode){
		int mode;
		
		if(SynchronizationMode==AUTO){
			mode = controlSynchronization();
		}else{
			mode = SynchronizationMode;
		}
		
		switch (mode){

		case SYNC:
			//return the most delayed image and the other with an added delay equal to the difference between both images delays
			
			
			
			break;
		case ASYNC:
			//return the current image
			//bufferCamera1.getJPEG();
			//bufferCamera2.getJPEG();			
			break;
		}
	}
	
	private void skipImages(){
		//If the buffer allocate too many images, jump to the image according to the current time, and skip the old ones.
		//Test it with the delay of the last image
	}
	
	private int controlSynchronization(){
		//If the delay is bigger than a bound, switch to asynchronous and viceversa
		//Do it with hysteresis to avoid switch continuously
		int SyncMode;
		
		if(delayCamera1>delayBound||delayCamera2>delayBound){
			SyncMode = ASYNC;
		}else{
			SyncMode = SYNC;
		}
		return SyncMode;
	}
	
	private void refreshBuffer(Buffer buffer){}
	//Delete the images not used;
	
	public void run(){}
}
