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
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class NewClientController {
	
	@FXML
	private ResourceBundle resource;
	@FXML
	private URL location;
	@FXML
	private TextField name;
	@FXML
	private TextField surname;
	@FXML
	private TextField jmbg;
	@FXML
	private Label message;
	@FXML
	private Button save;
	@FXML
	private Button cancel;
	@FXML
	private Label jmbgmessage;
	
	private Socket socket;
	
	private FXMLLoader loader = new FXMLLoader();
	private Scene scene;
	
	private boolean isInputCorrect() throws Exception{
		if(name.getText().isEmpty() || surname.getText().isEmpty() || jmbg.getText().isEmpty()) {
			return false;
		}else {
			return true;
		}	
	}
	
	void showNewClientView() throws IOException{
		loader.setLocation(getClass().getResource("/application/views/NewClientView.fxml"));
		Parent view = loader.load();
		scene = new Scene(view);
		Stage stage = new Stage();
		stage.setScene(scene);
		stage.setTitle("EuroBank - Novi klijent");
		stage.setResizable(false);
		stage.show();
		
		stage.setOnCloseRequest(event -> {
			stage.close();
		});

	}
	
	@FXML
	void save(ActionEvent event) throws Exception{
		if(isInputCorrect() && !(jmbgIsNumber()==null)) {
			
			try {
				socket = new Socket(InetAddress.getByName("127.0.0.1"), 9999);
				BufferedReader reader = new BufferedReader(new InputStreamReader(
						socket.getInputStream()));
				PrintWriter writer = new PrintWriter(new BufferedWriter(
						new OutputStreamWriter(socket.getOutputStream())), true);
				
				String request = "NEWCLIENT#"+name.getText()+"#"+surname.getText()+"#"+jmbg.getText();
				
				writer.println(request);
				
				String response = reader.readLine();
				
				if(response.split("#")[0].equals("ERROR")) {
					Alert alert = new Alert(AlertType.ERROR, response.split("#")[1] + "!", ButtonType.OK);
					alert.show();
					alert.setTitle("Greska");
					alert.setHeaderText("Greska!");
					if(alert.getResult() == ButtonType.OK) {
						alert.close();
					}
				}else if(response.split("#")[0].equals("OK")) {
					Alert alert = new Alert(AlertType.INFORMATION, response.split("#")[1] + ".",ButtonType.OK);
					alert.setTitle("Informacija");
					alert.setHeaderText("Info");
					alert.showAndWait();
					if(alert.getResult() == ButtonType.OK) {
						alert.close();
						((Node)event.getSource()).getScene().getWindow().hide();
					}
				}
				
				reader.close();
				writer.close();
				socket.close();
				
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
			
		}else {
			if(jmbgIsNumber()==null) {
				jmbgmessage.setText("*Unesite broj!");
			}else {
				message.setText("*Popunite sva polja!");
			}
		}
	}
	
	@FXML
	void cancel(ActionEvent event) {
		((Node)event.getSource()).getScene().getWindow().hide();
	}
	
	@FXML
	void initialize() throws IOException{
		
	}
	
	private Long jmbgIsNumber() {
		try {
			return Long.parseLong(jmbg.getText());
		} catch (Exception e) {
			return null;
		}
	}
	
	
}
