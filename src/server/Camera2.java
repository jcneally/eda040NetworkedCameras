package server;

public class Camera2 {
	
	public Camera2(){}
	
	public static void main(String[] args) {
		ServerMonitor mon = new ServerMonitor();
		mon.setMode(1); //movie
		System.out.println("serverMonitor created");
		
		CaptureAndSend captureAndSend = new CaptureAndSend(6078, mon);
		captureAndSend.start();
		System.out.println("Server up and running");
	}
}
