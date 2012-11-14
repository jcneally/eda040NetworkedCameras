import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;


public class TestServer {
	
	/**
	 * Just for testing..
	 * @param args
	 * @throws IOException
	 */
	
	public static void main(String[] args) throws IOException {
		ServerMonitor mon = new ServerMonitor();
		System.out.println("before start...");
		clientConnect();
		CaptureAndSend captureAndSend = new CaptureAndSend(6077);
		
		captureAndSend.start();
		System.out.println("Server up and running");
	}
	
	private static void clientConnect() {
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			Socket socket = new Socket("localhost", 6077);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
