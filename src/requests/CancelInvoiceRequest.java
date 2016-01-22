package requests;

public class CancelInvoiceRequest {
	public CancelInvoiceRequest(){
	    contentType = "text/xml; charset=UTF-8;";
	    endpoint = "/InvoiceManagementModelService/InvoiceAppModuleService";	
	}
	
	public CancelInvoiceRequest setInvoiceID(String invoiceID) {
		this.invoiceID = invoiceID;
		return this;
	}

	private String invoiceID;
	
	public String contentType  = "";
	public String endpoint = "";

	public String done(){
		return "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:typ=\"/com/vistajet/invoicemanagmenet/model/common/types/\">"
				+ "   <soapenv:Header/>"
				+ "   <soapenv:Body>"
				+ "      <typ:cancelInvoice>"
				+ "         <typ:invoiceId>" + invoiceID + "</typ:invoiceId>"
				+ "         <typ:cancelledBy>stoyanov</typ:cancelledBy>"
				+ "         <typ:processId>proc</typ:processId>"
				+ "      </typ:cancelInvoice>"
				+ "   </soapenv:Body>"
				+ "</soapenv:Envelope>";				
	}
}

