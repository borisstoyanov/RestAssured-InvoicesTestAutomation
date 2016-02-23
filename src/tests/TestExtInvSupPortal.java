package tests;
import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasXPath;

import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
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
import utils.StoreResults;
import utils.TestInstance;
import utils.Util;
import utils.WebServiceTest;

public class TestExtInvSupPortal extends WebServiceTest{
	
	@BeforeMethod(alwaysRun = true)
	public void setup(){
		RestAssured.baseURI = TestInstance.getServerName();
	}
	
	@AfterMethod(alwaysRun = true)
	public void tearDown(ITestResult tr) {
		tr.setAttribute("test_instance", RestAssured.baseURI);
		StoreResults.insertResults(tr);

	}

	private void setResponse(String response) {

		ITestResult result = Reporter.getCurrentTestResult();
		result.setAttribute("resp", response);
	}
	
	private void setRequest(String request) {

		ITestResult result = Reporter.getCurrentTestResult();
		result.setAttribute("request", request);
	}

	private String getInvoiceID(ExtInvoiceSupPortRequest req){
		RetrieveInvoiceHeaderRequest retrReq = new RetrieveInvoiceHeaderRequest();
		setRequest(retrReq.setInvoiceNumber(req.getInvoiceNumber()).done());
		
		Response resp = given().request()
			.contentType(retrReq.contentType).body(retrReq.setInvoiceNumber(req.getInvoiceNumber()).done())
			
		.when()
			.post(retrReq.endpoint);
		setResponse(resp.asString());

		Assert.assertTrue(resp.getStatusCode() == 200, "Status Code was not 200, it was: " + resp.getStatusCode());	
		String invoiceID = Util.getValueFromResponse(resp.asString(), "ns0:InvoiceId");
		return invoiceID;
		
	}
	
	private Response getInvoiceInfo(String invoiceID) {
		RetrieveInvoiceInfoRequest retrInfoReq = new RetrieveInvoiceInfoRequest();
		
		String req = retrInfoReq.setInvoiceID(invoiceID).done();
		setRequest(req);
		Response resp = given().request()
				.contentType(retrInfoReq.contentType).body(req)
				
		.when()
			.post(retrInfoReq.endpoint);		
		return resp;
	}
	
	
	private void checkFileExtension(String invoiceID, String fileExtension) {
			String query = "select * from invoice_attachment where invoice_id = "+ invoiceID +" order by creation_date desc"; 
			String rs = DatabaseUtil.executeQuery(query, "DESCRIPTION");
			
			System.out.println("INVOICEID = " + invoiceID + " Current Extension = " + fileExtension + ", DB extension = " + rs);
			Assert.assertTrue(rs.contains(fileExtension), "File Extension did not match.");
					
		}

	private  String createInvoiceWithFileExtension(String fileType) {
		ExtInvoiceSupPortRequest request;
	
		request = new ExtInvoiceSupPortRequest();
		String req = request.setFileType(fileType).done();
		setRequest(req);
		
		//Create invoice with default supplierCreatorID 
		Response resp = given().request()
				.headers(request.header).auth().basic(Users.TESTAPUK_USER.getUsername(), Pass.TESTAPUK_PASS.getPassword())
		.contentType(request.contentType).body(req)
		
		.when().post(request.endpoint);
	
		setResponse(resp.asString());
		resp.then().statusCode(200).
			body(hasXPath("//code", containsString("0"))).
			body(hasXPath("//responseMessage", containsString("Success")));
		
		return getInvoiceID(request);		
	}

