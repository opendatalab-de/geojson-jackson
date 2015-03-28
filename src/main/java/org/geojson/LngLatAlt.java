package org.geojson;

import java.math.BigDecimal;

import org.geojson.jackson.LngLatAltDeserializer;
import org.geojson.jackson.LngLatAltSerializer;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonDeserialize(using = LngLatAltDeserializer.class)
@JsonSerialize(using = LngLatAltSerializer.class)
public class LngLatAlt {

	private BigDecimal longitude = null;
	private BigDecimal latitude = null;
	private BigDecimal altitude = null;

	public LngLatAlt() {
	}

	public LngLatAlt(double longitude, double latitude) {
		this.longitude = BigDecimal.valueOf(longitude);
		this.latitude = BigDecimal.valueOf(latitude);
	}

	public LngLatAlt(double longitude, double latitude, double altitude) {
      this.longitude = BigDecimal.valueOf(longitude);
      this.latitude = BigDecimal.valueOf(latitude);
		this.altitude = BigDecimal.valueOf(altitude);
	}

   public LngLatAlt(BigDecimal longitude, BigDecimal latitude) {
      this.longitude = longitude;
      this.latitude = latitude;
   }

   public LngLatAlt(BigDecimal longitude, BigDecimal latitude, BigDecimal altitude) {
      this.longitude = longitude;
      this.latitude = latitude;
      this.altitude = altitude;
   }

	public boolean hasAltitude() {
		return altitude != null;
	}

	public BigDecimal getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
      if (!Double.isNaN(longitude)) {
         this.longitude = BigDecimal.valueOf(longitude);
      }
      else {
         this.longitude = null;
      }
	}

	public BigDecimal getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
      if (!Double.isNaN(latitude)) {
         this.latitude = BigDecimal.valueOf(latitude);
      }
      else {
         this.latitude = null;
      }
	}

	public BigDecimal getAltitude() {
		return altitude;
	}

	public void setAltitude(double altitude) {
	   if (!Double.isNaN(altitude)) {
	      this.altitude = BigDecimal.valueOf(altitude);
	   }
	   else {
	      this.altitude = null;
	   }
	}

	@Override
   public boolean equals(Object obj)
   {
      if (this == obj) {
         return true;
      }
      if (obj == null) {
         return false;
      }
      if (!(obj instanceof LngLatAlt)) {
         return false;
      }
      LngLatAlt other = (LngLatAlt) obj;
      if (altitude == null) {
         if (other.altitude != null) {
            return false;
         }
      }
      else if (!altitude.equals(other.altitude)) {
         return false;
      }
      if (latitude == null) {
         if (other.latitude != null) {
            return false;
         }
      }
      else if (!latitude.equals(other.latitude)) {
         return false;
      }
      if (longitude == null) {
         if (other.longitude != null) {
            return false;
         }
      }
      else if (!longitude.equals(other.longitude)) {
         return false;
      }
      return true;
   }

	@Override
   public int hashCode()
   {
      final int prime = 31;
      int result = 1;
      result = prime * result + ((altitude == null) ? 0 : altitude.hashCode());
      result = prime * result + ((latitude == null) ? 0 : latitude.hashCode());
      result = prime * result + ((longitude == null) ? 0 : longitude.hashCode());
      return result;
   }

	@Override
	public String toString() {
		return "LngLatAlt{" + "longitude=" + longitude + ", latitude=" + latitude + ", altitude=" + altitude + '}';
	}
}
