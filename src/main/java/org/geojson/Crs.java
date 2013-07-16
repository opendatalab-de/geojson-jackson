package org.geojson;

import java.util.HashMap;
import java.util.Map;

public class Crs {

	private String type = "name";
	private Map<String, Object> properties = new HashMap<String, Object>();

	public String getType() {
		return type;
	}

	public Map<String, Object> getProperties() {
		return properties;
	}

	public void setProperties(Map<String, Object> properties) {
		this.properties = properties;
	}
}
