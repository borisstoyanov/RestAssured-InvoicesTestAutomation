package requests;

import static com.jayway.restassured.RestAssured.given;

import com.jayway.restassured.RestAssured;

public class RetrieveMeasurementsUnitsRequest extends RestAssured {

	public RetrieveMeasurementsUnitsRequest(){
	    contentType = "text/xml; charset=UTF-8;";
	    endpoint = "/InvoiceManagementModelService/InvoiceAppModuleService";	
	}
	
	public String contentType  = "";
	public String endpoint = "";

	public String done(){
		return "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:typ=\"/com/vistajet/invoicemanagmenet/model/common/types/\">"
				+ "   <soapenv:Header/>"
				+ "   <soapenv:Body>"
				+ "      <typ:retrieveMeasurementUnits/>"
				+ "   </soapenv:Body>"
				+ "</soapenv:Envelope>";								
	}
}
