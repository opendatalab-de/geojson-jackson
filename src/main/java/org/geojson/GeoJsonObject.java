package org.geojson;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@JsonTypeInfo(property = "type", use = Id.NAME)
@JsonSubTypes({ @Type(Feature.class), @Type(Polygon.class), @Type(MultiPolygon.class), @Type(FeatureCollection.class),
		@Type(Point.class), @Type(MultiPoint.class), @Type(MultiLineString.class), @Type(LineString.class),
                @Type(GeometryCollection.class) })
@JsonInclude(Include.NON_NULL)
public abstract class GeoJsonObject implements Serializable {
  static final String[] RESERVED_KEYS = new String[]{"crs", "bbox"};

  private Map<String, Object> foreignMembers = new HashMap<String, Object>();

  @JsonAnyGetter
  public Map<String, Object> getForeignMembers() {
    return foreignMembers;
  }

  public void addForeignMember(String key, Map<String, Object> value) {
    if (getReservedKeys().contains(key.toLowerCase()))
      throw new IllegalArgumentException("Invalid Foreign Member key " + key);
    foreignMembers.put(key, value);
  }

  protected List<String> getReservedKeys() {
    return Arrays.asList(RESERVED_KEYS);
  }

	private Crs crs;
	private double[] bbox;

	public Crs getCrs() {
		return crs;
	}

	public void setCrs(Crs crs) {
		this.crs = crs;
	}

	public double[] getBbox() {
		return bbox;
	}

	public void setBbox(double[] bbox) {
		this.bbox = bbox;
	}


	public abstract <T> T accept(GeoJsonObjectVisitor<T> geoJsonObjectVisitor);

	@Override public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		GeoJsonObject that = (GeoJsonObject)o;
		if (crs != null ? !crs.equals(that.crs) : that.crs != null)
			return false;
		return Arrays.equals(bbox, that.bbox);
	}

	@Override public int hashCode() {
		int result = crs != null ? crs.hashCode() : 0;
		result = 31 * result + (bbox != null ? Arrays.hashCode(bbox) : 0);
		return result;
	}

	@Override
	public String toString() {
		return "GeoJsonObject{}";
	}
}
