package org.geojson.jackson;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.geojson.GeoJsonObject;
import org.geojson.GeoJsonObjectVisitor;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class GeoJsonObjectTest {

	private ObjectMapper mapper = new ObjectMapper();

	@Test
	public void itShouldHaveProperties() throws Exception {
		TestGeoJsonObject testObject = new TestGeoJsonObject();
		assertNotNull(testObject.getProperties());
	}

	@Test
	public void itShouldSerializeEmptyProperties() throws Exception {
		// http://geojson.org/geojson-spec.html#feature-objects
		// A feature object must have a member with the name "properties".
		// The value of the properties member is an object (any JSON object or a JSON null value).
		TestGeoJsonObject testObject = new TestGeoJsonObject();
		assertEquals("{\"type\":\"GeoJsonObjectTest$TestGeoJsonObject\",\"properties\":{}}",
				mapper.writeValueAsString(testObject));
	}

	private class TestGeoJsonObject extends GeoJsonObject {

		@Override
		public <T> T accept(GeoJsonObjectVisitor<T> geoJsonObjectVisitor) {
			throw new RuntimeException("not implemented");
		}
	}
}
