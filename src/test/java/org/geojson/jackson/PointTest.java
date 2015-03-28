package org.geojson.jackson;

import static org.junit.Assert.*;

import java.math.BigDecimal;

import org.geojson.GeoJsonObject;
import org.geojson.LngLatAlt;
import org.geojson.Point;
import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

public class PointTest {

	private ObjectMapper mapper = new ObjectMapper();

	@Test
	public void itShouldSerializeAPoint() throws Exception {
		Point point = new Point(100, 0);
		assertEquals("{\"type\":\"Point\",\"coordinates\":[100.0,0.0]}", mapper.writeValueAsString(point));
	}

	@Test
	public void itShouldDeserializeAPoint() throws Exception {
		GeoJsonObject value = mapper.readValue("{\"type\":\"Point\",\"coordinates\":[100.0,5.0]}", GeoJsonObject.class);
		assertNotNull(value);
		assertTrue(value instanceof Point);
		Point point = (Point)value;
		assertLngLatAlt(new BigDecimal("100.0"), new BigDecimal("5.0"), null, point.getCoordinates());
	}

	public static void assertLngLatAlt(BigDecimal expectedLongitue, BigDecimal expectedLatitude, BigDecimal expectedAltitude,
			LngLatAlt point) {
		//assertEquals(expectedLongitue, point.getLongitude(), 0.00001);
		assertEquals(expectedLongitue, point.getLongitude());
		//assertEquals(expectedLatitude, point.getLatitude(), 0.00001);
		assertEquals(expectedLatitude, point.getLatitude());
		if (expectedAltitude == null)
			assertFalse(point.hasAltitude());
		else
			//assertEquals(expectedAltitude, point.getAltitude(), 0.00001);
		   assertEquals(expectedAltitude, point.getAltitude());
	}

	@Test
	public void itShouldDeserializeAPointWithAltitude() throws Exception {
		GeoJsonObject value = mapper.readValue("{\"type\":\"Point\",\"coordinates\":[100.0,5.0,123]}",
				GeoJsonObject.class);
		Point point = (Point)value;
		assertLngLatAlt(new BigDecimal("100.0"), new BigDecimal("5.0"), new BigDecimal("123.0"), point.getCoordinates());
	}

	@Test
	public void itShouldSerializeAPointWithAltitude() throws Exception {
		Point point = new Point(100, 0, 256);
		assertEquals("{\"type\":\"Point\",\"coordinates\":[100.0,0.0,256.0]}", mapper.writeValueAsString(point));
	}
}
