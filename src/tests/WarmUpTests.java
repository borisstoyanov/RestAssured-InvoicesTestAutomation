package tests;

import static com.jayway.restassured.RestAssured.given;

import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.SkipException;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.response.Response;

import enums.Pass;
import enums.Users;
import requests.ExtInvoiceSupPortRequest;
import requests.RetrieveInvoiceHeaderRequest;
import utils.StoreResults;
import utils.TestInstance;

public class WarmUpTests {
	
	@BeforeMethod(alwaysRun = true)
	public void setup(){
		RestAssured.baseURI = TestInstance.getServerName();
	}
	
	@AfterMethod(alwaysRun = true)
	public void tearDown(ITestResult tr) {
		tr.setAttribute("test_instance", RestAssured.baseURI);
		StoreResults.insertResults(tr);
	}
	
	public void setResponse(String response) {

		ITestResult result = Reporter.getCurrentTestResult();
		result.setAttribute("resp", response);
	}
	
	public void setRequest(String request) {

		ITestResult result = Reporter.getCurrentTestResult();
		result.setAttribute("request", request);
	}

	@Test(groups = "warmUp")
	public void executeNormalTest() throws InterruptedException{
		System.out.println("warmUp");
		ExtInvoiceSupPortRequest request;
		request = new ExtInvoiceSupPortRequest();
		String req = request.done();
		setRequest(req);
		Response resp = given().request()
				.headers(request.header).auth().basic(Users.TESTAPUK_USER.getUsername(), Pass.TESTAPUK_PASS.getPassword())
		
		.contentType(request.contentType).body(req)
		
		.when().post(request.endpoint);
		setResponse(resp.asString());

		if(resp.asString().contains("Waiting for response has timed out")){
			createCache();
			executeNormalTest();
			throw new SkipException("Response Timeout \nJIRA Item BPMINVOICE-1636 needs to be fixed");

		}
		Assert.assertTrue(resp.getStatusCode() == 200, resp.asString() + "\n" + req);		
		Assert.assertTrue(resp.asString().contains("Success"), resp.asString() + "\n" + RestAssured.baseURI);	
		
		
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
					, "SupplierCreatorID did not matched.");
	}
	
	
	
	@Test(groups = { "warmUp" })
	public void createCache() throws InterruptedException{
		System.out.println("createCache");
		
		createInvoiceReq();	
		createInvalidWorkOrderID();
		invalidCurrency();				
		documentFlag();
	}

	private void documentFlag() throws InterruptedException {
		System.out.println("documentFlag");
		ExtInvoiceSupPortRequest createInvoiceRequest = new ExtInvoiceSupPortRequest();
		String req;
		createInvoiceRequest = new ExtInvoiceSupPortRequest();
		req = createInvoiceRequest.setDocumentTypeFlag("SomeInvalidFlag").done();
		setRequest(req);

		Response resp = given().request()
				.headers(createInvoiceRequest.header).auth().basic(Users.TESTAPUK_USER.getUsername(), Pass.TESTAPUK_PASS.getPassword())
		.contentType(createInvoiceRequest.contentType).body(req)
		
		.when().post(createInvoiceRequest.endpoint);
		setResponse(resp.asString());

		Assert.assertTrue(resp.asString().contains("ERROR_INPUT_017")
				, "ErrorCode is not presented");
		Assert.assertTrue(resp.asString().contains("Invalid DocumentTypeFlag.")
				, "ErrorMessage is not presented");
		if(resp.asString().contains("Waiting for response has timed out.")){
			createCache();
			throw new SkipException("Skipping test.");

		}		
	}

	private void invalidCurrency() throws InterruptedException {
		System.out.println("invalidCurrency");
		ExtInvoiceSupPortRequest createInvoiceRequest = new ExtInvoiceSupPortRequest();
		String req;
		createInvoiceRequest = new ExtInvoiceSupPortRequest();
		req = createInvoiceRequest.setInvoiceCurrency("$").done();
		setRequest(req);

		Response resp = given().request()
				.headers(createInvoiceRequest.header).auth().basic(Users.TESTAPUK_USER.getUsername(), Pass.TESTAPUK_PASS.getPassword())
		.contentType(createInvoiceRequest.contentType).body(req)
		
		.when().post(createInvoiceRequest.endpoint);
		setResponse(resp.asString());

		resp.then().statusCode(200);

		if(resp.asString().contains("Waiting for response has timed out.")){
			createCache();
			throw new SkipException("Skipping test.");

		}
		
		Assert.assertTrue(resp.asString().contains("ERROR_INPUT_016")
					, "ErrorCode did not matched.");
		Assert.assertTrue(resp.asString().contains("Invalid Currency")
				, "ErrorMessage did not matched.");
				
	}

	private void createInvoiceReq() throws InterruptedException {
		System.out.println("Create Invoice Request");
		ExtInvoiceSupPortRequest createInvoiceRequest = new ExtInvoiceSupPortRequest();
		String req = createInvoiceRequest.done();
		setRequest(req);

		Response resp = given().request()
				.headers(createInvoiceRequest.header).auth().basic(Users.TESTAPUK_USER.getUsername(), Pass.TESTAPUK_PASS.getPassword())
		.contentType(createInvoiceRequest.contentType)
		
			.body(req)
		
		.when().post(createInvoiceRequest.endpoint);
		setResponse(resp.asString());

		if(resp.asString().contains("Waiting for response has timed out")){
			createCache();
			throw new SkipException("Skipping test");

		}
		Assert.assertTrue(resp.statusCode() == 200, "Status is: " + resp.getStatusCode());
				
	}

	private void createInvalidWorkOrderID() throws InterruptedException {
		System.out.println("createInvalidWorkOrderID ");
		ExtInvoiceSupPortRequest createInvoiceRequest = new ExtInvoiceSupPortRequest();
		String req = createInvoiceRequest.setWorkOrderId("Invalid").done();
		setRequest(req);

		Response resp = given().request()
				.headers(createInvoiceRequest.header).auth().basic(Users.TESTAPUK_USER.getUsername(), Pass.TESTAPUK_PASS.getPassword())
		.contentType(createInvoiceRequest.contentType).body(req)
		
		.when().post(createInvoiceRequest.endpoint);
		setResponse(resp.asString());
		
		if(resp.asString().contains("Waiting for response has timed out")){
			createCache();
			throw new SkipException("Skipping test");

		}
		Assert.assertTrue(resp.statusCode() == 200, "Status is: " + resp.getStatusCode());

		Assert.assertTrue(resp.asString().contains("ERROR_INPUT_011")
				, "ErrorCode did not matched. \n" + resp.asString() + "\n" + "Request is: " + req + 
				"\nURL is: " + RestAssured.baseURI + "\n InvoiceID is: " + createInvoiceRequest.getInvoiceNumber());
		Assert.assertTrue(resp.asString().contains("Invalid WorkOrderId")
				, "ErrorMessage did not matched.");
				
	}
}
