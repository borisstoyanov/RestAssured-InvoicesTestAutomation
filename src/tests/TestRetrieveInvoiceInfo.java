package tests;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasXPath;

import org.testng.Assert;
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
import utils.TestInstance;
import utils.Util;
import utils.WebServiceTest;

public class TestRetrieveInvoiceInfo extends WebServiceTest{

	RetrieveInvoiceInfoRequest retrInvInfoReq;
	ExtInvoiceSupPortRequest createInvoiceReq;
	RetrieveInvoiceHeaderRequest retrInvHeaderReq;
	
	private String createInvoice(){
		
		createInvoiceReq = new ExtInvoiceSupPortRequest();
		Response resp = given().request()
		.headers(createInvoiceReq.header).auth().basic(Users.DIMITROV.getUsername(), Pass.DIMITROV.getPassword())
		.contentType(createInvoiceReq.contentType).body(createInvoiceReq.done())
		
		.when().post(createInvoiceReq.endpoint);
		
		resp.then().
			body(hasXPath("//code", containsString("0"))).
			body(hasXPath("//responseMessage", containsString("Success")));
		
		
		retrInvHeaderReq = new RetrieveInvoiceHeaderRequest();
		resp = given().request()
			.contentType(retrInvHeaderReq.contentType).body(retrInvHeaderReq.setInvoiceNumber(createInvoiceReq.getInvoiceNumber()).done())
			
		.when()
			.post(retrInvHeaderReq.endpoint);

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
		
		String invoiceID = createInvoice();
		
		RetrieveInvoiceInfoRequest retrInfoReq = new RetrieveInvoiceInfoRequest();
		
		Response resp = given().request()
				.contentType(retrInfoReq.contentType).body(retrInfoReq.setInvoiceID(invoiceID).done())
				
		.when()
			.post(retrInfoReq.endpoint);
		
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
		
		createInvoiceReq = new ExtInvoiceSupPortRequest();
		Response resp = given().request()
		.headers(createInvoiceReq.header).auth().basic(Users.DIMITROV.getUsername(), Pass.DIMITROV.getPassword())
		.contentType(createInvoiceReq.contentType).body(createInvoiceReq.setLineItemNetAmount("0").setLineItemTotalAmount("0").setLineItemVatAmount("0").done())
		
		.when().post(createInvoiceReq.endpoint);
		
		resp.then().
			body(hasXPath("//code", containsString("0"))).
			body(hasXPath("//responseMessage", containsString("Success")));
		
		
		retrInvHeaderReq = new RetrieveInvoiceHeaderRequest();
		resp = given().request()
			.contentType(retrInvHeaderReq.contentType).body(retrInvHeaderReq.setInvoiceNumber(createInvoiceReq.getInvoiceNumber()).done())
			
		.when()
			.post(retrInvHeaderReq.endpoint);

		Assert.assertTrue(resp.getStatusCode() == 200);		
		String invoiceID = Util.getValueFromResponse(resp.asString(), "ns0:InvoiceId");
		
		RetrieveInvoiceInfoRequest retrInfoReq = new RetrieveInvoiceInfoRequest();
		
		resp = given().request()
				.contentType(retrInfoReq.contentType).body(retrInfoReq.setInvoiceID(invoiceID).done())
				
		.when()
			.post(retrInfoReq.endpoint);
		
		resp.then().statusCode(200);
		Assert.assertFalse(resp.asString().contains("<ns0:ItemDescription>")
				, "Item Description is  available in the response");
		Assert.assertFalse(resp.asString().contains("SupplierDescription")
				, "Supplier Description is displayed in the response");
	}
	
	@Test(groups = { "2.4.1.0" })
	public void test_1467(){
		
		createInvoiceReq = new ExtInvoiceSupPortRequest();
		Response resp = given().request()
		.headers(createInvoiceReq.header).auth().basic(Users.DIMITROV.getUsername(), Pass.DIMITROV.getPassword())
		.contentType(createInvoiceReq.contentType).body(createInvoiceReq.setLineItemNetAmount("0").setLineItemTotalAmount("0").setLineItemVatAmount("0").done())
		
		.when().post(createInvoiceReq.endpoint);
		
		resp.then().
			body(hasXPath("//code", containsString("0"))).
			body(hasXPath("//responseMessage", containsString("Success")));
		
		
		retrInvHeaderReq = new RetrieveInvoiceHeaderRequest();
		resp = given().request()
			.contentType(retrInvHeaderReq.contentType).body(retrInvHeaderReq.setInvoiceNumber(createInvoiceReq.getInvoiceNumber()).done())
			
		.when()
			.post(retrInvHeaderReq.endpoint);

		Assert.assertTrue(resp.getStatusCode() == 200);		
		String invoiceID = Util.getValueFromResponse(resp.asString(), "ns0:InvoiceId");
		
		RetrieveInvoiceInfoRequest retrInfoReq = new RetrieveInvoiceInfoRequest();
		
		resp = given().request()
				.contentType(retrInfoReq.contentType).body(retrInfoReq.setInvoiceID(invoiceID).done())
				
		.when()
			.post(retrInfoReq.endpoint);
		
		resp.then().statusCode(200);
		Assert.assertFalse(resp.asString().contains("line"), "RetrieveInvoiceInfo service should return Line Items which have amount > 0");
		
		
	}
	
	@Test(groups = { "2.4.1.0" })
	public void test_1463(){
		String invoiceID = createInvoice();
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
		resp = given().request()
				.contentType(retrInfoReq.contentType).body(retrInfoReq.setInvoiceID(invoiceID).done())
				
		.when()
			.post(retrInfoReq.endpoint);
		
		resp.then().statusCode(200);
		Assert.assertTrue(resp.asString().contains("TestAutomationComment"), "Comment is not visible");
		
	}
	
	
}
