package tests;

import org.testng.Assert;
import org.testng.IClass;
import org.testng.ITestNGMethod;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.response.Response;

import enums.Pass;
import enums.Users;
import requests.RetrieveMeasurementsUnitsRequest;
import utils.TestInstance;
import utils.WebServiceTest;

public class TestRetrieveMeasurementsUnits {
	RetrieveMeasurementsUnitsRequest request;
	WebServiceTest test;

	@BeforeClass(alwaysRun = true)
	public void setup() {
		RestAssured.baseURI = TestInstance.getServerName();
		request = new RetrieveMeasurementsUnitsRequest();

	}


	@AfterMethod(alwaysRun = true)
	public void tearDown(ITestResult tr) {

		IClass cls = tr.getTestClass();
		ITestNGMethod method = tr.getMethod();

		System.out.println("For class: " + cls + " for test: " + method + " req is: " + tr.getAttribute("request"));

	}

	@Test(groups = { "2.4.1.0" })
	public void test3() {

		ITestResult result = Reporter.getCurrentTestResult();
		result.setAttribute("request", "test3");
	}





	@Test(groups = { "2.4.1.0" })
	public void checkRetrieveMeasurementsUnits() {
		test.req = "test2";

		
		 String req = request.done();
		
		
		 Response resp =
		 given().request().auth().basic(Users.DIMITROV.getUsername(),
		 Pass.DIMITROV.getPassword())
		 .contentType(request.contentType).body(req)
		
		 .when()
		 .post(request.endpoint);
		
		 Assert.assertTrue(resp.asString().contains("<ns0:Code>W</ns0:Code>"),
		 "Response does not contain Code");
		 Assert.assertTrue(resp.asString().contains("<ns0:Description>Week</ns0:Description>"),
		 "Response does not contain Description");
		 Assert.assertTrue(resp.asString().contains("<ns0:Code>HT</ns0:Code>"),
		 "Response does not contain Code");
		 Assert.assertTrue(resp.asString().contains("<ns0:Description>30minutes</ns0:Description>"),
				 "Response does not contain Description");

	}

}
