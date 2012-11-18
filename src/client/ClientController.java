package client;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import se.lth.cs.fakecamera.Axis211A;

public class ClientController extends Thread {
	
	String serverCamera1;
	String serverCamera2;
    int portCamera1;
    int portCamera2;
    byte [] jpeg = new byte[Axis211A.IMAGE_BUFFER_SIZE];
    JPEGBuffer bufferCamera1;
    JPEGBuffer bufferCamera2;
        
	public ClientController(String server1, String server2 ,int port1, int port2, JPEGBuffer buffer1, JPEGBuffer buffer2){
	    serverCamera1 = server1;
	    portCamera1 = port1;
	    serverCamera2 = server2;
	    portCamera2 = port2;
	    bufferCamera1 = buffer1;
	    bufferCamera2 = buffer2;
	}
	
	private void receiveData(String server, int port, JPEGBuffer camera){
		try {
	        // Open a socket to the server, get the input/output streams
	        Socket sock = new Socket(server, port);
	        InputStream is = sock.getInputStream();
	        OutputStream os = sock.getOutputStream();  
	      
	        // Send a simple request, always for "/image.jpg"
	        putLine(os, "GET /image.jpg HTTP/1.0");
	        putLine(os, "");        // The request ends with an empty line
	
	        // Read the first line of the response (status line)
	        int responseLine;
	        responseLine = is.read();
	        System.out.println("HTTP server says '" + responseLine + "'.");
	        // Ignore the following header lines up to the final empty one.
	        do {
	            responseLine = getLine(is);
	        } while (!(responseLine.equals("")));
	
	        // Now load the JPEG image.
	        int bufferSize = jpeg.length;
	        int bytesRead  = 0;
	        int bytesLeft  = bufferSize;
	        int status;
	
	        // We have to keep reading until -1 (meaning "end of file") is
	        // returned. The socket (which the stream is connected to)
	        // does not wait until all data is available; instead it
	        // returns if nothing arrived for some (short) time.
	        do {
	            status = is.read(jpeg, bytesRead, bytesLeft);
	            // The 'status' variable now holds the no. of bytes read,
	            // or -1 if no more data is available
	            if (status > 0) {
	                bytesRead += status;
	                bytesLeft -= status;
	            }
	        } while (status >= 0);
	        sock.close();
	
	        camera.putJPEG(jpeg);
	        
	        System.out.println("Received image data ("+ bytesRead + " bytes).");
	    }
	    catch (IOException e) {
	        System.out.println("Error when receiving image.");
	        return;
	    }
	}

	public void run(){
		receiveData(serverCamera1,portCamera1,bufferCamera1);
		receiveData(serverCamera2,portCamera2,bufferCamera2);
	}
}
