package org.geojson;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ComplexFeatureTest {

    private GeoJsonMapper mapper;

    @Before
    public void setUp() {
        // Reset to default configuration before each test
        GeoJsonConfig.useLegacyMode();
        // Use RFC 7946 mapper but disable polygon orientation validation for tests
        mapper = new GeoJsonMapper(true, false);
    }

    @After
    public void tearDown() {
        // Reset to default configuration after each test
        GeoJsonConfig.useLegacyMode();
    }

    @Test
    public void testFeatureWithComplexProperties() throws IOException {
        Point point = new Point(10, 20);

        // Create a Feature with complex properties
        Feature feature = new Feature();
        feature.setGeometry(point);
        feature.setId("complex-feature-1");

        feature.setProperty("name", "Complex Feature");
        feature.setProperty("elevation", 1234.5);
        feature.setProperty("active", true);

        feature.setProperty("tags", Arrays.asList("important", "test", "complex"));

        Map<String, Object> metadata = new HashMap<>();
        metadata.put("created", "2023-01-15T12:00:00Z");
        metadata.put("author", "Test User");
        metadata.put("version", 2);

        Map<String, Object> system = new HashMap<>();
        system.put("id", 12345);
        system.put("ref", "ABC-XYZ");
        metadata.put("system", system);

        feature.setProperty("metadata", metadata);

        String json = mapper.writeValueAsString(feature);
        Feature deserializedFeature = mapper.readValue(json, Feature.class);

        assertEquals("Complex Feature", deserializedFeature.getProperty("name"));
        assertEquals(1234.5, deserializedFeature.getProperty("elevation"), 0.001);
        assertEquals(true, deserializedFeature.getProperty("active"));

        Object tagsObj = deserializedFeature.getProperty("tags");
        assertTrue(tagsObj instanceof java.util.List);
        java.util.List<?> tags = (java.util.List<?>) tagsObj;
        assertEquals(3, tags.size());
        assertEquals("important", tags.get(0));
        assertEquals("test", tags.get(1));
        assertEquals("complex", tags.get(2));

        Object metadataObj = deserializedFeature.getProperty("metadata");
        assertTrue(metadataObj instanceof Map);
        Map<?, ?> metadataMap = (Map<?, ?>) metadataObj;
        assertEquals("2023-01-15T12:00:00Z", metadataMap.get("created"));
        assertEquals("Test User", metadataMap.get("author"));
        assertEquals(2, metadataMap.get("version"));

        Object systemObj = metadataMap.get("system");
        assertTrue(systemObj instanceof Map);
        Map<?, ?> systemMap = (Map<?, ?>) systemObj;
        assertEquals(12345, systemMap.get("id"));
        assertEquals("ABC-XYZ", systemMap.get("ref"));
    }

    @Test
    public void testMixedFeatureCollection() throws IOException {
        FeatureCollection featureCollection = new FeatureCollection();

        Point point = new Point(10, 20, 30);
        Feature pointFeature = new Feature();
        pointFeature.setGeometry(point);
        pointFeature.setProperty("type", "point");
        pointFeature.setProperty("name", "Mount Example");
        featureCollection.add(pointFeature);

        LineString lineString = new LineString(
                new LngLatAlt(10, 20),
                new LngLatAlt(20, 30),
                new LngLatAlt(30, 40)
        );
        Feature lineFeature = new Feature();
        lineFeature.setGeometry(lineString);
        lineFeature.setProperty("type", "line");
        lineFeature.setProperty("name", "Example River");
        lineFeature.setProperty("length_km", 42.5);
        featureCollection.add(lineFeature);

        Polygon polygon = new Polygon();
        polygon.add(Arrays.asList(
                new LngLatAlt(0, 0),
                new LngLatAlt(10, 0),
                new LngLatAlt(10, 10),
                new LngLatAlt(0, 10),
                new LngLatAlt(0, 0)  // Close the ring
        ));
        polygon.addInteriorRing(
                new LngLatAlt(2, 2),
                new LngLatAlt(8, 2),
                new LngLatAlt(8, 8),
                new LngLatAlt(2, 8),
                new LngLatAlt(2, 2)  // Close the ring
        );
        Feature polygonFeature = new Feature();
        polygonFeature.setGeometry(polygon);
        polygonFeature.setProperty("type", "polygon");
        polygonFeature.setProperty("name", "Example Lake");
        polygonFeature.setProperty("area_km2", 100.0);
        featureCollection.add(polygonFeature);

        Feature nullGeometryFeature = new Feature();
        nullGeometryFeature.setGeometry(null);
        nullGeometryFeature.setProperty("type", "unlocated");
        nullGeometryFeature.setProperty("name", "Unlocated Feature");
        featureCollection.add(nullGeometryFeature);

        String json = mapper.writeValueAsString(featureCollection);
        FeatureCollection deserializedCollection = mapper.readValue(json, FeatureCollection.class);

        assertEquals(4, deserializedCollection.getFeatures().size());

        int pointCount = 0;
        int lineCount = 0;
        int polygonCount = 0;
        int nullGeometryCount = 0;

        for (Feature feature : deserializedCollection) {
            String type = feature.getProperty("type");

            if ("point".equals(type)) {
                pointCount++;
                assertTrue(feature.getGeometry() instanceof Point);
                Point p = (Point) feature.getGeometry();
                assertEquals(10.0, p.getCoordinates().getLongitude(), 0.001);
                assertEquals(20.0, p.getCoordinates().getLatitude(), 0.001);
                assertEquals(30.0, p.getCoordinates().getAltitude(), 0.001);
                assertEquals("Mount Example", feature.getProperty("name"));
            } else if ("line".equals(type)) {
                lineCount++;
                assertTrue(feature.getGeometry() instanceof LineString);
                LineString l = (LineString) feature.getGeometry();
                assertEquals(3, l.getCoordinates().size());
                assertEquals("Example River", feature.getProperty("name"));
                assertEquals(42.5, feature.getProperty("length_km"), 0.001);
            } else if ("polygon".equals(type)) {
                polygonCount++;
                assertTrue(feature.getGeometry() instanceof Polygon);
                Polygon p = (Polygon) feature.getGeometry();
                assertEquals(2, p.getCoordinates().size()); // Exterior + 1 interior
                assertEquals("Example Lake", feature.getProperty("name"));
                assertEquals(100.0, feature.getProperty("area_km2"), 0.001);
            } else if ("unlocated".equals(type)) {
                nullGeometryCount++;
                assertNull(feature.getGeometry());
                assertEquals("Unlocated Feature", feature.getProperty("name"));
            }
        }

        assertEquals(1, pointCount);
        assertEquals(1, lineCount);
        assertEquals(1, polygonCount);
        assertEquals(1, nullGeometryCount);
    }

    @Test
    public void testFeatureWithGeometryCollection() throws IOException {
        GeometryCollection geometryCollection = new GeometryCollection();
        geometryCollection.add(new Point(10, 20));
        geometryCollection.add(new LineString(
                new LngLatAlt(10, 20),
                new LngLatAlt(20, 30)
        ));

        Feature feature = new Feature();
        feature.setGeometry(geometryCollection);
        feature.setProperty("name", "Complex Feature");

        String json = mapper.writeValueAsString(feature);
        Feature deserializedFeature = mapper.readValue(json, Feature.class);

        assertTrue(deserializedFeature.getGeometry() instanceof GeometryCollection);
        GeometryCollection gc = (GeometryCollection) deserializedFeature.getGeometry();
        assertEquals(2, gc.getGeometries().size());
        assertTrue(gc.getGeometries().get(0) instanceof Point);
        assertTrue(gc.getGeometries().get(1) instanceof LineString);
    }

    @Test
    public void testFeatureWithForeignMembers() throws IOException {
        String json = "{" +
                "\"type\": \"Feature\"," +
                "\"geometry\": {" +
                "  \"type\": \"Point\"," +
                "  \"coordinates\": [10.0, 20.0]" +
                "}," +
                "\"properties\": {" +
                "  \"name\": \"Test Feature\"" +
                "}," +
                "\"customField\": \"This is a custom field\"," +
                "\"customObject\": {" +
                "  \"nestedField\": 123" +
                "}" +
                "}";

        Feature feature = mapper.readValue(json, Feature.class);

        assertTrue(feature.getGeometry() instanceof Point);
        assertEquals("Test Feature", feature.getProperty("name"));

        // Foreign members are ignored by default in our implementation
    }

    @Test
    public void testFeatureWithBbox() throws IOException {
        String json = "{" +
                "\"type\": \"Feature\"," +
                "\"bbox\": [-10.0, -10.0, 10.0, 10.0]," +
                "\"geometry\": {" +
                "  \"type\": \"Polygon\"," +
                "  \"coordinates\": [" +
                "    [" +
                "      [-10.0, -10.0]," +
                "      [10.0, -10.0]," +
                "      [10.0, 10.0]," +
                "      [-10.0, 10.0]," +
                "      [-10.0, -10.0]" +
                "    ]" +
                "  ]" +
                "}," +
                "\"properties\": {" +
                "  \"name\": \"Bounding Box Example\"" +
                "}" +
                "}";

        Feature feature = mapper.readValue(json, Feature.class);

        assertNotNull(feature.getBbox());
        assertEquals(4, feature.getBbox().length);
        assertEquals(-10.0, feature.getBbox()[0], 0.001);
        assertEquals(-10.0, feature.getBbox()[1], 0.001);
        assertEquals(10.0, feature.getBbox()[2], 0.001);
        assertEquals(10.0, feature.getBbox()[3], 0.001);

        assertTrue(feature.getGeometry() instanceof Polygon);
        assertEquals("Bounding Box Example", feature.getProperty("name"));
    }
}
