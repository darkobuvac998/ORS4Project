package application.server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;

public class ServerThread extends Thread {

	private Socket socket;
	private BufferedReader reader;
	private PrintWriter writer;
	private String request;
	private String response;

	public ServerThread(Socket s) {
		try {
			
			socket = s;
			reader = new BufferedReader(new InputStreamReader(s.getInputStream()));
			writer = new PrintWriter(new BufferedWriter(new OutputStreamWriter(s.getOutputStream())), true);

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	@Override
	public void run() {

		try {

			String request = reader.readLine();
			this.request = request;

			String[] data = request.split("#");

			// Prilikom prijave salju se podaci na server
			if (data[0].equals("USER")) {
				// data = {"USER","username","password"}
				String response = String.valueOf(authentification(data[1], data[2]));
				this.response = response;
				writer.println(response);
			}
			if (data[0].equals("MAPVIEW")) {
				String response = bankBranchesView();
				this.response = response;
				writer.println(response);
			}
			if (data[0].substring(0, 6).equals("BRANCH")) {
				String response = bankBranchView(data[0].charAt(7));
				this.response = response;
				writer.println("response");
			}
			if (data[0].equals("CLIENTS")) {
				String response = clientsView();
				this.response = response;
				writer.println(response);
			}
			if (data[0].equals("CLIENTSINFO")) {
				String response = clientInfoView();
				this.response = response;
				writer.println(response);
			}
			if (data[0].equals("NEWCLIENT")) {
				String response = addNewClient(data[1], data[2], data[3]);
				this.response = response;
				writer.println(response);
			}
			if (data[0].equals("NEWTRANSACTION")) {
				String response = addTransaction(data[1], data[2], data[3], data[4], data[5]);
				this.response = response;
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

	// provjera da li je prijava validna
	public boolean authentification(String username, String password) throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader("@../../Database/Users.txt"));
		String s;
		while ((s = reader.readLine()) != null) {
			String[] user = s.split("#");
			if (user[0].equals(username) && user[1].equals(password)) {
				reader.close();
				return true;
			}
		}
		reader.close();
		return false;
	}

	// Metoda koja vraca sve filijale banke potrebne za prozor BankBranchesView
	public String bankBranchesView() throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader("@../../Database/BankBranches.txt"));
		String response = "";
		String s;
		while ((s = reader.readLine()) != null) {
			response += s;
			response += ";";
		}
		reader.close();
		return response;
	}

	// Metoda koja vraca filiju banke potrebne za prozor BranchOffice
	public String bankBranchView(char lineC) throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader("@../../Database/BankBranches.txt"));
		String response = "";
		String s;
		int count = 0;
		int line = Integer.parseInt(String.valueOf(lineC)) - 1;
		while ((s = reader.readLine()) != null) {
			if (count == line)
				response += s;
			count++;
		}
		reader.close();
		return response;
	}

	public String addNewClient(String name, String surname, String jmbg) throws IOException {

		BufferedReader reader = new BufferedReader(new FileReader("@../../Database/Clients.txt"));
		BufferedWriter writer = new BufferedWriter(new FileWriter("@../../Database/Clients.txt", true));

		String s;
		while ((s = reader.readLine()) != null) {
			String[] data = s.split("#");
			if (data[0].equals(jmbg)) {
				reader.close();
				writer.close();
				return "ERROR#Uneseni jmbg vec postoji";
			}
		}
		// Kada se doda novi klijent stanje na racunu je 0
		String client = jmbg + "#" + name + "#" + surname + "#" + "0";

		writer.write(client);
		writer.newLine();

		writer.close();
		reader.close();

		return "OK#Novi klijent dodat";
	}

	// Metoda koja vraca sve klijente banke potrebne za prozor BranchOffice
	public String clientsView() throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader("@../../Database/Clients.txt"));
		
		String response = "";
		String s;
		while ((s = reader.readLine()) != null) {
			response += s;
			response += ";";
		}
		reader.close();
		
		return response;
	}

	// Metoda koja vraca informacije o klijentu potrebne za prozor InfoView
	public String clientInfoView() throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader("@../../Database/History.txt"));
		
		String response = "";
		String s;
		while ((s = reader.readLine()) != null) {
			response += s;
			response += ";";
		}
		reader.close();
		
		return response;
	}

	public String getRequest() {
		return request;
	}

	public String getResponse() {
		return response;
	}

	public String addTransaction(String jmbgP1, String jmbgP2, String amount, String date, String branch) throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader("@../../Database/Clients.txt"));
		BufferedWriter writerH = new BufferedWriter(new FileWriter("@../../Database/History.txt", true));

		String s;
		ArrayList<String> cli = new ArrayList<String>();
		ArrayList<String[]> pom = new ArrayList<String[]>();
		
		while ((s = reader.readLine()) != null) {
			cli.add(s);
			pom.add(s.split("#"));
		}

		int ind1, ind2;
		if (jmbgP2.equals("0") && !jmbgP1.equals("0")) {
			ind1 = getIndex(pom, jmbgP1);
			if ((Double.parseDouble(pom.get(ind1)[3]) < Double.parseDouble(amount))) {
				reader.close();
				writerH.close();
				return "ERROR#Klijent nema dovoljno km na stanju";
			} else {
				String money = String.valueOf(Double.parseDouble(pom.get(ind1)[3]) - Double.parseDouble(amount));
				cli.set(ind1, pom.get(ind1)[0] + "#" + pom.get(ind1)[1] + "#" + pom.get(ind1)[2] + "#" + money);

				String hist = jmbgP1 + "#-" + amount + "#" + date + "#" + branch;
				writerH.write(hist);
				writerH.newLine();
			}
		} else if (jmbgP1.equals("0") && !jmbgP2.equals("0")) {
			ind2 = getIndex(pom, jmbgP2);
			String money = String.valueOf(Double.parseDouble(pom.get(ind2)[3]) + Double.parseDouble(amount));
			cli.set(ind2, pom.get(ind2)[0] + "#" + pom.get(ind2)[1] + "#" + pom.get(ind2)[2] + "#" + money);

			String hist = jmbgP2 + "#" + amount + "#" + date + "#" + branch;
			writerH.write(hist);
			writerH.newLine();
		} else {
			ind1 = getIndex(pom, jmbgP1);
			ind2 = getIndex(pom, jmbgP2);

			if ((Double.parseDouble(pom.get(ind1)[3]) < Double.parseDouble(amount))) {
				reader.close();
				writerH.close();
				return "ERROR#Klijent nema dovoljno km na stanju";
			} else {
				String money1 = String.valueOf(Double.parseDouble(pom.get(ind1)[3]) - Double.parseDouble(amount));
				String money2 = String.valueOf(Double.parseDouble(pom.get(ind2)[3]) + Double.parseDouble(amount));
				cli.set(ind1, pom.get(ind1)[0] + "#" + pom.get(ind1)[1] + "#" + pom.get(ind1)[2] + "#" + money1);
				cli.set(ind2, pom.get(ind2)[0] + "#" + pom.get(ind2)[1] + "#" + pom.get(ind2)[2] + "#" + money2);

				String hist = jmbgP1 + "#" + jmbgP2 + "#" + amount + "#" + date + "#" + branch;
				writerH.write(hist);
				writerH.newLine();
			}
		}

		BufferedWriter writerC = new BufferedWriter(new FileWriter("@../../Database/Clients.txt", false)); 
		writerC.close();
		writerC = new BufferedWriter(new FileWriter("@../../Database/Clients.txt", true));
		for (int i = 0; i < cli.size(); i++) {
			writerC.write(cli.get(i));
			writerC.newLine();
		}

		writerC.close();
		writerH.close();
		reader.close();

		return "OK#Transakcija izvrsena";
	}

	int getIndex(ArrayList<String[]> pom, String s) {
		for (int i = 0; i < pom.size(); i++) {
			if (Arrays.stream(pom.get(i)).anyMatch(x -> x.equals(s)))
				return i;
		}
		return -1;
	}

}
