package requests;

import java.util.HashMap;
import java.util.Map;

import com.jayway.restassured.response.Header;

import utils.Util;

public class ExtInvoiceSupPortRequest {
	public Map<String, String> header = new HashMap<String, String>();
	public Header h;
	public String endpoint = "";
	
	public String contentType  = "";
	
	public ExtInvoiceSupPortRequest(){
		h = new Header("Content-Type","text/xml; charset=UTF-8");
	    header.put("SOAPAction", "/com/vistajet/invoicemanagmenet/model/common/retrieveInvoicesHeader");
	    header.put("Content-Type","text/xml; charset=UTF-8");
	    
	    endpoint = "/soa-infra/services/default/InvoiceApproval/SupplierPortalApprovalProcess.service";	
	    
	    contentType = "text/xml; charset=UTF-8;";
	    
	    //invoiceDate = Util.getDate();
	    setInvoiceDate(Util.getDate());
	    setItemDate(Util.getDate());
	    
	    
}
	
	public ExtInvoiceSupPortRequest setSupplierCode(String supplierCode) {
		this.supplierCode = supplierCode;
		return this;
	}

	public ExtInvoiceSupPortRequest setSupplierName(String supplierName) {
		this.supplierName = supplierName;
		return this;
	}

	public ExtInvoiceSupPortRequest setVjCompanyCode(String vjCompanyCode) {
		this.vjCompanyCode = vjCompanyCode;
		return this;
	}

	public ExtInvoiceSupPortRequest setVjCompanyName(String vjCompanyName) {
		this.vjCompanyName = vjCompanyName;
		return this;
	}

	public ExtInvoiceSupPortRequest setInvoiceHeader(String invoiceHeader) {
		this.invoiceHeader = invoiceHeader;
		return this;
	}

	public ExtInvoiceSupPortRequest setInvoiceNumber(String invoiceNumber) {
		this.invoiceNumber = invoiceNumber;
		return this;
	}

	public ExtInvoiceSupPortRequest setDocumentTypeFlag(String documentTypeFlag) {
		this.documentTypeFlag = documentTypeFlag;
		return this;
	}

	public ExtInvoiceSupPortRequest setInvoiceDate(String invoiceDate) {
		this.invoiceDate = invoiceDate;
		return this;
	}

	public ExtInvoiceSupPortRequest setInvoiceDueDate(String invoiceDueDate) {
		this.invoiceDueDate = invoiceDueDate;
		return this;
	}

	public ExtInvoiceSupPortRequest setInvoiceCurrency(String invoiceCurrency) {
		this.invoiceCurrency = invoiceCurrency;
		return this;
	}

	public ExtInvoiceSupPortRequest setNetAmount(String netAmount) {
		this.netAmount = netAmount;
		return this;
	}

	public ExtInvoiceSupPortRequest setVatAmount(String vatAmount) {
		this.vatAmount = vatAmount;
		return this;
	}

	public ExtInvoiceSupPortRequest setTotalAmount(String totalAmount) {
		this.totalAmount = totalAmount;
		return this;
	}

	public ExtInvoiceSupPortRequest setPoNumber(String poNumber) {
		this.poNumber = poNumber;
		return this;
	}

	public ExtInvoiceSupPortRequest setLineItemNumber(String lineItemNumber) {
		this.lineItemNumber = lineItemNumber;
		return this;
	}

	public ExtInvoiceSupPortRequest setDescription(String description) {
		this.description = description;
		return this;
	}

	public ExtInvoiceSupPortRequest setCurrency(String currency) {
		this.currency = currency;
		return this;
	}

	public ExtInvoiceSupPortRequest setSignIndicator(String signIndicator) {
		this.signIndicator = signIndicator;
		return this;
	}

	public ExtInvoiceSupPortRequest setLineItemNetAmount(String lineItemNetAmount) {
		this.lineItemNetAmount = lineItemNetAmount;
		return this;
	}

	public ExtInvoiceSupPortRequest setLineItemVatExemptionFlag(String lineItemVatExemptionFlag) {
		this.lineItemVatExemptionFlag = lineItemVatExemptionFlag;
		return this;
	}

	public ExtInvoiceSupPortRequest setLineItemVatAmount(String lineItemVatAmount) {
		this.lineItemVatAmount = lineItemVatAmount;
		return this;
	}

	public ExtInvoiceSupPortRequest setLineItemTotalAmount(String lineItemTotalAmount) {
		this.lineItemTotalAmount = lineItemTotalAmount;
		return this;
	}

