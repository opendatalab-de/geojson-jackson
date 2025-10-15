package org.geojson.jackson;

import org.geojson.GeoJsonObject;
import org.geojson.GeoJsonObjectVisitor;
import tools.jackson.databind.ObjectMapper;

public class GeoJsonObjectTest {

	private ObjectMapper mapper = new ObjectMapper();


	private class TestGeoJsonObject extends GeoJsonObject {

		@Override
		public <T> T accept(GeoJsonObjectVisitor<T> geoJsonObjectVisitor) {
			throw new RuntimeException("not implemented");
		}
	}
}
