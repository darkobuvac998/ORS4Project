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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.ResourceBundle;

import application.clients.Clients;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Tab;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.Callback;

public class BranchOfficeController implements Initializable {

	@FXML
	private Tab btnT;
	@FXML
	private Label labelNameC;
	@FXML
	private RadioButton rbDeposit;
	@FXML
	private RadioButton rbWithdraw;
	@FXML
	private ToggleGroup tgTransaction;
	@FXML
	private ComboBox<String> chooseSender;
	@FXML
	private ComboBox<String> chooseRecipient;
	@FXML
	private TextField amount;
	@FXML
	private TextField date;
	@FXML
	private Label chooseMsg;
	@FXML
	private Label transactionMsg;
	@FXML
	private Label amountMsg;
	@FXML
	private Label dateMsg;
	@FXML
	private Label message;
	@FXML
	private Button btnDoTransaction;
	@FXML
	private Button btnCancel;
	@FXML
	private Tab btnC;
	@FXML
	private Label labelNameT;
	@FXML
	private TableView<Clients> tableView = new TableView<>();
	@FXML
	private TableColumn<Clients, String> nameC;
	@FXML
	private TableColumn<Clients, String> balanceC;
	@FXML
	private TableColumn<Clients, Button> infC;
	@FXML
	private Button btnNewClient;

	private Socket socket;

	private FXMLLoader loader = new FXMLLoader();
	private Scene scene;

	private static String id;
	private static String name;

	private static ArrayList<Clients> clients = new ArrayList<Clients>();
	private static ArrayList<String> clientsName = new ArrayList<String>();
	private static ObservableList<Clients> list;
	private static ObservableList<String> listCName;

	private ArrayList<Button> buttons = new ArrayList<Button>();

	void showBranchOfficeView(String id, String name) throws IOException {
		BranchOfficeController.id = id;
		BranchOfficeController.name = name;
		loader.setLocation(getClass().getResource("/application/views/BranchOfficeView.fxml"));
		Parent view = (Parent) loader.load();
		scene = new Scene(view);
		scene.getStylesheets().add(getClass().getResource("/application/application.css").toExternalForm());
		Stage stage = new Stage();
		stage.setTitle("EuroBank - " + id + " - " + name);
		stage.setScene(scene);
		stage.setResizable(false);
		stage.show();

		stage.setOnCloseRequest(event -> {
			stage.close();
		});
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		Thread init = new Thread(() -> {
			try {
				socket = new Socket(InetAddress.getByName("127.0.0.1"), 9999);

				BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				PrintWriter writer = new PrintWriter(
						new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);

				String request = "BRANCH" + id;
				writer.println(request);

				labelNameT.setText(name);
				labelNameC.setText(name);

				reader.close();
				writer.close();
				socket.close();

			} catch (IOException e) {
				System.out.println(e.getMessage());
			}

			// inicijalizacija tabele
			clientsView();
			list = FXCollections.observableArrayList(clients);

			nameC.setCellValueFactory(new PropertyValueFactory<Clients, String>("name"));
			balanceC.setCellValueFactory(new PropertyValueFactory<Clients, String>("balance"));

			// -----------------Button
			Callback<TableColumn<Clients, Button>, TableCell<Clients, Button>> cellFactory = new Callback<TableColumn<Clients, Button>, TableCell<Clients, Button>>() {
				@Override
				public TableCell<Clients, Button> call(final TableColumn<Clients, Button> param) {
					final TableCell<Clients, Button> cell = new TableCell<Clients, Button>() {

						final Button btn = new Button("više info");

						public void updateItem(Button item, boolean empty) {
							super.updateItem(item, empty);
							if (empty) {
								setGraphic(null);
								setText(null);
							} else {
								btn.setOnAction(event -> {
									Clients client = getTableView().getItems().get(getIndex());
									try {
										new InfoViewController().showInfoView(client);
									} catch (IOException e) {
										e.printStackTrace();
									}
								});
								setGraphic(btn);
								setText(null);
							}
						}
					};
					return cell;
				}
			};
			infC.setCellFactory(cellFactory);

			tableView.setItems(list);

			btnNewClient.setOnAction(event -> {
				try {
					new NewClientController().showNewClientView();
				} catch (IOException e) {
					e.printStackTrace();
				}
			});

			// cancel button
			btnCancel.setOnAction(event -> {
				amount.clear();
				chooseSender.getSelectionModel().clearSelection();
				chooseRecipient.getSelectionModel().clearSelection();
				rbDeposit.setSelected(false);
				rbWithdraw.setSelected(false);
				date.clear();
				chooseMsg.setText("");
				transactionMsg.setText("");
				dateMsg.setText("");
				amountMsg.setText("");
				message.setText("");
			});

			// combobox setup
			listCName = FXCollections.observableArrayList(clientsName);
			chooseSender.setItems(listCName);
			chooseRecipient.setItems(listCName);

			tgTransaction.selectedToggleProperty().addListener((v, oldValue, newValue) -> {
				if (tgTransaction.getSelectedToggle() == rbWithdraw)
					chooseRecipient.setDisable(true);
				else
					chooseRecipient.setDisable(false);
			});

			// doTransaction button
			btnDoTransaction.setOnAction(event -> {
				chooseMsg.setText("");
				transactionMsg.setText("");
				dateMsg.setText("");
				amountMsg.setText("");
				message.setText("");

				if (isInputCorrect() && isDateCorrect() && isAmountCorrect()) {
					try {
						socket = new Socket(InetAddress.getByName("127.0.0.1"), 9999);
						BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
						PrintWriter writer = new PrintWriter(
								new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);

						String request;
						if (tgTransaction.getSelectedToggle() == rbWithdraw) {
							request = "NEWTRANSACTION#"
									+ clients.get(chooseSender.getSelectionModel().getSelectedIndex()).getJmbg() + "#0#"
									+ amount.getText() + "#" + date.getText() + "#" + id + " - " + name; // 0 za da primlac nije izabran
						} else {
							if (chooseSender.getSelectionModel().isEmpty()) {
								request = "NEWTRANSACTION#0#"
										+ clients.get(chooseRecipient.getSelectionModel().getSelectedIndex()).getJmbg()
										+ "#" + amount.getText() + "#" + date.getText() + "#" + id + " - " + name;
							} else {
								request = "NEWTRANSACTION#"
										+ clients.get(chooseSender.getSelectionModel().getSelectedIndex()).getJmbg()
										+ "#"
										+ clients.get(chooseRecipient.getSelectionModel().getSelectedIndex()).getJmbg()
										+ "#" + amount.getText() + "#" + date.getText() + "#" + id + " - " + name;
							}
						}

						writer.println(request);

						String response = reader.readLine();

						if (response.split("#")[0].equals("ERROR")) {
							Alert alert = new Alert(AlertType.ERROR, response.split("#")[1] + "!", ButtonType.OK);
							alert.show();
							alert.setTitle("Greska");
							alert.setHeaderText("Greska!");
							if (alert.getResult() == ButtonType.OK) {
								alert.close();
							}
						} else if (response.split("#")[0].equals("OK")) {
							Alert alert = new Alert(AlertType.INFORMATION, response.split("#")[1] + ".", ButtonType.OK);
							alert.setTitle("Informacija");
							alert.setHeaderText("Info");
							alert.showAndWait();
							if (alert.getResult() == ButtonType.OK) {
								alert.close();
								updateTable1();
							}
						}

						reader.close();
						writer.close();
						socket.close();

					} catch (Exception e) {
						System.out.println(e.getMessage());
					}

					System.out.println("proslo");
					transactionMsg.setText("");
					chooseMsg.setText("");
					dateMsg.setText("");
					amountMsg.setText("");
					message.setText("");
				} else {
					if (tgTransaction.getSelectedToggle() == null) {
						transactionMsg.setText("*Izaberite opciju!");
					} else if (chooseSender.getSelectionModel().isEmpty()
							&& chooseRecipient.getSelectionModel().isEmpty()) {
						chooseMsg.setText("*Unesite primaoca/pošiljaoca!");
					} else if (tgTransaction.getSelectedToggle() == rbWithdraw
							&& chooseSender.getSelectionModel().isEmpty()) {
						chooseMsg.setText("*Pogrešan unos!");
					} else if (tgTransaction.getSelectedToggle() == rbDeposit
							&& chooseRecipient.getSelectionModel().isEmpty()) {
						chooseMsg.setText("*Pogrešan unos!");
					}else if (amount.getText().isEmpty() || !isAmountCorrect()) {
						amountMsg.setText("*Pogrešan unos!");
					} else if (date.getText().isEmpty() || !isDateCorrect()) {
						dateMsg.setText("*Pogrešan unos!");
					} else if (chooseSender.getSelectionModel().getSelectedItem()
							.equals(chooseRecipient.getSelectionModel().getSelectedItem())) {
						chooseMsg.setText("*Pogrešan unos!");
					} else {
						message.setText("*Popunite sva polja!");
					}
				}
			});
		});
		init.start();
	}

