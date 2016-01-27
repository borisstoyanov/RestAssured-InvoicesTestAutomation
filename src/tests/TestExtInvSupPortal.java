package tests;
import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasXPath;

import java.sql.ResultSet;
import java.sql.SQLException;

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
import requests.RetrieveInvoiceInfoSPRequest;
import utils.DatabaseUtil;
import utils.TestInstance;
import utils.Util;
import utils.WebServiceTest;

public class TestExtInvSupPortal extends WebServiceTest{

	ExtInvoiceSupPortRequest request;
	

	
	@BeforeClass(alwaysRun = true)
	public void setup(){
		RestAssured.baseURI = TestInstance.getServerName();
	}

	private String getInvoiceID(ExtInvoiceSupPortRequest req){
		RetrieveInvoiceHeaderRequest retrReq = new RetrieveInvoiceHeaderRequest();
		Response resp = given().request()
			.contentType(retrReq.contentType).body(retrReq.setInvoiceNumber(req.getInvoiceNumber()).done())
			
		.when()
			.post(retrReq.endpoint);

		Assert.assertTrue(resp.getStatusCode() == 200, "Status Code was not 200, it was: " + resp.getStatusCode());	
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
	public void test_1408(){
		
		request = new ExtInvoiceSupPortRequest();
		String req = request.setWorkOrderId("some invalid work Order").done();
		
		Response resp = given().request()
		.headers(request.header).auth().basic(Users.DIMITROV.getUsername(), Pass.DIMITROV.getPassword())
		.contentType(request.contentType).body(req)
		
		.when().post(request.endpoint);
		
		Assert.assertTrue(resp.asString().contains("ERROR_INPUT_011")
				, "ErrorCode did not matched.");
		Assert.assertTrue(resp.asString().contains("The provided workOrderId is not valid")
				, "ErrorMessage did not matched.");
		
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
		Assert.assertTrue(resp.asString().contains("ERROR_INPUT_016")
					, "ErrorCode did not matched.");
		Assert.assertTrue(resp.asString().contains("Invalid Currency")
				, "ErrorMessage did not matched.");
	}
	
	
	
	
	@Test(groups = { "2.4.1.0" })
	public void test_1466_1532(){
		
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
		//String s = resp.asString();
		Assert.assertTrue(resp.getStatusCode() == 200);		
		Assert.assertTrue(resp.asString().contains("<ns0:ItemDescription>A description - A full description</ns0:ItemDescription>")
				, "ItemDescription did not matched");
		
		//RetrieveInvoiceInfoSPRequest with to see descriptions appear separately
		RetrieveInvoiceInfoSPRequest retrInfoReq = new RetrieveInvoiceInfoSPRequest();
		req = retrInfoReq.setInvoiceID(invoiceID).done();
		resp = given().request()
				.contentType(retrInfoReq.contentType).body(req)
				
		.when()
			.post(retrInfoReq.endpoint);	
		//s = resp.asString();
		Assert.assertTrue(resp.asString().contains("<ns0:Description>A description</ns0:Description>")
				, "Description did not matched");
		Assert.assertTrue(resp.asString().contains("<ns0:ItemDescription xsi:nil=\"true\"/>")
				, "ItemDescription did not matched");
		Assert.assertTrue(resp.asString().contains("<ns0:FullDescription>A full description</ns0:FullDescription>")
				, "FullDescription did not matched");
		
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

		Assert.assertTrue(resp.asString().contains("ERROR_INPUT_017")
				, "ErrorCode is not presented");
		Assert.assertTrue(resp.asString().contains("Invalid DocumentTypeFlag")
				, "ErrorMessage is not presented");
		
		//Type 'E'
		req = request.setDocumentTypeFlag("E").done();
		
		resp = given().request()
		.headers(request.header).auth().basic(Users.DIMITROV.getUsername(), Pass.DIMITROV.getPassword())
		.contentType(request.contentType).body(req)
		
		.when().post(request.endpoint);
		
		Assert.assertTrue(resp.asString().contains("ERROR_INPUT_017")
				, "ErrorCode is not presented");
		Assert.assertTrue(resp.asString().contains("Invalid DocumentTypeFlag")
				, "ErrorMessage is not presented");
		
		//Type '#'
		req = request.setDocumentTypeFlag("#").done();
		
		resp = given().request()
		.headers(request.header).auth().basic(Users.DIMITROV.getUsername(), Pass.DIMITROV.getPassword())
		.contentType(request.contentType).body(req)
		
		.when().post(request.endpoint);

		Assert.assertTrue(resp.asString().contains("ERROR_INPUT_017")
				, "ErrorCode is not presented");
		Assert.assertTrue(resp.asString().contains("Invalid DocumentTypeFlag")
				, "ErrorMessage is not presented");
	}
		
	@Test(groups = { "2.4.1.0" })
	public void test_1615(){
		//Type 'SomeInvalidFlag'
		request = new ExtInvoiceSupPortRequest();
		String req = request.setPeriodOfCost("").setItemDate("").done();
		
		Response resp = given().request()
		.headers(request.header).auth().basic(Users.DIMITROV.getUsername(), Pass.DIMITROV.getPassword())
		.contentType(request.contentType).body(req)
		
		.when().post(request.endpoint);

		
		Assert.assertTrue(resp.getStatusCode() == 200, "Status Code was not 200, it was: " + resp.getStatusCode());		

		Assert.assertTrue(resp.asString().contains("<code>0</code>")
				, "ErrorCode did not match");
		Assert.assertTrue(resp.asString().contains("<responseMessage>Success</responseMessage>")
				, "ErrorMessage did not match");
			
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
		checkFileExtension(invoiceID, ".jpg");
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
