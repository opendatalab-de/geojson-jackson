package org.geojson.jackson;

import static org.junit.Assert.*;

import org.geojson.GeoJsonObject;
import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

public class CrsTest {

	private ObjectMapper mapper = new ObjectMapper();

	@Test
	public void itShouldParseCrsWithLink() throws Exception {
		GeoJsonObject value = mapper.readValue("{\"crs\": { \"type\": \"link\", \"properties\": "
				+ "{ \"href\": \"http://example.com/crs/42\", \"type\": \"proj4\" }},"
				+ "\"type\":\"Point\",\"coordinates\":[100.0,5.0]}", GeoJsonObject.class);
		assertNotNull(value);
		assertEquals("link", value.getCrs().getType());
	}
}
