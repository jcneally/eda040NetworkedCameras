package server;

public class TestServer {
	
	/**
	 * Just for testing..
	 * @param args
	 * @throws IOException
	 */
	static int port = 7009;
	
	public static void main(String[] args) throws InterruptedException {
		ServerMonitor mon = new ServerMonitor();
		System.out.println("before start...");
		
		//Start captureAndSend (server)
		CaptureAndSend captureAndSend = new CaptureAndSend(port, mon);
		captureAndSend.start();
		System.out.println("Server up and running");
		
		//Start TestConnection (client)
		TestConnection testConnection = new TestConnection(port);
		testConnection.start();
			
		//Close socket
		Thread.sleep(3000);
		testConnection.closeSocket();
		System.out.println("Client:Socket closed");
		
	}
	
	
}
