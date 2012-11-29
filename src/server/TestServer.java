package server;

public class TestServer {
	
	/**
	 * Just for testing..
	 * @param args
	 * @throws IOException
	 */
	static int port = 6077;
	
	public static void main(String[] args) throws InterruptedException {
		ServerMonitor mon = new ServerMonitor();
		mon.setMode(ServerMonitor.IDLE_MODE);
		System.out.println("before start...");
		
		//Start captureAndSend (server)
		CaptureAndSend captureAndSend = new CaptureAndSend(port, mon);
		captureAndSend.start();
		System.out.println("Server up and running");
		Receive receive = new Receive(mon);
		receive.start();
		CommandController commandController = new CommandController(mon);
		commandController.start();
		
		//Start TestConnection (client)
		TestConnection testConnection = new TestConnection(port);
		testConnection.start();
		
		Thread.sleep(1000);
		mon.setMode(0);
		Thread.sleep(1000);
		mon.setMode(1);
		Thread.sleep(1000);
		mon.setMode(2);
		//Close socket
		Thread.sleep(30000);
		testConnection.closeSocket();
		System.out.println("Client:Socket closed");
		
	}
	
	
}
