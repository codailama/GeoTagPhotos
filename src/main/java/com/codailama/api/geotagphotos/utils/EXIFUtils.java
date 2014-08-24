package com.codailama.api.geotagphotos.utils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.stream.XMLStreamException;

import org.apache.commons.imaging.ImageReadException;
import org.apache.commons.imaging.ImageWriteException;
import org.apache.commons.imaging.Imaging;
import org.apache.commons.imaging.common.IImageMetadata;
import org.apache.commons.imaging.formats.jpeg.JpegImageMetadata;
import org.apache.commons.imaging.formats.jpeg.exif.ExifRewriter;
import org.apache.commons.imaging.formats.tiff.TiffField;
import org.apache.commons.imaging.formats.tiff.TiffImageMetadata;
import org.apache.commons.imaging.formats.tiff.constants.ExifTagConstants;
import org.apache.commons.imaging.formats.tiff.taginfos.TagInfo;
import org.apache.commons.imaging.formats.tiff.write.TiffOutputSet;
import org.apache.commons.imaging.util.IoUtils;

import com.codailama.api.geotagphotos.kml.KmlParser;
import com.codailama.api.geotagphotos.kml.gx.Coord;
import com.codailama.api.geotagphotos.kml.gx.GxEnum;
import com.codailama.api.geotagphotos.kml.gx.When;

public class EXIFUtils {

	public static void main(String[] args) throws ImageReadException, ImageWriteException, IOException, ParseException {
		String filePath = "D:\\Team Offsite\\Santosh\\IMG_20140223_222345.jpg";
		final IImageMetadata metadata = Imaging.getMetadata(new File(filePath));
        final JpegImageMetadata jpegMetadata = (JpegImageMetadata) metadata;
        Long timestamp = getTimeStampFromImage(jpegMetadata);
		String path = "C:/Users/mmt3822/Downloads/history-02-23-2014.kml";
		KmlParser kmlParser = new  KmlParser();
		try {
			Map<When, Coord> map = kmlParser.getTrackMap(path);
			Coord coord = KMLUtils.getCoordinateByTime(map, timestamp, GxEnum.APPROXIMATE);
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
	
	}


	/**
	 * @param jpegMetadata
	 * 		Pass the metadata of the image.
	 * @return
	 * 		Capture timestamp of the image from the metadata. {@link ExifTagConstants.EXIF_TAG_DATE_TIME_ORIGINAL} is used to get the value.
	 * @throws ParseException
	 */
	public static Long getTimeStampFromImage(final JpegImageMetadata jpegMetadata) throws ParseException{
		final TagInfo tagInfo = ExifTagConstants.EXIF_TAG_DATE_TIME_ORIGINAL;
		final TiffField field = jpegMetadata.findEXIFValueWithExactMatch(tagInfo);
		String dateOfCaptureString = field.getValueDescription();
		SimpleDateFormat sdf = new SimpleDateFormat("''yyyy:MM:dd hh:mm:ss''");
		Date dateOfCapture = sdf.parse(dateOfCaptureString);
		return dateOfCapture.getTime();
	}

	/**
	 * @param jpegMetadata
	 * 		Input jpeg file.
	 * @param longitude
	 * 		Longitude to be tagged.
	 * @param latitude
	 * 		Latitude to be tagged.
	 * @return TiffOutputSet with GPS set to new latlong.
	 * @throws IOException
	 * @throws ImageReadException
	 * @throws ImageWriteException
	 */
	public static TiffOutputSet setTiffOutputSet(final JpegImageMetadata jpegMetadata, final double longitude, final double latitude) throws IOException, ImageReadException, ImageWriteException {
		TiffOutputSet outputSet = null;
		if (null != jpegMetadata) {
			final TiffImageMetadata exif = jpegMetadata.getExif();
			if (null != exif) {
				outputSet = exif.getOutputSet();
			}
		}
		if (null == outputSet) {
			outputSet = new TiffOutputSet();
		}
		outputSet.setGPSInDegrees(longitude, latitude);
		return outputSet;
	}


	/**
	 * This method sets the EXIF the of the JPEG file and outputs it to given directory.
	 * @param jpegImageFile
	 * 		Input jpeg file.
	 * @param dst
	 * 		output jpeg file.
	 * @param longitude
	 * 		Longitude to be tagged.
	 * @param latitude
	 * 	 	Latitude to be tagged.
	 * @throws IOException
	 * @throws ImageReadException
	 * @throws ImageWriteException
	 */
	public static void setExifGPSTag(final File jpegImageFile, final File dst, final double longitude, final double latitude) throws IOException,
	ImageReadException, ImageWriteException {
		OutputStream os = null;
		boolean canThrow = false;
		try {
			
			final IImageMetadata metadata = Imaging.getMetadata(jpegImageFile);
			final JpegImageMetadata jpegMetadata = (JpegImageMetadata) metadata;
			TiffOutputSet outputSet = setTiffOutputSet(jpegMetadata, longitude, latitude);
			os = new FileOutputStream(dst);
			os = new BufferedOutputStream(os);
			new ExifRewriter().updateExifMetadataLossless(jpegImageFile, os, outputSet);
			canThrow = true;
		} finally {
			IoUtils.closeQuietly(canThrow, os);
		}
	}


}
