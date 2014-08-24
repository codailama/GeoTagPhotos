/**
 * 
 */
package com.codailama.api.geotagphotos.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.Map;
import java.util.Set;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.stream.XMLStreamException;

import com.codailama.api.geotagphotos.kml.KmlParser;
import com.codailama.api.geotagphotos.kml.gx.Coord;
import com.codailama.api.geotagphotos.kml.gx.GxEnum;
import com.codailama.api.geotagphotos.kml.gx.When;

/**
 * @author digvijaybhakuni
 *
 */
public class KMLUtils {

	/**
	 * 
	 */
	public KMLUtils() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * This method return Coord from the Map provider as per approximately time provide in millisecond
	 * @param tracMap <Map>
	 * @param time <Date>
	 * @return <Coord>
	 */
	public static Coord getCoordinateByTime(Map<When,Coord> tracMap,long time){
		return getCoordinateByTime(tracMap, time, GxEnum.APPROXIMATE);
	}
	
	/**
	 * This method return Coord from the Map provider as per GxEnum time provide in millisecond
	 * @param tracMap
	 * @param time
	 * @param closeTo
	 * @return
	 */
	public static Coord getCoordinateByTime(Map<When,Coord> tracMap,long time,GxEnum closeTo){
		return tracMap.get(getWhenFor(tracMap.keySet(),time,closeTo));
	}
	

	private static When getWhenFor(Set<When> whens, long time,
			GxEnum closeTo) {
		long diff = 0;
		When afterWhen = null;
		When beforeWhen = null;
		long after = Long.MAX_VALUE;
		long before = Long.MIN_VALUE;
		for(When w : whens){
			diff = time - w.getWhen();
			if(diff == 0){
				return w;
			}else if(diff > 0 && after > diff){
				after = diff;
				afterWhen = w;
			}else if(diff < 0 && before < diff) {
				before = diff;
				beforeWhen = w;
			}
		}
		switch(closeTo){
			case AFTER:
				System.out.println("after >> "+after);
				return afterWhen;
			case BEFORE:
				System.out.println("before >> "+before);
				return beforeWhen;
			case APPROXIMATE:
				System.out.println("after >> "+after+" before >> "+before);
				return ((before*(-1)) > after)? afterWhen : beforeWhen ;
			default:
				return new When();
		}
	}

	/**
	 * This method return new object of Map<When, Coord> by Parsing KML File (In InputStream)
	 * @param inputStream
	 * @return Map<When, Coord>
	 * @throws XMLStreamException
	 * @throws DatatypeConfigurationException
	 * @throws IOException
	 */
	public static Map<When, Coord> gatTrackMap(InputStream inputStream) throws XMLStreamException, DatatypeConfigurationException, IOException{
		return (new  KmlParser()).getTrackMap(inputStream);
	}
	
	/**
	 * This method return new object of Map<When, Coord> by Parsing KML File (In String Path of File)
	 * @param path
	 * @return Map<When, Coord>
	 * @throws XMLStreamException
	 * @throws DatatypeConfigurationException
	 * @throws IOException
	 */
	public static Map<When, Coord> gatTrackMap(String path) throws XMLStreamException, DatatypeConfigurationException, IOException{
		return (new  KmlParser()).getTrackMap(path);
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		long s = Calendar.getInstance().getTimeInMillis();
		String path = "/home/dbhakuni/Downloads/history-07-17-2014.kml";
		KmlParser kmlParser = new  KmlParser();
		try {
			Map<When, Coord> map = kmlParser.getTrackMap(path);
			Coord coord = getCoordinateByTime(map, 1405658575805L, GxEnum.APPROXIMATE);
			System.out.println(coord);
		} catch (XMLStreamException e) {
			System.out.println("XMLStreamException");
			e.printStackTrace();
		} catch (DatatypeConfigurationException e) {
			System.out.println("DatatypeConfigurationException");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("IOException");
			e.printStackTrace();
		}
		long e = Calendar.getInstance().getTimeInMillis();
		System.out.println("TIME SPAN (milisecond) : "+ (e - s));
		System.out.println("KMLUtils Main Ends...");
	}

}
