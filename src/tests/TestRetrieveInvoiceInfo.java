package tests;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasXPath;

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
import requests.RetrieveInvoiceInfoRequest;
import requests.RetrieveInvoiceInfoSPRequest;
import utils.DatabaseUtil;
import utils.StoreResults;
import utils.TestInstance;
import utils.Util;
import utils.WebServiceTest;

public class TestRetrieveInvoiceInfo extends WebServiceTest{
	RetrieveInvoiceInfoRequest retrInvInfoReq;

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
	
	private String createInvoice(ExtInvoiceSupPortRequest createInvoiceReq){
		RetrieveInvoiceHeaderRequest retrInvHeaderReq = new RetrieveInvoiceHeaderRequest();
		
		Response resp = given().request()
		.headers(createInvoiceReq.header).auth().basic(Users.TESTAPUK_USER.getUsername(), Pass.TESTAPUK_PASS.getPassword())
		.contentType(createInvoiceReq.contentType).body(createInvoiceReq.done())
		
		.when().post(createInvoiceReq.endpoint);
		
		resp.then().
			body(hasXPath("//code", containsString("0"))).
			body(hasXPath("//responseMessage", containsString("Success")));
		
		String req = retrInvHeaderReq.setInvoiceNumber(createInvoiceReq.getInvoiceNumber()).done();
		resp = given().request()
			.contentType(retrInvHeaderReq.contentType).body(req)
			
		.when()
			.post(retrInvHeaderReq.endpoint);
		
		setRequest(req);
		setResponse(resp.asString());
		
		Assert.assertTrue(resp.getStatusCode() == 200);		
		String invoiceID = Util.getValueFromResponse(resp.asString(), "ns0:InvoiceId");
		return invoiceID;
	}
	
	@BeforeMethod(alwaysRun = true)
	public void setup(){
		
		RestAssured.baseURI = TestInstance.getServerName(); 
		
		retrInvInfoReq = new RetrieveInvoiceInfoRequest();
		
	}
	
	@Test(groups = { "2.4.1.0" })
	public void test_1485(){
		ExtInvoiceSupPortRequest createInvoiceReq = new ExtInvoiceSupPortRequest();
		String invoiceID = createInvoice(createInvoiceReq);
		
		RetrieveInvoiceInfoRequest retrInfoReq = new RetrieveInvoiceInfoRequest();
		String req = retrInfoReq.setInvoiceID(invoiceID).done();
		Response resp = given().request()
				.contentType(retrInfoReq.contentType).body(req)
				
		.when()
			.post(retrInfoReq.endpoint);
		setRequest(req);
		setResponse(resp.asString());
		
		Assert.assertTrue(resp.asString().contains("<ns0:Unit>" + createInvoiceReq.getUnit() + "</ns0:Unit>"));
		Assert.assertTrue(resp.asString().contains("<ns0:Quantity>" + createInvoiceReq.getQuantity() + "</ns0:Quantity>"));
		Assert.assertTrue(resp.asString().contains("<ns0:PricePerUnit>" + createInvoiceReq.getPricePerUnit() + "</ns0:PricePerUnit>"));
		Assert.assertTrue(resp.asString().contains("<ns0:VatAmount>" + createInvoiceReq.getVatAmount() + "</ns0:VatAmount>"));
		Assert.assertTrue(resp.asString().contains("<ns0:AirportFrom>" + createInvoiceReq.getAirportFrom() + "</ns0:AirportFrom>"));
		Assert.assertTrue(resp.asString().contains("<ns0:AirportTo>" + createInvoiceReq.getAirportTo() + "</ns0:AirportTo>"));
		Assert.assertTrue(resp.asString().contains("<ns0:TailNumber>" + createInvoiceReq.getTailNumber() + "</ns0:TailNumber>"));
		Assert.assertTrue(resp.asString().contains("<ns0:Callsign>" + createInvoiceReq.getCallsign() + "</ns0:Callsign>"));
		Assert.assertTrue(resp.asString().contains(createInvoiceReq.getItemDate()));
		Assert.assertTrue(resp.asString().contains("<ns0:ServiceRenderedAirport>" + createInvoiceReq.getServiceRenderAirport() + "</ns0:ServiceRenderedAirport>"));
		Assert.assertTrue(resp.asString().contains("<ns0:TotalAmount>" + createInvoiceReq.getLineItemTotalAmount() + "</ns0:TotalAmount>"));
		Assert.assertTrue(resp.asString().contains("<ns0:Amount>" + createInvoiceReq.getLineItemAmount() + "</ns0:Amount>"));

	}

