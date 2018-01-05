package org.geojson;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.ArrayList;
import java.util.List;

import static org.geojson.GeoJsonObjectTest.TOLERANCE;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;

/**
 * Exercises {@link FeatureCollection}.
 * @since issue #45
 */
@RunWith(JUnit4.class)
public class FeatureCollectionTest
{
	/**
	 * Exercises {@link FeatureCollection#calculateBounds()}, and also verifies
	 * that the setter methods will update the bounds automatically.
	 */
	@Test
	public void testCalculateBounds()
	{
		FeatureCollection fc = new FeatureCollection() ;
		Feature f1 = new Feature() ;
		Polygon p1 = new Polygon(
				new LngLatAlt( -1.0d, -1.0d ),
				new LngLatAlt( 0.0d, 2.0d ),
				new LngLatAlt( 1.0d, -2.0d )
			);
		f1.setGeometry(p1) ;
		Feature f2 = new Feature() ;
		Polygon p2 = new Polygon(
				new LngLatAlt( -2.0d, 0.0d ),
				new LngLatAlt( 0.0d, 3.0d ),
				new LngLatAlt( 0.5d, 0.5d )
			);
		f2.setGeometry(p2) ;
		List<Feature> aFeatures = new ArrayList<Feature>() ;
		aFeatures.add(f1) ;
		aFeatures.add(f2) ;
		fc.setFeatures( aFeatures ) ;
		double[] arExpected = { -2.0d, -2.0d, 1.0d, 3.0d } ;
		assertArrayEquals( arExpected, fc.getBbox(), TOLERANCE ) ;
		assertArrayEquals( arExpected, fc.calculateBounds(), TOLERANCE );
		Feature f3 = new Feature() ;
		Polygon p3 = new Polygon(
				new LngLatAlt( -0.5d, 3.5d ),
				new LngLatAlt( 0.1d, -2.0d ),
				new LngLatAlt( 4.0d, 2.0d )
			);
		f3.setGeometry(p3) ;
		fc.add(f3) ;
		double[] arExpected3 = { -2.0d, -2.0d, 4.0d, 3.5d } ;
		assertArrayEquals( arExpected3, fc.getBbox(), TOLERANCE ) ;
		fc.setFeatures(null) ;
		assertNull( fc.getBbox() ) ;
		aFeatures.clear() ;
		aFeatures.add(f1) ; aFeatures.add(f2) ; aFeatures.add(f3) ;
		fc.addAll(aFeatures) ;
		assertArrayEquals( arExpected3, fc.getBbox(), TOLERANCE ) ;
	}
}
