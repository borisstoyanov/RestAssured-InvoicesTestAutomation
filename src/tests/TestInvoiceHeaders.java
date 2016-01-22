package tests;
import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasToString;
import static org.hamcrest.Matchers.hasXPath;

import org.testng.annotations.BeforeGroups;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.jayway.restassured.RestAssured;

import enums.Pass;
import enums.Users;
import requests.ExtInvoiceSupPortRequest;
import requests.RetrieveInvoiceHeaderRequest;
import utils.TestInstance;
import utils.WebServiceTest;

public class TestInvoiceHeaders extends WebServiceTest{

	RetrieveInvoiceHeaderRequest request = new RetrieveInvoiceHeaderRequest();
	
	@BeforeGroups(groups = { "2.4.1.0" })
	public void beforeGroups(){
		setup();
	}
		
	@BeforeTest
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
	public void testInvoiceHeaders(){
		
		given().request()
			.contentType(request.contentType).body(request.done())
		
		.when()
			.post(request.endpoint)
		
		.then()
			.body(hasToString(containsString("<ns2:TotalRegistries>1</ns2:TotalRegistries>")))
			.body(hasToString(containsString(request.getInvoiceNumber())));
	}
	
	@Test(groups = { "2.4.1.0" })
	public void test_1368(){
		
		given().request()
			.contentType(request.contentType).body(request.done())
		
		.when()
			.post(request.endpoint)
		
		.then()
			.body(hasToString(containsString("<ns0:SupplierCreatorId>estafetdev3</ns0:SupplierCreatorId>")))
			.body(hasToString(containsString("<ns0:InvoiceCreationType>SPortal Invoice</ns0:InvoiceCreationType>")))
			.body(hasToString(containsString("<ns2:TotalRegistries>1</ns2:TotalRegistries>")));
	}
	
	@Test(groups = { "2.4.1.0" })
	public void test_1413_1427(){
		
		given().request()
			.contentType(request.contentType).body(request.done())
		
		.when()
			.post(request.endpoint)
		
		.then()
			.body(hasToString(containsString("<ns0:SapClearingDate xsi:nil=\"true\"/>")));
	}
	
	@Test(groups = { "2.4.1.0" })
	public void test_1432(){
		
		given().request()
			.contentType(request.contentType).body(request.setDocumentType("KR").done())
		
		.when()
			.post(request.endpoint)
		
		.then()
			.body(hasToString(containsString("<ns0:DocumentType>KR</ns0:DocumentType>")));
	}

}
