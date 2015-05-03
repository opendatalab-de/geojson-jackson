package org.geojson;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class FeatureTest {

	private ObjectMapper mapper = new ObjectMapper();

	@Test
	public void itShouldSerializeFeature() throws Exception {
		Feature feature = new Feature();
		assertEquals("{\"type\":\"Feature\",\"properties\":{},\"geometry\":null}",
				mapper.writeValueAsString(feature));
	}
}