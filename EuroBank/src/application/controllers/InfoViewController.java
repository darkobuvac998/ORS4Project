package application.controllers;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;

import application.clients.Clients;
import application.clients.History;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class InfoViewController {

	@FXML
	private TableView<History> tableView;
	@FXML
	private TableColumn<History, String> branchOfficeC;
	@FXML
	private TableColumn<History, String> dateC;
	@FXML
	private TableColumn<History, String> amountC;
	@FXML
	private TableColumn<History, String> personC;
	@FXML
	private Label nameL;
	@FXML
	private Label surnameL;
	@FXML
	private Label jmbgL;
	@FXML
	private Label balanceL;
	@FXML
	private Button btnClose;

	@FXML
	void close(ActionEvent event) throws IOException {
		((Node) event.getSource()).getScene().getWindow().hide();
		if (!socket.isClosed()) {
			socket.close();
		}
	}

	private Socket socket;

	private FXMLLoader loader = new FXMLLoader();
	private Scene scene;

	private static Clients client;

	void showInfoView(Clients clientP) throws IOException {
		client = (Clients) clientP;

		loader.setLocation(getClass().getResource("/application/views/InfoView.fxml"));
		Parent view = loader.load();
		scene = new Scene(view);
		Stage stage = new Stage();
		stage.setScene(scene);
		stage.setTitle("EuroBank - Klijent info");
		stage.setResizable(false);
		stage.show();

		stage.setOnCloseRequest(event -> {
			stage.close();
		});
	}

	private ArrayList<History> info = new ArrayList<History>();
	private ObservableList<History> list;

	@FXML
	void initialize() throws IOException {
		nameL.setText(client.getFirstName());
		surnameL.setText(client.getLastName());
		jmbgL.setText(client.getJmbg());
		balanceL.setText(client.getBalance());
		try {
			socket = new Socket(InetAddress.getByName("127.0.0.1"), 9999);

			BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			PrintWriter writer = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())),
					true);

			String request = "CLIENTSINFO";
			writer.println(request);
			String response = reader.readLine();
			if (response.equals("")) {
				System.out.println("prazna");
			} else {
				String[] data = response.split(";");
				for (String s : data) {
					String[] inf = s.split("#");
					if ((inf[0].equals(client.getJmbg()))
							|| ((inf[1].matches("[0-9]*") == true) && (inf[1].equals(client.getJmbg())))) {
						if (inf.length == 4) {
							info.add(new History(inf[3], inf[0], "0", inf[1], inf[2], client.getJmbg()));
						} else
							info.add(new History(inf[4], inf[0], inf[1], inf[2], inf[3], client.getJmbg()));
					}
				}
			}
			reader.close();
			writer.close();
			socket.close();

		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
		// ubacimo u tabelu
		list = FXCollections.observableArrayList(info);

		branchOfficeC.setCellValueFactory(new PropertyValueFactory<History, String>("branchOffice"));
		dateC.setCellValueFactory(new PropertyValueFactory<History, String>("date"));
		amountC.setCellValueFactory(new PropertyValueFactory<History, String>("amount"));
		personC.setCellValueFactory(new PropertyValueFactory<History, String>("person"));

		tableView.setItems(list);
		
		branchOfficeC.setReorderable(false);
		dateC.setReorderable(false);
		amountC.setReorderable(false);
		personC.setReorderable(false);
	}
}
