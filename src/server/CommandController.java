package server;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

public class CommandController extends Thread{
	private ServerMonitor serverMonitor;
	private Socket clientSocket;
	private OutputStream os;

	public CommandController(ServerMonitor serverMonitor) {
		this.serverMonitor = serverMonitor;
	}
	public void run() {
		while(true) {

			clientSocket = serverMonitor.waitForClientSocket();
			if(clientSocket != null) {
				try {
					os = clientSocket.getOutputStream();
				} catch (IOException e) {
					System.out.println("CommandController: Failed to get output stream. Get new connection.");
					continue;
				}


				while(clientSocket.isConnected()) {
					serverMonitor.sendCommand(os);
				}

			}
		}
	}
}
