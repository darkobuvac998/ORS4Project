package application.server.controllers;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;


import application.server.ServerThread;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

public class ServerController {
	
	@FXML
	private ResourceBundle resource;
	@FXML
	private URL location;
	@FXML
	private Label labelName;
	@FXML
	private TextArea textArea;
	@FXML
	private Button start;
	@FXML
	private Button stop;
	
	private final int SERVER_PORT = 9999;
	private ServerSocket serverSocket;
	
	@FXML
	void start(ActionEvent event) {
		start.setDisable(true);
		labelName.setStyle("-fx-text-fill: green");
		Thread server = new Thread(()->{
			try {
				textArea.appendText("================ Server je pokrenut ================ \n");
				while(true) {
					textArea.appendText("Cekanje na zahtjev klijenta... \n");
					Socket socket = serverSocket.accept();
//					new ServerThread(socket).start();
					ServerThread serverThread = new ServerThread(socket);
					serverThread.start();
					try {
						Thread.sleep(500);
					} catch (Exception e) {}
					textArea.appendText("Zahtjev klijenta: "+serverThread.getRequest() + "\n");
					textArea.appendText("Odgovor servera: "+ serverThread.getResponse() + "\n");
				}
				
			} catch (Exception e) {
				textArea.appendText(e.getMessage());
			}
			
		});
		server.start();
	}
	@FXML
	void stop(ActionEvent event) throws IOException{
		serverSocket.close();
		System.exit(0);
	}
	@FXML
	void onMouseEntered() {
		String style = labelName.getStyle();
		if(style.equals("-fx-text-fill: green")) {
			labelName.setStyle("-fx-text-fill: red");
		}
	}
	@FXML
	void onMouseExited() {
		String style = labelName.getStyle();
		if(style.equals("-fx-text-fill: red")) {
			labelName.setStyle("-fx-text-fill: green");
		}
	}
	
	@FXML
	void initialize() throws Exception{
		serverSocket = new ServerSocket(SERVER_PORT);
		serverSocket.setReuseAddress(true);
		textArea.setDisable(true);
		start.getTooltip().setShowDelay(new javafx.util.Duration(200));
		stop.getTooltip().setShowDelay(new javafx.util.Duration(200));
	}
	
}
