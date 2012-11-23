package server;

public class Main {
	private static int port;
	
	public Main() {
		port = 7000;
	}
	
	public static void main(String[] args) {
		ServerMonitor mon = new ServerMonitor();
		System.out.println("serverMonitor created");
		
		CaptureAndSend captureAndSend = new CaptureAndSend(port, mon);
		captureAndSend.start();
		System.out.println("Server up and running");
	}
}
