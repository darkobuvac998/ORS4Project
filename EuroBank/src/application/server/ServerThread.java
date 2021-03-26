package application.server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

public class ServerThread extends Thread{
	
	Socket socket;
	BufferedReader reader;
	PrintWriter writer;
	
	public ServerThread(Socket s) {
		try {
			
			socket = s;
			reader = new BufferedReader(new InputStreamReader(s.getInputStream()));
			writer = new PrintWriter(new BufferedWriter(
					new OutputStreamWriter(s.getOutputStream())), true);
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	@Override
	public void run() {
		
		try {
			
			String request = reader.readLine();
			String[] data = request.split("#");
			
			//Prilikom prijave salju se podaci na server
			if(data[0].equals("USER")) {
				//data = {"USER","username","password"}
				String response = String.valueOf(
						authentification(data[1], data[2]));
				writer.println(response);
			}
			
			reader.close();
			writer.close();
			socket.close();
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		
	}
	
	public String typeOfRequest(String request) {
		return request.split("#")[0];
	}
	
	//provjera da li je prijava validna
	public boolean authentification(String username, String password) throws IOException {
//		ArrayList<User> users = new ArrayList<User>();
		BufferedReader reader = new BufferedReader(
				new FileReader("@../../Database/Users.txt"));
		String s;
		while((s = reader.readLine())!=null) {
			String[] user = s.split("#");
			if(user[0].equals(username) && user[1].equals(password)) {
				reader.close();
				return true;
			}
		}
		reader.close();
		return false;
	}

}
