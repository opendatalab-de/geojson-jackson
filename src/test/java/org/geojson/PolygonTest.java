package org.geojson;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertArrayEquals ;

import static org.geojson.GeoJsonObjectTest.TOLERANCE ;
import static org.junit.Assert.assertEquals;

/**
 * Exercises {@link Polygon}.
 * @since issue #45
 */
@RunWith(JUnit4.class)
public class PolygonTest
{
	/** Exercises {@link Polygon#calculateBounds}. */
	@Test
	public void testCalculateBounds()
	{
		Polygon p = new Polygon(
				new LngLatAlt( -5.0d, -10.0d ),
				new LngLatAlt( -7.0d, -12.0d ),
				new LngLatAlt( -6.0d, 20.0d ),
				new LngLatAlt( 15.0d, 11.0d ),
				new LngLatAlt( 0.0d, -1.0d )
			);
		double[] arExpected = { -7.0d, -12.0d, 15.0d, 20.0d } ;
		assertArrayEquals( arExpected, p.getBbox(), TOLERANCE ) ;
		assertArrayEquals( arExpected, p.calculateBounds(), TOLERANCE ) ;
	}

	/** Exercises methods that deal with exterior/interior ring management. */
	@Test
	public void testRingManagement()
	{
		Polygon p = new Polygon( // tiny triangle
				new LngLatAlt( -0.1d, -0.1d ),
				new LngLatAlt( 0.0d, 0.1d ),
				new LngLatAlt( 0.1d, -0.1d )
			);
		List<LngLatAlt> newExterior = new ArrayList<LngLatAlt>() ; // bigger
		newExterior.add( new LngLatAlt( -1.0d, -1.0d ) ) ;
		newExterior.add( new LngLatAlt( 0.0d, 1.0d ) ) ;
		newExterior.add( new LngLatAlt( 1.0d, -1.0d ) ) ;
		p.setExteriorRing( newExterior ) ;
		assertEquals( 2, p.getCoordinates().size() ) ;
		List<LngLatAlt> poly = p.coordinates.get(0) ; // directly get the member
		assertEquals( newExterior, poly ) ;

		p.addInteriorRing( // triangle in size between the other two
				new LngLatAlt( -0.2d, -0.2d ),
				new LngLatAlt( 0.0d, 0.2d ),
				new LngLatAlt( 0.2d, -0.2d )
			);
		assertEquals( 3, p.getCoordinates().size() ) ;
		poly = p.coordinates.get(0) ;
		assertEquals( newExterior, poly ) ; // big triangle is still the exterior

	}

}
