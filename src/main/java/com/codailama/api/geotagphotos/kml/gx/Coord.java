/**
 * 
 */
package com.codailama.api.geotagphotos.kml.gx;

import com.codailama.api.geotagphotos.utils.GxUtils;

/**
 * @author digvijaybhakuni
 *
 */
public class Coord {

	/**
	 * 
	 */
	public Coord() {
		// TODO Auto-generated constructor stub
	}
	
	public Coord(String coord) {
		float [] corrdsF = GxUtils.getCoord(coord);
		this.latitude  = corrdsF[0];
		this.longitude = corrdsF[1];
		this.elevation = corrdsF[2];
	}
	
	public Coord(float lat, float lng, float elv){
		this.latitude  = lat;
		this.longitude = lng;
		this.elevation = elv;
	}

	private float latitude;
	private float longitude;
	private float elevation;
	public float getLatitude() {
		return latitude;
	}

	public void setLatitude(float latitude) {
		this.latitude = latitude;
	}

	public float getLongitude() {
		return longitude;
	}

	public void setLongitude(float longitude) {
		this.longitude = longitude;
	}

	public float getElevation() {
		return elevation;
	}

	public void setElevation(float elevation) {
		this.elevation = elevation;
	}
	
	@Override public String toString(){
		return this.getLatitude()+" "+this.getLongitude()+" "+this.getElevation();
	}
	
}
