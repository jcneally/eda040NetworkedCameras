import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import javax.swing.JButton;

import se.lth.cs.fakecamera.Axis211A;
import se.lth.cs.realtime.event.Buffer;

public class ClientController extends Thread {

	Buffer Buffer1;
	Buffer Buffer2;
	
	String server;
    int port;
    byte [] jpeg = new byte[Axis211A.IMAGE_BUFFER_SIZE];
        
	public ClientController(Buffer Buffer1, Buffer Buffer2,String server,int port){
	    this.server = server;
	    this.port = port;
		this.Buffer1=Buffer1;
		this.Buffer2=Buffer2;
	}
	
	public void OpenSocket(){
		try {
	        // Open a socket to the server, get the input/output streams
	        Socket sock = new Socket(server, port);
	        InputStream is = sock.getInputStream();
	        OutputStream os = sock.getOutputStream();
	
	        // Send a simple request, always for "/image.jpg"
	        putLine(os, "GET /image.jpg HTTP/1.0");
	        putLine(os, "");        // The request ends with an empty line
	
	        // Read the first line of the response (status line)
	        String responseLine;
	        responseLine = getLine(is);
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
	
	        System.out.println("Received image data ("
	                           + bytesRead + " bytes).");
	
	    }
	    catch (IOException e) {
	        System.out.println("Error when receiving image.");
	        return;
	    }
	}
	
	public void saveImage(Buffer buffer, int currentBufferIndex){}
	// Save the image from the socket to the buffer;

	
}
