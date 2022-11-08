package org.geojson;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;


public class NoAutoDetectGettersTest {

	private Feature testObject;
	private ObjectMapper mapper;

	@Before
    public void setUp() {
		mapper = new ObjectMapper();
		mapper.configure(MapperFeature.AUTO_DETECT_GETTERS, false);
		mapper.configure(MapperFeature.AUTO_DETECT_SETTERS, false);

		testObject = new Feature();
		testObject.setGeometry(new Polygon(new LngLatAlt(15, 58)));
    }


	@Test
	public void itShouldSerializePropertiesAndGeometry() throws Exception {
		// Make sure that the serialized object contains properties and 
		// geometry (that are defined using getters/setters), even though
		// auto detect getters & setters are disabled on the ObjectMapper.

		assertEquals("{\"type\":\"Feature\",\"properties\":{},\"geometry\":{\"type\":\"Polygon\",\"coordinates\":[[[15.0,58.0]]]}}",
				mapper.writeValueAsString(testObject));
	}

	@Test
	public void itShouldParsePropertiesAndGeometry() throws Exception {
		Feature feature = mapper.readValue("{\"type\":\"Feature\",\"properties\":{},\"geometry\":{\"type\":\"Polygon\",\"coordinates\":[[[15.0,58.0]]]}}", Feature.class);
		assertEquals(testObject, feature);
	}
}