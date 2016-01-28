package tests;

import static com.jayway.restassured.RestAssured.given;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.response.Response;

import enums.Pass;
import enums.Users;
import requests.RetrieveMeasurementsUnitsRequest;
import utils.TestInstance;

public class TestRetrieveMeasurementsUnits {
	RetrieveMeasurementsUnitsRequest request;

	@BeforeMethod(alwaysRun = true)
	public void setup(){
		RestAssured.baseURI = TestInstance.getServerName(); 
		request = new RetrieveMeasurementsUnitsRequest();
	}
	
	@Test(groups = { "2.4.1.0" })
	public void checkRetrieveMeasurementsUnits(){
	String req = request.done();
		Response resp = given().request().auth().basic(Users.DIMITROV.getUsername(), Pass.DIMITROV.getPassword())
		.contentType(request.contentType).body(req)
	
	.when()
		.post(request.endpoint);
	
	Assert.assertTrue(resp.asString().contains("<ns0:Code>W</ns0:Code>"), "Response does not contain Code");
	Assert.assertTrue(resp.asString().contains("<ns0:Description>Week</ns0:Description>"), "Response does not contain Description");
	Assert.assertTrue(resp.asString().contains("<ns0:Code>HT</ns0:Code>"), "Response does not contain Code");
	Assert.assertTrue(resp.asString().contains("<ns0:Description>30 minutes</ns0:Description>"), "Response does not contain Description");

		
	}

}
