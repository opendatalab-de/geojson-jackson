package org.geojson;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;

@JsonTypeInfo(property = "type", use = Id.NAME)
@JsonSubTypes({ @Type(Feature.class), @Type(Polygon.class), @Type(MultiPolygon.class), @Type(FeatureCollection.class),
		@Type(Point.class), @Type(MultiPoint.class), @Type(MultiLineString.class), @Type(LineString.class) })
@JsonInclude(Include.NON_NULL)
public abstract class GeoJsonObject {

	private Crs crs;
	private double[] bbox;
	@JsonInclude(Include.NON_EMPTY)
	private Map<String, Object> properties = new HashMap<String, Object>();

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

	public void setProperty(String key, Object value) {
		properties.put(key, value);
	}

	@SuppressWarnings("unchecked")
	public <T> T getProperty(String key) {
		return (T)properties.get(key);
	}

	public Map<String, Object> getProperties() {
		return properties;
	}

	public void setProperties(Map<String, Object> properties) {
		this.properties = properties;
	}

	public abstract <T> T accept(GeoJsonObjectVisitor<T> geoJsonObjectVisitor);

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof GeoJsonObject))
			return false;
		GeoJsonObject that = (GeoJsonObject)o;
		if (!Arrays.equals(bbox, that.bbox)) {
			return false;
		}
		if (crs != null ? !crs.equals(that.crs) : that.crs != null) {
			return false;
		}
		return !(properties != null ? !properties.equals(that.properties) : that.properties != null);
	}

	@Override
	public int hashCode() {
		int result = crs != null ? crs.hashCode() : 0;
		result = 31 * result + (bbox != null ? Arrays.hashCode(bbox) : 0);
		result = 31 * result + (properties != null ? properties.hashCode() : 0);
		return result;
	}
}
