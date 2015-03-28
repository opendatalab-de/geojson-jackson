package org.geojson.jackson;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.util.List;

import org.geojson.LineString;
import org.geojson.LngLatAlt;
import org.geojson.MultiPoint;
import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

public class LineStringTest {

	private ObjectMapper mapper = new ObjectMapper();

	@Test
	public void itShouldSerializeMultiPoint() throws Exception {
		MultiPoint lineString = new LineString(new LngLatAlt(100, 0), new LngLatAlt(101, 1));
		assertEquals("{\"type\":\"LineString\",\"coordinates\":[[100.0,0.0],[101.0,1.0]]}",
				mapper.writeValueAsString(lineString));
	}

	@Test
	public void itShouldDeserializeLineString() throws Exception {
		LineString lineString = mapper.readValue("{\"type\":\"LineString\",\"coordinates\":[[100.0,0.0],[101.0,1.0]]}",
				LineString.class);
		assertNotNull(lineString);
		List<LngLatAlt> coordinates = lineString.getCoordinates();
		PointTest.assertLngLatAlt(new BigDecimal("100.0"), new BigDecimal("0.0"), null, coordinates.get(0));
		PointTest.assertLngLatAlt(new BigDecimal("101.0"), new BigDecimal("1.0"), null, coordinates.get(1));
	}
}
