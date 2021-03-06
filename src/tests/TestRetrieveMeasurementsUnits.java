package tests;

import static com.jayway.restassured.RestAssured.given;

import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.response.Response;

import requests.RetrieveMeasurementsUnitsRequest;
import utils.StoreResults;
import utils.TestInstance;

public class TestRetrieveMeasurementsUnits {

	@BeforeClass(alwaysRun = true)
	public void setup() {
		RestAssured.baseURI = TestInstance.getServerName();

	}

	@AfterMethod(alwaysRun = true)
	public void tearDown(ITestResult tr) {
		tr.setAttribute("test_instance", RestAssured.baseURI);
		StoreResults.insertResults(tr);
	}
	
	public void setResponse(String response) {

		ITestResult result = Reporter.getCurrentTestResult();
		result.setAttribute("resp", response);
	}
	
	public void setRequest(String request) {

		ITestResult result = Reporter.getCurrentTestResult();
		result.setAttribute("request", request);
	}

	@Test(groups = { "2.4.1.0" })
	public void checkRetrieveMeasurementsUnits() throws InterruptedException {
		RetrieveMeasurementsUnitsRequest request = new RetrieveMeasurementsUnitsRequest();
		String req = request.done();
		Response resp = given().request()
				.contentType(request.contentType).body(req)

				.when().post(request.endpoint);
		setRequest(req);
		setResponse(resp.asString());
		Assert.assertTrue(resp.asString().contains("<ns0:Code>W</ns0:Code>"), "Response does not contain Code");
		Assert.assertTrue(resp.asString().contains("<ns0:Description>Week</ns0:Description>"),
				"Response does not contain Description");
		Assert.assertTrue(resp.asString().contains("<ns0:Code>HT</ns0:Code>"), "Response does not contain Code.");
		Assert.assertTrue(resp.asString().contains("<ns0:Description>30 minutes</ns0:Description>"),
				"Response does not contain Description");

	}

}