	@Test(groups = { "2.4.1.0" })
	public void test_1492(){
		
		ExtInvoiceSupPortRequest createInvoiceReq = new ExtInvoiceSupPortRequest();
		String req = createInvoiceReq.setLineItemNetAmount("0").setLineItemTotalAmount("0").setLineItemVatAmount("0").done();
		Response resp = given().request()
		.headers(createInvoiceReq.header).auth().basic(Users.TESTAPUK_USER.getUsername(), Pass.TESTAPUK_PASS.getPassword())
		.contentType(createInvoiceReq.contentType).body(req)
		
		.when().post(createInvoiceReq.endpoint);
		
		resp.then().
			body(hasXPath("//code", containsString("0"))).
			body(hasXPath("//responseMessage", containsString("Success")));
		setRequest(req);
		setResponse(resp.asString());
		
		RetrieveInvoiceHeaderRequest retrInvHeaderReq = new RetrieveInvoiceHeaderRequest();
		resp = given().request()
			.contentType(retrInvHeaderReq.contentType).body(retrInvHeaderReq.setInvoiceNumber(createInvoiceReq.getInvoiceNumber()).done())
			
		.when()
			.post(retrInvHeaderReq.endpoint);

		Assert.assertTrue(resp.getStatusCode() == 200);		
		String invoiceID = Util.getValueFromResponse(resp.asString(), "ns0:InvoiceId");
		
		RetrieveInvoiceInfoRequest retrInfoReq = new RetrieveInvoiceInfoRequest();
		req = retrInfoReq.setInvoiceID(invoiceID).done();
		resp = given().request()
				.contentType(retrInfoReq.contentType).body(req)
				
		.when()
			.post(retrInfoReq.endpoint);
		setRequest(req);
		setResponse(resp.asString());
		
		resp.then().statusCode(200);
		Assert.assertFalse(resp.asString().contains("<ns0:ItemDescription>")
				, "Item Description is  available in the response");
		Assert.assertFalse(resp.asString().contains("SupplierDescription")
				, "Supplier Description is displayed in the response");
	}
	
	@Test(groups = { "2.4.1.0" })
	public void test_1467(){
		
		ExtInvoiceSupPortRequest createInvoiceReq = new ExtInvoiceSupPortRequest();
		Response resp = given().request()
		.headers(createInvoiceReq.header).auth().basic(Users.TESTAPUK_USER.getUsername(), Pass.TESTAPUK_PASS.getPassword())
		.contentType(createInvoiceReq.contentType).body(createInvoiceReq.setLineItemNetAmount("0").setLineItemTotalAmount("0").setLineItemVatAmount("0").done())
		
		.when().post(createInvoiceReq.endpoint);
		
		resp.then().
			body(hasXPath("//code", containsString("0"))).
			body(hasXPath("//responseMessage", containsString("Success")));
		
		
		RetrieveInvoiceHeaderRequest retrInvHeaderReq = new RetrieveInvoiceHeaderRequest();
		resp = given().request()
			.contentType(retrInvHeaderReq.contentType).body(retrInvHeaderReq.setInvoiceNumber(createInvoiceReq.getInvoiceNumber()).done())
			
		.when()
			.post(retrInvHeaderReq.endpoint);

		Assert.assertTrue(resp.getStatusCode() == 200);		
		String invoiceID = Util.getValueFromResponse(resp.asString(), "ns0:InvoiceId");
		
		RetrieveInvoiceInfoRequest retrInfoReq = new RetrieveInvoiceInfoRequest();
		String req  = retrInfoReq.setInvoiceID(invoiceID).done();
		resp = given().request()
				.contentType(retrInfoReq.contentType).body(req)
				
		.when()
			.post(retrInfoReq.endpoint);
		setRequest(req);
		setResponse(resp.asString());
		
		resp.then().statusCode(200);
		Assert.assertFalse(resp.asString().contains("line"), "RetrieveInvoiceInfo service should return Line Items which have amount > 0");
		
		
	}
	
