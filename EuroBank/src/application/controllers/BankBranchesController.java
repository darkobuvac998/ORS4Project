package application.controllers;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Paths;
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
		URL url =Paths.get("C:\\Users\\Dell\\eclipse-workspace\\ORS4Projekat\\ORS4Project\\EuroBank\\src\\application\\views\\BankBranchesView.fxml").toUri().toURL();
		loader.setLocation(url);
		Parent view = (Parent)loader.load();
		scene = new Scene(view);
		Stage stage = new Stage();
		stage.setTitle("EuroBank - Filijale");
		stage.setScene(scene);
		stage.setResizable(false);
		stage.show();
		
	}

}
