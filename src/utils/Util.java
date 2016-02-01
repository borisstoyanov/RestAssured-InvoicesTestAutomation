package utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

import org.joda.time.DateTime;
import org.testng.ITestResult;

public class Util {

	public static void after(ITestResult result) {
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(result.getEndMillis());
		WebServiceTest.executionDate = dateFormat.format(calendar.getTime());
		WebServiceTest.verificationErrors = result.getThrowable();
	}

	public static String getRandomID() {

		return UUID.randomUUID().toString() ;
	}

	private static DateTime getDateTime(){
		return new DateTime();
	}
	
	public static String getDate() {
		TimeZone tz = TimeZone.getTimeZone("UTC");
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:MM:ss");
		String date = getDateTime().toDate().toString();
		
		String dateWithFormat = df.format(getDateTime().toDate());
		
		df.setTimeZone(tz);
		String nowAsISO = df.format(getDateTime().toDate());
//		String nowAsISO = "2016-02-01T08:02:11";
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

}
