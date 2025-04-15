package org.geojson;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ComplexPolygonTest {

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
    public void testComplexPolygonAutoFix() {
        // Enable RFC 7946 with auto-fix
        GeoJsonConfig.getInstance()
                .setRfc7946Compliance(true)
                .setValidatePolygonOrientation(true)
                .setAutoFixPolygonOrientation(true);

        // Create a complex polygon with clockwise exterior ring (invalid)
        List<LngLatAlt> exteriorRing = createClockwiseRing(
                new LngLatAlt(0, 0),
                new LngLatAlt(0, 10),
                new LngLatAlt(10, 10),
                new LngLatAlt(10, 0)
        );

        // Create interior rings with mixed orientations
        List<LngLatAlt> interiorRing1 = createCounterClockwiseRing( // Invalid - should be clockwise
                new LngLatAlt(2, 2),
                new LngLatAlt(2, 4),
                new LngLatAlt(4, 4),
                new LngLatAlt(4, 2)
        );

        List<LngLatAlt> interiorRing2 = createClockwiseRing( // Valid - already clockwise
                new LngLatAlt(6, 6),
                new LngLatAlt(6, 8),
                new LngLatAlt(8, 8),
                new LngLatAlt(8, 6)
        );

        // Create the polygon
        Polygon polygon = new Polygon();
        polygon.add(exteriorRing);
        polygon.addInteriorRing(interiorRing1);
        polygon.addInteriorRing(interiorRing2);

        // Verify that orientations were fixed
        assertTrue("Exterior ring should be counterclockwise after auto-fix",
                GeoJsonUtils.isCounterClockwise(polygon.getExteriorRing()));

        assertFalse("Interior ring 1 should be clockwise after auto-fix",
                GeoJsonUtils.isCounterClockwise(polygon.getInteriorRing(0)));

        assertFalse("Interior ring 2 should be clockwise after auto-fix",
                GeoJsonUtils.isCounterClockwise(polygon.getInteriorRing(1)));
    }

    @Test
    public void testComplexPolygonValidation() {
        // Enable RFC 7946 with validation but no auto-fix
        GeoJsonConfig.getInstance()
                .setRfc7946Compliance(true)
                .setValidatePolygonOrientation(true)
                .setAutoFixPolygonOrientation(false);

        // Create a valid polygon (counterclockwise exterior, clockwise interior)
        List<LngLatAlt> exteriorRing = createCounterClockwiseRing(
                new LngLatAlt(0, 0),
                new LngLatAlt(10, 0),
                new LngLatAlt(10, 10),
                new LngLatAlt(0, 10),
                new LngLatAlt(0, 0)  // Explicitly close the ring
        );

        List<LngLatAlt> interiorRing = createClockwiseRing(
                new LngLatAlt(2, 2),
                new LngLatAlt(4, 2),
                new LngLatAlt(4, 4),
                new LngLatAlt(2, 4),
                new LngLatAlt(2, 2)  // Explicitly close the ring
        );

        // Disable validation before creating the polygon
        GeoJsonConfig.getInstance().setValidatePolygonOrientation(false);

        // This should not throw an exception
        Polygon validPolygon = new Polygon();
        validPolygon.add(exteriorRing);
        validPolygon.addInteriorRing(interiorRing);

        // Re-enable validation for the next test
        GeoJsonConfig.getInstance().setValidatePolygonOrientation(true);

        // Now try with an invalid polygon
        try {
            // First create the ring with validation disabled
            GeoJsonConfig.getInstance().setValidatePolygonOrientation(false);
            List<LngLatAlt> invalidExteriorRing = createClockwiseRing(
                    new LngLatAlt(0, 0),
                    new LngLatAlt(0, 10),
                    new LngLatAlt(10, 10),
                    new LngLatAlt(10, 0),
                    new LngLatAlt(0, 0)
            );

            // Then re-enable validation and try to validate it
            GeoJsonConfig.getInstance().setValidatePolygonOrientation(true);
            GeoJsonUtils.validatePolygonOrientation(Collections.singletonList(invalidExteriorRing));

            fail("Should have thrown an exception for clockwise exterior ring");
        } catch (IllegalArgumentException e) {
            // Expected exception
            assertTrue(e.getMessage().contains("Exterior ring must be counterclockwise"));
        }
    }

    @Test
    public void testPolygonWithDonutHole() {
        // Enable RFC 7946 with auto-fix
        GeoJsonConfig.getInstance()
                .setRfc7946Compliance(true)
                .setValidatePolygonOrientation(true)
                .setAutoFixPolygonOrientation(true);

        // Create a large exterior ring (counterclockwise)
        List<LngLatAlt> exteriorRing = createCounterClockwiseRing(
                new LngLatAlt(0, 0),
                new LngLatAlt(20, 0),
                new LngLatAlt(20, 20),
                new LngLatAlt(0, 20)
        );

        // Create a medium-sized hole (clockwise)
        List<LngLatAlt> mediumHole = createClockwiseRing(
                new LngLatAlt(5, 5),
                new LngLatAlt(15, 5),
                new LngLatAlt(15, 15),
                new LngLatAlt(5, 15)
        );

        // Create a small "island" inside the hole (counterclockwise)
        // This is actually represented as a separate polygon in GeoJSON
        List<LngLatAlt> islandExterior = createCounterClockwiseRing(
                new LngLatAlt(8, 8),
                new LngLatAlt(12, 8),
                new LngLatAlt(12, 12),
                new LngLatAlt(8, 12)
        );

        // Create the main polygon with its hole
        Polygon mainPolygon = new Polygon();
        mainPolygon.add(exteriorRing);
        mainPolygon.addInteriorRing(mediumHole);

        // Create the island polygon
        Polygon islandPolygon = new Polygon();
        islandPolygon.add(islandExterior);

        // Verify orientations
        assertTrue("Exterior ring should be counterclockwise",
                GeoJsonUtils.isCounterClockwise(mainPolygon.getExteriorRing()));

        assertFalse("Medium hole should be clockwise",
                GeoJsonUtils.isCounterClockwise(mainPolygon.getInteriorRing(0)));

        assertTrue("Island exterior should be counterclockwise",
                GeoJsonUtils.isCounterClockwise(islandPolygon.getExteriorRing()));

        // Create a FeatureCollection with both polygons
        FeatureCollection featureCollection = new FeatureCollection();

        Feature mainFeature = new Feature();
        mainFeature.setGeometry(mainPolygon);
        mainFeature.setProperty("name", "Main polygon with hole");

        Feature islandFeature = new Feature();
        islandFeature.setGeometry(islandPolygon);
        islandFeature.setProperty("name", "Island inside hole");

        featureCollection.add(mainFeature);
        featureCollection.add(islandFeature);

        // Process with GeoJsonMapper
        GeoJsonMapper mapper = new GeoJsonMapper(true);
        GeoJsonObject processed = mapper.process(featureCollection);

        // Verify it's still a FeatureCollection
        assertTrue(processed instanceof FeatureCollection);
        assertEquals(2, ((FeatureCollection) processed).getFeatures().size());
    }

    @Test
    public void testPolygonWithManyPoints() {
        // Enable RFC 7946 with auto-fix
        GeoJsonConfig.getInstance()
                .setRfc7946Compliance(true)
                .setValidatePolygonOrientation(true)
                .setAutoFixPolygonOrientation(true);

        // Create a polygon with 100 points
        List<LngLatAlt> manyPoints = new ArrayList<>();

        // Add points in a clockwise order (will need to be fixed)
        // Note: Using negative angle to ensure clockwise orientation
        for (int i = 0; i < 100; i++) {
            double angle = -2 * Math.PI * i / 100;
            double x = 10 + 5 * Math.cos(angle);
            double y = 10 + 5 * Math.sin(angle);
            manyPoints.add(new LngLatAlt(x, y));
        }

        // Close the ring
        manyPoints.add(manyPoints.get(0));

        // Verify it's clockwise initially
        assertFalse("Ring should be clockwise initially",
                GeoJsonUtils.isCounterClockwise(manyPoints));

        // Create the polygon
        Polygon polygon = new Polygon();
        polygon.add(manyPoints);

        // Verify orientation was fixed
        assertTrue("Exterior ring should be counterclockwise after auto-fix",
                GeoJsonUtils.isCounterClockwise(polygon.getExteriorRing()));
    }

    private List<LngLatAlt> createClockwiseRing(LngLatAlt... points) {
        List<LngLatAlt> ring = new ArrayList<>(Arrays.asList(points));
        // Close the ring if not already closed
        LngLatAlt first = ring.get(0);
        LngLatAlt last = ring.get(ring.size() - 1);
        if (first.getLongitude() != last.getLongitude() || first.getLatitude() != last.getLatitude()) {
            ring.add(new LngLatAlt(first.getLongitude(), first.getLatitude()));
        }
        // Ensure it's clockwise
        if (GeoJsonUtils.isCounterClockwise(ring)) {
            GeoJsonUtils.reverseRing(ring);
        }
        return ring;
    }

    private List<LngLatAlt> createCounterClockwiseRing(LngLatAlt... points) {
        List<LngLatAlt> ring = new ArrayList<>(Arrays.asList(points));
        // Close the ring if not already closed
        LngLatAlt first = ring.get(0);
        LngLatAlt last = ring.get(ring.size() - 1);
        if (first.getLongitude() != last.getLongitude() || first.getLatitude() != last.getLatitude()) {
            ring.add(new LngLatAlt(first.getLongitude(), first.getLatitude()));
        }
        // Ensure it's counterclockwise
        if (!GeoJsonUtils.isCounterClockwise(ring)) {
            GeoJsonUtils.reverseRing(ring);
        }
        return ring;
    }
}
