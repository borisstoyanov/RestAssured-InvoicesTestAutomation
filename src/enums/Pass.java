package enums;

public enum Pass {
	ADMINISTRATOR_PASSWORD(""), DIMITROV("Tainaparola1");

	private String password;

	private Pass(String passwords) {
		this.password = passwords;
	}

	public String getPassword() {
		return this.password;
	}
}