	@Test(groups = { "2.4.1.0" })
	public void test_1339(){
		
		ExtInvoiceSupPortRequest request = new ExtInvoiceSupPortRequest();
		
		String req = request.done();
		Response resp = given().request()
		.headers(request.header).auth().basic(Users.TESTAPUK_USER.getUsername(), Pass.TESTAPUK_PASS.getPassword())
		
		.contentType(request.contentType).body(req)
		
		.when().post(request.endpoint);
	
		Assert.assertTrue(resp.getStatusCode() == 200, "Request is: " + req + "\nResponse is: " + resp.asString() );		
		Assert.assertTrue(resp.asString().contains("Success"), resp.asString());	
		
		
		//Check if fields are created in the DB
		String query = "SELECT creator_name, invoice_creator FROM INVOICE";
		if(DatabaseUtil.executeQuery(query, "CREATOR_NAME").equals(null)){
			Assert.fail("Test Failed: creator_name, invoice_creator are not available in INVOICE table.");
		}
		
		//Execute retrieve invoice headers request 
		RetrieveInvoiceHeaderRequest retrReq = new RetrieveInvoiceHeaderRequest();
		req = retrReq.setInvoiceNumber(request.getInvoiceNumber()).done();
		setRequest(req);
		
		resp = given().request()
				.contentType(retrReq.contentType).body(req)
			
			.when()
				.post(retrReq.endpoint);
		setResponse(resp.asString());
	
			Assert.assertTrue(resp.getStatusCode() == 200);		
			Assert.assertTrue(resp.asString().contains("<ns0:SupplierCreatorId>" + request.getSupplierCreatorID() + "</ns0:SupplierCreatorId>")
					, "SupplierCreatorID did not matched");
	}

	@Test(groups = { "2.4.1.0" })
	public void test_1442(){
		ExtInvoiceSupPortRequest request;
		Response resp;
		
		request = new ExtInvoiceSupPortRequest();
		String req = request.setInvoiceCurrency("$").done();
		setRequest(req);
		
		resp = given().request()
				.headers(request.header).auth().basic(Users.TESTAPUK_USER.getUsername(), Pass.TESTAPUK_PASS.getPassword())
		.contentType(request.contentType).body(req)
		
		.when().post(request.endpoint);
		setResponse(resp.asString());

		resp.then().statusCode(200);

		Assert.assertTrue(resp.asString().contains("ERROR_INPUT_016")
					, "ErrorCode did not matched.");
		Assert.assertTrue(resp.asString().contains("Invalid Currency")
				, "ErrorMessage did not matched.");
	}
	
	
	
	
	@Test(groups = { "2.4.1.0" })
	public void test_1455(){
		ExtInvoiceSupPortRequest request;
	
		request = new ExtInvoiceSupPortRequest();
		String req = request.done();
		
		
		Response resp = given().request()
				.headers(request.header).auth().basic(Users.TESTAPUK_USER.getUsername(), Pass.TESTAPUK_PASS.getPassword())
		
		.contentType(request.contentType).body(req)
		
		.when().post(request.endpoint);
		
		Assert.assertTrue(resp.getStatusCode() == 200, resp.asString() + "\n" + req);		
		Assert.assertTrue(resp.asString().contains("Success"), resp.asString());	
		
		//RetrieveInvoiceRequest of the same invoice number and get invoiceID
		String invoiceID = getInvoiceID(request);
				
		//RetrieveInvoiceInfoRequest with invoiceID from RetrieveHeaderResponse and check if comment section is concatenated
		resp = getInvoiceInfo(invoiceID);
		setResponse(resp.asString());
		
		Assert.assertTrue(resp.getStatusCode() == 200);		
		Assert.assertTrue(resp.asString().contains("<ns0:SupplierCreatorId>" + request.getSupplierCreatorID() + "</ns0:SupplierCreatorId>")
				, "SupplierCreatorID did not matched.");
	}

