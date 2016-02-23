package utils;

import org.testng.ITestResult;
import org.testng.Reporter;

public class WebServiceTest {
	
	public WebServiceTest(){

	}
	
	protected String executionDate;
	protected String testInstance;
	
	protected String build;
	protected Throwable verificationErrors;
	protected String testName;
	protected ITestResult result;
	protected String bug_id;
	protected String testID;
	
	public String req = "";
	protected String response;
	
	/*
	protected void setRequest(String request) {
		ITestResult result = Reporter.getCurrentTestResult();
		req = request;
		result.setAttribute("request", req);
	}
	*/
	
	/*
	protected void setResponse(String response){
		ITestResult result = Reporter.getCurrentTestResult();
		this.response = response;
		result.setAttribute("resp", this.response);
		
	}
	*/
}
