package org.geojson.jackson;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.geojson.MultiPoint;
import org.geojson.Position;
import org.geojson.PositionFactory;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class MultiPointTest {

	private ObjectMapper mapper = new ObjectMapper();

	@Test
	public void itShouldSerializeMultiPoint() throws Exception {
		MultiPoint multiPoint = new MultiPoint(PositionFactory.create(100, 0), PositionFactory.create(101, 1));
		assertEquals("{\"type\":\"MultiPoint\",\"coordinates\":[[100.0,0.0],[101.0,1.0]]}",
				mapper.writeValueAsString(multiPoint));
	}

	@Test
	public void itShouldDeserializeMultiPoint() throws Exception {
		MultiPoint multiPoint = mapper
				.readValue("{\"type\":\"MultiPoint\",\"coordinates\":[[100.0,0.0],[101.0,1.0]]}",
				MultiPoint.class);
		assertNotNull(multiPoint);
		List<Position> coordinates = multiPoint.coordinates;
		PointTest.assertLngLatAlt(100, 0, coordinates.get(0));
		PointTest.assertLngLatAlt(101, 1, coordinates.get(1));
	}
}