	public ExtInvoiceSupPortRequest setItemDate(String itemDate) {
		this.itemDate = itemDate;
		return this;
	}

	public ExtInvoiceSupPortRequest setSvcRenderedAirport(String svcRenderedAirport) {
		this.svcRenderedAirport = svcRenderedAirport;
		return this;
	}

	public ExtInvoiceSupPortRequest setAirportFrom(String airportFrom) {
		this.airportFrom = airportFrom;
		return this;
	}

	public ExtInvoiceSupPortRequest setAirportTo(String airportTo) {
		this.airportTo = airportTo;
		return this;
	}

	public ExtInvoiceSupPortRequest setAircraftRegistration(String aircraftRegistration) {
		this.aircraftRegistration = aircraftRegistration;
		return this;
	}

	public ExtInvoiceSupPortRequest setCallSign(String callSign) {
		this.callSign = callSign;
		return this;
	}

	public ExtInvoiceSupPortRequest setServiceTypeCode(String serviceTypeCode) {
		this.serviceTypeCode = serviceTypeCode;
		return this;
	}

	public ExtInvoiceSupPortRequest setWorkOrderId(String workOrderId) {
		this.workOrderId = workOrderId;
		return this;
	}

	public ExtInvoiceSupPortRequest setPeriodOfCost(String periodOfCost) {
		this.periodOfCost = periodOfCost;
		return this;
	}

	public ExtInvoiceSupPortRequest setUnit(String unit) {
		this.unit = unit;
		return this;
	}

	public ExtInvoiceSupPortRequest setQuantity(String quantity) {
		this.quantity = quantity;
		return this;
	}

	public ExtInvoiceSupPortRequest setPricePerUnit(String pricePerUnit) {
		this.pricePerUnit = pricePerUnit;
		return this;
	}

	public ExtInvoiceSupPortRequest setTaxCode(String taxCode) {
		this.taxCode = taxCode;
		return this;
	}

	public ExtInvoiceSupPortRequest setFullDescription(String fullDescription) {
		this.fullDescription = fullDescription;
		return this;
	}

	public ExtInvoiceSupPortRequest setContentSize(String contentSize) {
		this.contentSize = contentSize;
		return this;
	}

	public ExtInvoiceSupPortRequest setFileType(String fileType) {
		this.fileType = fileType;
		return this;
	}

	public ExtInvoiceSupPortRequest setFileContent(String fileContent) {
		this.fileContent = fileContent;
		return this;
	}

	public ExtInvoiceSupPortRequest setCommentText(String commentText) {
		this.commentText = commentText;
		return this;
	}

	public ExtInvoiceSupPortRequest setAuthor(String author) {
		this.author = author;
		return this;
	}

	public ExtInvoiceSupPortRequest setSupplierCreatorId(String supplierCreatorId) {
		this.supplierCreatorId = supplierCreatorId;
		return this;
	}

	public ExtInvoiceSupPortRequest setSupplierCreatorName(String supplierCreatorName) {
		this.supplierCreatorName = supplierCreatorName;
		return this;
	}

	/*
	 * Invoice Header Fields
	 */
	private String supplierCode = "0000100924";
	private String supplierName = "";
	private String vjCompanyCode = "0200";
	private String vjCompanyName = "";
	private String invoiceHeader = "SupplP3311";
	private String invoiceNumber = Util.getRandomID().substring(0, 16);
	private String documentTypeFlag = "I";
	private String invoiceDate;
	private String invoiceDueDate = "";
	private String invoiceCurrency = "AON";
	private String netAmount = "120";
	private String vatAmount = "104";
	private String totalAmount = "2461";
	private String poNumber = "";
	//	End of invoice header fields
		
	/*
	 *  Invoice Line Items
	 */
	private String lineItemNumber = "0";
	private String description = "WATER SERVICE";
	private String currency = "$";
	private String signIndicator = "D";
	private String lineItemNetAmount = "500";
	private String lineItemVatExemptionFlag = "0";
	private String lineItemVatAmount = "502";
	private String lineItemTotalAmount = "501";
	private String itemDate = "";
	private String svcRenderedAirport = "UUWW";
	private String airportFrom = "EGLF";
	private String airportTo = "UKOO";
	private String aircraftRegistration = "OE-GVH";
	private String callSign = "II";
	private String serviceTypeCode = "";
	private String workOrderId = "";
	private String periodOfCost = "";
	private String unit = "TRD";
	private String quantity = "24";
	private String pricePerUnit = "2.7";
	private String taxCode = "A1";
	private String fullDescription = "ddd";
	// End of Line Items 
		
