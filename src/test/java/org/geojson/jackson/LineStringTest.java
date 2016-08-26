package org.geojson.jackson;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.geojson.LineString;
import org.geojson.MultiPoint;
import org.geojson.Position;
import org.geojson.PositionFactory;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class LineStringTest {

	private ObjectMapper mapper = new ObjectMapper();

	@Test
	public void itShouldSerializeMultiPoint() throws Exception {
		MultiPoint lineString = new LineString(PositionFactory.create(100, 0), PositionFactory.create(101, 1));
		assertEquals("{\"type\":\"LineString\",\"coordinates\":[[100.0,0.0],[101.0,1.0]]}",
				mapper.writeValueAsString(lineString));
	}

	@Test
	public void itShouldDeserializeLineString() throws Exception {
		LineString lineString = mapper.readValue("{\"type\":\"LineString\",\"coordinates\":[[100.0,0.0],[101.0,1.0]]}", LineString.class);
		assertNotNull(lineString);
		List<Position> coordinates = lineString.coordinates;
		PointTest.assertLngLatAlt(100, 0, coordinates.get(0));
		PointTest.assertLngLatAlt(101, 1, coordinates.get(1));
	}
}
