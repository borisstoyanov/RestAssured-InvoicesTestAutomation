package tests;
import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasXPath;

import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.response.Response;

import enums.Pass;
import enums.Users;
import requests.ExtInvoiceSupPortRequest;
import requests.RetrieveInvoiceHeaderRequest;
import utils.StoreResults;
import utils.TestInstance;

public class TestInvoiceHeaders{
	
	RetrieveInvoiceHeaderRequest request;
	
	@BeforeClass(alwaysRun = true)
	public void setup(){
		request= new RetrieveInvoiceHeaderRequest();

		RestAssured.baseURI = TestInstance.getServerName();
		
		ExtInvoiceSupPortRequest createInvoiceRequest = new ExtInvoiceSupPortRequest();
		given().request()
		.headers(createInvoiceRequest.header).auth().basic(Users.TESTAPUK_USER.getUsername(), Pass.TESTAPUK_PASS.getPassword())

		.contentType(createInvoiceRequest.contentType)
		
			.body(createInvoiceRequest.done())
		
		.when().post(createInvoiceRequest.endpoint).then().
			body(hasXPath("//code", containsString("0"))).
			body(hasXPath("//responseMessage", containsString("Success")));
		
		request.setInvoiceNumber(createInvoiceRequest.getInvoiceNumber());
	}

	@AfterMethod(alwaysRun = true)
	public void tearDown(ITestResult tr) {
		tr.setAttribute("test_instance", RestAssured.baseURI);
		StoreResults.insertResults(tr);

	}

	protected void setRequest(String request) {
		ITestResult result = Reporter.getCurrentTestResult();
		result.setAttribute("request", request);
	}

	protected void setResponse(String response){
		ITestResult result = Reporter.getCurrentTestResult();
		result.setAttribute("resp", response);
		
	}
	
	@Test(groups = { "2.4.1.0" })
	public void test_1368(){
		String req = request.done();
		setRequest(req);
		Response resp = given().request()
			.contentType(request.contentType).body(req)
		
		.when()
			.post(request.endpoint);
		setResponse(resp.asString());
		
		Assert.assertTrue(resp.asString().contains("<ns0:InvoiceCreationType>SPortal Invoice</ns0:InvoiceCreationType>")
				, "Response does not contain CreationType");		
		Assert.assertTrue(resp.asString().contains("<ns0:SupplierCreatorId>estafetdev3</ns0:SupplierCreatorId>")
				, "Response does not contain SupplierCreatorID");
		Assert.assertTrue(resp.asString().contains("<ns2:TotalRegistries>")
				, "Response does not contain one record");
		}
	
	@Test(groups = { "2.4.1.0" })
	public void test_1413_1427(){

		String req = request.done();
		setRequest(req);

		Response resp = given().request()
			.contentType(request.contentType).body(req)
		
		.when()
			.post(request.endpoint);
		setResponse(resp.asString());

		Assert.assertTrue(resp.asString().contains("<ns0:SapClearingDate xsi:nil=\"true\"/>")
				, "Response does not contain one SapClearingDate");
	}
	
	@Test(groups = { "2.4.1.0" })
	public void test_1432(){
		String req = request.setDocumentType("KR").done();
		setRequest(req);

		Response resp = given().request()
			.contentType(request.contentType).body(req)
		
		.when()
			.post(request.endpoint);
		setResponse(resp.asString());

		Assert.assertTrue(resp.asString().contains("<ns0:DocumentType>KR</ns0:DocumentType>")
				, "Response does not contain KR as DocType");

	}

	@Test( groups = { "2.4.1.0"})
	public void test_1656(){
		String req = request.done();
		setRequest(req);

		Response resp = given().request()
				.contentType(request.contentType).body(req)
				
				
				.when().post(request.endpoint);
		setResponse(resp.asString());

		Assert.assertTrue(resp.statusCode() == 200);
		Assert.assertTrue(resp.asString().contains("VendorName"));
			
	}
}
