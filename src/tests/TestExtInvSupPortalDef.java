package tests;

import static com.jayway.restassured.RestAssured.given;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.response.Response;

import enums.Pass;
import enums.Users;
import requests.ExtInvoiceSupPortRequest;
import requests.RetrieveInvoiceHeaderRequest;
import requests.RetrieveInvoiceInfoRequest;
import utils.DatabaseUtil;
import utils.TestInstance;
import utils.Util;
import utils.WebServiceTest;

public class TestExtInvSupPortalDef extends WebServiceTest{

	ExtInvoiceSupPortRequest request;
	
	
	@BeforeMethod(alwaysRun = true)
	public void setup(){
		RestAssured.baseURI = TestInstance.getServerName();
		
		request = new ExtInvoiceSupPortRequest();
		String req = request.done();
		
		Response resp = given().request()
		.headers(request.header).auth().basic(Users.DIMITROV.getUsername(), Pass.DIMITROV.getPassword())
		
		.contentType(request.contentType).body(req)
		
		.when().post(request.endpoint);
		
		Assert.assertTrue(resp.getStatusCode() == 200, resp.asString() + "\n" + req);		
		Assert.assertTrue(resp.asString().contains("Success"), resp.asString());	
				
	}

	private String getInvoiceID(ExtInvoiceSupPortRequest req){
		RetrieveInvoiceHeaderRequest retrReq = new RetrieveInvoiceHeaderRequest();
		Response resp = given().request()
			.contentType(retrReq.contentType).body(retrReq.setInvoiceNumber(req.getInvoiceNumber()).done())
			
		.when()
			.post(retrReq.endpoint);

		Assert.assertTrue(resp.getStatusCode() == 200);		
		String invoiceID = Util.getValueFromResponse(resp.asString(), "ns0:InvoiceId");
		return invoiceID;
		
	}
	
	private Response getInvoiceInfo(String invoiceID) {
		RetrieveInvoiceInfoRequest retrInfoReq = new RetrieveInvoiceInfoRequest();
		
		Response resp = given().request()
				.contentType(retrInfoReq.contentType).body(retrInfoReq.setInvoiceID(invoiceID).done())
				
		.when()
			.post(retrInfoReq.endpoint);		
		return resp;
	}
	
	
	private void checkFileExtension(String invoiceID, String fileExtension) {
		String query = "select * from invoice_attachment where invoice_id = "+ invoiceID +""; 
		ResultSet rs = DatabaseUtil.executeQuery(query);
		try {
			rs.next();
			String q = rs.getString("DESCRIPTION");
			Assert.assertTrue(q.contains(fileExtension), "File Extension did not match");
		} catch (SQLException e) {
			e.printStackTrace();
			throw new SkipException("SQL Exception");
		}		
	}

	
}