	@Test(groups = { "2.4.1.0" })
	public void test_1458(){
		ExtInvoiceSupPortRequest request;
	
		request = new ExtInvoiceSupPortRequest();
		String req = request.done();
		
		Response resp = given().request()
				.headers(request.header).auth().basic(Users.TESTAPUK_USER.getUsername(), Pass.TESTAPUK_PASS.getPassword())
		
		.contentType(request.contentType).body(req)
		
		.when().post(request.endpoint);
		
		Assert.assertTrue(resp.getStatusCode() == 200, resp.asString() + "\n" + req);		
		Assert.assertTrue(resp.asString().contains("Success"), resp.asString());	
		
		//RetrieveInvoiceRequest of the same invoice number and get invoiceID
		String invoiceID = getInvoiceID(request);
				
		//RetrieveInvoiceInfoRequest with invoiceID from RetrieveHeaderResponse and check if comment section is concatenated
		resp = getInvoiceInfo(invoiceID);
		setResponse(resp.asString());
	
		Assert.assertTrue(resp.getStatusCode() == 200);		
		Assert.assertTrue(resp.asString().contains("<ns0:Username>" + request.getAuthor() + "</ns0:Username>")
				, "Author did not matched");
	}

	@Test(groups = { "2.4.1.0" })
	public void test_1466_1532(){
		ExtInvoiceSupPortRequest request;
		Response resp;
		String req = "";
		
		request = new ExtInvoiceSupPortRequest();
		req = request.setDescription("A description").setFullDescription("A full description").done();
		
		//Create invoice with default supplierCreatorID 
		resp = given().request()
				.headers(request.header).auth().basic(Users.TESTAPUK_USER.getUsername(), Pass.TESTAPUK_PASS.getPassword())
		.contentType(request.contentType).body(req)
		
		.when().post(request.endpoint);

		resp.then().statusCode(200).
			body(hasXPath("//code", containsString("0"))).
			body(hasXPath("//responseMessage", containsString("Success")));
		
		
		//RetrieveInvoiceRequest of the same invoice number and get invoiceID
		String invoiceID = getInvoiceID(request);
				
		//RetrieveInvoiceInfoRequest with invoiceID from RetrieveHeaderResponse and check if comment section is concatenated
		resp = getInvoiceInfo(invoiceID);
		//String s = resp.asString();
		Assert.assertTrue(resp.getStatusCode() == 200);		
		//Assert.assertTrue(resp.asString().contains("<ns0:ItemDescription>A description - A full description</ns0:ItemDescription>")
				//, "ItemDescription did not matched");
		
		//RetrieveInvoiceInfoSPRequest with to see descriptions appear separately
		RetrieveInvoiceInfoSPRequest retrInfoReq = new RetrieveInvoiceInfoSPRequest();
		req = retrInfoReq.setInvoiceID(invoiceID).done();
		setRequest(req);
		resp = given().request()
				.contentType(retrInfoReq.contentType).body(req)
				
		.when()
			.post(retrInfoReq.endpoint);	
		setResponse(resp.asString());
		
		Assert.assertTrue(resp.asString().contains("<ns0:Description>A description</ns0:Description>")
				, "Description did not matched");
		Assert.assertTrue(resp.asString().contains("<ns0:ItemDescription xsi:nil=\"true\"/>")
				, "ItemDescription did not matched");
		Assert.assertTrue(resp.asString().contains("<ns0:FullDescription>A full description</ns0:FullDescription>")
				, "FullDescription did not matched");
		
	}
	
