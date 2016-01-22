package tests;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasToString;

import org.testng.annotations.BeforeGroups;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.jayway.restassured.RestAssured;

import enums.Pass;
import enums.Users;
import requests.RetrieveMeasurementsUnitsRequest;
import utils.TestInstance;

public class TestRetrieveMeasurementsUnits {
	RetrieveMeasurementsUnitsRequest request;
	
	@BeforeGroups(groups = { "2.4.1.0" })
	public void beforeGroups(){
		setup();
	}

	@BeforeMethod
	public void setup(){
		RestAssured.baseURI = TestInstance.getServerName(); 
		request = new RetrieveMeasurementsUnitsRequest();
	}
	
	@Test(groups = { "2.4.1.0" })
	public void checkRetrieveMeasurementsUnits(){
	String req = request.done();
		given().request().auth().basic(Users.DIMITROV.getUsername(), Pass.DIMITROV.getPassword())
		.contentType(request.contentType).body(req)
	
	.when()
		.post(request.endpoint)
	
	.then()
		.body(hasToString(containsString("<ns0:Code>W</ns0:Code>")))
		.body(hasToString(containsString("<ns0:Description>Week</ns0:Description>")))
		.body(hasToString(containsString("<ns0:Code>HT</ns0:Code>")))
		.body(hasToString(containsString("<ns0:Description>30 minutes</ns0:Description>")));
		
	}

}
