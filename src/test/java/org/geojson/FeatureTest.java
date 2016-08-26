package org.geojson;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class FeatureTest {

	private Feature testObject = new Feature();
	private ObjectMapper mapper = new ObjectMapper();

	@Test
	public void itShouldNotHaveProperties() throws Exception {
		assertNull(testObject.properties);
	}

	@Test
	public void itShouldSerializeFeature() throws Exception {
		// http://geojson.org/geojson-spec.html#feature-objects
		// A feature object must have a member with the name "properties".
		// The value of the properties member is an object (any JSON object or a JSON null value).
		assertEquals("{\"type\":\"Feature\",\"properties\":null,\"geometry\":null}",
				mapper.writeValueAsString(testObject));
	}
}