package org.geojson.jackson;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.geojson.LngLatAlt;
import org.geojson.Polygon;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class PolygonTest {

	private ObjectMapper mapper = new ObjectMapper();

	@Test
	public void itShouldSerialize() throws Exception {
		Polygon polygon = new Polygon(MockData.EXTERNAL);
		assertEquals("{\"type\":\"Polygon\",\"coordinates\":"
				+ "[[[100.0,0.0],[101.0,0.0],[101.0,1.0],[100.0,1.0],[100.0,0.0]]]}",
				mapper.writeValueAsString(polygon));
	}

	@Test
	public void itShouldSerializeWithHole() throws Exception {
		Polygon polygon = new Polygon(MockData.EXTERNAL);
		polygon.addInteriorRing(MockData.INTERNAL);
		assertEquals("{\"type\":\"Polygon\",\"coordinates\":"
				+ "[[[100.0,0.0],[101.0,0.0],[101.0,1.0],[100.0,1.0],[100.0,0.0]],"
				+ "[[100.2,0.2],[100.8,0.2],[100.8,0.8],[100.2,0.8],[100.2,0.2]]]}", mapper.writeValueAsString(polygon));
	}

	@Test(expected = RuntimeException.class)
	public void itShouldFailOnAddInteriorRingWithoutExteriorRing() throws Exception {
		Polygon polygon = new Polygon();
		polygon.addInteriorRing(MockData.EXTERNAL);
	}

	@Test
	public void itShouldDeserialize() throws Exception {
		Polygon polygon = mapper.readValue("{\"type\":\"Polygon\",\"coordinates\":"
				+ "[[[100.0,0.0],[101.0,0.0],[101.0,1.0],[100.0,1.0],[100.0,0.0]],"
				+ "[[100.2,0.2],[100.8,0.2],[100.8,0.8],[100.2,0.8],[100.2,0.2]]]}", Polygon.class);
		assertListEquals(MockData.EXTERNAL, polygon.getExteriorRing());
		assertListEquals(MockData.INTERNAL, polygon.getInteriorRing(0));
		assertListEquals(MockData.INTERNAL, polygon.getInteriorRings().get(0));
	}

	@Test
	public void itShouldSetExteriorRing() throws Exception {
		Polygon polygon = new Polygon();
		polygon.setExteriorRing(MockData.EXTERNAL);
		assertEquals(MockData.EXTERNAL, polygon.getExteriorRing());
	}

	@Test
	public void itShouldReplaceExteriorRing() throws Exception {
		Polygon polygon = new Polygon(Arrays.asList(
					new LngLatAlt(0, 0), new LngLatAlt(1, 0), new LngLatAlt(1, 1), new LngLatAlt(0, 1), new LngLatAlt(0, 0)));
		polygon.setExteriorRing(MockData.EXTERNAL);
		assertEquals(MockData.EXTERNAL, polygon.getExteriorRing());
		assertEquals(0, polygon.getInteriorRings().size());
	}

	private void assertListEquals(List<LngLatAlt> expectedList, List<LngLatAlt> actualList) {
		for (int x = 0; x < actualList.size(); x++) {
			LngLatAlt expected = expectedList.get(x);
			LngLatAlt actual = actualList.get(x);
			PointTest.assertLngLatAlt(expected.getLongitude(), expected.getLatitude(), expected.getAltitude(), actual);
		}
	}
}
