package client;

import se.lth.cs.realtime.event.Buffer;


public class ImageDispatcher extends Thread{
	
	///////////////////////////////////////////////////////////////////////////////////////
	//	This thread is responsible of taking care about the images delay dipending on    //
	//	the mode the user wants to have, even if it is auto, and send the delay suitable //
	//	images to the GUI, and make them available to the user view.                     //
	///////////////////////////////////////////////////////////////////////////////////////
	
	JPEGBuffer bufferCamera1;
	JPEGBuffer bufferCamera2;
	GUI gui;
	
	private int delayCamera1;
	private int delayCamera2;
	
	private final int delayUpBound = 200; //Change this delay bound to switch to async, when test the program
	private final int delayLowBound = 100; //Change this delay bound to switch to sync, when test the program
	private final int delayMargin = 100; //Change this delay margin to adjust the acceptable difference delay while synchronized
	private final int SYNC = 1;
	private final int ASYNC  = 2;
	private final int AUTO = 0;
	private final int CAMERA1 = 1;
	private final int CAMERA2 = 2; 
	
	int mode = ASYNC;
	
	public ImageDispatcher(JPEGBuffer buffer1, JPEGBuffer buffer2, GUI gui){
		bufferCamera1 = buffer1;
		bufferCamera2 = buffer2;
		this.gui = gui;
	}
	
	private void refreshImage(int SynchronizationMode){
		
		if(SynchronizationMode==AUTO){
			mode = controlSynchronization(mode);
			//gui.setMode(mode);
		}else{
			mode = SynchronizationMode;
		}
	
		
		switch (mode){

		case SYNC:
			//return the most delayed image and the other with an added delay equal to the difference between both images delays
			syncImages();
			gui.updateCamera1(bufferCamera1.getJPEG());
			gui.updateCamera2(bufferCamera2.getJPEG());		
			break;
		
		case ASYNC:
			//return the current image
			gui.updateCamera1(bufferCamera1.getJPEG());
			gui.updateCamera2(bufferCamera2.getJPEG());			
			break;
		}
	}
	
	private void syncImages(){
		//If the buffer allocate too many images, jump to the image according to the current time, and skip the old ones.
		//Test it with the delay of the last image
		if(delayCamera1>delayCamera2){
			while(delayCamera1<(delayCamera2+delayMargin)){
				bufferCamera1.skipJPEG();
				delayCamera1 = getDelay(CAMERA1);
			}
		}else if(delayCamera1<delayCamera2){
			while((delayCamera1+delayMargin)>delayCamera2){
				bufferCamera2.skipJPEG();
				delayCamera2 = getDelay(CAMERA2);
			}
		}
	}
	
	private int controlSynchronization(int currentMode){
		//If the delay is bigger than a bound, switch to asynchronous and viceversa
		//Do it with hysteresis to avoid switching continuously
		int SyncMode=currentMode;
		
		if(delayCamera1>delayUpBound||delayCamera2>delayUpBound){
			SyncMode = ASYNC;
		}else if(delayCamera1<delayLowBound&&delayCamera2<delayLowBound){
			SyncMode = SYNC;
		}
		return SyncMode;
	}
	
	private int getDelay(int camera){
		//return the delay of the next image of 'camera'
		
		return 0;
	}
	
	public void run(){
		delayCamera1 = getDelay(CAMERA1);
		delayCamera2 = getDelay(CAMERA2);
		refreshImage(ASYNC);
	}
}
