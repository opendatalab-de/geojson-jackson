package org.geojson.jackson;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.geojson.LngLatAlt;
import org.geojson.MultiPolygon;
import org.geojson.Polygon;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MultiPoligonTest {

	private ObjectMapper mapper = new ObjectMapper();

	@Test
	public void itShouldSerialize() throws Exception {
		MultiPolygon multiPolygon = new MultiPolygon();
		multiPolygon.add(new Polygon(new LngLatAlt(102, 2), new LngLatAlt(103, 2), new LngLatAlt(103, 3),
				new LngLatAlt(102, 3), new LngLatAlt(102, 2)));
		Polygon polygon = new Polygon(MockData.EXTERNAL);
		polygon.addInteriorRing(MockData.INTERNAL);
		multiPolygon.add(polygon);
		assertEquals(
				"{\"type\":\"MultiPolygon\",\"coordinates\":[[[[102.0,2.0],[103.0,2.0],[103.0,3.0],[102.0,3.0],[102.0,2.0]]],"
						+ "[[[100.0,0.0],[101.0,0.0],[101.0,1.0],[100.0,1.0],[100.0,0.0]],"
						+ "[[100.2,0.2],[100.8,0.2],[100.8,0.8],[100.2,0.8],[100.2,0.2]]]]}",
				mapper.writeValueAsString(multiPolygon));
	}

	@Test
	public void itShouldDeserialize() throws Exception {
		MultiPolygon multiPolygon = mapper.readValue(
				"{\"type\":\"MultiPolygon\",\"coordinates\":[[[[102.0,2.0],[103.0,2.0],[103.0,3.0],[102.0,3.0],[102.0,2.0]]],"
						+ "[[[100.0,0.0],[101.0,0.0],[101.0,1.0],[100.0,1.0],[100.0,0.0]],"
						+ "[[100.2,0.2],[100.8,0.2],[100.8,0.8],[100.2,0.8],[100.2,0.2]]]]}", MultiPolygon.class);
		assertEquals(2, multiPolygon.getCoordinates().size());
	}
}
