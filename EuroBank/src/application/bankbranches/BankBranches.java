package application.bankbranches;

public class BankBranches {
	
	private String id;
	private String name;
	private String street;
	private String workingTime;
	
	public BankBranches(String id, String name, String street, String workingTime) {
		super();
		this.id = id;
		this.name = name;
		this.street = street;
		this.workingTime = workingTime;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getWorkingTime() {
		return workingTime;
	}

	public void setWorkingTime(String workingTime) {
		this.workingTime = workingTime;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
}
