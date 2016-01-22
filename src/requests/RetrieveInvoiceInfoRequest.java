package requests;

public class RetrieveInvoiceInfoRequest {

	public RetrieveInvoiceInfoRequest(){
	    contentType = "text/xml; charset=UTF-8;";
	    endpoint = "/InvoiceManagementModelService/InvoiceAppModuleService";	
	}
	
	public RetrieveInvoiceInfoRequest setInvoiceID(String invoiceID) {
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
				+ "      <typ:retrieveInvoiceInfo>"
				+ "         <typ:InvoiceId>" + invoiceID + "</typ:InvoiceId>"
				+ "      </typ:retrieveInvoiceInfo>"
				+ "   </soapenv:Body>"
				+ "</soapenv:Envelope>";
				
	}

}
