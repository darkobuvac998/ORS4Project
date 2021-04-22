package application.clients;

import javafx.beans.property.SimpleStringProperty;

public class History {

	private SimpleStringProperty jmdbP1 = new SimpleStringProperty();
	private SimpleStringProperty jmdbP2 = new SimpleStringProperty();
	private SimpleStringProperty clientJmdb = new SimpleStringProperty();
	private SimpleStringProperty person = new SimpleStringProperty();
	private SimpleStringProperty amount = new SimpleStringProperty();
	private SimpleStringProperty date = new SimpleStringProperty();
	private SimpleStringProperty branchOffice = new SimpleStringProperty();

	public History(String branchOffice, String jmdbP1, String jmdbP2, String amount, String date, String clientJmdb) {
		setJmbdP1(jmdbP1);
		setJmdbP2(jmdbP2);
		setClientJmdb(clientJmdb);
		setAmount(amount);
		setDate(date);
		setBranchOffice(branchOffice);
		setPerson();
	}

	private void setClientJmdb(String client) {
		this.clientJmdb.set(client);
	}
	
	private void setJmbdP1(String jmdbP1) {
		this.jmdbP1.set(jmdbP1);
	}

	private void setJmdbP2(String jmdbP2) {
		this.jmdbP2.set(jmdbP2);
	}

	private void setAmount(String amount) {
		if(getJmdbP2().equals("0"))
			this.amount.set(amount);
		else if(getJmdbP1().equals(getClientJmdb()))
			this.amount.set("-" + amount);
		else if(getJmdbP2().equals(getClientJmdb()))
			this.amount.set(amount);
	}

	private void setDate(String date) {
		this.date.set(date);
	}
	
	private void setBranchOffice(String branchOffice) {
		this.branchOffice.set(branchOffice);
	}
	
	private void setPerson() {
		if(getJmdbP2().equals("0"))
			this.person.set("-");
		else if(getJmdbP1().equals(getClientJmdb()))
			this.person.set(getJmdbP2());
		else
			this.person.set(getJmdbP1());
	}

	public String getJmdbP1() {
		return this.jmdbP1.get();
	}

	public String getJmdbP2() {
		return this.jmdbP2.get();
	}
	
	public String getClientJmdb() {
		return this.clientJmdb.get();
	}

	public String getAmount() {
		return this.amount.get();
	}

	public String getDate() {
		return this.date.get();
	}
	
	public String getBranchOffice() {
		return this.branchOffice.get();
	}
	
	public String getPerson() {
		return this.person.get();
	}

	public SimpleStringProperty jmdbP1Property() {
		return jmdbP1;
	}

	public SimpleStringProperty jmdbP2Property() {
		return jmdbP2;
	}
	
	public SimpleStringProperty clientJmdbProperty() {
		return clientJmdb;
	}

	public SimpleStringProperty amountProperty() {
		return amount;
	}

	public SimpleStringProperty dateProperty() {
		return date;
	}
	
	public SimpleStringProperty branchOfficeProperty() {
		return branchOffice;
	}
	
	public SimpleStringProperty datePerson() {
		return person;
	}

	@Override
	public String toString() {
		return "Clients [jmdbP1=" + getJmdbP1() + ", jmdbP2=" + getJmdbP2() + ", amount=" + getAmount()
				+ ", date=" + getDate() + "]";
	}
}