	void clientsView() {
		try {
			socket = new Socket(InetAddress.getByName("127.0.0.1"), 9999);

			BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			PrintWriter writer = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())),
					true);

			String request = "CLIENTS";
			writer.println(request);

			clients.clear();
			String response = reader.readLine();
			if (response.equals("")) {
				System.out.println("prazna");
			} else {
				String[] data = response.split(";");
				for (String s : data) {
					String[] clientsP = s.split("#");
					clients.add(new Clients(clientsP[0], clientsP[1], clientsP[2], clientsP[3]));
					clientsName.add(clients.get(clients.size() - 1).getName());
				}
			}
			reader.close();
			writer.close();
			socket.close();

		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}

	public void updateTable() throws IOException {
		clientsView();
		list.add(clients.get(clients.size() - 1));
		listCName.add(clients.get(clients.size() - 1).getName());
		buttons.add(new Button());
	}

	public void updateTable1() throws IOException {
		clientsView();
		for (int i = 0; i < clients.size(); i++) {
			list.get(i).setBalance(clients.get(i).getBalance());
		}
	}

	private boolean isInputCorrect() {
		if (tgTransaction.getSelectedToggle() == null) {
			return false;
		} else if (tgTransaction.getSelectedToggle() == rbWithdraw) {
			if (chooseSender.getSelectionModel().isEmpty() || amount.getText().isEmpty() || date.getText().isEmpty()) {
				return false;
			} else {
				return true;
			}
		} else {
			if (chooseRecipient.getSelectionModel().isEmpty() || amount.getText().isEmpty()
					|| date.getText().isEmpty()) {
				return false;
			} else {
				if (chooseSender.getSelectionModel().isEmpty())
					return true;
				else if (chooseSender.getSelectionModel().getSelectedItem()
						.equals(chooseRecipient.getSelectionModel().getSelectedItem())) {
					return false;
				}
			}
		}
		return true;
	}

	private boolean isDateCorrect() {
		String pom = date.getText();
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		sdf.setLenient(false);
		try {
			sdf.parse(pom);
			if (pom.substring(pom.lastIndexOf('/') + 1, pom.length()).length() > 4)
				return false;
			else
				return true;
		} catch (Exception e) {
			// e.printStackTrace();
			return false;
		}
	}

	// double sa tackom
	private boolean isAmountCorrect() {
		try {
			Double.parseDouble(amount.getText());
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}

}
