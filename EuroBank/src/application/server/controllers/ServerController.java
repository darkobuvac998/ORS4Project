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
	@FXML
	private Button close;
	@FXML
	private Button clear;
	
	private final int SERVER_PORT = 9999;
	private ServerSocket serverSocket;
	
	@FXML
	void start(ActionEvent event) {
		start.setDisable(true);
		stop.setDisable(false);
		labelName.setStyle("-fx-text-fill: green");
		Thread server = new Thread(()->{
			try {
				textArea.appendText("================ Server je pokrenut ================ \n");
				serverSocket = new ServerSocket(SERVER_PORT);
				serverSocket.setReuseAddress(true);
				while(true) {
					textArea.appendText("================ Cekanje zahtjeva ================ \n");
					Socket socket = serverSocket.accept();
					ServerThread serverThread = new ServerThread(socket);
					serverThread.start();
					try {
						Thread.sleep(500);
					} catch (Exception e) {}
					textArea.appendText("== Zahtjev klijenta: "+serverThread.getRequest() + "\n");
					textArea.appendText("== Odgovor servera: "+ serverThread.getResponse() + "\n");
				}
				
			} catch (Exception e) {
//				textArea.appendText(e.getMessage() + "\n");
			}
			
		});
		server.start();
	}
	@FXML
	void stop(ActionEvent event) throws IOException{
		serverSocket.close();
		start.setDisable(false);
		textArea.appendText("================ Server je ugasen ================ \n");
		stop.setDisable(true);
		labelName.setStyle("-fx-text-fill: black");
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
	void close(ActionEvent event) throws IOException{
		if(serverSocket!=null && !serverSocket.isClosed()) {
			serverSocket.close();
		}
		System.exit(0);
	}
	
	@FXML
	void clear(ActionEvent event) {
		textArea.clear();
	}
	
	@FXML
	void initialize() throws Exception{
		stop.setDisable(true);
		textArea.setDisable(true);
		start.getTooltip().setShowDelay(new javafx.util.Duration(200));
		stop.getTooltip().setShowDelay(new javafx.util.Duration(200));
		clear.getTooltip().setShowDelay(new javafx.util.Duration(200));
	}
	
}
