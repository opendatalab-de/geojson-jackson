package org.geojson.jackson;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.geojson.GeoJsonObject;
import org.geojson.Position;
import org.geojson.Point;
import org.geojson.PointFactory;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;

import static org.junit.Assert.*;

public class PointTest {

	private ObjectMapper mapper = new ObjectMapper();

	public static void assertLngLatAlt(double expectedLongitude, double expectedLatitude, Position point) {
		assertLngLatAlt(expectedLongitude, expectedLatitude, null, null, point);
	}

	public static void assertLngLatAlt(double expectedLongitude, double expectedLatitude, Double expectedAltitude,
									   Position point) {
		assertLngLatAlt(expectedLongitude, expectedLatitude, expectedAltitude, null, point);
	}

	public static void assertLngLatAlt(double expectedLongitude, double expectedLatitude, Double expectedAltitude,
									   Double[] expectedAdditionalElements, Position point) {
		assertEquals(expectedLongitude, point.longitude, 0.00001);
		assertEquals(expectedLatitude, point.latitude, 0.00001);
		if (expectedAltitude == null) {
			assertFalse(point.altitude != null);
		} else {
			assertEquals(expectedAltitude, point.altitude, 0.00001);
			assertTrue(Arrays.equals(expectedAdditionalElements, point.additionalElements));
		}
	}

	@Test
	public void itShouldSerializeAPoint() throws Exception {
		Point point = PointFactory.create(100, 0);
		assertEquals("{\"type\":\"Point\",\"coordinates\":[100.0,0.0]}",
				mapper.writeValueAsString(point));
	}

	@Test
	public void itShouldDeserializeAPoint() throws Exception {
		GeoJsonObject value = mapper
				.readValue("{\"type\":\"Point\",\"coordinates\":[100.0,5.0]}", GeoJsonObject.class);
		assertNotNull(value);
		assertTrue(value instanceof Point);
		Point point = (Point)value;
		assertLngLatAlt(100, 5, point.coordinates);
	}

	@Test
	public void itShouldDeserializeAPointWithAltitude() throws Exception {
		GeoJsonObject value = mapper.readValue("{\"type\":\"Point\",\"coordinates\":[100.0,5.0,123]}",
				GeoJsonObject.class);
		Point point = (Point)value;
		assertLngLatAlt(100, 5, 123.D, point.coordinates);
	}

	@Test
	public void itShouldSerializeAPointWithAltitude() throws Exception {
		Point point = PointFactory.create(100, 0, 256);
		assertEquals("{\"type\":\"Point\",\"coordinates\":[100.0,0.0,256.0]}",
				mapper.writeValueAsString(point));
	}

	@Test
	public void itShouldDeserializeAPointWithAdditionalAttributes() throws IOException {
		GeoJsonObject value = mapper.readValue("{\"type\":\"Point\",\"coordinates\":[100.0,5.0,123,456,789.2]}",
				GeoJsonObject.class);
		Point point = (Point)value;
		assertLngLatAlt(100, 5, 123.D, new Double[] {456d, 789.2}, point.coordinates);
	}

	@Test
	public void itShouldSerializeAPointWithAdditionalAttributes() throws JsonProcessingException {
		Point point = PointFactory.create(100, 0, 256, 345d, 678d);
		assertEquals("{\"type\":\"Point\",\"coordinates\":[100.0,0.0,256.0,345.0,678.0]}",
				mapper.writeValueAsString(point));
	}

	@Test
	public void itShouldSerializeAPointWithAdditionalAttributesAndNull() throws JsonProcessingException {
		Point point = PointFactory.create(100, 0, 256, 345d, 678d);
		assertEquals("{\"type\":\"Point\",\"coordinates\":[100.0,0.0,256.0,345.0,678.0]}",
				mapper.writeValueAsString(point));
	}
}
