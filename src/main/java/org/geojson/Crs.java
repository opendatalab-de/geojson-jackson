package org.geojson;

import org.geojson.jackson.CrsType;

import java.io.Serializable;
import java.util.Map;

public class Crs implements Serializable {

	public CrsType type = CrsType.name;
	public Map<String, Object> properties;

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Crs)) return false;

		Crs crs = (Crs) o;

		if (type != crs.type) return false;
		return properties != null ? properties.equals(crs.properties) : crs.properties == null;

	}

	@Override
	public int hashCode() {
		int result = type != null ? type.hashCode() : 0;
		result = 31 * result + (properties != null ? properties.hashCode() : 0);
		return result;
	}

	@Override
	public String toString() {
		return "Crs{" + "type='" + type + '\'' + ", properties=" + properties + '}';
	}
}
