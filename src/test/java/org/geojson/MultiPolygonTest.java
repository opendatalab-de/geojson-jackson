package org.geojson;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.geojson.GeoJsonObjectTest.TOLERANCE;
import static org.junit.Assert.assertArrayEquals;

/**
 * Exercises {@link MultiPolygon}.
 * @since issue #45 (zerobandwidth-net issue #1)
 */
@RunWith(JUnit4.class)
public class MultiPolygonTest
{
	/** Exercises {@link MultiPolygon#calculateBounds} */
	@Test
	public void testCalculateBounds()
	{
		MultiPolygon mp = new MultiPolygon() ;
		mp.add( new Polygon(
				new LngLatAlt( -1.0d, -2.0d ), new LngLatAlt( -3.0d, 4.0d ),
				new LngLatAlt( 5.0d, 6.0d ), new LngLatAlt( 7.0d, -8.0d )
			));
		mp.add( new Polygon(
				new LngLatAlt( 0.0d, 0.0d ), new LngLatAlt( -4.0d, 8.0d ),
				new LngLatAlt( 10.0d, 5.0d ), new LngLatAlt( 6.0d, -11.0d )
			));
		double[] arExpected = { -4.0d, -11.0d, 10.0d, 8.0d } ;
		assertArrayEquals( arExpected, mp.calculateBounds(), TOLERANCE ) ;
	}
}