	/*
	 * 	Invoice Files
	 */
	private String contentSize = "1000";
	private String fileType = "application/pdf";
	private String fileContent = "JVBERi0xLjQKJcOkw7zDtsOfCjIgMCBvYmoKPDwvTGVuZ3RoIDMgMCBSL0ZpbHRlci9GbGF0ZURl"
			+ "Y29kZT4+CnN0cmVhbQp4nK1ZS4/bNhC++1foHMAOh28ChoD1Wlu0t7QL9FD01CbtYdMiueTvd16k"
			+ "KPlZOAigkPTo4zw/DrVmB8O3zZfB4L9QwpC9Hb5+3Pz6bvhnAwP9+/rXxvDy500VeGNRg6++0Yv6"
			+ "v/zy9+bTuw0Uv0tDSrBz+Jordpd19jbILEPhmUguZyq5YagvuBhEPYOrUOLOooTB5x+fN+9//OyH"
			+ "47/DBzXBkLqH140vJOUSvvH65/D+BQ2B4fXTb3sD49btjR3T3rhxG/fGm2DiuIW9SeM2700e/d4U"
			+ "XnjCp9mbw7i1e/NMrxxlJfBPezPxLy84BlrM49ZXmCZepWQ3Xj6VBMNquREFRZm6KY0Fhn9kLAAa"
			+ "BgUWmUgodoYF2S+Apx9kQggQdCdcEBzeCSJjya6RlRy3RTftNKzYrnlHlOhUrFbhAiRGgkzvKkIg"
			+ "fzUxcbSaIriiQ1C7ROyoYvK/7TwUesceTwLWO1PGod9F4sB+8bMFBwloF8Iw/v7602Z63awTLQAl"
			+ "rHW7sko0QMsCql/joEpPY+QQQHN2UIleoYNmTAuXaiSBqWZqEqOkZye1PFaUa0l8El+dH1ocVC01"
			+ "AJ7IaSwjGQWdo8go1WEVvKoqm2LG0BXLuVD6heu72DUvsr5q0lP9vblmLhGFlx9FWUk7MZM1ngtH"
			+ "V3kouvg57SRfJA97jGrAI0yiBTJnV8stbyixmPOQeEPUMVIvj+3O41iFFhORYu5sYCFQgiYTkKoV"
			+ "jcdLNJVazlRuiSdbXYIziccqtJjcBPO2k5PJZVt7uGVZxjAkMCi6KsoDV5MEXmOpEeKgSOqUjnQl"
			+ "P1NPoM+jXzFKT+dcHTXLZ/ZkeoUjc0LNEildeVlSVPLGowaNs2tpdMTbJWtX/V2uspoT79HbsmDn"
			+ "lvQCzLV5i1///xnQdrFaWgA9y8PLfOz1pNZxk2daqMbVIKjHZblWN/OKWNIdgeiItDhZT85Xa66w"
			+ "OwwBYsfujhPJAgftMJa9tSNwW6FzOuzs3jrrr6L6UnZhhdq/z7xvyTQZyl5ySvNml88knzHxnYUO"
			+ "34vWjl60iTwElIo2E37h1YvqukxlBsnis+JZwXsiGp1YvTDOKcNhmkZfh+hlDoGnuAXJA+qi8BVK"
			+ "KW5oJA88V4rnFGSSvBkdn6mdXEXngCFHM3Fv9RhuBVdMDH6IMWDLeLZZ5GS6/jK6HFYv485eK8M+"
			+ "8/MiAjrYYV9MnLYKWBRuMI7cksktnPWBPWsp98h5ZiIXO2KCCztgOqKXqPV26y2OnGjP6iZErmBR"
			+ "Ahg0bcgNjUomPXz99e7IFWqPaFtYbztRdaJFTpnACHmYZ84BMQ8sRoBXMyXCS9eisg8up6xxaGiM"
			+ "/qQtU492R7pQGAaHqfkaYjgfZ9sfFDPvCdndiev8Lt6RP3J63ALDyl9nsmVScYbqdcHZfFQwy9xQ"
			+ "lEnFYxrZsznqgBpbCpOzzCxPo1WSkRziTD0Qb6Iejnn7QDWu4nnU9BMJ5ijNOwaBA4Mrg50wVtPU"
			+ "xcEFaZk8qkpj6kp0zO2CiHTD044EMBo+kjMJqOiYgGScqAWpQotJON+PsPcC9mYzJeeaPYGcR8cQ"
			+ "cSM5hQ3EmsxUBUBshisZhRwfDuhsbzOeCtIK8OHjyeEgAbCl9vf13USr4AL62EXZCh0ppxYsQFjy";
	//	End of Invoice Files
		
