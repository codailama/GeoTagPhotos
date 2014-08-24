/**
 * 
 */
package com.codailama.api.geotagphotos.utils;

import java.util.Date;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;

/**
 * @author digvijaybhakuni
 *
 */
public class GxUtils {

	/**
	 * 
	 */
	public GxUtils() {
		// TODO Auto-generated constructor stub
	}
	
	public static long getDateInLong(Date date){
		return date.getTime();
	}
	
	public static long getDateInLong(String date) throws DatatypeConfigurationException {
//		DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSzzzzzz");
//		//date = date.substring(0, date.lastIndexOf('-'));
//		//TimeZone tz = TimeZone.getTimeZone("UTC");
//		//format.setTimeZone(tz);
//		//date.lastIndexOf('-');
//		System.out.println("DateLong>>"+format.parse(date).getTime());
//		return format.parse(date).getTime();
		
		return DatatypeFactory.newInstance().newXMLGregorianCalendar(date).toGregorianCalendar().getTimeInMillis();
		
	}
	
	public static float[] getCoord(String coord) {
		String [] coords = coord.split(" ");
		float [] coordsF = new float[3];
		for(int i = 0; i< 3; i++)
			coordsF[i] = getFloatFromString(coords[i]);
		return coordsF;
	}
	
	public static float getFloatFromString(String value){
		return Float.parseFloat(value);
	}
	
	public static String getCData(String cdata){
		return (cdata!=null) ? cdata.trim() : "" ;
	}

}
