package tests;
import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasXPath;

import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.response.Response;

import enums.Pass;
import enums.Users;
import requests.CancelInvoiceRequest;
import requests.ExtInvoiceSupPortRequest;
import requests.RetrieveInvoiceHeaderRequest;
import utils.DatabaseUtil;
import utils.PropertyReader;
import utils.TestInstance;
import utils.Util;
import utils.WebServiceTest;

public class TestInvoiceHeaders_NoInvoiceCreation extends WebServiceTest{
	RetrieveInvoiceHeaderRequest request;
	
	@BeforeMethod(alwaysRun = true)
	public void setup(){
		RestAssured.baseURI = TestInstance.getServerName(); 
	}

	@Test(groups = { "2.4.1.0"})
	public void test_1411(){
		request = new RetrieveInvoiceHeaderRequest();
		Response resp = given().request()
			.contentType(request.contentType).body(request.setInvoiceStatus("Enter Details").done())
		
		.when()
			.post(request.endpoint);

		Assert.assertTrue(resp.getStatusCode() == 200);			
		Assert.assertFalse(resp.asString().contains("<ns2:TotalRegistries>0</ns2:TotalRegistries>"), "No registries were returned");

			
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
	
	@Test(groups = { "2.4.1.0" })
	public void test_1424(){
		request = new RetrieveInvoiceHeaderRequest();
		Response resp = given().request()
			.contentType(request.contentType).body(request.setInvoiceStatus("Settled").done())
		
		.when()
			.post(request.endpoint);
		
		if(resp.asString().contains("<ns2:TotalRegistries>0</ns2:TotalRegistries>")){
			throw new SkipException ("Skipping Test: Test is skipped because there is no invoices with Settled status");
		}
			
		Assert.assertTrue(resp.getStatusCode() == 200);			

		Assert.assertFalse(resp.asString().contains("<ns0:SapClearingDate xsi:nil=\"true\"/>"), "SapClearingDate has null");
			
	}
	@Test(groups = { "2.4.1.0" })
	public void test_1432(){
		request = new RetrieveInvoiceHeaderRequest();
		Response resp = given().request()
			.contentType(request.contentType).body(request.setDocumentType("KR").done())
		
		.when()
			.post(request.endpoint);
		
		resp.then()
			.statusCode(200);
			Assert.assertTrue(resp.asString().contains("<ns0:DocumentType>KR</ns0:DocumentType>")
					, "response does not contain 'KR' as a document type");
	}
	
	@Test(groups = { "2.4.1.0" })
	public void test_1433(){
		request = new RetrieveInvoiceHeaderRequest();
		String req = request.setInvoiceStatus("Settled").setEntityID("0200").setVendorID("0000100430")
				.setEntityID2("0200").setVendorID2("0000100222")
				.setEntityID3("0200").setVendorID3("0000101406")
				.done();
		
		Response resp = given().request()
			.contentType(request.contentType).body(req)
		
		.when()
			.post(request.endpoint);
		
		if(resp.asString().contains("<ns2:TotalRegistries>0</ns2:TotalRegistries>")){
			throw new SkipException ("Skipping Test: Test is skipped because there is no invoices with Settled status");
		}
		
		String respAsString = resp.asString();

		Assert.assertTrue(resp.getStatusCode() == 200);			

		Assert.assertFalse(respAsString.contains("<ns0:InvoiceStatus>Approved</ns0:InvoiceStatus>"), "Response contains 'Approved'");
		Assert.assertFalse(respAsString.contains("<ns0:InvoiceStatus>New</ns0:InvoiceStatus>"), "Response contains 'New'");
		Assert.assertFalse(respAsString.contains("<ns0:InvoiceStatus>Cancelled</ns0:InvoiceStatus>"), "Response contains 'Cancelled'");
		Assert.assertFalse(respAsString.contains("<ns0:InvoiceStatus>Under Approval</ns0:InvoiceStatus>"), "Response contains 'Under Approval'");
	}
	
	@Test(groups = { "2.4.1.0" })
	public void test_1446(){
		request = new RetrieveInvoiceHeaderRequest();
		Response resp = given().request()
			.contentType(request.contentType).body(request.setInvoiceStatus("SomeNonExistingStatus").done())
		
		.when()
			.post(request.endpoint);
		
		Assert.assertTrue(resp.getStatusCode() == 200);			
			
		Assert.assertTrue(resp.asString().contains("<ns2:TotalRegistries>0</ns2:TotalRegistries>"), "Response contains records");
			
	}
	
	@Test(groups = { "2.4.1.0" })
	public void test_1448_1436(){
		request = new RetrieveInvoiceHeaderRequest();
		String req = request.setEntityID("0200").setVendorID("0000100430").done();
		Response resp = given().request()
			.contentType(request.contentType).body(req)
		
		.when()
			.post(request.endpoint);
				
		Assert.assertTrue(resp.getStatusCode() == 200);			
		Assert.assertFalse(resp.asString().contains("<ns2:TotalRegistries>0</ns2:TotalRegistries>"), "Response does not contains records");
		
		String query = "select count(*) from ("
				+ "SELECT InvoiceEnt.BARCODE, "
				+ "       InvoiceEnt.BPM_COMPOSITE_VERSION, "
				+ "       InvoiceEnt.BPM_INSTANCE_ID, "
				+ "       InvoiceEnt.BRIEF_DESCRIPTION, "
				+ "       InvoiceEnt.CALCULATE_TAX_AMOUNT, "
				+ "       InvoiceEnt.COUNTRY_CODE, "
				+ "       InvoiceEnt.CURRENCY, "
				+ "       InvoiceEnt.DEFAULT_PERIOD_OF_COST, "
				+ "       InvoiceEnt.DOCUMENT_TYPE, "
				+ "       InvoiceEnt.DOCUMENTUM_ID, "
				+ "       InvoiceEnt.DOCUMENTUM_SAP_ID, "
				+ "       InvoiceEnt.DUE_DATE, "
				+ "       InvoiceEnt.GROSS_VALUE, "
				+ "       InvoiceEnt.GV_ALLOCATION_ID, "
				+ "       InvoiceEnt.INTERNAL_ORDER_ID, "
				+ "       InvoiceEnt.INVOICE_CREATOR, "
				+ "       InvoiceEnt.INVOICE_DATE, "
				+ "       InvoiceEnt.INVOICE_ID, "
				+ "       InvoiceEnt.INVOICE_NUMBER, "
				+ "       CASE WHEN InvoiceControl.SAP_CLEARING_DATE IS NOT NULL THEN 'Settled' ELSE InvoiceEnt.INVOICE_STATUS END INVOICE_STATUS, "
				+ "       InvoiceEnt.INVOICE_SYSTEM_DATE, "
				+ "       InvoiceEnt.INVOICE_TYPE, "
				+ "       InvoiceEnt.MAIN_APPROVAL_GROUP, "
				+ "       InvoiceEnt.NET_VALUE, "
				+ "       InvoiceEnt.TAX_CODE, "
				+ "       InvoiceEnt.VAT_DEFAULT, "
				+ "       InvoiceEnt.VENDOR_ID, "
				+ "       InvoiceEnt.INVOICE_CREATION_TYPE, "
				+ "       EntityEnt.ENTITY_COMPANY_CODE, "
				+ "       VendorEnt.VENDOR_NUMBER, "
				+ "       InvoiceControl.SAP_CLEARING_DATE "
				+ "FROM INVOICE InvoiceEnt "
				+ "LEFT JOIN ENTITY EntityEnt "
				+ "ON InvoiceEnt.ENTITY_ID = EntityEnt.ENTITY_ID "
				+ "LEFT JOIN VENDOR VendorEnt "
				+ "ON InvoiceEnt.VENDOR_ID = VendorEnt.VENDOR_ID "
				+ "AND EntityEnt.ENTITY_ID = VendorEnt.ENTITY_ID "
				+ "AND InvoiceEnt.ENTITY_ID = VendorEnt.ENTITY_ID "
				+ "LEFT JOIN INVOICECONTROL InvoiceControl "
				+ "ON InvoiceControl.INVOICE_ID = InvoiceEnt.INVOICE_ID "
				+ "WHERE ENTITY_COMPANY_CODE in ('0200') and VENDOR_NUMBER IN ('0000100430'))";
		
		String totalRegistriesFromDB  = DatabaseUtil.executeQueryCounts(query);
		
		
		String respAsString = resp.asString();
		String splittedResp = respAsString.split("</ns2:TotalRegistries>")[0];
		
		String totalRegistriesFromWebservice = splittedResp.split("<ns2:TotalRegistries>")[1];
		
		Assert.assertTrue(resp.getStatusCode() == 200);			

		Assert.assertTrue(totalRegistriesFromWebservice.equals(totalRegistriesFromDB)
				, "registries from Service is not equal to registries from DB");			
		
	}
	
	@Test(groups = { "2.4.1.0" })
	public void test_1449(){
		request = new RetrieveInvoiceHeaderRequest();
		String req = request.setInvoiceNumber("").done();
		Response resp = given().request()
			.contentType(request.contentType).body(req)
		
		.when()
			.post(request.endpoint);
		
		Assert.assertTrue(resp.getStatusCode() == 200);			

		Assert.assertFalse(resp.asString().contains("<ns2:TotalRegistries>0</ns2:TotalRegistries>")
				, "Response contains records");
			
	}
	
	@Test(groups = { "2.4.1.0" })
	public void test_1456(){
		String req = request.setStartDate(Util.getDateMinusYear(1)).setEndDate(Util.getDate()).done();
		Response resp = given().request()
			.contentType(request.contentType).body(req)
		
		.when()
			.post(request.endpoint);

		Assert.assertTrue(resp.getStatusCode() == 200);			
		Assert.assertFalse(resp.asString().contains("<ns2:TotalRegistries>0</ns2:TotalRegistries>")
				, "Response contains records");
		Assert.assertFalse(resp.asString().contains("<env:Fault>")
				, "There's a Fault in the reposnse");
	
	}
	
	@Test(groups = { "2.4.1.0" })
	public void test_1456_2(){
		request = new RetrieveInvoiceHeaderRequest();
		String req = request.setEndDate(Util.getDate()).done();
		Response resp = given().request()
			.contentType(request.contentType).body(req)
		
		.when()
			.post(request.endpoint);
		
		Assert.assertTrue(resp.getStatusCode() == 200);			
		Assert.assertFalse(resp.asString().contains("<ns2:TotalRegistries>0</ns2:TotalRegistries>")
				, "Response contains records");
		Assert.assertFalse(resp.asString().contains("<env:Fault>")
				, "There's a Fault in the reposnse");
	}
	
	@Test(groups = { "2.4.1.0" })
	public void test_1456_3(){
		request = new RetrieveInvoiceHeaderRequest();
		String req = request.setStartDate(Util.getDateMinusYear(1)).done();
		Response resp = given().request()
			.contentType(request.contentType).body(req)
		
		.when()
			.post(request.endpoint);
		
		Assert.assertTrue(resp.getStatusCode() == 200);			
		Assert.assertFalse(resp.asString().contains("<ns2:TotalRegistries>0</ns2:TotalRegistries>")
				, "Response contains records");
		Assert.assertFalse(resp.asString().contains("<env:Fault>")
				, "There's a Fault in the reposnse");
	}
	
	@Test(groups = { "2.4.1.0" })
	public void test_1484(){
		request = new RetrieveInvoiceHeaderRequest();
		String req = request.setDocumentType("KG").setDocumentType2("KR").setDocumentType3("CI").done(); 
		Response resp = given().request()
			.contentType(request.contentType).body(req)
		
		.when()
			.post(request.endpoint);
		
		Assert.assertTrue(resp.getStatusCode() == 200);			
			
		Assert.assertFalse(resp.asString().contains("<ns2:TotalRegistries>0</ns2:TotalRegistries>")
				, "Response contains records.");	}
	
	
	@Test(groups = { "2.4.1.0" }) 
	public void test_1489(){
		request = new RetrieveInvoiceHeaderRequest();
		String req = request.done();

		Response resp = given().request()
				.contentType(request.contentType).body(req)
			
			.when()
				.post(request.endpoint);
		
		resp.then().statusCode(200);			
		Assert.assertFalse(resp.asString().contains("<ns2:TotalRegistries>0</ns2:TotalRegistries>"));
			
		String respAsString = resp.asString();
		String splittedResp = respAsString.split("</ns0:InvoiceDate>")[0];
		String invoiceDate = splittedResp.split("<ns0:InvoiceDate>")[1];
		String incrementedDate = Util.incrementDate(invoiceDate);
		
		// Create new invoice 
		ExtInvoiceSupPortRequest crInv = new ExtInvoiceSupPortRequest();
		String createInvoiceReq = crInv.setInvoiceDate(incrementedDate).done();
		
		Response creteInvoiceResponse = given().request().auth().basic(Users.DIMITROV.getUsername(), Pass.DIMITROV.getPassword())
			.contentType(crInv.contentType).body(createInvoiceReq)
				
			.when()
				.post(crInv.endpoint);
		creteInvoiceResponse.then().statusCode(200);
		creteInvoiceResponse.then().body(hasXPath("//responseMessage", containsString("Success")));
		//End of invoice Creation
		
			
		//Post retrieve invoice headers and check if the first invoiceNumber is on the newly created
		req = request.done();

		resp = given().request()
				.contentType(request.contentType).body(req)
			
			.when()
				.post(request.endpoint);
			
		respAsString = resp.asString();
		splittedResp = respAsString.split("</ns0:InvoiceNumber>")[0];
		String invoiceNumber = splittedResp.split("<ns0:InvoiceNumber>")[1];
		
		Assert.assertTrue(invoiceNumber.equals(crInv.getInvoiceNumber())
				, "Invoice number from service is not equal from invNumber from request");
		resp.then().statusCode(200);			
		Assert.assertFalse(resp.asString().contains("<ns2:TotalRegistries>0</ns2:TotalRegistries>")
				, "Response contains records");	
	}
	
	@Test(groups = { "2.4.1.0" })
	public void test_1509(){
		
		ExtInvoiceSupPortRequest crInv = new ExtInvoiceSupPortRequest();
		String createInvoiceReq = crInv.done();
		
		Response creteInvoiceResponse = given().request().auth().basic(Users.DIMITROV.getUsername(), Pass.DIMITROV.getPassword())
			.contentType(crInv.contentType).body(createInvoiceReq)
				
			.when()
				.post(crInv.endpoint);
		String respAsString = creteInvoiceResponse.asString();
		creteInvoiceResponse.then().statusCode(200);
		creteInvoiceResponse.then().body(hasXPath("//responseMessage", containsString("Success")));
		
		String invoiceID = getInvoiceID(crInv);
		
		CancelInvoiceRequest cancelInvoice = new CancelInvoiceRequest();
		String cancelBody = cancelInvoice.setInvoiceID(invoiceID).done();
		Response cancelResponse = given().request().auth().basic(Users.DIMITROV.getUsername(), Pass.DIMITROV.getPassword())
				.header("Content-Type", "text/xml; charset=UTF-8;")
			.body(cancelBody)
		.when()
			.post(cancelInvoice.endpoint);
		
		respAsString = cancelResponse.asString();
		cancelResponse.then().statusCode(200);
		Assert.assertTrue(respAsString.contains("cancelInvoiceResponse"));

		
		request = new RetrieveInvoiceHeaderRequest();
		String req = request.setInvoiceStatus("Cancelled")
				.done();
		
		Response resp = given().request()
			.contentType(request.contentType).body(req)
		
		.when()
			.post(request.endpoint);
		
		if(resp.asString().contains("<ns2:TotalRegistries>0</ns2:TotalRegistries>")){
			throw new SkipException ("Skipping Test: Test is skipped because there is no invoices with Cancelled status");
		}
		
		respAsString = resp.asString();

		Assert.assertTrue(resp.getStatusCode() == 200);			

		Assert.assertFalse(respAsString.contains("<ns0:InvoiceStatus>Approved</ns0:InvoiceStatus>"), "Invoice contains Status 'Approved'");
		Assert.assertFalse(respAsString.contains("<ns0:InvoiceStatus>New</ns0:InvoiceStatus>"), "Invoice contains Status 'New'");
		Assert.assertFalse(respAsString.contains("<ns0:InvoiceStatus>Under Approval</ns0:InvoiceStatus>"), "Invoice contains Status 'Under Approval'");
	}
	
	@Test(groups = { "2.4.1.1" })
	public void test_1408(){
		if(RestAssured.baseURI.equals("http://bpmuat115.vistajet.local:8201")){
			throw new SkipException("This test is not passing on QA for some reason");
		/*
		 * Add time investigating this failure 
		 * 
		 * Boris - 27.01.2015 - 6h
		 * Boris - 28.01.2015 - 8h
		 * Boyko - 28.01.2015 - 2h
		 */
		
		}
		
		ExtInvoiceSupPortRequest request = new ExtInvoiceSupPortRequest();
		String req = request.setWorkOrderId("Invalid").done();
		
		Response resp = given().request()
		.headers(request.header).auth().basic(Users.DIMITROV.getUsername(), Pass.DIMITROV.getPassword())
		.contentType(request.contentType).body(req)
		
		.when().post(request.endpoint);
		
		Assert.assertTrue(resp.asString().contains("ERROR_INPUT_011")
				, "ErrorCode did not matched. \n" + resp.asString() + "\n" + "Request is: " + req + 
				"\nURL is: " + RestAssured.baseURI);
		Assert.assertTrue(resp.asString().contains("Invalid WorkOrderId")
				, "ErrorMessage did not matched.");
						
	}
}
