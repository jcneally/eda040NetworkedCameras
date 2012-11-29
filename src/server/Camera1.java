package server;

public class Camera1 {
	
	public Camera1(){}
	
	public static void main(String[] args) {
		ServerMonitor mon = new ServerMonitor();
		mon.setMode(1); //movie
		System.out.println("serverMonitor created");
		
		CaptureAndSend captureAndSend = new CaptureAndSend(6077, mon);
		Receive receive = new Receive(mon);
		CommandController commandController = new CommandController(mon);
		
		captureAndSend.start();
		receive.start();
		//commandController.start();
		
		System.out.println("Server up and running");
	}
}
