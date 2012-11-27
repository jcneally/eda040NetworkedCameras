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
	

    Socket camera1Sock, camera2Sock;	
	String serverCamera1;
	String serverCamera2;
    int portCamera1;
    int portCamera2;
    byte [] jpeg = new byte[Axis211A.IMAGE_BUFFER_SIZE];
    JPEGBuffer bufferCamera1;
    JPEGBuffer bufferCamera2;
    
	public ClientDataController(String server1, String server2 ,int port1, int port2, JPEGBuffer buffer1, JPEGBuffer buffer2){
	    serverCamera1 = server1;
	    portCamera1 = port1;
	    serverCamera2 = server2;
	    portCamera2 = port2;
	    bufferCamera1 = buffer1;
	    bufferCamera2 = buffer2;
	}
	
	private static final byte[] CRLF      = { 13, 10 };

    /**
     * Read a line from InputStream 's', terminated by CRLF. The CRLF is
     * not included in the returned string.
     */
    private static String getLine(InputStream s) throws IOException {
        boolean done = false;
        String result = "";

        while(!done) {
            int ch = s.read();        // Read
            if (ch <= 0 || ch == 10) {
                // Something < 0 means end of data (closed socket)
                // ASCII 10 (line feed) means end of line
                done = true;
            }
            else if (ch >= ' ') {
                result += (char)ch;
            }
        }

        return result;
    }

    /**
     * Send a line on OutputStream 's', terminated by CRLF. The CRLF should not
     * be included in the string str.
     */
    private static void putLine(OutputStream s, String str) throws IOException {
        s.write(str.getBytes());
        s.write(CRLF);
    }
	
	private void receiveData(int cam, JPEGBuffer camera){
		try {
		
            InputStream is = (cam == 1) ? camera1Sock.getInputStream() : camera2Sock.getInputStream();
            //OutputStream os = camera1Sock.getOutputStream();

            // Read the first line of the response (status line)
            /*String responseLine;
            responseLine = getLine(is);
            System.out.println("HTTP server says '" + responseLine + "'.");
            // Ignore the following header lines up to the final empty one.
            do {
                responseLine = getLine(is);
            } while (!(responseLine.equals("")));
		*/
            // Now load the JPEG image.
            
            //int bufferSize = jpeg.length;
            //int bytesRead  = 0;
            //int bytesLeft  = bufferSize;
            //int status;
            
            // Read header - read is blocking
			byte hi = (byte)is.read();
			// If read returns -1 then end-of-stream has been reached
			if (hi == -1) throw new IOException("End of stream");
			byte lo = (byte)is.read();
			if (lo == -1) throw new IOException("End of stream");
				
			// Calculate size of package
			// Byte is signed. & 0xFF creates an int from the byte bit pattern, allowing
			// for interpretation of byte values in the range 0-254. Leave 255 as it is
			// used to indicate end-of-stream in read().
			int size = (hi & 0xFF)*255 + (lo & 0xFF);
            

            // Read package
			int read = 0; // Number of read bytes so far
			while (read != size) {
			    
				// Read bytes and put in data array until size bytes are read
				// Read returns number of bytes read <= size-read
				int n = is.read(jpeg, read, size - read);
				if (n == -1) throw new IOException("End of stream");
				read += n;
			}
            // We have to keep reading until -1 (meaning "end of file") is
            // returned. The socket (which the stream is connected to)
            // does not wait until all data is available; instead it
            // returns if nothing arrived for some (short) time.
            //do {
                //status = is.read(jpeg, bytesRead, bytesLeft);
                // The 'status' variable now holds the no. of bytes read,
                // or -1 if no more data is available
           //     if (status > 0) {
           //         bytesRead += status;
           //         bytesLeft -= status;
           //     }
           // } while (status >= 0);
            
           // sock.close();
            //gui.updateCamera1(jpeg);
            camera.putJPEG(jpeg);

        }
        catch (IOException e) {
            System.out.println("Error when receiving image.");
            System.out.println(e.getMessage());
	    return;
        }
	}

	public void run(){
	if(connect())
          System.out.println("Successfully connected");
	else
          System.out.println("There was an error connecting.");
        while(true){
          receiveData(1,bufferCamera1);
          receiveData(2,bufferCamera2);
        }
	}

  public boolean connect(){
    System.out.println("Trying to connect to " + serverCamera1 + " at port " + portCamera1);
    try{
       camera1Sock = new Socket(serverCamera1, portCamera1);
       camera2Sock = new Socket(serverCamera2, portCamera2);
      return true;
    } catch (java.net.UnknownHostException e){
      System.out.println("Could not connect, unknown host");
      return false;
    } catch (java.io.IOException e){
      System.out.println("IO Exception. Could not connect");
      return false;
    }
  }

}
