package org.geojson.jackson;

import static org.junit.Assert.*;

import org.geojson.GeoJsonObject;
import org.geojson.GeoJsonObjectVisitor;
import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

public class GeoJsonObjectTest {

	private ObjectMapper mapper = new ObjectMapper();

	private class TestGeoJsonObject extends GeoJsonObject {
        @Override
        public <T> T accept(GeoJsonObjectVisitor<T> geoJsonObjectVisitor) {
            // TODO ?
            return null;
        }
    }

	@Test
	public void itShouldHaveProperties() throws Exception {
		TestGeoJsonObject testObject = new TestGeoJsonObject();
		assertNotNull(testObject.getProperties());
	}

	@Test
	public void itShouldNotSerializeEmptyProperties() throws Exception {
		TestGeoJsonObject testObject = new TestGeoJsonObject();
		assertEquals("{\"type\":\"GeoJsonObjectTest$TestGeoJsonObject\"}", mapper.writeValueAsString(testObject));
	}
}
