package client;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import se.lth.cs.fakecamera.*;

public class ClientMonitor {

    final static int IDLE = 0;
    final static int MOVIE = 1;
    final static int AUTO = 2;

    private Socket camera1Sock, camera2Sock;	
	private String serverCamera1;
	private String serverCamera2;
    private int portCamera1;
    private int portCamera2;
    private byte [] jpeg = new byte[Axis211A.IMAGE_BUFFER_SIZE];
    public JPEGBuffer bufferCamera1;
    public JPEGBuffer bufferCamera2;
    private int mode = IDLE;   
    public int alerted_camera = 0; // the camera that alerted the user to movement. 0 if none, or 1 and 2 respectively
    GUI gui;
    
    boolean camera1Connected = false;
    boolean camera2Connected = false;
    
    
    public ClientMonitor(String server1, String server2 ,int port1, int port2, GUI gui){
	    serverCamera1 = server1;
	    portCamera1 = port1;
	    serverCamera2 = server2;
	    portCamera2 = port2;
	    bufferCamera1 = new JPEGBuffer();
	    bufferCamera2 = new JPEGBuffer();
	    this.gui = gui;
	}


    public synchronized boolean connect(int cam){
     boolean cam1 = (cam == 1);
     boolean alreadyConnected = (cam1) ? camera1Connected : camera2Connected;
     if(!alreadyConnected){
     
     System.out.println("Trying to connect camera " + cam + " to " + serverCamera1 + " at port " + portCamera1);
     try{
        Socket sock = (cam1) ? camera1Sock : camera2Sock;
        String server = (cam1) ? serverCamera1 : serverCamera2;
        int port = (cam1) ? portCamera1 : portCamera2;
        sock = new Socket(server, port);
        if (cam1){
          camera1Connected = true;
          camera1Sock = sock;
        }
        else{
          camera2Connected = true;
          camera2Sock = sock;
        }
        return true;
     } catch (java.net.UnknownHostException e){
      System.out.println("Could not connect, unknown host");
      return false;
     } catch (java.io.IOException e){
       System.out.println("IO Exception. Could not connect");
       return false;
     }
     }
     
     return false;
    }
    
    public synchronized boolean disconnect(int cam){
     boolean cam1 = (cam == 1);
     boolean connected = (cam1) ? camera1Connected : camera2Connected;
     if(connected){
      System.out.println("Trying to disconnect camera " + cam);
      try{
        Socket sock = (cam1) ? camera1Sock : camera2Sock;
        if (cam1)
          camera1Connected = false;
        else
          camera2Connected = false;
        sock.close();
        return true;
		} catch (java.net.UnknownHostException e) {
			// Occurs if the socket cannot find the host
			e.printStackTrace();
			return false;
		} catch (java.io.IOException e) {
			// Occurs if there is an error trying to connect to the host,
			// or there is an error during the call to the write method.
			//
			// Example: the connection is closed on the server side, but
			// the client is still trying to write data.
			e.printStackTrace();
			return false;
		}
	  }
	  return false;
    }
    
	public synchronized void receiveData(int cam){
	    boolean cameraConnected = (cam == 1) ? camera1Connected : camera2Connected;
	    if(cameraConnected){
	    
		try {
		    //System.out.println("see if it's here");
            InputStream is = (cam == 1) ? camera1Sock.getInputStream() : camera2Sock.getInputStream();
            //System.out.println("does this print?");
            JPEGBuffer buffer = (cam == 1) ? bufferCamera1 : bufferCamera2;
    
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
			
			byte command = (byte)is.read();
            int is_command = (command & 0xFF);
            
            if(is_command == 1){
                handle_command(is, cam);
            }
            else {
                handle_jpeg(is, size, buffer);
            }

        }
        catch (IOException e) {
            System.out.println("Error when receiving image.");
            System.out.println(e.getMessage());
	    return;
        }
        
        }
	}
	
	// Commands are:
	// 1 - Camera goes to IDLE, send images every 5 secs
	// 2 - Camera goes to MOVIE, send images constantly
	// 3 - Camera goes to AUTO, should tell the client about movement
	// OBS:  Commands are ALWAYS sent to both servers
	public synchronized void send_command(int command) throws IOException{
	    if(camera1Connected){
	      OutputStream os = camera1Sock.getOutputStream();
	      os.write( (byte)command );
	    }
	    if(camera2Connected){
	      OutputStream os2 = camera2Sock.getOutputStream();
	      os2.write( (byte)command );
	    }
	    System.out.println("Sent command " + command);
	}
	
	// Sets the mode of the system
	public synchronized void set_mode(int n){
	    mode = n;
	    // reswitched to auto mode, so reset the alerted camera so it can be set again.
	    if(mode == AUTO){
	        alerted_camera = 0;
	    }
	}
	
		// camera is 1 or 2
	private synchronized void handle_command(InputStream is, int camera) throws IOException{
	    int command = (is.read() & 0xFF);
	    if(command == -1) throw new IOException("Expected valid command but was not valid");
	    System.out.println("The command sent from camera " + camera + " was " + command);
	    if(command == 1 && mode == AUTO){
	      gui.selectMovement(MOVIE);
	      mode = MOVIE;
	      alerted_camera = camera;
	      gui.alert(alerted_camera);
	    }
	}
	
	// Params:
	// is - Input stream to use for the JPEG
	// size - size of JPEG image from header
	// buffer - JPEG buffer to put the image in.
	private synchronized void handle_jpeg(InputStream is, int size, JPEGBuffer buffer) throws IOException{
	    // Read package
	    int read = 0; // Number of read bytes so far
	    while (read != size) {
			    
    	    // Read bytes and put in data array until size bytes are read
    		// Read returns number of bytes read <= size-read
			int n = is.read(jpeg, read, size - read);
	    	if (n == -1) throw new IOException("End of stream");
	    		read += n;
	    }
	    
        buffer.putJPEG(jpeg);
	}

}