	@Test(groups = { "2.4.1.0" })
	public void test_1479(){
		ExtInvoiceSupPortRequest request;
		Response resp;
		String req = "";
		//Type 'SomeInvalidFlag'
		request = new ExtInvoiceSupPortRequest();
		req = request.setDocumentTypeFlag("SomeInvalidFlag").done();
		
		resp = given().request()
				.headers(request.header).auth().basic(Users.TESTAPUK_USER.getUsername(), Pass.TESTAPUK_PASS.getPassword())
		.contentType(request.contentType).body(req)
		
		.when().post(request.endpoint);

		Assert.assertTrue(resp.asString().contains("ERROR_INPUT_017")
				, "ErrorCode is not presented");
		Assert.assertTrue(resp.asString().contains("Invalid DocumentTypeFlag")
				, "ErrorMessage is not presented");
		
		//Type 'E'
		req = request.setDocumentTypeFlag("E").done();
		
		resp = given().request()
				.headers(request.header).auth().basic(Users.TESTAPUK_USER.getUsername(), Pass.TESTAPUK_PASS.getPassword())
		.contentType(request.contentType).body(req)
		
		.when().post(request.endpoint);
		
		Assert.assertTrue(resp.asString().contains("ERROR_INPUT_017")
				, "ErrorCode is not presented");
		Assert.assertTrue(resp.asString().contains("Invalid DocumentTypeFlag")
				, "ErrorMessage is not presented");
		
		//Type '#'
		req = request.setDocumentTypeFlag("#").done();
		setRequest(req);
		
		resp = given().request()
				.headers(request.header).auth().basic(Users.TESTAPUK_USER.getUsername(), Pass.TESTAPUK_PASS.getPassword())
		.contentType(request.contentType).body(req)
		
		.when().post(request.endpoint);
		setResponse(resp.asString());

		Assert.assertTrue(resp.asString().contains("ERROR_INPUT_017")
				, "ErrorCode is not presented");
		Assert.assertTrue(resp.asString().contains("Invalid DocumentTypeFlag")
				, "ErrorMessage is not presented");
	}
		
	@Test(groups = { "2.4.1.0" })
	public void test_1483(){
		ExtInvoiceSupPortRequest request;

		request = new ExtInvoiceSupPortRequest();
		String req = request.done();
		
		Response resp = given().request()
				.headers(request.header).auth().basic(Users.TESTAPUK_USER.getUsername(), Pass.TESTAPUK_PASS.getPassword())
		
		.contentType(request.contentType).body(req)
		
		.when().post(request.endpoint);
		
		Assert.assertTrue(resp.getStatusCode() == 200, resp.asString() + "\n" + req);		
		Assert.assertTrue(resp.asString().contains("Success"), resp.asString());	
		
		//RetrieveInvoiceRequest of the same invoice number and get invoiceID
		String invoiceID = getInvoiceID(request);
		
		//RetrieveInvoiceInfoRequest with invoiceID from RetrieveHeaderResponse and check if comment section is concatenated
		resp = getInvoiceInfo(invoiceID);
		setResponse(resp.asString());
		
		Assert.assertTrue(resp.getStatusCode() == 200);		
		Assert.assertTrue(resp.asString().contains("<ns0:UploadedBy>" + request.getAuthor() + "</ns0:UploadedBy>")
				, "UploadedBy does not match");
		Assert.assertTrue(resp.asString().contains("<ns0:TotalAmount>" + request.getLineItemTotalAmount() + "</ns0:TotalAmount>")
				, "TotalAmount does not match");
	}
	
	@Test(groups = { "2.4.1.0" })
	public void test_1498(){
		ExtInvoiceSupPortRequest request;

		request = new ExtInvoiceSupPortRequest();
		String req = request.done();
		
		Response resp = given().request()
				.headers(request.header).auth().basic(Users.TESTAPUK_USER.getUsername(), Pass.TESTAPUK_PASS.getPassword())
		
		.contentType(request.contentType).body(req)
		
		.when().post(request.endpoint);
		
		Assert.assertTrue(resp.getStatusCode() == 200, resp.asString() + "\n" + req);		
		Assert.assertTrue(resp.asString().contains("Success"), resp.asString());	
		
		//RetrieveInvoiceRequest of the same invoice number and get invoiceID
		String invoiceID = getInvoiceID(request);
		
		//RetrieveInvoiceInfoRequest with invoiceID from RetrieveHeaderResponse and check if comment section is concatenated
		resp = getInvoiceInfo(invoiceID);
		setResponse(resp.asString());
		
		String periodOfCost = Util.getValueFromResponse(resp.asString(), "ns0:ItemDate");
		
		periodOfCost = Util.formatDate(periodOfCost, "MM/yyyy");
		//String asstr = resp.asString();
		Assert.assertTrue(resp.getStatusCode() == 200);		
		Assert.assertTrue(resp.asString().contains("<ns0:PeriodOfCost>" + periodOfCost + "</ns0:PeriodOfCost>")
				, "Response does not match Period of Cost");
	}
	
