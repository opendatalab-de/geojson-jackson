package org.geojson.jackson;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.geojson.*;
import org.junit.Test;

import static org.junit.Assert.*;

public class GeometryCollectionTest {

	private ObjectMapper mapper = new ObjectMapper();

	@Test
	public void itShouldSerialize() throws Exception {
		GeometryCollection gc = new GeometryCollection();
		gc.add(PointFactory.create(100, 0));
		gc.add(new LineString(PositionFactory.create(101, 0), PositionFactory.create(102, 1)));
		assertEquals("{\"type\":\"GeometryCollection\","
						+ "\"geometries\":[{\"type\":\"Point\",\"coordinates\":[100.0,0.0]},"
						+ "{\"type\":\"LineString\",\"coordinates\":[[101.0,0.0],[102.0,1.0]]}]}",
				mapper.writeValueAsString(gc));
	}

	@Test
	public void itShouldDeserialize() throws Exception {
		GeometryCollection geometryCollection = mapper
				.readValue("{\"type\":\"GeometryCollection\","
								+ "\"geometries\":[{\"type\":\"Point\",\"coordinates\":[100.0,0.0]},"
								+ "{\"type\":\"LineString\",\"coordinates\":[[101.0,0.0],[102.0,1.0]]}]}",
						GeometryCollection.class);
		assertNotNull(geometryCollection);
	}

	@Test
	public void itShouldDeserializeSubtype() throws Exception {
		FeatureCollection collection = mapper
				.readValue("{\"type\": \"FeatureCollection\","
								+ "  \"features\": ["
								+ "    {"
								+ "      \"type\": \"Feature\","
								+ "      \"geometry\": {"
								+ "        \"type\": \"GeometryCollection\","
								+ "        \"geometries\": ["
								+ "          {"
								+ "            \"type\": \"Point\","
								+ "            \"coordinates\": [100.0, 0.0]"
								+ "          }"
								+ "        ]"
								+ "      }"
								+ "    }"
								+ "  ]"
								+ "}",
						FeatureCollection.class);
		assertNotNull(collection);
		assertTrue(collection.features.get(0).geometry instanceof GeometryCollection);
	}
}
