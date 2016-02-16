package utils;

import java.util.HashMap;
import java.util.Map;

import org.testng.SkipException;

public class TestInstance {

	public TestInstance() {

	}

	public static String getServerName() {
		PropertyReader prop  = new PropertyReader();
		
		String serverName = prop.getProperty("customProp");

		switch (serverName) {
		case "UAT-HA":
			serverName = "http://bpmuat.vistajet.local";
			break;
		case "uat-ha":
			serverName = "http://bpmuat.vistajet.local";
			break;
		case "UAT":
			serverName = "http://bpmuat115.vistajet.local:8001";
			break;
		case "uat":
			serverName = "http://bpmuat115.vistajet.local:8001";
			break;
		case "DEV":
			serverName = "http://bpmdev116.vistajet.local:8001";
			break;
		case "dev":
			serverName = "http://bpmdev116.vistajet.local:8001";
			break;
		case "QA":
			serverName = "http://bpmuat115.vistajet.local:8201";
			break;
		case "qa":
			serverName = "http://bpmuat115.vistajet.local:8201";
			break;

		default:
			throw new SkipException("Skipping Test: TestInstance is not configured properly");

		}
		return serverName;
	}

	public static Map<String, String> getDBConString() {
		PropertyReader prop  = new PropertyReader();
		
		String testEnv = prop.getProperty("customProp");		
		Map<String, String> conDetails = new HashMap<>();
		
		
		switch (testEnv) {
		case "UAT-HA":
			conDetails.put("connString", "UKDCORACLEUAT-SCAN:1521/VJUAT");
			conDetails.put("dbUsername", "VJINVOICES");
			conDetails.put("dbPass", "VJINVOICES");
			break;
		case "uat-ha":
			conDetails.put("connString", "UKDCORACLEUAT-SCAN:1521/VJUAT");
			conDetails.put("dbUsername", "VJINVOICES");
			conDetails.put("dbPass", "VJINVOICES");
			break;		
		case "UAT":
			conDetails.put("connString", "192.168.201.94:1521/orcl");
			conDetails.put("dbUsername", "vjinvoices");
			conDetails.put("dbPass", "vjinvoices");
			break;
		case "uat":
			conDetails.put("connString", "192.168.201.94:1521/orcl");
			conDetails.put("dbUsername", "vjinvoices");
			conDetails.put("dbPass", "vjinvoices");
			break;
		case "DEV":
			conDetails.put("connString", "192.168.201.95:1521/orcl");
			conDetails.put("dbUsername", "VJINVOICES");
			conDetails.put("dbPass", "VJINVOICES");
			break;
		case "dev":
			conDetails.put("connString", "192.168.201.95:1521/orcl");
			conDetails.put("dbUsername", "VJINVOICES");
			conDetails.put("dbPass", "VJINVOICES");
			break;
		case "QA":
			conDetails.put("connString", "192.168.201.94:1521/orclqa");
			conDetails.put("dbUsername", "VJINVOICES");
			conDetails.put("dbPass", "VJINVOICES");
			break;
		case "qa":
			conDetails.put("connString", "192.168.201.94:1521/orclqa");
			conDetails.put("dbUsername", "VJINVOICES");
			conDetails.put("dbPass", "VJINVOICES");
			break;

		default:
			throw new SkipException("Skipping Test: DB Connection string is not configured properly");

		}
		return conDetails;
	}

}
