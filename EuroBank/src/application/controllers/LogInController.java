package application.controllers;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import application.users.User;
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
	
	ArrayList<User> users = new ArrayList<User>();
	
	@FXML
	void close(ActionEvent event) {
		((Stage)(((Button)event.getSource()).getScene().getWindow())).close();
	}
	
	@FXML
	void logIn(ActionEvent event) throws IOException{
		boolean data=false;
		String user = username.getText();
		String password = this.password.getText();
		
		for(User u : users) {
			if(u.authentification(user, password)) {
				data = true;
				break;
			}
		}
		if(data) {
			new BankBranchesController().showBankBranchesView();
			((Node)event.getSource()).getScene().getWindow().hide();
		}else {
			message.setText("*Pogrešna lozinka ili korisničko ime");
		}
	}
	@FXML
	void onMouseExited(MouseEvent event) {
		message.setText("");
	}
	
	@FXML //Method called from FXMLloader after initialization 
	void initialize() {
		try {
			
			BufferedReader reader = new BufferedReader(
					new FileReader("@../../Database/Users.txt"));
			
			String s;
			while((s = reader.readLine())!=null) {
				String[] data = s.split("#");
				users.add(new User(data[0], data[1]));
			}
			reader.close();
		} catch (IOException ex) {
			System.out.println(ex.getMessage());
		}
	}
	
}
