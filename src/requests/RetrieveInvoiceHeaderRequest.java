package requests;

public class RetrieveInvoiceHeaderRequest {

	public RetrieveInvoiceHeaderRequest(){
		
		    endpoint = "/InvoiceManagementModelService/InvoiceAppModuleService";	
		    
		    contentType = "text/xml; charset=UTF-8;";
	}

	private String invoiceNumber = "";

	private String vendorID = "";

	private String entityID = "";

	private String supplierCreatorId = "";

	private String invoiceStatus = "";

	private String endDate = "";

	private String startDate = "";

	private String documentType = "";

	public String getInvoiceStatus() {
		return invoiceStatus;
	}

	public String endpoint = "";
	
	public String contentType  = "";

	private String documentType2 = "";

	private String documentType3 = "";

	private String entityID2 = "";

	private String vendorID2 = "";

	private String entityID3 = "";

	private String vendorID3 = "";

	public RetrieveInvoiceHeaderRequest setSupplierCreatorId(String supplierCreatorId) {
		this.supplierCreatorId = supplierCreatorId;
		return this;
	}

	/*
	 * Optional, can have many pairs
	 */
	public RetrieveInvoiceHeaderRequest setEntityID(String entityID) {
		this.entityID = entityID;
		return this;
	}

	/*
	 * Optional, can have many pairs
	 */
	public RetrieveInvoiceHeaderRequest setEntityID2(String entityID) {
		this.entityID2 = entityID;
		return this;
	}
	
	/*
	 * Optional, can have many pairs
	 */
	public RetrieveInvoiceHeaderRequest setEntityID3(String entityID) {
		this.entityID3 = entityID;
		return this;
	}
	
	
	/*
	 * Optional, can have many pairs
	 */
	public RetrieveInvoiceHeaderRequest setVendorID(String vendorID) {
		this.vendorID = vendorID;
		return this;
	}
	
	/*
	 * Optional, can have many pairs
	 */
	public RetrieveInvoiceHeaderRequest setVendorID2(String vendorID) {
		this.vendorID2 = vendorID;
		return this;
	}
	
	/*
	 * Optional, can have many pairs
	 */
	public RetrieveInvoiceHeaderRequest setVendorID3(String vendorID) {
		this.vendorID3 = vendorID;
		return this;
	}

	/*
	 * Optional
	 */
	public RetrieveInvoiceHeaderRequest setInvoiceNumber(String invoiceID) {
		this.invoiceNumber = invoiceID;
		return this;
	}

	/*
	 * Optional
	 */
	public RetrieveInvoiceHeaderRequest setInvoiceStatus(String invoiceStatus) {
		this.invoiceStatus = invoiceStatus;
		return this;
	}
	
	public RetrieveInvoiceHeaderRequest setStartDate(String string) {
		this.startDate = string;
		return this;
	}

	public RetrieveInvoiceHeaderRequest setEndDate(String string) {
		this.endDate = string;
		return this;
	}
	
	/*
	 * Zero or more repetitions:
	 */
	public RetrieveInvoiceHeaderRequest setDocumentType(String documentType){
		this.documentType = documentType;
		return this;
	}
	
	/*
	 * Zero or more repetitions:
	 */
	public RetrieveInvoiceHeaderRequest setDocumentType2(String documentType){
		this.documentType2 = documentType;
		return this;
	}
	
	/*
	 * Zero or more repetitions:
	 */
	public RetrieveInvoiceHeaderRequest setDocumentType3(String documentType){
		this.documentType3 = documentType;
		return this;
	}
	
	public String done(){
		return "<soapenv:Envelope "
				+ "xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" "
				+ "xmlns:typ=\"/com/vistajet/invoicemanagmenet/model/common/types/\" " 
				+ "xmlns:com=\"/com/vistajet/invoicemanagmenet/model/view/ws/common/\"> "  
  + 				"<soapenv:Header/>" 
  + 				"<soapenv:Body>"
  +				    "<typ:retrieveInvoicesHeader>"
  +				       "<typ:supplierCreatorId>" + supplierCreatorId + "</typ:supplierCreatorId>"
  +				       "<typ:entityVendorPair>"			
  +				          "<com:EntityId>" + entityID + "</com:EntityId>"		
  +				          "<com:VendorId>" + vendorID + "</com:VendorId>"		
  + 				   "</typ:entityVendorPair>"
  +				       "<typ:entityVendorPair>"			
  +				          "<com:EntityId>" + entityID2 + "</com:EntityId>"		
  +				          "<com:VendorId>" + vendorID2 + "</com:VendorId>"		
  + 				   "</typ:entityVendorPair>"
  +				       "<typ:entityVendorPair>"			
  +				          "<com:EntityId>" + entityID3 + "</com:EntityId>"		
  +				          "<com:VendorId>" + vendorID3 + "</com:VendorId>"		
  + 				   "</typ:entityVendorPair>"
  + 				   "<typ:startDate>" + startDate + "</typ:startDate>"
  +				       "<typ:endDate>" + endDate + "</typ:endDate>"
  +				       "<typ:documentType>" + documentType + "</typ:documentType>"		
  +				       "<typ:documentType>" + documentType2 + "</typ:documentType>"		
  +				       "<typ:documentType>" + documentType3 + "</typ:documentType>"		
  +				       "<typ:status>"		//Optional
  +				       "<com:InvoiceStatus>" + invoiceStatus + "</com:InvoiceStatus>"
  +				       "</typ:status>"
  +				       "<typ:invoiceNumber>" + invoiceNumber + "</typ:invoiceNumber>"
  +				       "<typ:registriesPerPage>20</typ:registriesPerPage>"
  +				       "<typ:pageNumber>1</typ:pageNumber>"
  +				    "</typ:retrieveInvoicesHeader>"
  +				 "</soapenv:Body>"
  +			 "</soapenv:Envelope>" ;

	}

	public String getInvoiceNumber() {
		return invoiceNumber;
	}


	
}
