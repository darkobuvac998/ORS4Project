package application.controllers;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import application.bankbranches.BankBranches;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class BankBranchesController {

	@FXML
	private ResourceBundle resources;
	@FXML
	private URL location;
	@FXML
	private Label name;
	@FXML
	private Label street;
	@FXML
	private Label workingtime;
	@FXML
	private Label ebtitle;
	@FXML
	private Button btnf1;
	@FXML
	private Button btnf2;
	@FXML
	private Button btnf3;
	@FXML
	private Button btnf4;
	@FXML
	private Button btnf5;
	@FXML
	private Button btnf6;

	private Socket socket;

	private ArrayList<BankBranches> branches = new ArrayList<BankBranches>();

	private FXMLLoader loader = new FXMLLoader();
	private Scene scene;

	void showBankBranchesView() throws IOException {
		loader.setLocation(getClass().getResource("/application/views/BankBranchesView.fxml"));
		Parent view = (Parent)loader.load();
		scene = new Scene(view);
		scene.getStylesheets().add(getClass().getResource("/application/application.css").toExternalForm());
		Stage stage = new Stage();
		stage.setTitle("EuroBank - Filijale");
		stage.setScene(scene);
		stage.setResizable(false);
		stage.show();
			
		stage.setOnCloseRequest(event -> {
			System.exit(0);
		});

	}

	@FXML
	void onMouseEntered(MouseEvent event) {
		String id = ((Button)event.getSource()).getText();
		BankBranches temp = branches.stream().filter(b -> b.getId().equals(id)).findFirst().get();
		name.setText(temp.getName());
		street.setText(temp.getStreet());
		workingtime.setText(temp.getWorkingTime());
	}

	@FXML
	void onMouseExited(MouseEvent event) {
		name.setText("");
		street.setText("");
		workingtime.setText("");
	}

	@FXML
	void action(ActionEvent event) throws IOException {
			String id = ((Button) event.getSource()).getText();
			BankBranches temp = branches.stream().filter(b -> b.getId().equals(id)).findFirst().get();
			
			new BranchOfficeController().showBranchOfficeView(temp.getId(), temp.getName());
			((Node)event.getSource()).getScene().getWindow().hide();
	}

	@FXML //Metoda se poziva od strane FXMLloader-a, nakon linije loader.load()
	void initialize() {
		Thread init = new Thread(() -> {
			try {
				socket = new Socket(InetAddress.getByName("127.0.0.1"), 9999);

				BufferedReader reader = new BufferedReader(new InputStreamReader(
						socket.getInputStream()));
				PrintWriter writer = new PrintWriter(new BufferedWriter(
						new OutputStreamWriter(socket.getOutputStream())), true);

				String request = "MAPVIEW";
				writer.println(request);

				String response = reader.readLine();
				String[] data = response.split(";");
				for (String s : data) {
					String[] bank = s.split("#");
					branches.add(
							new BankBranches(bank[0], bank[1], bank[2], bank[3]));
				}
				reader.close();
				writer.close();
				socket.close();

			} catch (IOException e) {
				System.out.println(e.getMessage());
			}
		});
		init.start();
	}

}
