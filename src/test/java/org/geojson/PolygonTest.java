package org.geojson;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import static org.junit.Assert.assertArrayEquals ;

import static org.geojson.GeoJsonObjectTest.TOLERANCE ;

/**
 * Exercises {@link Polygon}.
 * @since issue #45 (zerobandwidth-net issue #1)
 */
@RunWith(JUnit4.class)
public class PolygonTest
{
	/** Exercises {@link GeoJsonObject#calculateBounds}. */
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
		assertArrayEquals( arExpected, p.calculateBounds(), TOLERANCE ) ;
	}
}