	@Test(groups = { "2.4.1.0" })
	public void test_1499(){
		ExtInvoiceSupPortRequest request;

		request = new ExtInvoiceSupPortRequest();
		String req = request.done();
		setRequest(req);
		
		Response resp = given().request()
				.headers(request.header).auth().basic(Users.TESTAPUK_USER.getUsername(), Pass.TESTAPUK_PASS.getPassword())
		
		.contentType(request.contentType).body(req)
		
		.when().post(request.endpoint);
		setResponse(resp.asString());

		Assert.assertTrue(resp.getStatusCode() == 200, resp.asString() + "\n" + req);		
		Assert.assertTrue(resp.asString().contains("Success"), resp.asString());	
		
		//RetrieveInvoiceRequest of the same invoice number and get invoiceID
		String invoiceID = getInvoiceID(request);
		
		String query = "select action_type from invoice_action_log where invoice_id = " + invoiceID;
		
		String rs = DatabaseUtil.executeQuery(query, "ACTION_TYPE");
		
		Assert.assertTrue(rs.contains("54"), "ActionType is not 54");
		
		
		query = "select source from invoice_comment where invoice_id = " + invoiceID;
		
		rs = DatabaseUtil.executeQuery(query, "SOURCE");
		
		Assert.assertTrue(rs.contains("SP"), "Source did not has SP");
				
		query = "select source from invoice_attachment where invoice_id = " + invoiceID;
		
		rs = DatabaseUtil.executeQuery(query, "SOURCE");
		
		Assert.assertTrue(rs.contains("SP"), "Source did not has SP");

	}
	
	@Test(groups = { "2.4.1.0" })
	public void test_1503(){
		ExtInvoiceSupPortRequest request;

		request = new ExtInvoiceSupPortRequest();
		String req = request.done();
		setRequest(req);
		Response resp = given().request()
				.headers(request.header).auth().basic(Users.TESTAPUK_USER.getUsername(), Pass.TESTAPUK_PASS.getPassword())
		
		.contentType(request.contentType).body(req)
		
		.when().post(request.endpoint);
		
		Assert.assertTrue(resp.getStatusCode() == 200, resp.asString() + "\n" + req);		
		Assert.assertTrue(resp.asString().contains("Success"), resp.asString());
		
		
		String invoiceID = getInvoiceID(request);
		setResponse(resp.asString());

		checkFileExtension(invoiceID, ".pdf");
		
		
	}

	@Test(groups = { "2.4.1.0" })
	public void test_1506_1(){
		String invoiceID = createInvoiceWithFileExtension("application/pdf");
		checkFileExtension(invoiceID, ".pdf");
	}

	@Test(groups = { "2.4.1.0" })
	public void test_1506_2(){
		String invoiceID = createInvoiceWithFileExtension("image/jpeg");
		checkFileExtension(invoiceID, ".jpg");
	}

	@Test(groups = { "2.4.1.0" })
	public void test_1506_3(){
		String invoiceID = createInvoiceWithFileExtension("image/png");
		checkFileExtension(invoiceID, ".png");
	}

	@Test(groups = { "2.4.1.0" })
	public void test_1506_4(){
		String invoiceID = createInvoiceWithFileExtension("text/html");
		checkFileExtension(invoiceID, ".html");
	}

	@Test(groups = { "2.4.1.0" })
	public void test_1506_5(){
		String invoiceID = createInvoiceWithFileExtension("application/vnd.openxmlformats-officedocument.wordprocessingml.document");
		checkFileExtension(invoiceID, ".docx");
	}

