package org.geojson.jackson;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.geojson.Crs;
import org.geojson.GeoJsonObject;
import org.geojson.Point;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class CrsTest {

	private ObjectMapper mapper = new ObjectMapper();

	@Test
	public void itShouldParseCrsWithLink() throws Exception {
		GeoJsonObject value = mapper.readValue("{\"crs\": { \"type\": \"link\", \"properties\": "
				+ "{ \"href\": \"http://example.com/crs/42\", \"type\": \"proj4\" }},"
				+ "\"type\":\"Point\",\"coordinates\":[100.0,5.0]}", GeoJsonObject.class);
		assertNotNull(value);
		assertEquals(CrsType.link, value.getCrs().getType());
	}

	@Test
	public void itShouldSerializeCrsWithLink() throws Exception {
		Point point = new Point();
		Crs crs = new Crs();
		crs.setType(CrsType.link);
		point.setCrs(crs);
		String value = mapper.writeValueAsString(point);
		assertEquals("{\"type\":\"Point\",\"crs\":{\"type\":\"link\",\"properties\":{}}}", value);
	}
}
