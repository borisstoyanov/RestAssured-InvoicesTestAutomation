package tests;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasXPath;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.omg.Messaging.SyncScopeHelper;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.BeforeClass;
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

public class TestExtInvSupPortalDef {

	ExtInvoiceSupPortRequest request;
	

	
	@BeforeClass(alwaysRun = true)
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
		
		//Execute an external invoice supplier portal request
//		request = new ExtInvoiceSupPortRequest();
//		String req = request.done();
//		Response resp = given().request()
//				.header(request.h).auth().basic(Users.DIMITROV.getUsername(), Pass.DIMITROV.getPassword())
//				.body(req)
//				
//				.when().post(request.endpoint);
//				
//				Assert.assertTrue(resp.getStatusCode() == 200, resp.asString() + "\n" + req);		
//				Assert.assertTrue(resp.asString().contains("Success"), resp.asString());		
				
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

	@Test(groups = { "2.4.1.0" })
	public void test_1339(){
		
		//Check if fields are created in the DB
		String query = "SELECT creator_name, invoice_creator FROM INVOICE";
		if(DatabaseUtil.executeQuery(query).equals(null)){
			Assert.fail("Test Failed: creator_name, invoice_creator are not available in INVOICE table.");
		}
		
		//Execute retrieve invoice headers request 
		RetrieveInvoiceHeaderRequest retrReq = new RetrieveInvoiceHeaderRequest();
		Response resp = given().request()
				.contentType(retrReq.contentType).body(retrReq.setInvoiceNumber(request.getInvoiceNumber()).done())
			
			.when()
				.post(retrReq.endpoint);

			Assert.assertTrue(resp.getStatusCode() == 200);		
			Assert.assertTrue(resp.asString().contains("<ns0:SupplierCreatorId>" + request.getSupplierCreatorID() + "</ns0:SupplierCreatorId>")
					, "SupplierCreatorID did not matched");
	}
	
	@Test(groups = { "2.4.1.0" })
	public void test_1455(){
				
		//RetrieveInvoiceRequest of the same invoice number and get invoiceID
		String invoiceID = getInvoiceID(request);
				
		//RetrieveInvoiceInfoRequest with invoiceID from RetrieveHeaderResponse and check if comment section is concatenated
		Response resp = getInvoiceInfo(invoiceID);

		Assert.assertTrue(resp.getStatusCode() == 200);		
		Assert.assertTrue(resp.asString().contains("<ns0:SupplierCreatorId>" + request.getSupplierCreatorID() + "</ns0:SupplierCreatorId>")
				, "SupplierCreatorID did not matched.");
	}
	
	@Test(groups = { "2.4.1.0" })
	public void test_1458(){
		
		//RetrieveInvoiceRequest of the same invoice number and get invoiceID
		String invoiceID = getInvoiceID(request);
				
		//RetrieveInvoiceInfoRequest with invoiceID from RetrieveHeaderResponse and check if comment section is concatenated
		Response resp = getInvoiceInfo(invoiceID);

		Assert.assertTrue(resp.getStatusCode() == 200);		
		Assert.assertTrue(resp.asString().contains("<ns0:Username>" + request.getAuthor() + "</ns0:Username>")
				, "Author did not matched");
	}
	@Test(groups = { "2.4.1.0" })
	public void test_1483(){
		
		//RetrieveInvoiceRequest of the same invoice number and get invoiceID
		String invoiceID = getInvoiceID(request);
		
		//RetrieveInvoiceInfoRequest with invoiceID from RetrieveHeaderResponse and check if comment section is concatenated
		Response resp = getInvoiceInfo(invoiceID);
		
		Assert.assertTrue(resp.getStatusCode() == 200);		
		Assert.assertTrue(resp.asString().contains("<ns0:UploadedBy>" + request.getAuthor() + "</ns0:UploadedBy>")
				, "UploadedBy does not match");
		Assert.assertTrue(resp.asString().contains("<ns0:TotalAmount>" + request.getLineItemTotalAmount() + "</ns0:TotalAmount>")
				, "TotalAmount does not match");
	}
	
