GeoTagPhotos
============

This API tags photos with the help of timestamps obtained from KML file and photos. Capable of tagging many formats that follow EXIF. 

.....


###To extract Track from KML File (Kml By Google Location History)  ###

* This will always return new Object Map<When, Coord> and take's a InputStream Object of KML File  
<code> 
Map<When, Coord> trackMap = KMLUtils.getTrackMap(inputStream);  
</code>
  
* This will return Coord Object from the Map provider at Approximate Time.  
<code>  
Coord coord = KMLUtils.getCoordinateByTime(trackMap, timeInLong);  
</code>
