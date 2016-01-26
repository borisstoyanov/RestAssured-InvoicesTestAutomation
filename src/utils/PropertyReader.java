package utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertyReader {

	static Properties prop = new Properties();
	InputStream input;
	 

	public String getProperty(String property) {
		String p = "";

		try {
			input = getClass().getResourceAsStream("/config.properties");

			// load a properties file
			prop.load(input); 

			// get the property value and print it out
			p = prop.getProperty(property);
			

		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return p;
	}

}
