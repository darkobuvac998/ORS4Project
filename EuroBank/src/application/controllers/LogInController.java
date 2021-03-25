package application.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class LogInController {

	@FXML
	private ResourceBundle resources;
	@FXML
	private URL location;
	@FXML
	private TextField username;
	@FXML
	private TextField password;
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
	void close(ActionEvent event) {
		((Stage)(((Button)event.getSource()).getScene().getWindow())).close();
	}
	
	@FXML
	void logIn(ActionEvent event) throws IOException{
		new BankBranchesController().showBankBranchesView();
		((Node)event.getSource()).getScene().getWindow().hide();
	}
	
}
