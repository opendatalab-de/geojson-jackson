package org.geojson;

import org.geojson.jackson.LngLatAltDeserializer;
import org.geojson.jackson.LngLatAltSerializer;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonDeserialize(using = LngLatAltDeserializer.class)
@JsonSerialize(using = LngLatAltSerializer.class)
public class LngLatAlt {

	private double longitude;
	private double latitude;
	private double altitude = Double.NaN;

	public LngLatAlt() {
	}

	public LngLatAlt(double longitude, double latitude) {
		this.longitude = longitude;
		this.latitude = latitude;
	}

	public LngLatAlt(double longitude, double latitude, double altitude) {
		this.longitude = longitude;
		this.latitude = latitude;
		this.altitude = altitude;
	}

	public boolean hasAltitude() {
		return !Double.isNaN(altitude);
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getAltitude() {
		return altitude;
	}

	public void setAltitude(double altitude) {
		this.altitude = altitude;
	}
	
	public boolean isNorthOf(LngLatAlt that)
	{
		return isFartherThan(that, Direction.N);
	}

	public boolean isSouthOf(LngLatAlt that)
	{
		return isFartherThan(that, Direction.S);
	}
	
	public boolean isEastOf(LngLatAlt that)
	{
		return isFartherThan(that, Direction.E);
	}

	public boolean isWestOf(LngLatAlt that)
	{
		return isFartherThan(that, Direction.W);
	}
	
	public boolean isFartherThan(LngLatAlt that, Direction d)
	{
		switch (d)
		{
			case N:
				return that.getLatitude() < this.getLatitude();
			case E:
				return that.getLongitude() < this.getLongitude();
			case S:
				return that.getLatitude() > this.getLatitude();
			case W:
				return that.getLongitude() > this.getLongitude();
			default:
				return false;
		}
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if (!(obj instanceof LngLatAlt)) return false;
		LngLatAlt that = (LngLatAlt) obj;
		return (that.latitude == this.latitude ? (that.longitude == this.longitude ? (that.altitude == this.altitude ? true : false) : false) : false);
	}

    private int hashCode = 17;
    private int hashSeed = 37;

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode()
	{
		hashCode = 17;
		append(latitude);
		append(longitude);
		append(altitude);
		return hashCode;
	}
	
	private void append(double val)
	{
        long latVal = Double.doubleToLongBits(val);
        hashCode = hashCode * hashSeed + ((int) (latVal ^ (latVal >> 32)));
        
	}
}
