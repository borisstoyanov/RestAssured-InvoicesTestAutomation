package enums;

public enum Users {
	ADMINISTRATOR(""),
	TESTAPUK_USER("testapuk"),
	DIMITROV("dimitrov");

	private String username;

	private Users(String username) {
		this.username = username;
	}

	public String getUsername() {
		return this.username;
	}

}
