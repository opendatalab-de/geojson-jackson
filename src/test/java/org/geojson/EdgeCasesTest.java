package org.geojson;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.util.Arrays;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class EdgeCasesTest {

    private GeoJsonMapper mapper;

    @Before
    public void setUp() {
        // Reset to default configuration before each test
        GeoJsonConfig.useLegacyMode();
        mapper = new GeoJsonMapper(true);
    }

    @After
    public void tearDown() {
        // Reset to default configuration after each test
        GeoJsonConfig.useLegacyMode();
    }

    @Test
    public void testEmptyGeometries() {
        LineString emptyLineString = new LineString();

        Polygon emptyPolygon = new Polygon();

        MultiPoint emptyMultiPoint = new MultiPoint();

        MultiLineString emptyMultiLineString = new MultiLineString();

        MultiPolygon emptyMultiPolygon = new MultiPolygon();

        GeometryCollection emptyGeometryCollection = new GeometryCollection();

        GeoJsonObject processedLineString = GeoJsonUtils.process(emptyLineString);
        GeoJsonObject processedPolygon = GeoJsonUtils.process(emptyPolygon);
        GeoJsonObject processedMultiPoint = GeoJsonUtils.process(emptyMultiPoint);
        GeoJsonObject processedMultiLineString = GeoJsonUtils.process(emptyMultiLineString);
        GeoJsonObject processedMultiPolygon = GeoJsonUtils.process(emptyMultiPolygon);
        GeoJsonObject processedGeometryCollection = GeoJsonUtils.process(emptyGeometryCollection);

        assertTrue(processedLineString instanceof LineString);
        assertTrue(processedPolygon instanceof Polygon);
        assertTrue(processedMultiPoint instanceof MultiPoint);
        assertTrue(processedMultiLineString instanceof MultiLineString);
        assertTrue(processedMultiPolygon instanceof MultiPolygon);
        assertTrue(processedGeometryCollection instanceof GeometryCollection);

        assertTrue(((LineString) processedLineString).getCoordinates().isEmpty());
        assertTrue(((Polygon) processedPolygon).getCoordinates().isEmpty());
        assertTrue(((MultiPoint) processedMultiPoint).getCoordinates().isEmpty());
        assertTrue(((MultiLineString) processedMultiLineString).getCoordinates().isEmpty());
        assertTrue(((MultiPolygon) processedMultiPolygon).getCoordinates().isEmpty());
        assertTrue(((GeometryCollection) processedGeometryCollection).getGeometries().isEmpty());
    }

    @Test
    public void testInvalidPolygonRings() {
        GeoJsonConfig.getInstance()
                .setRfc7946Compliance(true)
                .setValidatePolygonOrientation(true)
                .setAutoFixPolygonOrientation(false);

        try {
            Polygon invalidPolygon = new Polygon();
            invalidPolygon.add(Arrays.asList(
                    new LngLatAlt(0, 0),
                    new LngLatAlt(1, 1),
                    new LngLatAlt(0, 0)  // Only 3 points including closure
            ));
            fail("Should have thrown an exception for a ring with fewer than 4 points");
        } catch (Exception e) {
            // Expected exception
        }

        try {
            Polygon unclosedPolygon = new Polygon();
            unclosedPolygon.add(Arrays.asList(
                    new LngLatAlt(0, 0),
                    new LngLatAlt(1, 0),
                    new LngLatAlt(1, 1),
                    new LngLatAlt(0, 1)
                    // Missing closure point
            ));
            fail("Should have thrown an exception for an unclosed ring");
        } catch (Exception e) {
            // Expected exception
        }
    }

    @Test
    public void testExtremeCoordinates() throws IOException {
        Point extremeLongitude = new Point(180.0, 0.0);
        String json = mapper.writeValueAsString(extremeLongitude);
        Point deserializedPoint = mapper.readValue(json, Point.class);
        assertEquals(180.0, deserializedPoint.getCoordinates().getLongitude(), 0.001);

        Point extremeLatitude = new Point(0.0, 90.0);
        json = mapper.writeValueAsString(extremeLatitude);
        deserializedPoint = mapper.readValue(json, Point.class);
        assertEquals(90.0, deserializedPoint.getCoordinates().getLatitude(), 0.001);

        Point extremeAltitude = new Point(0.0, 0.0, 9999.9);
        json = mapper.writeValueAsString(extremeAltitude);
        deserializedPoint = mapper.readValue(json, Point.class);
        assertEquals(9999.9, deserializedPoint.getCoordinates().getAltitude(), 0.001);
    }

    @Test
    public void testInvalidJson() {
        try {
            mapper.readValue("{\"type\":\"Point\",\"coordinates\":[10,}", Point.class);
            fail("Should have thrown an exception for invalid JSON");
        } catch (IOException e) {
            // Expected exception
        }

        try {
            mapper.readValue("{\"type\":\"InvalidType\",\"coordinates\":[10,20]}", GeoJsonObject.class);
            fail("Should have thrown an exception for invalid GeoJSON type");
        } catch (IOException e) {
            // Expected exception
        }
    }

    @Test
    public void testAdditionalDimensions() throws IOException {
        String json = "{\"type\":\"Point\",\"coordinates\":[10.0,20.0,30.0]}";
        Point point3d = mapper.readValue(json, Point.class);
        assertEquals(10.0, point3d.getCoordinates().getLongitude(), 0.001);
        assertEquals(20.0, point3d.getCoordinates().getLatitude(), 0.001);
        assertEquals(30.0, point3d.getCoordinates().getAltitude(), 0.001);

        json = "{\"type\":\"Point\",\"coordinates\":[10.0,20.0,30.0,40.0]}";
        Point point4d = mapper.readValue(json, Point.class);
        assertEquals(10.0, point4d.getCoordinates().getLongitude(), 0.001);
        assertEquals(20.0, point4d.getCoordinates().getLatitude(), 0.001);
        assertEquals(30.0, point4d.getCoordinates().getAltitude(), 0.001);
    }

    @Test
    public void testNullGeometry() throws IOException {
        Feature feature = new Feature();
        feature.setGeometry(null);
        feature.setProperty("name", "Null Geometry Feature");

        String json = mapper.writeValueAsString(feature);
        Feature deserializedFeature = mapper.readValue(json, Feature.class);

        assertNull(deserializedFeature.getGeometry());
        assertEquals("Null Geometry Feature", deserializedFeature.getProperty("name"));
    }

    @Test
    public void testNullProperties() throws IOException {
        Feature feature = new Feature();
        feature.setGeometry(new Point(10, 20));
        feature.setProperties(null);

        String json = mapper.writeValueAsString(feature);
        Feature deserializedFeature = mapper.readValue(json, Feature.class);

        assertNull(deserializedFeature.getProperties());
    }

    @Test
    public void testEmptyFeatureCollection() throws IOException {
        FeatureCollection featureCollection = new FeatureCollection();

        String json = mapper.writeValueAsString(featureCollection);
        FeatureCollection deserializedCollection = mapper.readValue(json, FeatureCollection.class);

        assertTrue(deserializedCollection.getFeatures().isEmpty());
    }

    @Test
    public void testSingleGeometryCollection() throws IOException {
        GeometryCollection geometryCollection = new GeometryCollection();
        geometryCollection.add(new Point(10, 20));

        String json = mapper.writeValueAsString(geometryCollection);
        GeometryCollection deserializedCollection = mapper.readValue(json, GeometryCollection.class);

        assertEquals(1, deserializedCollection.getGeometries().size());
        assertTrue(deserializedCollection.getGeometries().get(0) instanceof Point);
    }

    @Test
    public void testNestedGeometryCollections() throws IOException {
        GeometryCollection innerCollection = new GeometryCollection();
        innerCollection.add(new Point(10, 20));
        innerCollection.add(new LineString(
                new LngLatAlt(10, 20),
                new LngLatAlt(20, 30)
        ));

        GeometryCollection outerCollection = new GeometryCollection();
        outerCollection.add(innerCollection);
        outerCollection.add(new Point(30, 40));

        String json = mapper.writeValueAsString(outerCollection);
        GeometryCollection deserializedCollection = mapper.readValue(json, GeometryCollection.class);

        assertEquals(2, deserializedCollection.getGeometries().size());
        assertTrue(deserializedCollection.getGeometries().get(0) instanceof GeometryCollection);
        assertTrue(deserializedCollection.getGeometries().get(1) instanceof Point);

        GeometryCollection nestedCollection = (GeometryCollection) deserializedCollection.getGeometries().get(0);
        assertEquals(2, nestedCollection.getGeometries().size());
        assertTrue(nestedCollection.getGeometries().get(0) instanceof Point);
        assertTrue(nestedCollection.getGeometries().get(1) instanceof LineString);
    }
}
