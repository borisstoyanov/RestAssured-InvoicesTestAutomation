package requests;

public class RetrieveInvoiceInfoSPRequest {
	public RetrieveInvoiceInfoSPRequest(){
	    contentType = "text/xml; charset=UTF-8;";
	    endpoint = "/InvoiceManagementModelService/InvoiceAppModuleService";	
	}
	
	public RetrieveInvoiceInfoSPRequest setInvoiceID(String invoiceID) {
		this.invoiceID = invoiceID;
		return this;
	}

	private String invoiceID;
	
	public String contentType  = "";
	public String endpoint = "";

	public String done(){
		return "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:typ=\"/com/vistajet/invoicemanagmenet/model/common/types/\"> "
				+ "<soapenv:Header/>"
				+ "   <soapenv:Body>"
				+ "      <typ:retrieveInvoiceInfoSP>"
				+ "         <typ:InvoiceId>" + invoiceID + "</typ:InvoiceId>"
				+ "      </typ:retrieveInvoiceInfoSP>"
				+ "   </soapenv:Body>"
				+ "</soapenv:Envelope>";
				
	}
	
}
