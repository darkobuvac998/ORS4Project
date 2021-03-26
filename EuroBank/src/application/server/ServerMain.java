package application.server;

import java.net.ServerSocket;
import java.net.Socket;

public class ServerMain {
	
	private static final int PORT = 9999;
	
	public static void main(String[] args) {
		
		try {
			System.out.print("Server je pokrenut");
			ServerSocket serverSocket = new ServerSocket(PORT);
			serverSocket.setReuseAddress(true);
			
			for(int i=0; i<1000; i++) {
				Socket socket = serverSocket.accept();
				new ServerThread(socket).start();
			}
			serverSocket.close();
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		
	}

}