	@Test(groups = { "2.4.1.0" })
	public void test_1498(){
		
		//RetrieveInvoiceRequest of the same invoice number and get invoiceID
		String invoiceID = getInvoiceID(request);
		
		//RetrieveInvoiceInfoRequest with invoiceID from RetrieveHeaderResponse and check if comment section is concatenated
		Response resp = getInvoiceInfo(invoiceID);
		
		String periodOfCost = Util.getValueFromResponse(resp.asString(), "ns0:ItemDate");
		
		periodOfCost = Util.formatDate(periodOfCost, "MM/yyyy");
		
		Assert.assertTrue(resp.getStatusCode() == 200);		
		Assert.assertTrue(resp.asString().contains("<ns0:PeriodOfCost>" + periodOfCost + "</ns0:PeriodOfCost>")
				, "Response does not match Period of Cost");
	}
	
	@Test(groups = { "2.4.1.0" })
	public void test_1499(){
		
		//RetrieveInvoiceRequest of the same invoice number and get invoiceID
		String invoiceID = getInvoiceID(request);
		
		String query = "select action_type from invoice_action_log where invoice_id = " + invoiceID;
		
		ResultSet rs = DatabaseUtil.executeQuery(query);
		
		try {
			Assert.assertTrue(rs.getString("ACTION_TYPE").contains("54"), "ActionType is not 54");
		} catch (SQLException e) {
			Assert.fail("SQL Exception during evaluating query");
			e.printStackTrace();
		}
		
		query = "select source from invoice_comment where invoice_id = " + invoiceID;
		
		rs = DatabaseUtil.executeQuery(query);
		
		try {
			Assert.assertTrue(rs.getString("SOURCE").contains("SP"), "Source did not has SP");
		} catch (SQLException e) {
			Assert.fail("SQL Exception during evaluating query");
			e.printStackTrace();
		}
		
		query = "select source from invoice_attachment where invoice_id = " + invoiceID;
		
		rs = DatabaseUtil.executeQuery(query);
		
		try {
			Assert.assertTrue(rs.getString("SOURCE").contains("SP"), "Source did not has SP");
		} catch (SQLException e) {
			Assert.fail("SQL Exception during evaluating query");
			e.printStackTrace();
		}
	}
	
	@Test(groups = { "2.4.1.0" })
	public void test_1549(){
				
		//RetrieveInvoiceRequest of the same invoice number and get invoiceID
		String invoiceID = getInvoiceID(request);
		
		//RetrieveInvoiceInfoRequest with invoiceID from RetrieveHeaderResponse and check if comment section is concatenated
		Response resp = getInvoiceInfo(invoiceID);
	
		String query = "select * from einvoice_description_mapping"
				+ " where LINE_ITEM_DESCRIPTION = '" + request.getDescription() + "'"
				+ " and ENTITY = " + request.getEntity()
				+ " and VENDOR = " + request.getVendor();
		
		ResultSet rs = DatabaseUtil.executeQuery(query);
	
		Assert.assertTrue(resp.getStatusCode() == 200);		
		
		try {
			Assert.assertTrue(resp.asString().contains("<ns0:GlAccount>" + rs.getString("GL_ACCOUNT_ID") + "</ns0:GlAccount>"),
					"response does not match GLAccount");
			Assert.assertTrue(resp.asString().contains("<ns0:CostCenter>" + rs.getString("COST_CENTRE_ID") + "</ns0:CostCenter>"),
					"response does not match Cost Center");
			Assert.assertTrue(resp.asString().contains("<ns0:ServiceTypeId>" + rs.getString("SERVICE_TYPE_ID") + "</ns0:ServiceTypeId>"),
					"response does not match ServiceTypeID");
		} catch (SQLException e) {
			Assert.fail("Line Item attributes are not the same as with the DB");
			e.printStackTrace();
		}
	}

	@Test(groups = { "2.4.1.0" })
	public void test_1503(){
		String invoiceID = getInvoiceID(request);
		
		checkFileExtension(invoiceID, ".pdf");
		
		
	}
}
