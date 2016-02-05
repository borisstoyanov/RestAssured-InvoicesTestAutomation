package requests;

public class RetrieveMeasurementsUnitsRequest {

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
