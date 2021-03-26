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
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class LogInController {

	@FXML
	private ResourceBundle resources;
	@FXML
	private URL location;
	@FXML
	private TextField username;
	@FXML
	private PasswordField password;
	@FXML
	private Button logIn;
	@FXML
	private Button cancel;
	@FXML
	private Label label;
	@FXML
	private Label eblabel;
	@FXML
	private ImageView image;
	@FXML
	private Label message;
	
	private Socket socket;
	
	@FXML
	void close(ActionEvent event) {
		((Stage)(((Button)event.getSource()).getScene().getWindow())).close();
	}
	
	@FXML
	void logIn(ActionEvent event) throws IOException{
		String user = username.getText();
		String password = this.password.getText();
		
		int PORT = 9999;
		
		if(isInputCorrect()) {
			try {
				InetAddress address = InetAddress.getByName("127.0.0.1");
				socket = new Socket(address,PORT);
				
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(socket.getInputStream()));
				PrintWriter writer = new PrintWriter(new BufferedWriter(
						new OutputStreamWriter(socket.getOutputStream())), true);
				
				String request = "USER#"+user+"#"+password;
				writer.println(request);
				
				String response = reader.readLine();
				if(response.equals("true")) {
					new BankBranchesController().showBankBranchesView();
					((Node)event.getSource()).getScene().getWindow().hide();
				}else {
					message.setText("*Pogrešna lozinka ili korisničko ime");
				}
				reader.close();
				writer.close();
				socket.close();		
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
		}else {
			message.setText("*Popunite sva polja!");
		}
		
	}
	@FXML
	void onMouseExited(MouseEvent event) {
		message.setText("");
	}
	
	@FXML //Method called from FXMLloader after initialization 
	void initialize() {

	}
	
	public boolean isInputCorrect() {
		return (!username.getText().isEmpty() && !password.getText().isEmpty());
	}
	
}
