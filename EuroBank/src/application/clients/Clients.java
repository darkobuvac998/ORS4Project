package application.clients;

import javafx.beans.property.SimpleStringProperty;

public class Clients {

	private SimpleStringProperty jmbg = new SimpleStringProperty();
	private SimpleStringProperty firstName = new SimpleStringProperty();
	private SimpleStringProperty lastName = new SimpleStringProperty();
	private SimpleStringProperty balance = new SimpleStringProperty();
	private SimpleStringProperty name = new SimpleStringProperty();

	public Clients(String jmbg, String firstName, String lastName, String balance) {
		setJmbg(jmbg);
		setFirstName(firstName);
		setLastName(lastName);
		setBalance(balance);
		setName(firstName, lastName);
	}

	private void setJmbg(String jmbg) {
		this.jmbg.set(jmbg);
	}

	private void setFirstName(String firstName) {
		this.firstName.set(firstName);
	}

	private void setLastName(String lastName) {
		this.lastName.set(lastName);
	}

	private void setName(String firstName, String lastName) {
		this.name.set(firstName + " " + lastName);
	}

	public void setBalance(String balance) {
		this.balance.set(balance);
	}

	public String getJmbg() {
		return this.jmbg.get();
	}

	public String getFirstName() {
		return this.firstName.get();
	}

	public String getLastName() {
		return this.lastName.get();
	}

	public String getName() {
		return this.name.get();
	}

	public String getBalance() {
		return this.balance.get();
	}

	public SimpleStringProperty jmbgProperty() {
		return jmbg;
	}

	public SimpleStringProperty firstNameProperty() {
		return firstName;
	}

	public SimpleStringProperty lastNameProperty() {
		return lastName;
	}

	public SimpleStringProperty nameProperty() {
		return name;
	}

	public SimpleStringProperty balanceProperty() {
		return balance;
	}

	@Override
	public String toString() {
		return "Clients [jmbg=" + getJmbg() + ", firstName=" + getFirstName() + ", lastName=" + getLastName()
				+ ", balance=" + getBalance() + "]";
	}
	
}
