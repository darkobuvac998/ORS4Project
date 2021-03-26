package application.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
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
	
	FXMLLoader loader = new FXMLLoader();
	Scene scene;
	
	void showBankBranchesView() throws IOException{
		loader.setLocation(getClass().getResource("/application/views/BankBranchesView.fxml"));
		Parent view = (Parent)loader.load();
		scene = new Scene(view);
		scene.getStylesheets().add(getClass().getResource("/application/application.css").toExternalForm());
		Stage stage = new Stage();
		stage.setTitle("EuroBank - Filijale");
		stage.setScene(scene);
		stage.setResizable(false);
		stage.show();
		
	}

}