	@Test(groups = { "2.4.1.0" })
	public void test_1463(){
		ExtInvoiceSupPortRequest createInvoiceReq = new ExtInvoiceSupPortRequest();
		String invoiceID = createInvoice(createInvoiceReq);
		
		
		
		DatabaseUtil.insertComment(invoiceID, "TestAutomationComment");
		
		//Check not visible through RetrieveInvoiceInfoSP
		RetrieveInvoiceInfoSPRequest retrSPInfoReq = new RetrieveInvoiceInfoSPRequest();
		Response resp = given().request()
				.contentType(retrSPInfoReq.contentType).body(retrSPInfoReq.setInvoiceID(invoiceID).done())
				
		.when()
			.post(retrSPInfoReq.endpoint);
		
		resp.then().statusCode(200);
		Assert.assertFalse(resp.asString().contains("TestAutomationComment"), "Comment is visible");
		
		//Check visible through RetrieveInvoiceInfo
		RetrieveInvoiceInfoRequest retrInfoReq = new RetrieveInvoiceInfoRequest();
		String req = retrInfoReq.setInvoiceID(invoiceID).done();
		setRequest(req);
		
		resp = given().request()
				.contentType(retrInfoReq.contentType).body(req)
		.when()
			.post(retrInfoReq.endpoint);
		setRequest(req);
		setResponse(resp.asString());
		
		resp.then().statusCode(200);
		Assert.assertTrue(resp.asString().contains("TestAutomationComment"), "Comment is not visible");
		
	}

	@Test(groups = { "2.4.1.0" })
	public void test_1670(){
		RetrieveInvoiceHeaderRequest request = new RetrieveInvoiceHeaderRequest();
		Response resp = given().request()
			.contentType(request.contentType).body(request.setInvoiceStatus("Settled").done())
		
		.when()
			.post(request.endpoint);
		
		if(resp.asString().contains("<ns2:TotalRegistries>0</ns2:TotalRegistries>")){
			throw new SkipException ("Skipping Test: Test is skipped because there is no invoices with Settled status");
		}
		
		System.out.println(resp.asString());
		Assert.assertTrue(resp.getStatusCode() == 200);			

		Assert.assertFalse(resp.asString().contains("<ns0:SapClearingDate xsi:nil=\"true\"/>"), "SapClearingDate has null");

		
		String invoiceID = Util.getValueFromResponse(resp.asString(), "ns0:InvoiceId");
		
		RetrieveInvoiceInfoSPRequest retrieveInvoiceInfoSPReq = new RetrieveInvoiceInfoSPRequest();
		
		resp = given().request()
				.contentType(retrieveInvoiceInfoSPReq.contentType).body(retrieveInvoiceInfoSPReq.setInvoiceID(invoiceID).done())
				
				.when()
					.post(retrieveInvoiceInfoSPReq.endpoint);
		Assert.assertTrue(resp.getStatusCode() == 200);			
		Assert.assertTrue(resp.asString().contains("Settled"), "Status Settled is not displayed");
		
		RetrieveInvoiceInfoRequest retrieveInvInfoReq = new RetrieveInvoiceInfoRequest();
		String req = retrieveInvInfoReq.setInvoiceID(invoiceID).done();
		resp = given().request()
				.contentType(retrieveInvInfoReq.contentType).body(req)
				.when().post(retrieveInvInfoReq.endpoint);
		
		setRequest(req);
		setResponse(resp.asString());
		
		Assert.assertTrue(resp.getStatusCode() == 200);			
		Assert.assertFalse(resp.asString().contains("Settled"), "Status Settled is displayed");
						
	}
	
	@Test(groups = { "2.4.1.0" })
	public void test_1660(){
		
		ExtInvoiceSupPortRequest createInvoiceReq = new ExtInvoiceSupPortRequest();
		String invoiceID = createInvoice(createInvoiceReq);
		
		RetrieveInvoiceInfoSPRequest infoSPReq = new RetrieveInvoiceInfoSPRequest();
		String req = infoSPReq.setInvoiceID(invoiceID).done();
		setRequest(req); 
		
		Response resp = given().request()
				.contentType(infoSPReq.contentType).body(req)
			
			.when()
				.post(infoSPReq.endpoint);
		setResponse(resp.asString());

		Assert.assertTrue(resp.statusCode() == 200);
		Assert.assertTrue(resp.asString().contains("<ns0:Description>Attachment1.pdf</ns0:Description>"));
		Assert.assertTrue(resp.asString().contains("<ns0:Description>InvoicePDF</ns0:Description>"));
				
	}
}