	@Test(groups = { "2.4.1.0" })
	public void test_1506_6(){
		String invoiceID = createInvoiceWithFileExtension("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
		checkFileExtension(invoiceID, ".xlsx");
	}

	@Test(groups = { "2.4.1.0" })
	public void test_1506_7(){
		String invoiceID = createInvoiceWithFileExtension("application/vnd.openxmlformats-officedocument.presentationml.presentation");
		checkFileExtension(invoiceID, ".pptx");
	}

	@Test(groups = { "2.4.1.0" })
	public void test_1506_8(){
		String invoiceID = createInvoiceWithFileExtension("application/vnd.ms-outlook");
		checkFileExtension(invoiceID, ".msg");
	}

	@Test(groups = { "2.4.1.0" })
	public void test_1549(){
		ExtInvoiceSupPortRequest request;
	
		request = new ExtInvoiceSupPortRequest();
		String req = request.done();
		
		Response resp = given().request()
				.headers(request.header).auth().basic(Users.TESTAPUK_USER.getUsername(), Pass.TESTAPUK_PASS.getPassword())
		
		.contentType(request.contentType).body(req)
		
		.when().post(request.endpoint);
		
		Assert.assertTrue(resp.getStatusCode() == 200, resp.asString() + "\n" + req);		
		Assert.assertTrue(resp.asString().contains("Success"), resp.asString());	
				
		//RetrieveInvoiceRequest of the same invoice number and get invoiceID
		String invoiceID = getInvoiceID(request);
		
		//RetrieveInvoiceInfoRequest with invoiceID from RetrieveHeaderResponse and check if comment section is concatenated
		resp = getInvoiceInfo(invoiceID);
		setResponse(resp.asString());

	
		String query = "select * from einvoice_description_mapping"
				+ " where LINE_ITEM_DESCRIPTION = '" + request.getDescription() + "'"
				+ " and ENTITY = " + request.getEntity()
				+ " and VENDOR = " + request.getVendor();
		
		String glAccount = DatabaseUtil.executeQuery(query, "GL_ACCOUNT_ID");
		String costCenter = DatabaseUtil.executeQuery(query, "COST_CENTRE_ID");
		String serviceType = DatabaseUtil.executeQuery(query, "SERVICE_TYPE_ID");

		Assert.assertTrue(resp.getStatusCode() == 200);		
		
		Assert.assertTrue(resp.asString().contains("<ns0:GlAccount>" + glAccount + "</ns0:GlAccount>"),
					"response does not match GLAccount");
		Assert.assertTrue(resp.asString().contains("<ns0:CostCenter>" + costCenter + "</ns0:CostCenter>"),
					"response does not match Cost Center");
		Assert.assertTrue(resp.asString().contains("<ns0:ServiceTypeId>" + serviceType + "</ns0:ServiceTypeId>"),
					"response does not match ServiceTypeID");
		
	}

	@Test(groups = { "2.4.1.0" })
	public void test_1620(){
		ExtInvoiceSupPortRequest request;
		Response resp;
		String req = "";
		//Type 'SomeInvalidFlag'
		request = new ExtInvoiceSupPortRequest();
		req = request.setPeriodOfCost("").setItemDate("").done();
		setRequest(req);
		resp = given().request()
				.headers(request.header).auth().basic(Users.TESTAPUK_USER.getUsername(), Pass.TESTAPUK_PASS.getPassword())
		.contentType(request.contentType).body(req)
		
		.when().post(request.endpoint);
		setResponse(resp.asString());

		
		Assert.assertTrue(resp.getStatusCode() == 200, "Status Code was not 200, it was: " + resp.getStatusCode());		
	
		Assert.assertTrue(resp.asString().contains("<code>0</code>")
				, "ErrorCode did not match");
		Assert.assertTrue(resp.asString().contains("<responseMessage>Success</responseMessage>")
				, "ErrorMessage did not match");
			
	}
		
}
