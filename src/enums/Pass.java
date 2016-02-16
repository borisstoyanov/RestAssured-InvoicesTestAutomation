package enums;

public enum Pass {
	ADMINISTRATOR_PASSWORD(""),
	TESTAPUK_PASS("Welcome1"),
	DIMITROV("Tainaparola1");

	private String password;

	private Pass(String passwords) {
		this.password = passwords;
	}

	public String getPassword() {
		return this.password;
	}
}
