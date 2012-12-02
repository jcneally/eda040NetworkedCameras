package client;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import se.lth.cs.fakecamera.*;

public class ClientDataController extends Thread {
	
	////////////////////////////////////////////////////////////////////////////
	//	This thread is responsible of create a connection with the server by  //
	//	creating two sockets and read the full incoming images. Then, it      //
	//	saves the images in its respective buffers to allow the other threads //
	//	play with them concurrency save.                                      //
	////////////////////////////////////////////////////////////////////////////
	
	ClientMonitor monitor;
    
	public ClientDataController(ClientMonitor monitor){
	    this.monitor = monitor;
	}

	public void run(){
    while(true){
            monitor.receiveData(1);
            monitor.receiveData(2);
        }
	}

}
