package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class StoreResults {
	public static Connection conn;
   // private String db = ;

	public static Connection getConnection() {

		// Load the JDBC driver
		String driverName = "org.postgresql.Driver";
		Connection connection = null;
		
		try {
			Class.forName(driverName);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		try {
			conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres","postgres","admin");
			conn.setCatalog("postgres");
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
        
        System.out.println("Connection with: postgres!!");
		
		return connection;
	}
}
