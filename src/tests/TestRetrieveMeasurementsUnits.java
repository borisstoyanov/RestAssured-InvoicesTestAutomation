package tests;

import static com.jayway.restassured.RestAssured.given;

import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.response.Response;

import enums.Pass;
import enums.Users;
import requests.RetrieveMeasurementsUnitsRequest;
import utils.TestInstance;
import utils.WebServiceTest;

public class TestRetrieveMeasurementsUnits extends WebServiceTest{
	RetrieveMeasurementsUnitsRequest request;

	@BeforeMethod(alwaysRun = true)
	public void setup(){
		RestAssured.baseURI = TestInstance.getServerName(); 
		request = new RetrieveMeasurementsUnitsRequest();
		 
	}
	
	@AfterMethod(alwaysRun = true)
	public void tearDown(ITestContext testContext){
		
		System.out.println("for test: "  + testContext.getName() + " req is: " + req);
		
	}
	@Test(groups = { "2.4.1.0" })
	public void test3(){
		req = "test3";
	}
	@Test(groups = { "2.4.1.0" })
	public void test4(){
		req = "test4";
	}
	@Test(groups = { "2.4.1.0" })
	public void test5(){
		req = "test5";
	}
	@Test(groups = { "2.4.1.0" })
	public void test6(){
		req = "test6";
	}
	@Test(groups = { "2.4.1.0" })
	public void test7(){
		req = "test7";
	}
	@Test(groups = { "2.4.1.0" })
	public void test8(){
		req = "test8";
	}
	@Test(groups = { "2.4.1.0" })
	public void test9(){
		req = "test9";
	}
	@Test(groups = { "2.4.1.0" })
	public void test10(){
		req = "test10";
	}
	@Test(groups = { "2.4.1.0" })
	public void test11(){
		req = "test11";
	}
	@Test(groups = { "2.4.1.0" })
	public void test12(){
		req = "test12";
	}
	@Test(groups = { "2.4.1.0" })
	public void test13(){
		req = "test13";
	}
	
	@Test(groups = { "2.4.1.0" })
	public void checkRetrieveMeasurementsUnits(){
		req = "test2";
		
//		
//		req = request.done();
//		
//
//		Response resp = given().request().auth().basic(Users.DIMITROV.getUsername(), Pass.DIMITROV.getPassword())
//		.contentType(request.contentType).body(req)
//	
//	.when()
//		.post(request.endpoint);
//	
//	Assert.assertTrue(resp.asString().contains("<ns0:Code>W</ns0:Code>"), "Response does not contain Code");
//	Assert.assertTrue(resp.asString().contains("<ns0:Description>Week</ns0:Description>"), "Response does not contain Description");
//	Assert.assertTrue(resp.asString().contains("<ns0:Code>HT</ns0:Code>"), "Response does not contain Code");
//	Assert.assertTrue(resp.asString().contains("<ns0:Description>30 minutes</ns0:Description>"), "Response does not contain Description");

		
	}

}
