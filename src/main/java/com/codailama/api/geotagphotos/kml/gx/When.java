/**
 * 
 */
package com.codailama.api.geotagphotos.kml.gx;

import java.sql.Date;
import java.util.Comparator;

import javax.xml.datatype.DatatypeConfigurationException;

import com.codailama.api.geotagphotos.utils.GxUtils;

/**
 * @author digvijaybhakuni
 *
 */
public class When implements Comparator<When>,
		Comparable<When> {

	/**
	 * 
	 */
	public When() {
		// TODO Auto-generated constructor stub
	}

	public When(String when) throws DatatypeConfigurationException {
		this.setWhen(when);
	}

	public When(Date when) {
		this.setWhen(when);
	}

	public When(long when) {
		this.setWhen(when);
	}

	private long when;

	public long getWhen() {
		return when;
	}

	public void setWhen(long when) {
		this.when = when;
	}

	public void setWhen(Date when) {
		this.setWhen(GxUtils.getDateInLong(when));
	}

	public void setWhen(String when) throws DatatypeConfigurationException {
		this.setWhen(GxUtils.getDateInLong(when));
	}

	@Override
	public int hashCode() {
		//System.out.print("HashCode>>");
		//System.out.println(((Long) when).hashCode());
		return ((Long) when).hashCode();
	}

	public int compareTo(When when) {
		return compare(this, when);
	}

	public int compare(When o1, When o2) {
		//System.out.println(o1.getWhen()+" : "+o2.getWhen());
		return (o1.getWhen() < o2.getWhen()) ? -1 : ((o1.getWhen() == o2
				.getWhen()) ? 0 : 1);
	}
	
	@Override public String toString(){
		return String.valueOf(this.getWhen());
	}
	
}
