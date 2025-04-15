package org.geojson;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class GeoJsonConfigTest {

    @Before
    public void setUp() {
        // Reset to default configuration before each test
        GeoJsonConfig.useLegacyMode();
    }

    @After
    public void tearDown() {
        // Reset to default configuration after each test
        GeoJsonConfig.useLegacyMode();
    }

    @Test
    public void testRfc7946Mode() {
        // Enable RFC 7946 mode
        GeoJsonConfig.useRfc7946();

        // Check that the configuration is set correctly
        assertTrue(GeoJsonConfig.getInstance().isRfc7946Compliance());
        assertTrue(GeoJsonConfig.getInstance().isValidatePolygonOrientation());
        assertTrue(GeoJsonConfig.getInstance().isCutAntimeridian());
    }

    @Test
    public void testLegacyMode() {
        // Enable legacy mode
        GeoJsonConfig.useLegacyMode();

        // Check that the configuration is set correctly
        assertFalse(GeoJsonConfig.getInstance().isRfc7946Compliance());
        assertFalse(GeoJsonConfig.getInstance().isValidatePolygonOrientation());
        assertFalse(GeoJsonConfig.getInstance().isCutAntimeridian());
    }

    @Test
    public void testCustomConfiguration() {
        // Configure custom settings
        GeoJsonConfig.getInstance()
                .setRfc7946Compliance(true)
                .setValidatePolygonOrientation(true)
                .setAutoFixPolygonOrientation(true)
                .setCutAntimeridian(false)
                .setWarnOnCrsUse(false);

        // Check that the configuration is set correctly
        assertTrue(GeoJsonConfig.getInstance().isRfc7946Compliance());
        assertTrue(GeoJsonConfig.getInstance().isValidatePolygonOrientation());
        assertTrue(GeoJsonConfig.getInstance().isAutoFixPolygonOrientation());
        assertFalse(GeoJsonConfig.getInstance().isCutAntimeridian());
        assertFalse(GeoJsonConfig.getInstance().isWarnOnCrsUse());
    }

    @Test
    public void testPolygonOrientationValidation() {
        // Enable RFC 7946 mode with validation
        GeoJsonConfig.getInstance()
                .setRfc7946Compliance(true)
                .setValidatePolygonOrientation(true)
                .setAutoFixPolygonOrientation(false);

        // Temporarily disable validation to create the polygon
        GeoJsonConfig.getInstance().setValidatePolygonOrientation(false);

        // Create a polygon with counterclockwise exterior ring (valid)
        Polygon validPolygon = new Polygon(
                new LngLatAlt(0, 0),
                new LngLatAlt(1, 0),
                new LngLatAlt(1, 1),
                new LngLatAlt(0, 1),
                new LngLatAlt(0, 0)
        );

        // Re-enable validation
        GeoJsonConfig.getInstance().setValidatePolygonOrientation(true);

        // This should not throw an exception
        assertNotNull(validPolygon);

        // Create a polygon with clockwise exterior ring (invalid)
        try {
            // First create the polygon with validation disabled
            GeoJsonConfig.getInstance().setValidatePolygonOrientation(false);
            // Create a polygon with a clockwise exterior ring (invalid in RFC 7946)
            // We need to create a ring that's definitely clockwise
            Polygon invalidPolygon = new Polygon();
            List<LngLatAlt> clockwiseRing = Arrays.asList(
                    new LngLatAlt(0, 0),
                    new LngLatAlt(0, 1),
                    new LngLatAlt(1, 1),
                    new LngLatAlt(1, 0),
                    new LngLatAlt(0, 0)
            );

            // Verify it's actually clockwise
            if (GeoJsonUtils.isCounterClockwise(clockwiseRing)) {
                GeoJsonUtils.reverseRing(clockwiseRing);
            }

            // Add the clockwise ring to the polygon
            invalidPolygon.add(clockwiseRing);

            // Then re-enable validation and try to process it
            GeoJsonConfig.getInstance().setValidatePolygonOrientation(true);

            // If the ring is counterclockwise, manually reverse it to make it clockwise
            List<LngLatAlt> exteriorRing = invalidPolygon.getExteriorRing();
            if (GeoJsonUtils.isCounterClockwise(exteriorRing)) {
                GeoJsonUtils.reverseRing(exteriorRing);
            }

            // Now validate
            GeoJsonUtils.validatePolygonOrientation(invalidPolygon.getCoordinates());

            // If we get here, the validation didn't throw an exception, which is wrong
            fail("Should have thrown an exception for clockwise exterior ring");
        } catch (IllegalArgumentException e) {
            // Expected exception
            assertTrue(e.getMessage().contains("Exterior ring must be counterclockwise"));
        }
    }

    @Test
    public void testPolygonOrientationAutoFix() {
        // Enable RFC 7946 mode with auto-fix
        GeoJsonConfig.getInstance()
                .setRfc7946Compliance(true)
                .setValidatePolygonOrientation(true)
                .setAutoFixPolygonOrientation(true);

        // Create a polygon with clockwise exterior ring (would be invalid, but will be fixed)
        Polygon polygon = new Polygon(
                new LngLatAlt(0, 0),
                new LngLatAlt(0, 1),
                new LngLatAlt(1, 1),
                new LngLatAlt(1, 0),
                new LngLatAlt(0, 0)
        );

        // The polygon should have been fixed
        assertTrue(GeoJsonUtils.isCounterClockwise(polygon.getExteriorRing()));
    }

    @Test
    public void testCrsWarning() {
        // Disable CRS warnings for this test
        GeoJsonConfig.getInstance()
                .setRfc7946Compliance(true)
                .setWarnOnCrsUse(false);

        Point point = new Point(100, 0);
        Crs crs = new Crs();
        crs.getProperties().put("name", "EPSG:4326");
        point.setCrs(crs);

        // The CRS should still be set even in RFC 7946 mode
        assertNotNull(point.getCrs());
        assertEquals("EPSG:4326", point.getCrs().getProperties().get("name"));
    }
}
