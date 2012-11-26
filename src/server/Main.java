package server;

public class Main {
	
	public Main(){}
	
	public static void main(String[] args) {
		ServerMonitor mon = new ServerMonitor();
		System.out.println("serverMonitor created");
		
		CaptureAndSend captureAndSend = new CaptureAndSend(6077, mon);
		captureAndSend.start();
		System.out.println("Server up and running");
	}
}
