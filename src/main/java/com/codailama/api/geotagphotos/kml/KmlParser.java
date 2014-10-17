/**
 * 
 */
package com.codailama.api.geotagphotos.kml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import com.codailama.api.geotagphotos.kml.gx.Coord;
import com.codailama.api.geotagphotos.kml.gx.GxEnum;
import com.codailama.api.geotagphotos.kml.gx.When;
import com.codailama.api.geotagphotos.utils.GxUtils;

/**
 * @author digvijaybhakuni
 *
 */
public class KmlParser {

	/**
	 * 
	 */
	public KmlParser() {
	}

	public Map<When, Coord> getTrackMap(String path) throws XMLStreamException, DatatypeConfigurationException, IOException {
		return getTrackMap(new FileInputStream(new File(path)));
	}

	public Map<When, Coord> getTrackMap(InputStream inputStream) throws XMLStreamException, DatatypeConfigurationException, IOException {
		Map<When, Coord> trackMap = new HashMap<When, Coord>();
		try {
			
			XMLEventReader eventReader = XMLInputFactory.newInstance()
					.createXMLEventReader(inputStream);
			XMLEvent event = null;
			StartElement startElement = null;
			// EndElement endElement = null;
			Characters characters = null;
			GxEnum tagIs = GxEnum.NOACTION;
			String when = null;
			String coord = null;
			while (eventReader.hasNext()) {
				event = eventReader.nextEvent();
				if (event.isStartElement()) {
					startElement = event.asStartElement();
					if ("when".equals(startElement.getName().getLocalPart())) {
						tagIs = GxEnum.WHEN;
					} else if ("coord".equals(startElement.getName()
							.getLocalPart())) {
						tagIs = GxEnum.COORD;
					}
				} else if (event.isCharacters()) {
					characters = event.asCharacters();
					switch (tagIs) {
					case WHEN:
						when = GxUtils.getCData(characters.getData());
						tagIs = GxEnum.NOACTION;
						break;
					case COORD:
						coord = GxUtils.getCData(characters.getData());
						tagIs = GxEnum.PUTINMAP;
						break;
					default:
						break;
					}

				} else if (event.isEndElement()) {
					// endElement = event.asEndElement();
					if (tagIs == GxEnum.PUTINMAP) {
						//System.out.println(when + "{+}" + coord);
						trackMap.put(new When(when), new Coord(coord));
						tagIs = GxEnum.NOACTION;
					}
				}
			}
			inputStream.close();
			System.out.println(trackMap.size());
		}  catch (XMLStreamException e) {
			e.printStackTrace();
			throw new XMLStreamException(e);
		} catch (FactoryConfigurationError e) {
			e.printStackTrace();
			throw new FactoryConfigurationError();
		} catch (DatatypeConfigurationException e) {
			e.printStackTrace();
			throw new DatatypeConfigurationException();
		}finally{
			inputStream.close();
		}
		return new TreeMap<When, Coord>(trackMap);
	}
	
	
	/**
	 * @param args
	 * @throws FactoryConfigurationError
	 * @throws XMLStreamException
	 */
	public static void main(String[] args) {

		String path = "/home/dbhakuni/Downloads/history-07-17-2014.kml";
		Map<When, Coord> trackMap = null;
		try {
			trackMap = (new KmlParser()).getTrackMap(path);
			System.out.println(trackMap.size());
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (XMLStreamException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FactoryConfigurationError e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DatatypeConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
