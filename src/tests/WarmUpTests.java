package tests;

import static com.jayway.restassured.RestAssured.given;

import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.response.Response;

import enums.Pass;
import enums.Users;
import requests.ExtInvoiceSupPortRequest;
import requests.RetrieveInvoiceHeaderRequest;
import utils.DatabaseUtil;
import utils.TestInstance;

public class WarmUpTests {
	
	@BeforeMethod(alwaysRun = true)
	public void setup(){
		RestAssured.baseURI = TestInstance.getServerName();
	}

	@Test(groups = "warmUp")
	public void warmupTest(){
		ExtInvoiceSupPortRequest request;
		request = new ExtInvoiceSupPortRequest();
		String req = request.done();
		
		Response resp = given().request()
		.headers(request.header).auth().basic(Users.DIMITROV.getUsername(), Pass.DIMITROV.getPassword())
		
		.contentType(request.contentType).body(req)
		
		.when().post(request.endpoint);
		
		Assert.assertTrue(resp.getStatusCode() == 200, resp.asString() + "\n" + req);		
		Assert.assertTrue(resp.asString().contains("Success"), resp.asString());	
		
		
		//Check if fields are created in the DB
		String query = "SELECT creator_name, invoice_creator FROM INVOICE";
		if(DatabaseUtil.executeQuery(query).equals(null)){
			Assert.fail("Test Failed: creator_name, invoice_creator are not available in INVOICE table.");
		}
		
		//Execute retrieve invoice headers request 
		RetrieveInvoiceHeaderRequest retrReq = new RetrieveInvoiceHeaderRequest();
		resp = given().request()
				.contentType(retrReq.contentType).body(retrReq.setInvoiceNumber(request.getInvoiceNumber()).done())
			
			.when()
				.post(retrReq.endpoint);

			Assert.assertTrue(resp.getStatusCode() == 200);		
			Assert.assertTrue(resp.asString().contains("<ns0:SupplierCreatorId>" + request.getSupplierCreatorID() + "</ns0:SupplierCreatorId>")
					, "SupplierCreatorID did not matched");
	}
	
	
	@Test(groups = { "warmUp" })
	public void createCache() throws InterruptedException{
								
		ExtInvoiceSupPortRequest request = new ExtInvoiceSupPortRequest();
		String req = request.setWorkOrderId("Invalid").done();
		
		Response resp = given().request()
		.headers(request.header).auth().basic(Users.DIMITROV.getUsername(), Pass.DIMITROV.getPassword())
		.contentType(request.contentType).body(req)
		
		.when().post(request.endpoint);
		
		
		if(resp.asString().contains("Waiting for response has timed out")){
			warmupTest();
			throw new SkipException("Response Timeout \nJIRA Item BPMINVOICE-1636 needs to be fixed");

			/*
			 * Add time investigating this failure 
			 * 
			 * Boris - 27.01.2016 - 6h
			 * Boris - 28.01.2016 - 8h
			 * Boris - 01.02.2016 - 8h
			 * Boyko - 28.01.2016 - 2h
			 * Ceco  - 01.02.2016 - 6h
			 */
		}
		Assert.assertTrue(resp.statusCode() == 200, "Status is: " + resp.getStatusCode());

		Assert.assertTrue(resp.asString().contains("ERROR_INPUT_011")
				, "ErrorCode did not matched. \n" + resp.asString() + "\n" + "Request is: " + req + 
				"\nURL is: " + RestAssured.baseURI + "\n InvoiceID is: " + request.getInvoiceNumber());
		Assert.assertTrue(resp.asString().contains("Invalid WorkOrderId")
				, "ErrorMessage did not matched.");
						
	}
	
}
