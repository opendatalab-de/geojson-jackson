package org.geojson;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import static org.junit.Assert.assertArrayEquals ;
import static org.geojson.GeoJsonObjectTest.TOLERANCE ;

/**
 * Exercises {@link Point}.
 * @since issue #45 (zerobandwidth-net issue #1)
 */
@RunWith(JUnit4.class)
public class PointTest
{
	/** Exercises {@link Point#calculateBounds}. */
	@Test
	public void testCalculateBounds()
	{
		Point p = new Point( 5.0d, 10.0d ) ;
		double[] arExpected = { 5.0d, 10.0d, 5.0d, 10.0d } ;
		assertArrayEquals( arExpected, p.calculateBounds(), TOLERANCE ) ;
	}
}
