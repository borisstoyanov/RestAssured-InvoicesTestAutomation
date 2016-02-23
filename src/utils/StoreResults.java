package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import org.testng.ITestResult;

public class StoreResults {

	private static Connection getConnection() {

		// Load the JDBC driver
		String driverName = "org.postgresql.Driver";
		Connection connection = null;

		try {
			Class.forName(driverName);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		try {
			connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", "postgres", "admin");
			connection.setCatalog("postgres");
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		return connection;
	}

	public static void insertResults(ITestResult tr) {

		String req = "";
		String resp = "";
		
		PropertyReader prop  = new PropertyReader();
		
		String isStoring = prop.getProperty("storeResults");
		
		if (isStoring.equals("yes")){
			Connection conn = getConnection();

			String testId = Util.getRandomID();
			String testClass = tr.getTestClass().toString();
			String testMethod = tr.getMethod().toString();
			String executionTime = String.valueOf((tr.getEndMillis() - tr.getStartMillis())/1000.0);
			String testDate = Util.getDate();
			if(tr.getAttribute("request")!= null){
				req = tr.getAttribute("request").toString();
			}
			if(tr.getAttribute("resp")!= null){
				resp =  tr.getAttribute("resp").toString();
			}
			String testInstance = tr.getAttribute("test_instance").toString();

			String query = "INSERT INTO test_results ("
					+ " test_id ,"
					+ " execution_date,"
					+ " execution_time, "
					+ " request,"
					+ " response,"
					+ " test_class,"
					+ " test_name,"
					+ " test_instance)"
					
					+"VALUES"
	 					
					+ "('"+testId + "', '"
					+ testDate + "', '"
					+ executionTime + "', '"
					+ req + "', '"
					+ resp + "', '"
					+ testClass + "', '"
					+ testMethod + "', '"
					+ testInstance + "')";

			try { 
				Statement stmt = conn.createStatement();
				stmt.executeUpdate(query);
				stmt.close();
				conn.close();
							
			} catch (SQLException e) {
				System.out.println("There was an exception during storing results! ");
				e.printStackTrace();
			}
			
			if(!tr.isSuccess()){
				System.out.println("TestID is: " + testId);
			}
		}
	}
}
