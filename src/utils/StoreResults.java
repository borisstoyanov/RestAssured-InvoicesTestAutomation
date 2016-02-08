package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Map;

public class StoreResults {
	public Connection conn;
   // private String db = ;

	public Connection getConnection() {

		// Load the JDBC driver
		String driverName = "org.postgresql.Driver";
		Connection connection = null;
		try {
			Class.forName(driverName);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		// Create a connection to the database
		Map<String, String> serverName = TestInstance.getDBConString();

		String url = "jdbc:oracle:thin:@//" + serverName.get("connString");
		// String url = "jdbc:oracle:thin:@//" + serverName.get("connString");
		String username = serverName.get("dbUsername");
		String password = serverName.get("dbPass");

		
//		try {
//			//conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/"+db,"postgres","211271");
//		} catch (SQLException e1) {
			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
        //conn.setCatalog(db);
        //System.out.println("Connection with: "+db+"!!");
		
		
		try {
			connection = DriverManager.getConnection(url, username, password);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return connection;
	}
}
