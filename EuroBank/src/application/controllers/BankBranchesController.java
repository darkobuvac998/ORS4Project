package application.controllers;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import application.bankbranches.BankBranches;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
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
	
	ArrayList<BankBranches> branches = new ArrayList<BankBranches>();
	
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
	@FXML
	void onMouseEntered(MouseEvent event) {
		String id = ((Button)event.getSource()).getText();
		System.out.println(id);
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
	void initialize() {
		
		try {
			
			BufferedReader reader = new BufferedReader(
					new FileReader("@../../Database/BankBranches.txt"));
			
			String s;
			while((s = reader.readLine())!=null) {
				String[] data = s.split("#");
				branches.add(
						new BankBranches(data[0], data[1], data[2], data[3]));
			}
			reader.close();
			
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
		
	}

}
