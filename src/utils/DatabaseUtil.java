package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import org.testng.SkipException;

public class DatabaseUtil {
	static ResultSet res;

	private static Connection conn() {
		// Load the JDBC driver
		String driverName = "oracle.jdbc.OracleDriver";
		Connection connection = null;
		try {
			Class.forName(driverName);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		// Create a connection to the database
		Map<String, String> serverName = TestInstance.getDBConString();

		String url =  "jdbc:oracle:thin:@//" + serverName.get("connString");
		String username = serverName.get("dbUsername");
		String password = serverName.get("dbPass"); 

		try {
			connection = DriverManager.getConnection(url, username, password);
			//System.out.println(url + username + password);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return connection;
	}

	/*
	 * 	Executes a query that is selecting "Count"  
	 */
	public static String executeQueryCounts(String query) {
		Connection connection = conn();

		ResultSet rs;
		try {

			java.sql.Statement stmt = connection.createStatement();
			rs = stmt.executeQuery(query);

			while (rs.next()) {
				return rs.getString("COUNT(*)");

			}
			
			connection.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return null;
	}
	
	/*
	 * 	Executes a query and returns a ResultSet
	 */
	public static String executeQuery(String query, String columnName) {
		Connection connection = conn();
		String result = null;
		ResultSet rs = null;
		try {

			java.sql.Statement stmt = connection.createStatement();
			rs = stmt.executeQuery(query);

			while (rs.next()) {
				result =  rs.getString(columnName);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				connection.close();
				rs.close();

			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return result;
	}
	
	public static void insertComment(String invoiceID, String comment) {
		Connection connection = conn();
		String query = "INSERT INTO INVOICE_COMMENT (invoice_comment_id,invoice_id,username,display_name,date_time,comment_text,SOURCE)"
						+"VALUES                    ("+Util.generateNumber() +"," + invoiceID + ",'stoyanov','borisstoyanov','27-JAN-16 13.19.11.000000000','" + comment + "',null)";
		try {

			java.sql.Statement stmt = connection.createStatement();
			stmt.executeQuery(query);
			stmt.close();
			connection.close();

		} catch (SQLException e) {
			e.printStackTrace();
			throw new SkipException("Insert comment failed");
		}
		
	}

}
