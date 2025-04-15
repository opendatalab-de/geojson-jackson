package org.geojson;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class AntimeridianCuttingTest {

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
    public void testLineStringCutting() {
        GeoJsonConfig.getInstance()
                .setRfc7946Compliance(true)
                .setCutAntimeridian(true);

        LineString lineString = new LineString(
                new LngLatAlt(170, 45),
                new LngLatAlt(-170, 45)
        );

        GeoJsonObject processed = GeoJsonUtils.process(lineString);

        assertTrue("Should be a MultiLineString after cutting", processed instanceof MultiLineString);
        MultiLineString multiLineString = (MultiLineString) processed;
        assertEquals("Should have 2 segments", 2, multiLineString.getCoordinates().size());

        List<LngLatAlt> segment1 = multiLineString.getCoordinates().get(0);
        List<LngLatAlt> segment2 = multiLineString.getCoordinates().get(1);

        assertEquals("First segment should end at longitude 180", 180.0, segment1.get(segment1.size() - 1).getLongitude(), 0.001);

        assertEquals("Second segment should start at longitude -180", -180.0, segment2.get(0).getLongitude(), 0.001);
    }

    @Test
    public void testMultipleCrossings() {
        GeoJsonConfig.getInstance()
                .setRfc7946Compliance(true)
                .setCutAntimeridian(true);

        LineString lineString = new LineString(
                new LngLatAlt(170, 45),  // Start in eastern hemisphere
                new LngLatAlt(-170, 45), // Cross to western hemisphere
                new LngLatAlt(-160, 40), // Stay in western
                new LngLatAlt(160, 40)   // Cross back to eastern
        );

        GeoJsonObject processed = GeoJsonUtils.process(lineString);

        assertTrue("Should be a MultiLineString after cutting", processed instanceof MultiLineString);
        MultiLineString multiLineString = (MultiLineString) processed;

        assertTrue("Should have at least 2 segments", multiLineString.getCoordinates().size() >= 2);
    }

    @Test
    public void testPolygonCutting() {
        GeoJsonConfig.getInstance()
                .setRfc7946Compliance(true)
                .setCutAntimeridian(true);

        List<LngLatAlt> ring = Arrays.asList(
                new LngLatAlt(170, 40),
                new LngLatAlt(170, 50),
                new LngLatAlt(-170, 50),
                new LngLatAlt(-170, 40),
                new LngLatAlt(170, 40)  // Close the ring
        );

        Polygon polygon = new Polygon();
        polygon.add(ring);

        GeoJsonObject processed = GeoJsonUtils.process(polygon);

        assertTrue("Should be a MultiPolygon after cutting", processed instanceof MultiPolygon);
        MultiPolygon multiPolygon = (MultiPolygon) processed;

        assertTrue("Should have at least 1 polygon", multiPolygon.getCoordinates().size() >= 1);
    }

    @Test
    public void testFeatureCutting() {
        GeoJsonConfig.getInstance()
                .setRfc7946Compliance(true)
                .setCutAntimeridian(true);

        LineString lineString = new LineString(
                new LngLatAlt(170, 45),
                new LngLatAlt(-170, 45)
        );

        Feature feature = new Feature();
        feature.setGeometry(lineString);
        feature.setProperty("name", "International Date Line Crossing");
        feature.setProperty("length_km", 222.6);

        GeoJsonObject processed = GeoJsonUtils.process(feature);

        assertTrue("Should still be a Feature", processed instanceof Feature);
        Feature processedFeature = (Feature) processed;

        assertTrue("Geometry should be a MultiLineString after cutting",
                processedFeature.getGeometry() instanceof MultiLineString);

        assertEquals("Name property should be preserved",
                "International Date Line Crossing", processedFeature.getProperty("name"));
        assertEquals("Length property should be preserved",
                222.6, processedFeature.getProperty("length_km"), 0.1);
    }

    @Test
    public void testFeatureCollectionCutting() {
        GeoJsonConfig.getInstance()
                .setRfc7946Compliance(true)
                .setCutAntimeridian(true);

        LineString lineString = new LineString(
                new LngLatAlt(170, 45),
                new LngLatAlt(-170, 45)
        );

        List<LngLatAlt> ring = Arrays.asList(
                new LngLatAlt(170, 40),
                new LngLatAlt(170, 50),
                new LngLatAlt(-170, 50),
                new LngLatAlt(-170, 40),
                new LngLatAlt(170, 40)  // Close the ring
        );
        Polygon polygon = new Polygon();
        polygon.add(ring);

        Point point = new Point(175, 45);

        Feature lineFeature = new Feature();
        lineFeature.setGeometry(lineString);
        lineFeature.setProperty("type", "line");

        Feature polygonFeature = new Feature();
        polygonFeature.setGeometry(polygon);
        polygonFeature.setProperty("type", "polygon");

        Feature pointFeature = new Feature();
        pointFeature.setGeometry(point);
        pointFeature.setProperty("type", "point");

        FeatureCollection featureCollection = new FeatureCollection();
        featureCollection.add(lineFeature);
        featureCollection.add(polygonFeature);
        featureCollection.add(pointFeature);

        GeoJsonObject processed = GeoJsonUtils.process(featureCollection);

        assertTrue("Should still be a FeatureCollection", processed instanceof FeatureCollection);
        FeatureCollection processedCollection = (FeatureCollection) processed;

        assertEquals("Should still have 3 features", 3, processedCollection.getFeatures().size());

        int multiLineStrings = 0;
        int multiPolygons = 0;
        int points = 0;

        for (Feature feature : processedCollection) {
            if (feature.getGeometry() instanceof MultiLineString) {
                multiLineStrings++;
                assertEquals("line", feature.getProperty("type"));
            } else if (feature.getGeometry() instanceof MultiPolygon) {
                multiPolygons++;
                assertEquals("polygon", feature.getProperty("type"));
            } else if (feature.getGeometry() instanceof Point) {
                points++;
                assertEquals("point", feature.getProperty("type"));
            }
        }

        assertEquals("Should have 1 MultiLineString", 1, multiLineStrings);
        assertEquals("Should have 1 MultiPolygon", 1, multiPolygons);
        assertEquals("Should have 1 Point", 1, points);
    }

    @Test
    public void testGeometryCollectionCutting() {
        // Enable RFC 7946 with antimeridian cutting
        GeoJsonConfig.getInstance()
                .setRfc7946Compliance(true)
                .setCutAntimeridian(true);

        LineString lineString = new LineString(
                new LngLatAlt(170, 45),
                new LngLatAlt(-170, 45)
        );

        List<LngLatAlt> ring = Arrays.asList(
                new LngLatAlt(170, 40),
                new LngLatAlt(170, 50),
                new LngLatAlt(-170, 50),
                new LngLatAlt(-170, 40),
                new LngLatAlt(170, 40)  // Close the ring
        );
        Polygon polygon = new Polygon();
        polygon.add(ring);

        Point point = new Point(175, 45);

        GeometryCollection geometryCollection = new GeometryCollection();
        geometryCollection.add(lineString);
        geometryCollection.add(polygon);
        geometryCollection.add(point);

        GeoJsonObject processed = GeoJsonUtils.process(geometryCollection);

        assertTrue("Should still be a GeometryCollection", processed instanceof GeometryCollection);
        GeometryCollection processedCollection = (GeometryCollection) processed;

        assertEquals("Should still have 3 geometries", 3, processedCollection.getGeometries().size());

        int multiLineStrings = 0;
        int multiPolygons = 0;
        int points = 0;

        for (GeoJsonObject geometry : processedCollection) {
            if (geometry instanceof MultiLineString) {
                multiLineStrings++;
            } else if (geometry instanceof MultiPolygon) {
                multiPolygons++;
            } else if (geometry instanceof Point) {
                points++;
            }
        }

        assertEquals("Should have 1 MultiLineString", 1, multiLineStrings);
        assertEquals("Should have 1 MultiPolygon", 1, multiPolygons);
        assertEquals("Should have 1 Point", 1, points);
    }
}
