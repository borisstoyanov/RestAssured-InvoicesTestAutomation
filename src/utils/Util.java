package utils;

import java.io.StringReader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

import javax.print.Doc;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.joda.time.DateTime;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

public class Util {

	public static String getRandomID() {

		return UUID.randomUUID().toString() ;
	}

	private static DateTime getDateTime(){
		return new DateTime();
	}
	
	public static String getDate() {
		TimeZone tz = TimeZone.getTimeZone("UTC");
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:MM:ss");
		df.setTimeZone(tz);
		String nowAsISO = df.format(getDateTime().toDate());
		return nowAsISO;
	}

	public static String getDateMinusYear(int minusYears) {
		TimeZone tz = TimeZone.getTimeZone("UTC");
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:MM:ss");
		df.setTimeZone(tz);
		String nowAsISO = df.format(getDateTime().minusYears(minusYears).toDate());
		return nowAsISO;
		
		
	}

	public static String incrementDate(String invoiceDate) {
		
		DateFormat df = new SimpleDateFormat("yyyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
		Calendar c = Calendar.getInstance();

		try {
			c.setTime(df.parse(invoiceDate));

		} catch (ParseException e) {
			e.printStackTrace();
		}
		c.add(Calendar.DAY_OF_MONTH, 1);  
		invoiceDate = df.format(c.getTime());
		
		return invoiceDate;
		
	}

	public static String getValueFromResponse(String responseAsString, String node) {
		String splittedResp = responseAsString.split("</" + node +">")[0];
		return splittedResp.split("<" + node +">")[1];
	}

	public static String formatDate(String periodOfCost, String format) {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:MM:ss");
		Date parsedDate = null;
		try {
			parsedDate = df.parse(periodOfCost);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		DateFormat newDateFormat = new SimpleDateFormat(format);
		String newDate = newDateFormat.format(parsedDate);
		return newDate;

		
	}

	public static String generateNumber() {
		return new Integer(ThreadLocalRandom.current().nextInt(100000, 9999999 + 1)).toString();
	}

	public static Document textToXML(String xmlString){
		Document document = null;
	    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();  
	    DocumentBuilder builder;  
	    try  
	    {  
	        builder = factory.newDocumentBuilder();  
	        document = builder.parse( new InputSource( new StringReader( xmlString ) ) );  
	    } catch (Exception e) {  
	        e.printStackTrace();  
	    } 
	    
	    return document;
	}
	
	
}
