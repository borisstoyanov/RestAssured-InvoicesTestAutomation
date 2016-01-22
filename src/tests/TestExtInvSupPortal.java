package tests;
import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasXPath;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.SkipException;
import org.testng.annotations.AfterGroups;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeGroups;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.response.Response;

import enums.Pass;
import enums.Users;
import requests.ExtInvoiceSupPortRequest;
import requests.RetrieveInvoiceHeaderRequest;
import requests.RetrieveInvoiceInfoRequest;
import requests.RetrieveInvoiceInfoSPRequest;
import utils.DatabaseUtil;
import utils.TestInstance;
import utils.Util;
import utils.WebServiceTest;

public class TestExtInvSupPortal extends WebServiceTest{

	ExtInvoiceSupPortRequest request;
	
	@BeforeGroups(groups = { "2.4.1.0" })
	public void beforeGroups(){
		setup();
	}
	
	@AfterGroups(groups = { "2.4.1.0" })
	public void afterGroups(){
		afterMethod(result);
	}
	
	@BeforeTest
	public void setup(){
		RestAssured.baseURI = TestInstance.getServerName();

	}
	@AfterMethod
	public void afterMethod(ITestResult result) {
	  Util.after(result);
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
		
	@Test(groups = { "2.4.1.0" })
	public void testExternalInvoice(){
		request = new ExtInvoiceSupPortRequest();

		given().request()
		.headers(request.header).auth().basic(Users.DIMITROV.getUsername(), Pass.DIMITROV.getPassword())
		.contentType(request.contentType).body(request.done())
		
		.when().post(request.endpoint).then().
			body(hasXPath("//code", containsString("0"))).
			body(hasXPath("//responseMessage", containsString("Success")));
		
	}
	
	
	@Test(groups = { "2.4.1.0" })
	public void test_1408(){
		
		request = new ExtInvoiceSupPortRequest();
		String req = request.setWorkOrderId("some invalid work Order").done();
		
		Response resp = given().request()
		.headers(request.header).auth().basic(Users.DIMITROV.getUsername(), Pass.DIMITROV.getPassword())
		.contentType(request.contentType).body(req)
		
		.when().post(request.endpoint);
		
		resp.then().statusCode(200).
			body(hasXPath("//code", containsString("ERROR_INPUT_011"))).
			body(hasXPath("//responseMessage", containsString("The provided workOrderId is not valid! ")));
	}
	
	@Test(groups = { "2.4.1.0" })
	public void test_1339(){
		
		//Check if fields are created in the DB
		String query = "SELECT creator_name, invoice_creator FROM INVOICE";
		if(DatabaseUtil.executeQuery(query).equals(null)){
			Assert.fail("Test Failed: creator_name, invoice_creator are not available in INVOICE table.");
		}
		
		
		//Execute an external invoice supplier portal request
		request = new ExtInvoiceSupPortRequest();
		String req = request.done();
		
		Response resp = given().request()
		.headers(request.header).auth().basic(Users.DIMITROV.getUsername(), Pass.DIMITROV.getPassword())
		.contentType(request.contentType).body(req)
		
		.when().post(request.endpoint);
		
		resp.then().statusCode(200).
			body(hasXPath("//code", containsString("0"))).
			body(hasXPath("//responseMessage", containsString("Success")));
		
		
		//Execute retrieve invoice headers request 
		RetrieveInvoiceHeaderRequest retrReq = new RetrieveInvoiceHeaderRequest();
		resp = given().request()
				.contentType(retrReq.contentType).body(retrReq.setInvoiceNumber(request.getInvoiceNumber()).done())
			
			.when()
				.post(retrReq.endpoint);

			Assert.assertTrue(resp.getStatusCode() == 200);		
			Assert.assertTrue(resp.asString().contains("<ns0:SupplierCreatorId>" + request.getSupplierCreatorID() + "</ns0:SupplierCreatorId>"));
	}
	
	@Test(groups = { "2.4.1.0" })
	public void test_1442(){
		
		request = new ExtInvoiceSupPortRequest();
		String req = request.setInvoiceCurrency("$").done();
		
		Response resp = given().request()
		.headers(request.header).auth().basic(Users.DIMITROV.getUsername(), Pass.DIMITROV.getPassword())
		.contentType(request.contentType).body(req)
		
		.when().post(request.endpoint);

		resp.then().statusCode(200).
			body(hasXPath("//code", containsString("ERROR_INPUT_016"))).
			body(hasXPath("//responseMessage", containsString("Invalid Currency")));
	}
	
	
	@Test(groups = { "2.4.1.0" })
	public void test_1455(){
		request = new ExtInvoiceSupPortRequest();
		String req = request.done();
		
		//Create invoice with default supplierCreatorID 
		Response resp = given().request()
		.headers(request.header).auth().basic(Users.DIMITROV.getUsername(), Pass.DIMITROV.getPassword())
		.contentType(request.contentType).body(req)
		
		.when().post(request.endpoint);

		resp.then().statusCode(200).
			body(hasXPath("//code", containsString("0"))).
			body(hasXPath("//responseMessage", containsString("Success")));
		
		
		//RetrieveInvoiceRequest of the same invoice number and get invoiceID
		String invoiceID = getInvoiceID(request);
				
		//RetrieveInvoiceInfoRequest with invoiceID from RetrieveHeaderResponse and check if comment section is concatenated
		resp = getInvoiceInfo(invoiceID);

		Assert.assertTrue(resp.getStatusCode() == 200);		
		Assert.assertTrue(resp.asString().contains("<ns0:SupplierCreatorId>" + request.getSupplierCreatorID() + "</ns0:SupplierCreatorId>"));
	}
	
	@Test(groups = { "2.4.1.0" })
	public void test_1458(){
		
		request = new ExtInvoiceSupPortRequest();
		String req = request.done();
		
		//Create invoice with default supplierCreatorID 
		Response resp = given().request()
		.headers(request.header).auth().basic(Users.DIMITROV.getUsername(), Pass.DIMITROV.getPassword())
		.contentType(request.contentType).body(req)
		
		.when().post(request.endpoint);

		resp.then().statusCode(200).
			body(hasXPath("//code", containsString("0"))).
			body(hasXPath("//responseMessage", containsString("Success")));
		
		
		//RetrieveInvoiceRequest of the same invoice number and get invoiceID
		String invoiceID = getInvoiceID(request);
				
		//RetrieveInvoiceInfoRequest with invoiceID from RetrieveHeaderResponse and check if comment section is concatenated
		resp = getInvoiceInfo(invoiceID);

		Assert.assertTrue(resp.getStatusCode() == 200);		
		Assert.assertTrue(resp.asString().contains("<ns0:Username>" + request.getAuthor() + "</ns0:Username>"));
	}
	
	
	@Test(groups = { "2.4.1.0" })
	public void test_1466(){
		
		request = new ExtInvoiceSupPortRequest();
		String req = request.setDescription("A description").setFullDescription("A full description").done();
		
		//Create invoice with default supplierCreatorID 
		Response resp = given().request()
		.headers(request.header).auth().basic(Users.DIMITROV.getUsername(), Pass.DIMITROV.getPassword())
		.contentType(request.contentType).body(req)
		
		.when().post(request.endpoint);

		resp.then().statusCode(200).
			body(hasXPath("//code", containsString("0"))).
			body(hasXPath("//responseMessage", containsString("Success")));
		
		
		//RetrieveInvoiceRequest of the same invoice number and get invoiceID
		String invoiceID = getInvoiceID(request);
				
		//RetrieveInvoiceInfoRequest with invoiceID from RetrieveHeaderResponse and check if comment section is concatenated
		resp = getInvoiceInfo(invoiceID);

		Assert.assertTrue(resp.getStatusCode() == 200);		
		Assert.assertTrue(resp.asString().contains("<ns0:Description>A description - A full description</ns0:Description>"));
		
		//RetrieveInvoiceInfoSPRequest with to see descriptions appear separately
		RetrieveInvoiceInfoSPRequest retrInfoReq = new RetrieveInvoiceInfoSPRequest();
		req = retrInfoReq.setInvoiceID(invoiceID).done();
		resp = given().request()
				.contentType(retrInfoReq.contentType).body(req)
				
		.when()
			.post(retrInfoReq.endpoint);	
		Assert.assertTrue(resp.asString().contains("<ns0:Description>A description</ns0:Description>"));
		Assert.assertTrue(resp.asString().contains("<ns0:FullDescription>A full description</ns0:FullDescription>"));
		
	}
	
	@Test(groups = { "2.4.1.0" })
	public void test_1479(){
		//Type 'SomeInvalidFlag'
		request = new ExtInvoiceSupPortRequest();
		String req = request.setDocumentTypeFlag("SomeInvalidFlag").done();
		
		Response resp = given().request()
		.headers(request.header).auth().basic(Users.DIMITROV.getUsername(), Pass.DIMITROV.getPassword())
		.contentType(request.contentType).body(req)
		
		.when().post(request.endpoint);

		resp.then().statusCode(200).
			body(hasXPath("//code", containsString("ERROR_INPUT_017"))).
			body(hasXPath("//responseMessage", containsString("Invalid DocumentTypeFlag")));
		
		//Type 'E'
		req = request.setDocumentTypeFlag("E").done();
		
		resp = given().request()
		.headers(request.header).auth().basic(Users.DIMITROV.getUsername(), Pass.DIMITROV.getPassword())
		.contentType(request.contentType).body(req)
		
		.when().post(request.endpoint);

		resp.then().statusCode(200).
			body(hasXPath("//code", containsString("ERROR_INPUT_017"))).
			body(hasXPath("//responseMessage", containsString("Invalid DocumentTypeFlag")));
		
		//Type '#'
		req = request.setDocumentTypeFlag("#").done();
		
		resp = given().request()
		.headers(request.header).auth().basic(Users.DIMITROV.getUsername(), Pass.DIMITROV.getPassword())
		.contentType(request.contentType).body(req)
		
		.when().post(request.endpoint);

		resp.then().statusCode(200).
			body(hasXPath("//code", containsString("ERROR_INPUT_017"))).
			body(hasXPath("//responseMessage", containsString("Invalid DocumentTypeFlag")));
	}
	
	
	@Test(groups = { "2.4.1.0" })
	public void test_1483(){
		
		request = new ExtInvoiceSupPortRequest();
		String req = request.done();
		
		//Create invoice with default supplierCreatorID 
		Response resp = given().request()
		.headers(request.header).auth().basic(Users.DIMITROV.getUsername(), Pass.DIMITROV.getPassword())
		.contentType(request.contentType).body(req)
		
		.when().post(request.endpoint);

		resp.then().statusCode(200).
			body(hasXPath("//code", containsString("0"))).
			body(hasXPath("//responseMessage", containsString("Success")));
		
		
		//RetrieveInvoiceRequest of the same invoice number and get invoiceID
		String invoiceID = getInvoiceID(request);
		
		//RetrieveInvoiceInfoRequest with invoiceID from RetrieveHeaderResponse and check if comment section is concatenated
		resp = getInvoiceInfo(invoiceID);
		
		Assert.assertTrue(resp.getStatusCode() == 200);		
		Assert.assertTrue(resp.asString().contains("<ns0:UploadedBy>" + request.getAuthor() + "</ns0:UploadedBy>"));
		Assert.assertTrue(resp.asString().contains("<ns0:TotalAmount>" + request.getLineItemTotalAmount() + "</ns0:TotalAmount>"));
	}
	
	@Test(groups = { "2.4.1.0" })
	public void test_1498(){
		
		request = new ExtInvoiceSupPortRequest();
		String req = request.done();
		
		//Create invoice with default supplierCreatorID 
		Response resp = given().request()
		.headers(request.header).auth().basic(Users.DIMITROV.getUsername(), Pass.DIMITROV.getPassword())
		.contentType(request.contentType).body(req)
		
		.when().post(request.endpoint);

		resp.then().statusCode(200).
			body(hasXPath("//code", containsString("0"))).
			body(hasXPath("//responseMessage", containsString("Success")));
		
		
		//RetrieveInvoiceRequest of the same invoice number and get invoiceID
		String invoiceID = getInvoiceID(request);
		
		//RetrieveInvoiceInfoRequest with invoiceID from RetrieveHeaderResponse and check if comment section is concatenated
		resp = getInvoiceInfo(invoiceID);
		
		String periodOfCost = Util.getValueFromResponse(resp.asString(), "ns0:ItemDate");
		
		periodOfCost = Util.formatDate(periodOfCost, "MM/yyyy");
		
		Assert.assertTrue(resp.getStatusCode() == 200);		
		Assert.assertTrue(resp.asString().contains("<ns0:PeriodOfCost>" + periodOfCost + "</ns0:PeriodOfCost>"));
	}
	
	@Test(groups = { "2.4.1.0" })
	public void test_1499(){
		
		request = new ExtInvoiceSupPortRequest();
		String req = request.done();
		
		//Create invoice with default supplierCreatorID 
		Response resp = given().request()
		.headers(request.header).auth().basic(Users.DIMITROV.getUsername(), Pass.DIMITROV.getPassword())
		.contentType(request.contentType).body(req)
		
		.when().post(request.endpoint);
		
		resp.then().statusCode(200).
			body(hasXPath("//code", containsString("0"))).
			body(hasXPath("//responseMessage", containsString("Success")));
		
		
		//RetrieveInvoiceRequest of the same invoice number and get invoiceID
		String invoiceID = getInvoiceID(request);
		
		String query = "select action_type from invoice_action_log where invoice_id = " + invoiceID;
		
		ResultSet rs = DatabaseUtil.executeQuery(query);
		
		try {
			Assert.assertTrue(rs.getString("ACTION_TYPE").contains("54"));
		} catch (SQLException e) {
			Assert.fail("SQL Exception during evaluating query");
			e.printStackTrace();
		}
		
		query = "select source from invoice_comment where invoice_id = " + invoiceID;
		
		rs = DatabaseUtil.executeQuery(query);
		
		try {
			Assert.assertTrue(rs.getString("SOURCE").contains("SP"));
		} catch (SQLException e) {
			Assert.fail("SQL Exception during evaluating query");
			e.printStackTrace();
		}
		
		query = "select source from invoice_attachment where invoice_id = " + invoiceID;
		
		rs = DatabaseUtil.executeQuery(query);
		
		try {
			Assert.assertTrue(rs.getString("SOURCE").contains("SP"));
		} catch (SQLException e) {
			Assert.fail("SQL Exception during evaluating query");
			e.printStackTrace();
		}
	}
	
	@Test(groups = { "2.4.1.0" })
	public void test_1549(){
				
		request = new ExtInvoiceSupPortRequest();
		String req = request.done();
		
		//Create invoice with default supplierCreatorID 
		Response resp = given().request()
		.headers(request.header).auth().basic(Users.DIMITROV.getUsername(), Pass.DIMITROV.getPassword())
		.contentType(request.contentType).body(req)
		
		.when().post(request.endpoint);

		resp.then().statusCode(200).
			body(hasXPath("//code", containsString("0"))).
			body(hasXPath("//responseMessage", containsString("Success")));
		
		
		//RetrieveInvoiceRequest of the same invoice number and get invoiceID
		String invoiceID = getInvoiceID(request);
		
		//RetrieveInvoiceInfoRequest with invoiceID from RetrieveHeaderResponse and check if comment section is concatenated
		resp = getInvoiceInfo(invoiceID);
	
		String query = "select * from einvoice_description_mapping"
				+ " where LINE_ITEM_DESCRIPTION = '" + request.getDescription() + "'"
				+ " and ENTITY = " + request.getEntity()
				+ " and VENDOR = " + request.getVendor();
		
		ResultSet rs = DatabaseUtil.executeQuery(query);
	
		Assert.assertTrue(resp.getStatusCode() == 200);		
		
		try {
			Assert.assertTrue(resp.asString().contains("<ns0:GlAccount>" + rs.getString("GL_ACCOUNT_ID") + "</ns0:GlAccount>"));
			Assert.assertTrue(resp.asString().contains("<ns0:CostCenter>" + rs.getString("COST_CENTRE_ID") + "</ns0:CostCenter>"));
			Assert.assertTrue(resp.asString().contains("<ns0:ServiceTypeId>" + rs.getString("SERVICE_TYPE_ID") + "</ns0:ServiceTypeId>"));
		} catch (SQLException e) {
			Assert.fail("Line Item attributes are not the same as with the DB");
			e.printStackTrace();
		}
	}

	@Test(groups = { "2.4.1.0" })
	public void test_1503(){
		
		request = new ExtInvoiceSupPortRequest();
		String req = request.done();
		
		//Create invoice with default supplierCreatorID 
		Response resp = given().request()
		.headers(request.header).auth().basic(Users.DIMITROV.getUsername(), Pass.DIMITROV.getPassword())
		.contentType(request.contentType).body(req)
		
		.when().post(request.endpoint);

		resp.then().statusCode(200).
			body(hasXPath("//code", containsString("0"))).
			body(hasXPath("//responseMessage", containsString("Success")));
		
		String invoiceID = getInvoiceID(request);
		
		checkFileExtension(invoiceID, ".pdf");
		
		
	}
	private void checkFileExtension(String invoiceID, String fileExtension) {
		String query = "select * from invoice_attachment where invoice_id = "+ invoiceID +""; 
		ResultSet rs = DatabaseUtil.executeQuery(query);
		try {
			rs.next();
			String q = rs.getString("DESCRIPTION");
			Assert.assertTrue(q.contains(fileExtension));
		} catch (SQLException e) {
			e.printStackTrace();
			throw new SkipException("SQL Exception");
		}		
	}
		
	private  String createInvoiceWithFileExtension(String fileType) {
		request = new ExtInvoiceSupPortRequest();
		String req = request.setFileType(fileType).done();
		
		//Create invoice with default supplierCreatorID 
		Response resp = given().request()
		.headers(request.header).auth().basic(Users.DIMITROV.getUsername(), Pass.DIMITROV.getPassword())
		.contentType(request.contentType).body(req)
		
		.when().post(request.endpoint);
	
		resp.then().statusCode(200).
			body(hasXPath("//code", containsString("0"))).
			body(hasXPath("//responseMessage", containsString("Success")));
		
		return getInvoiceID(request);		
	}
	
	@Test(groups = { "2.4.1.0" })
	public void test_1506_1(){
		String invoiceID = createInvoiceWithFileExtension("application/pdf");
		checkFileExtension(invoiceID, ".pdf");
	}
	
	@Test(groups = { "2.4.1.0" })
	public void test_1506_2(){
		String invoiceID = createInvoiceWithFileExtension("image/jpeg");
		checkFileExtension(invoiceID, ".jpeg");
	}
	
	@Test(groups = { "2.4.1.0" })
	public void test_1506_3(){
		String invoiceID = createInvoiceWithFileExtension("image/png");
		checkFileExtension(invoiceID, ".png");
	}
	
	@Test(groups = { "2.4.1.0" })
	public void test_1506_4(){
		String invoiceID = createInvoiceWithFileExtension("text/plain");
		checkFileExtension(invoiceID, ".txt");
	}
	
	@Test(groups = { "2.4.1.0" })
	public void test_1506_5(){
		String invoiceID = createInvoiceWithFileExtension("image/gif");
		checkFileExtension(invoiceID, ".gif");
	}
	
	@Test(groups = { "2.4.1.0" })
	public void test_1506_6(){
		String invoiceID = createInvoiceWithFileExtension("application/msword");
		checkFileExtension(invoiceID, ".doc");
	}
	
	@Test(groups = { "2.4.1.0" })
	public void test_1506_7(){
		String invoiceID = createInvoiceWithFileExtension("text/html");
		checkFileExtension(invoiceID, ".html");
	}
	
	@Test(groups = { "2.4.1.0" })
	public void test_1506_8(){
		String invoiceID = createInvoiceWithFileExtension("application/vnd.openxmlformats-officedocument.wordprocessingml.document");
		checkFileExtension(invoiceID, ".docx");
	}
	
	@Test(groups = { "2.4.1.0" })
	public void test_1506_9(){
		String invoiceID = createInvoiceWithFileExtension("application/vnd.ms-powerpoint");
		checkFileExtension(invoiceID, ".ppt");
	}
	
	@Test(groups = { "2.4.1.0" })
	public void test_1506_10(){
		String invoiceID = createInvoiceWithFileExtension("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
		checkFileExtension(invoiceID, ".xlsx");
	}
	
	@Test(groups = { "2.4.1.0" })
	public void test_1506_11(){
		String invoiceID = createInvoiceWithFileExtension("application/vnd.ms-excel");
		checkFileExtension(invoiceID, ".xls");
	}
	
	@Test(groups = { "2.4.1.0" })
	public void test_1506_12(){
		String invoiceID = createInvoiceWithFileExtension("image/tiff");
		checkFileExtension(invoiceID, ".tiff");
	}
	
	@Test(groups = { "2.4.1.0" })
	public void test_1506_13(){
		String invoiceID = createInvoiceWithFileExtension("application/vnd.openxmlformats-officedocument.presentationml.presentation");
		checkFileExtension(invoiceID, ".pptx");
	}
	
	@Test(groups = { "2.4.1.0" })
	public void test_1506_14(){
		String invoiceID = createInvoiceWithFileExtension("application/vnd.ms-outlook");
		checkFileExtension(invoiceID, ".msg");
	}
		
}
