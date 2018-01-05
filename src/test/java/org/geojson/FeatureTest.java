package org.geojson;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import java.util.HashMap;

import static org.geojson.GeoJsonObjectTest.TOLERANCE;
import static org.junit.Assert.*;

public class FeatureTest {

	private Feature testObject = new Feature();
	private ObjectMapper mapper = new ObjectMapper();

	@Test
	public void itShouldHaveProperties()
	{
		assertNotNull(testObject.getProperties());
	}

	@Test
	public void itShouldSerializeFeature() throws Exception {
		// http://geojson.org/geojson-spec.html#feature-objects
		// A feature object must have a member with the name "properties".
		// The value of the properties member is an object (any JSON object or a JSON null value).
		assertEquals("{\"type\":\"Feature\",\"properties\":{},\"geometry\":null}",
				mapper.writeValueAsString(testObject));
	}

	/**
	 * Exercises {@link Feature#calculateBounds()}.
	 * @since issue #45
	 */
	@Test
	public void testCalculateBounds()
	{
		Feature f = new Feature() ;
		Polygon p = new Polygon(
				new LngLatAlt( -5.0d, -10.0d ),
				new LngLatAlt( -7.0d, -12.0d ),
				new LngLatAlt( -6.0d, 20.0d ),
				new LngLatAlt( 15.0d, 11.0d ),
				new LngLatAlt( 0.0d, -1.0d )
			);
		f.setGeometry(p) ;
		double[] arExpected = { -7.0d, -12.0d, 15.0d, 20.0d } ;
		assertArrayEquals( arExpected, f.getBbox(), TOLERANCE ) ;
		assertArrayEquals( arExpected, f.calculateBounds(), TOLERANCE ) ;
	}

	/**
	 * Exercises accessors/mutators for the feature "properties".
	 * @since issue #45
	 */
	@Test
	public void testProperties()
	{
		Feature f = new Feature() ;
		f.setProperty( "foo", "bar" ) ;
		assertEquals( "bar", f.getProperty("foo") ) ;
		HashMap<String,Object> mapAlt = new HashMap<String,Object>() ;
		mapAlt.put( "flargle", "bargle" ) ;
		mapAlt.put( "dargle", "blarg" ) ;
		f.setProperties(mapAlt) ;
		assertNull( f.getProperty("foo") ) ;
		assertEquals( "bargle", f.getProperty("flargle") ) ;
		assertEquals( "blarg", f.getProperty("dargle") ) ;
		f.setProperties(null) ;
		assertNotNull( f.getProperties() ) ;
		assertTrue( f.getProperties().isEmpty() ) ;
	}
}