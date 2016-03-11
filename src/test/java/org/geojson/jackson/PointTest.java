package org.geojson.jackson;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.geojson.GeoJsonObject;
import org.geojson.LngLatAlt;
import org.geojson.Point;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;

import static org.junit.Assert.*;

public class PointTest {

	private ObjectMapper mapper = new ObjectMapper();

	public static void assertLngLatAlt(double expectedLongitude, double expectedLatitude, double expectedAltitude,
									   LngLatAlt point) {
		assertLngLatAlt(expectedLongitude, expectedLatitude, expectedAltitude, new double[0], point);
	}

	public static void assertLngLatAlt(double expectedLongitude, double expectedLatitude, double expectedAltitude,
									   double[] expectedAdditionalElements, LngLatAlt point) {
		assertEquals(expectedLongitude, point.getLongitude(), 0.00001);
		assertEquals(expectedLatitude, point.getLatitude(), 0.00001);
		if (Double.isNaN(expectedAltitude)) {
			assertFalse(point.hasAltitude());
		} else {
			assertEquals(expectedAltitude, point.getAltitude(), 0.00001);
			assertTrue(Arrays.equals(expectedAdditionalElements, point.getAdditionalElements()));
		}
	}

	@Test
	public void itShouldSerializeAPoint() throws Exception {
		Point point = new Point(100, 0);
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
		assertLngLatAlt(100, 5, Double.NaN, point.getCoordinates());
	}

	@Test
	public void itShouldDeserializeAPointWithAltitude() throws Exception {
		GeoJsonObject value = mapper.readValue("{\"type\":\"Point\",\"coordinates\":[100.0,5.0,123]}",
				GeoJsonObject.class);
		Point point = (Point)value;
		assertLngLatAlt(100, 5, 123, point.getCoordinates());
	}

	@Test
	public void itShouldSerializeAPointWithAltitude() throws Exception {
		Point point = new Point(100, 0, 256);
		assertEquals("{\"type\":\"Point\",\"coordinates\":[100.0,0.0,256.0]}",
				mapper.writeValueAsString(point));
	}

	@Test
	public void itShouldDeserializeAPointWithAdditionalAttributes() throws IOException {
		GeoJsonObject value = mapper.readValue("{\"type\":\"Point\",\"coordinates\":[100.0,5.0,123,456,789.2]}",
				GeoJsonObject.class);
		Point point = (Point)value;
		assertLngLatAlt(100, 5, 123, new double[] {456d, 789.2}, point.getCoordinates());
	}

	@Test
	public void itShouldSerializeAPointWithAdditionalAttributes() throws JsonProcessingException {
		Point point = new Point(100, 0, 256, 345d, 678d);
		assertEquals("{\"type\":\"Point\",\"coordinates\":[100.0,0.0,256.0,345.0,678.0]}",
				mapper.writeValueAsString(point));
	}

	@Test
	public void itShouldSerializeAPointWithAdditionalAttributesAndNull() throws JsonProcessingException {
		Point point = new Point(100, 0, 256, 345d, 678d);
		assertEquals("{\"type\":\"Point\",\"coordinates\":[100.0,0.0,256.0,345.0,678.0]}",
				mapper.writeValueAsString(point));
	}
}
