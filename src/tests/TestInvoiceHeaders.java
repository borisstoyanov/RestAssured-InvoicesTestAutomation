package tests;
import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasToString;
import static org.hamcrest.Matchers.hasXPath;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.response.Response;

import enums.Pass;
import enums.Users;
import requests.ExtInvoiceSupPortRequest;
import requests.RetrieveInvoiceHeaderRequest;
import utils.TestInstance;
import utils.WebServiceTest;

public class TestInvoiceHeaders extends WebServiceTest{

	RetrieveInvoiceHeaderRequest request = new RetrieveInvoiceHeaderRequest();
	
	@BeforeClass(alwaysRun = true)
	public void setup(){
		RestAssured.baseURI = TestInstance.getServerName();
		
		ExtInvoiceSupPortRequest createInvoiceRequest = new ExtInvoiceSupPortRequest();
		given().request()
		.headers(createInvoiceRequest.header).auth().basic(Users.DIMITROV.getUsername(), Pass.DIMITROV.getPassword())
		.contentType(createInvoiceRequest.contentType)
		
			.body(createInvoiceRequest.done())
		
		.when().post(createInvoiceRequest.endpoint).then().
			body(hasXPath("//code", containsString("0"))).
			body(hasXPath("//responseMessage", containsString("Success")));
		
		request.setInvoiceNumber(createInvoiceRequest.getInvoiceNumber());
		
		
	}

	@Test(groups = { "2.4.1.0" })
	public void test_1368(){
		
		Response resp = given().request()
			.contentType(request.contentType).body(request.done())
		
		.when()
			.post(request.endpoint);
		
		resp.then()
			.body(hasToString(containsString("<ns0:SupplierCreatorId>estafetdev3</ns0:SupplierCreatorId>")))
			.body(hasToString(containsString("<ns0:InvoiceCreationType>SPortal Invoice</ns0:InvoiceCreationType>")))
			.body(hasToString(containsString("<ns2:TotalRegistries>1</ns2:TotalRegistries>")));
		Assert.assertTrue(resp.asString().contains("<ns0:InvoiceCreationType>SPortal Invoice</ns0:InvoiceCreationType>")
				, "Response does not contain CreationType");		
		Assert.assertTrue(resp.asString().contains("<ns0:SupplierCreatorId>estafetdev3</ns0:SupplierCreatorId>")
				, "Response does not contain SupplierCreatorID");
		Assert.assertTrue(resp.asString().contains("<ns2:TotalRegistries>1</ns2:TotalRegistries>")
				, "Response does not contain one record");
		}
	
	@Test(groups = { "2.4.1.0" })
	public void test_1413_1427(){
		
		Response resp = given().request()
			.contentType(request.contentType).body(request.done())
		
		.when()
			.post(request.endpoint);
		
		Assert.assertTrue(resp.asString().contains("<ns0:SapClearingDate xsi:nil=\"true\"/>")
				, "Response does not contain one SapClearingDate");
	}
	
	@Test(groups = { "2.4.1.0" })
	public void test_1432(){
		
		Response resp = given().request()
			.contentType(request.contentType).body(request.setDocumentType("KR").done())
		
		.when()
			.post(request.endpoint);

		Assert.assertTrue(resp.asString().contains("<ns0:DocumentType>KR</ns0:DocumentType>")
				, "Response does not contain KR as DocType");

	}

}
