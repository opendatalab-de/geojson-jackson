package org.geojson.jackson;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.util.List;

import org.geojson.LngLatAlt;
import org.geojson.MultiPoint;
import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

public class MultiPointTest {

	private ObjectMapper mapper = new ObjectMapper();

	@Test
	public void itShouldSerializeMultiPoint() throws Exception {
		MultiPoint multiPoint = new MultiPoint(new LngLatAlt(100, 0), new LngLatAlt(101, 1));
		assertEquals("{\"type\":\"MultiPoint\",\"coordinates\":[[100.0,0.0],[101.0,1.0]]}",
				mapper.writeValueAsString(multiPoint));
	}

	@Test
	public void itShouldDeserializeMultiPoint() throws Exception {
		MultiPoint multiPoint = mapper.readValue("{\"type\":\"MultiPoint\",\"coordinates\":[[100.0,0.0],[101.0,1.0]]}",
				MultiPoint.class);
		assertNotNull(multiPoint);
		List<LngLatAlt> coordinates = multiPoint.getCoordinates();
		PointTest.assertLngLatAlt(new BigDecimal("100.0"), new BigDecimal("0.0"), null, coordinates.get(0));
		PointTest.assertLngLatAlt(new BigDecimal("101.0"), new BigDecimal("1.0"), null, coordinates.get(1));
	}
}
