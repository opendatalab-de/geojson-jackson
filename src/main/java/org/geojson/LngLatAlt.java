package org.geojson;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.geojson.jackson.LngLatAltDeserializer;
import org.geojson.jackson.LngLatAltSerializer;

import java.io.Serializable;

@JsonDeserialize(using = LngLatAltDeserializer.class)
@JsonSerialize(using = LngLatAltSerializer.class)
public class LngLatAlt implements Serializable{

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

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof LngLatAlt)) {
			return false;
		}
		LngLatAlt lngLatAlt = (LngLatAlt)o;
		return Double.compare(lngLatAlt.latitude, latitude) == 0 && Double.compare(lngLatAlt.longitude, longitude) == 0
				&& Double.compare(lngLatAlt.altitude, altitude) == 0;
	}

	@Override
	public int hashCode() {
		long temp = Double.doubleToLongBits(longitude);
		int result = (int)(temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(latitude);
		result = 31 * result + (int)(temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(altitude);
		result = 31 * result + (int)(temp ^ (temp >>> 32));
		return result;
	}

	@Override
	public String toString() {
		return "LngLatAlt{" + "longitude=" + longitude + ", latitude=" + latitude + ", altitude=" + altitude + '}';
	}
}