	/*
	 * Invoice Comments
	 */
	private String commentText = "My Test Comment 1";
	private String author = "estafetdev3";
	//	End of invoice comments
	
	/*
	 * Supplier Portal Data
	 */
	private String supplierCreatorId = "estafetdev3";
	private String supplierCreatorName = "Angel";
	
	
	public String done(){
		return  "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" "
				+ "xmlns:sup=\"http://xmlns.oracle.com/bpmn/bpmnProcess/SupplierPortalApprovalProcess\" "
				+ "xmlns:ext=\"http://xmlns.oracle.com/bpm/bpmobject/PrePopInvoiceSupplierPortal/ExternalInvoiceSupplierPortal\" "
				+ "xmlns:rec=\"http://xmlns.oracle.com/bpm/bpmobject/PrePopInvoice/RecordType0\" "
				+ "xmlns:rec1=\"http://xmlns.oracle.com/bpm/bpmobject/PrePopInvoice/RecordType1\" "
				+ "xmlns:rec2=\"http://xmlns.oracle.com/bpm/bpmobject/PrePopInvoice/RecordType2\" "
				+ "xmlns:rec3=\"http://xmlns.oracle.com/bpm/bpmobject/PrePopInvoice/RecordType3\" "
				+ "xmlns:sup1=\"http://xmlns.oracle.com/bpm/bpmobject/PrePopInvoiceSupplierPortal/SupplierPortalData\">"
				+ "		<soapenv:Header/>"
				+ "	   <soapenv:Body>"
				+ "	      <sup:start>"
				+ "	         <ext:ExternalInvoiceSupplierPortal>"
				+ "	            <ext:invoiceHeader>"
				+ "	               <rec:supplierCode>" + supplierCode + "</rec:supplierCode>"
				+ "	               <rec:supplierName>" + supplierName + "</rec:supplierName>"
				+ "	               <rec:vjCompanyCode>" + vjCompanyCode + "</rec:vjCompanyCode>"
				+ "	               <rec:vjCompanyName>" + vjCompanyName + "</rec:vjCompanyName>"
				+ "	               <rec:invoiceHeader>" + invoiceHeader + "</rec:invoiceHeader>"
				+ "	               <rec:invoiceNumber>" + invoiceNumber + "</rec:invoiceNumber>"
				+ "	               <rec:documentTypeFlag>" + documentTypeFlag + "</rec:documentTypeFlag>"
				+ "	               <rec:invoiceDate>" + invoiceDate + "</rec:invoiceDate>"
				+ "	               <rec:invoiceDueDate>" + invoiceDueDate + "</rec:invoiceDueDate>"
				+ "	               <rec:invoiceCurrency>" + invoiceCurrency + "</rec:invoiceCurrency>"
				+ "	               <rec:netAmount>" + netAmount + "</rec:netAmount>"
				+ "	               <rec:vatAmount>" + vatAmount + "</rec:vatAmount>"
				+ "	               <rec:totalAmount>" + totalAmount + "</rec:totalAmount>"
				+ "	               <rec:poNumber>" + poNumber + "</rec:poNumber>"
				+ "	            </ext:invoiceHeader>"
				+ "	            <!--1 or more repetitions:-->"
				+ "	            <ext:lineItems>"
				+ "	               <rec1:lineItemNumber>" + lineItemNumber + "</rec1:lineItemNumber>"
				+ "	               <rec1:description>" + description + "</rec1:description>"
				+ "	               <rec1:currency>" + currency + "</rec1:currency>"
				+ "	               <rec1:signIndicator>" + signIndicator + "</rec1:signIndicator>"
				+ "	               <rec1:netAmount>" + lineItemNetAmount + "</rec1:netAmount>"
				+ "	               <rec1:vatExemptionFlag>" + lineItemVatExemptionFlag + "</rec1:vatExemptionFlag>"
				+ "	               <rec1:vatAmount>" + lineItemVatAmount + "</rec1:vatAmount>"
				+ "	               <rec1:totalAmount>" + lineItemTotalAmount + "</rec1:totalAmount>"
				+ "	               <rec1:itemDate>" + itemDate + "</rec1:itemDate>"
				+ "	               <rec1:svcRenderedAirport>" + svcRenderedAirport + "</rec1:svcRenderedAirport>"
				+ "	               <rec1:airportFrom>" + airportFrom + "</rec1:airportFrom>"
				+ "	               <rec1:airportTo>" + airportTo + "</rec1:airportTo>"
				+ "	               <rec1:aircraftRegistration>" + aircraftRegistration + "</rec1:aircraftRegistration>"
				+ "	               <rec1:callsign>" + callSign + "</rec1:callsign>"
				+ "	               <rec1:serviceTypeCode>" + serviceTypeCode + "</rec1:serviceTypeCode>"
				+ "	               <rec1:workOrderId>" + workOrderId + "</rec1:workOrderId>"
				+ "	               <rec1:periodOfCost>" + periodOfCost + "</rec1:periodOfCost>"
				+ "	               <rec1:unit>" + unit + "</rec1:unit>"
				+ "	               <rec1:quantity>" + quantity + "</rec1:quantity>"
				+ "	               <rec1:pricePerUnit>" + pricePerUnit + "</rec1:pricePerUnit>"
				+ "	               <rec1:taxCode>" + taxCode + "</rec1:taxCode>"
				+ "	               <rec1:fullDescription>" + fullDescription + "</rec1:fullDescription>"
				+ "	            </ext:lineItems>"
				+ "	            <!--1 or more repetitions:-->"
				+ "	            <ext:invoiceFiles>"
				+ "	               <rec2:contentSize>" + contentSize + "</rec2:contentSize>"
				+ "	               <rec2:fileType>" + fileType + "</rec2:fileType>"
				+ "	               <rec2:fileContent>" + fileContent + "</rec2:fileContent>"
				+ "	            </ext:invoiceFiles>"
				+ "	            <ext:invoiceFiles>"
				+ "	               <rec2:contentSize>" + contentSize + "</rec2:contentSize>"
				+ "	               <rec2:fileType>" + fileType + "</rec2:fileType>"
				+ "	               <rec2:fileContent>" + fileContent + "</rec2:fileContent>"
				+ "	            </ext:invoiceFiles>"
				+ "	            <!--1 or more repetitions:-->"
				+ "	            <ext:invoiceComments>"
				+ "	               <rec3:commentText>" + commentText + "</rec3:commentText>"
				+ "	               <rec3:author>" + author + "</rec3:author>"
				+ "	            </ext:invoiceComments>"
				+ "	            <ext:supplierPortalData>"
				+ "	               <sup1:supplierCreatorId>" + supplierCreatorId + "</sup1:supplierCreatorId>"
				+ "	               <sup1:supplierCreatorName>" + supplierCreatorName + "</sup1:supplierCreatorName>"
				+ "	            </ext:supplierPortalData>"
				+ "	         </ext:ExternalInvoiceSupplierPortal>"
				+ "	      </sup:start>"
				+ "	   </soapenv:Body>"
				+ "	</soapenv:Envelope>";

	}

	public String getInvoiceNumber() {
		return invoiceNumber;
	}

	public String getSupplierCreatorID() {
		return supplierCreatorId;
	}

	public String getAuthor() {
		return author;
	}

	public String getLineItemTotalAmount() {
		return lineItemTotalAmount;
	}

	public String getDescription() {
		return description;
	}

	public String getEntity() {
		return vjCompanyCode;
	}

	public String getVendor() {
		return supplierCode;
	}

	public String getUnit() {
		return unit;
	}

	public String getQuantity() {
		return quantity;
	}

	public String getPricePerUnit() {
		return pricePerUnit;
	}

	public String getVatAmount() {
		return lineItemVatAmount;
	}

	public String getAirportFrom() {
		return airportFrom;
	}

	public String getAirportTo() {
		return airportTo;
	}

	public String getTailNumber() {
		return aircraftRegistration;
	}

	public String getCallsign() {
		return callSign;
	}

	public String getServiceRenderAirport() {
		return svcRenderedAirport;
	}

	public String getTotalAmount() {
		return totalAmount;
	}

	public String getAmount() {
		return netAmount;
	}

	public String getItemDate() {
		return itemDate;
	}

	public String getLineItemAmount() {
		return lineItemNetAmount;
	}

}
