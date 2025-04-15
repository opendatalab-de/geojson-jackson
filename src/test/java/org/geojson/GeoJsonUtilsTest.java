package org.geojson;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class GeoJsonUtilsTest {

    @Test
    public void testIsCounterClockwise() {
        // Create a counterclockwise ring (0,0) -> (1,0) -> (1,1) -> (0,1) -> (0,0)
        List<LngLatAlt> ccwRing = Arrays.asList(
                new LngLatAlt(0, 0),
                new LngLatAlt(1, 0),
                new LngLatAlt(1, 1),
                new LngLatAlt(0, 1),
                new LngLatAlt(0, 0)
        );

        // Create a clockwise ring (0,0) -> (0,1) -> (1,1) -> (1,0) -> (0,0)
        List<LngLatAlt> cwRing = Arrays.asList(
                new LngLatAlt(0, 0),
                new LngLatAlt(0, 1),
                new LngLatAlt(1, 1),
                new LngLatAlt(1, 0),
                new LngLatAlt(0, 0)
        );

        // Test the counterclockwise ring
        assertTrue("Ring should be counterclockwise", GeoJsonUtils.isCounterClockwise(ccwRing));

        // Test the clockwise ring
        assertFalse("Ring should be clockwise", GeoJsonUtils.isCounterClockwise(cwRing));
    }
}
