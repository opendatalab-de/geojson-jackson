package org.geojson;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.HashMap;
import java.util.Map;

public class Feature extends GeoJsonObject {

	public String id;
	@JsonInclude(JsonInclude.Include.ALWAYS)
	public Map<String, Object> properties;

	@JsonInclude(JsonInclude.Include.ALWAYS)
	public GeoJsonObject geometry;

	public void addProperty(String key, Object value) {
		if (properties == null) properties = new HashMap<String, Object>();
		properties.put(key, value);
	}

	@Override
	public <T> T accept(GeoJsonObjectVisitor<T> geoJsonObjectVisitor) {
		return geoJsonObjectVisitor.visit(this);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Feature)) return false;
		if (!super.equals(o)) return false;

		Feature feature = (Feature) o;

		if (id != null ? !id.equals(feature.id) : feature.id != null) return false;
		if (properties != null ? !properties.equals(feature.properties) : feature.properties != null) return false;
		return geometry != null ? geometry.equals(feature.geometry) : feature.geometry == null;

	}

	@Override
	public int hashCode() {
		int result = super.hashCode();
		result = 31 * result + (id != null ? id.hashCode() : 0);
		result = 31 * result + (properties != null ? properties.hashCode() : 0);
		result = 31 * result + (geometry != null ? geometry.hashCode() : 0);
		return result;
	}

	@Override
	public String toString() {
		return "Feature{properties=" + properties + ", geometry=" + geometry + ", id='" + id + "'}";
	}
}
