package application.controllers;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.SplitMenuButton;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class BranchOfficeController {
	
	@FXML
    private Tab btnT;
    @FXML
    private Label labelNameC;
    @FXML
    private RadioButton payment;
    @FXML
    private RadioButton withdrawal;
    @FXML
    private SplitMenuButton chooseSender;
    @FXML
    private SplitMenuButton chooseRecipient;
    @FXML
    private TextField amount;
    @FXML
    private DatePicker date;
    @FXML
    private Button btnDoTransaction;
    @FXML
    private Tab btnC;
    @FXML
    private Label labelNameT;
    @FXML
    private TableColumn<?, ?> listing;
    @FXML
    private TableColumn<?, ?> balance;
    @FXML
    private TableColumn<?, ?> inf;
    @FXML
    private Button btnNewClient;

    @FXML
    void addNewClient(ActionEvent event) {

    }

    @FXML
    void doTransaction(ActionEvent event) {

    }
	
    private Socket socket;
	
	private FXMLLoader loader = new FXMLLoader();
	private Scene scene;
	
	private static String id;
	 
	void showBranchOfficeView(String id, String name) throws IOException {
		BranchOfficeController.id = id;
		loader.setLocation(getClass().getResource("/application/views/BranchOfficeView.fxml"));
		Parent view = (Parent)loader.load();
		scene = new Scene(view);
		scene.getStylesheets().add(getClass().getResource("/application/application.css").toExternalForm());
		Stage stage = new Stage();
		stage.setTitle("EuroBank - Filijala" + id + " (" + name + ")");
		stage.setScene(scene);
		stage.setResizable(false);
		stage.show();
		
	}
	
	@FXML //Method called from FXMLloader after initialization 
	void initialize() throws IOException {
		Thread init = new Thread(()->{
			try {
				socket = new Socket(InetAddress.getByName("127.0.0.1"), 9999);
				
				BufferedReader reader = new BufferedReader(new InputStreamReader(
						socket.getInputStream()));
				PrintWriter writer = new PrintWriter(new BufferedWriter(
						new OutputStreamWriter(socket.getOutputStream())), true);
				
				String request = "BRANCH" + id;
				writer.println(request);
				
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
//jmbg#ime#prezime#saldo
//history
