package org.geojson.jackson;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.geojson.MultiLineString;
import org.junit.Test;

import java.util.Arrays;

import static org.geojson.PositionFactory.create;
import static org.junit.Assert.assertEquals;

public class MultiLineStringTest {

	private ObjectMapper mapper = new ObjectMapper();

	@Test
	public void itShouldSerialize() throws Exception {
		MultiLineString multiLineString = new MultiLineString();
		multiLineString.add(Arrays.asList(create(100, 0), create(101, 1)));
		multiLineString.add(Arrays.asList(create(102, 2), create(103, 3)));
		assertEquals("{\"type\":\"MultiLineString\",\"coordinates\":"
				+ "[[[100.0,0.0],[101.0,1.0]],[[102.0,2.0],[103.0,3.0]]]}", mapper.writeValueAsString(multiLineString));
	}
}
