package org.geojson.jackson;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import org.geojson.LngLatAlt;
import org.geojson.MultiLineString;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;

public class MultiLineStringTest {

	private ObjectMapper mapper = new ObjectMapper();

	@Test
	public void itShouldSerialize() throws Exception {
		MultiLineString multiLineString = new MultiLineString();
		multiLineString.add(Arrays.asList(new LngLatAlt(100, 0), new LngLatAlt(101, 1)));
		multiLineString.add(Arrays.asList(new LngLatAlt(102, 2), new LngLatAlt(103, 3)));
		assertEquals("{\"type\":\"MultiLineString\",\"coordinates\":"
				+ "[[[100.0,0.0],[101.0,1.0]],[[102.0,2.0],[103.0,3.0]]]}", mapper.writeValueAsString(multiLineString));
	}

	@Test
	public void itShouldSerializeIgnoringWriteSingleElemArraysUnwrapped() throws Exception {
		ObjectMapper mapper = new ObjectMapper()
			.enable(SerializationFeature.WRITE_SINGLE_ELEM_ARRAYS_UNWRAPPED);

		MultiLineString multiLineString = new MultiLineString();
		multiLineString.add(Arrays.asList(new LngLatAlt(100, 0), new LngLatAlt(101, 1)));
		assertEquals("{\"type\":\"MultiLineString\",\"coordinates\":"
				+ "[[[100.0,0.0],[101.0,1.0]]]}", mapper.writeValueAsString(multiLineString));
	}
}
