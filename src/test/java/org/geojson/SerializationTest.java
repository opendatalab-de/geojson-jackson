package org.geojson;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.Arrays;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class SerializationTest {

    private GeoJsonMapper legacyMapper;
    private GeoJsonMapper rfc7946Mapper;

    @Before
    public void setUp() {
        // Reset to default configuration before each test
        GeoJsonConfig.useLegacyMode();
        legacyMapper = new GeoJsonMapper(false);
        // Use RFC 7946 mapper but disable polygon orientation validation for tests
        rfc7946Mapper = new GeoJsonMapper(true, false);
    }

    @After
    public void tearDown() {
        // Reset to default configuration after each test
        GeoJsonConfig.useLegacyMode();
    }

    @Test
    public void testPointSerialization() throws IOException {
        Point point = new Point(10, 20, 30);

        String legacyJson = legacyMapper.writeValueAsString(point);

        Point legacyPoint = legacyMapper.readValue(legacyJson, Point.class);
        assertEquals(10.0, legacyPoint.getCoordinates().getLongitude(), 0.001);
        assertEquals(20.0, legacyPoint.getCoordinates().getLatitude(), 0.001);
        assertEquals(30.0, legacyPoint.getCoordinates().getAltitude(), 0.001);

        String rfc7946Json = rfc7946Mapper.writeValueAsString(point);

        Point rfc7946Point = rfc7946Mapper.readValue(rfc7946Json, Point.class);
        assertEquals(10.0, rfc7946Point.getCoordinates().getLongitude(), 0.001);
        assertEquals(20.0, rfc7946Point.getCoordinates().getLatitude(), 0.001);
        assertEquals(30.0, rfc7946Point.getCoordinates().getAltitude(), 0.001);
    }

    @Test
    public void testLineStringSerialization() throws IOException {
        LineString lineString = new LineString(
                new LngLatAlt(10, 20),
                new LngLatAlt(20, 30),
                new LngLatAlt(30, 40)
        );

        String legacyJson = legacyMapper.writeValueAsString(lineString);

        LineString legacyLineString = legacyMapper.readValue(legacyJson, LineString.class);
        assertEquals(3, legacyLineString.getCoordinates().size());
        assertEquals(10.0, legacyLineString.getCoordinates().get(0).getLongitude(), 0.001);
        assertEquals(20.0, legacyLineString.getCoordinates().get(0).getLatitude(), 0.001);

        String rfc7946Json = rfc7946Mapper.writeValueAsString(lineString);

        LineString rfc7946LineString = rfc7946Mapper.readValue(rfc7946Json, LineString.class);
        assertEquals(3, rfc7946LineString.getCoordinates().size());
        assertEquals(10.0, rfc7946LineString.getCoordinates().get(0).getLongitude(), 0.001);
        assertEquals(20.0, rfc7946LineString.getCoordinates().get(0).getLatitude(), 0.001);
    }

    @Test
    public void testPolygonSerialization() throws IOException {
        Polygon polygon = new Polygon();
        polygon.add(Arrays.asList(
                new LngLatAlt(0, 0),
                new LngLatAlt(10, 0),
                new LngLatAlt(10, 10),
                new LngLatAlt(0, 10),
                new LngLatAlt(0, 0)
        ));
        polygon.addInteriorRing(
                new LngLatAlt(2, 2),
                new LngLatAlt(8, 2),
                new LngLatAlt(8, 8),
                new LngLatAlt(2, 8),
                new LngLatAlt(2, 2)
        );

        String legacyJson = legacyMapper.writeValueAsString(polygon);

        Polygon legacyPolygon = legacyMapper.readValue(legacyJson, Polygon.class);
        assertEquals(2, legacyPolygon.getCoordinates().size());
        assertEquals(5, legacyPolygon.getExteriorRing().size());
        assertEquals(5, legacyPolygon.getInteriorRing(0).size());

        String rfc7946Json = rfc7946Mapper.writeValueAsString(polygon);

        Polygon rfc7946Polygon = rfc7946Mapper.readValue(rfc7946Json, Polygon.class);
        assertEquals(2, rfc7946Polygon.getCoordinates().size());
        assertEquals(5, rfc7946Polygon.getExteriorRing().size());
        assertEquals(5, rfc7946Polygon.getInteriorRing(0).size());

        assertTrue(GeoJsonUtils.isCounterClockwise(rfc7946Polygon.getExteriorRing()));
        assertFalse(GeoJsonUtils.isCounterClockwise(rfc7946Polygon.getInteriorRing(0)));
    }

    @Test
    public void testFeatureWithCrsSerialization() throws IOException {
        Point point = new Point(10, 20);

        Crs crs = new Crs();
        crs.getProperties().put("name", "EPSG:4326");
        point.setCrs(crs);

        Feature feature = new Feature();
        feature.setGeometry(point);
        feature.setProperty("name", "Feature with CRS");

        String legacyJson = legacyMapper.writeValueAsString(feature);

        Feature legacyFeature = legacyMapper.readValue(legacyJson, Feature.class);
        assertNotNull(legacyFeature.getGeometry().getCrs());
        assertEquals("EPSG:4326", legacyFeature.getGeometry().getCrs().getProperties().get("name"));

        String rfc7946Json = rfc7946Mapper.writeValueAsString(feature);

        Feature rfc7946Feature = rfc7946Mapper.readValue(rfc7946Json, Feature.class);
        assertNotNull(rfc7946Feature.getGeometry().getCrs());
        assertEquals("EPSG:4326", rfc7946Feature.getGeometry().getCrs().getProperties().get("name"));
    }

    @Test
    public void testFeatureCollectionSerialization() throws IOException {
        FeatureCollection featureCollection = new FeatureCollection();

        Point point = new Point(10, 20);
        Feature pointFeature = new Feature();
        pointFeature.setGeometry(point);
        pointFeature.setProperty("type", "point");
        featureCollection.add(pointFeature);

        LineString lineString = new LineString(
                new LngLatAlt(10, 20),
                new LngLatAlt(20, 30)
        );
        Feature lineFeature = new Feature();
        lineFeature.setGeometry(lineString);
        lineFeature.setProperty("type", "line");
        featureCollection.add(lineFeature);

        String legacyJson = legacyMapper.writeValueAsString(featureCollection);

        FeatureCollection legacyCollection = legacyMapper.readValue(legacyJson, FeatureCollection.class);
        assertEquals(2, legacyCollection.getFeatures().size());

        String rfc7946Json = rfc7946Mapper.writeValueAsString(featureCollection);

        FeatureCollection rfc7946Collection = rfc7946Mapper.readValue(rfc7946Json, FeatureCollection.class);
        assertEquals(2, rfc7946Collection.getFeatures().size());
    }

    @Test
    public void testGeometryCollectionSerialization() throws IOException {
        GeometryCollection geometryCollection = new GeometryCollection();
        geometryCollection.add(new Point(10, 20));
        geometryCollection.add(new LineString(
                new LngLatAlt(10, 20),
                new LngLatAlt(20, 30)
        ));

        String legacyJson = legacyMapper.writeValueAsString(geometryCollection);

        GeometryCollection legacyCollection = legacyMapper.readValue(legacyJson, GeometryCollection.class);
        assertEquals(2, legacyCollection.getGeometries().size());
        assertTrue(legacyCollection.getGeometries().get(0) instanceof Point);
        assertTrue(legacyCollection.getGeometries().get(1) instanceof LineString);

        String rfc7946Json = rfc7946Mapper.writeValueAsString(geometryCollection);

        GeometryCollection rfc7946Collection = rfc7946Mapper.readValue(rfc7946Json, GeometryCollection.class);
        assertEquals(2, rfc7946Collection.getGeometries().size());
        assertTrue(rfc7946Collection.getGeometries().get(0) instanceof Point);
        assertTrue(rfc7946Collection.getGeometries().get(1) instanceof LineString);
    }

    @Test
    public void testBboxSerialization() throws IOException {
        // Create a Polygon with a bbox
        Polygon polygon = new Polygon();
        polygon.add(Arrays.asList(
                new LngLatAlt(0, 0),
                new LngLatAlt(10, 0),
                new LngLatAlt(10, 10),
                new LngLatAlt(0, 10),
                new LngLatAlt(0, 0)  // Close the ring
        ));
        polygon.setBbox(new double[] { 0, 0, 10, 10 });

        // Serialize with legacy mapper
        String legacyJson = legacyMapper.writeValueAsString(polygon);

        // Deserialize with legacy mapper
        Polygon legacyPolygon = legacyMapper.readValue(legacyJson, Polygon.class);
        assertNotNull(legacyPolygon.getBbox());
        assertEquals(4, legacyPolygon.getBbox().length);
        assertEquals(0, legacyPolygon.getBbox()[0], 0.001);
        assertEquals(0, legacyPolygon.getBbox()[1], 0.001);
        assertEquals(10, legacyPolygon.getBbox()[2], 0.001);
        assertEquals(10, legacyPolygon.getBbox()[3], 0.001);

        // Serialize with RFC 7946 mapper
        String rfc7946Json = rfc7946Mapper.writeValueAsString(polygon);

        // Deserialize with RFC 7946 mapper
        Polygon rfc7946Polygon = rfc7946Mapper.readValue(rfc7946Json, Polygon.class);
        assertNotNull(rfc7946Polygon.getBbox());
        assertEquals(4, rfc7946Polygon.getBbox().length);
        assertEquals(0, rfc7946Polygon.getBbox()[0], 0.001);
        assertEquals(0, rfc7946Polygon.getBbox()[1], 0.001);
        assertEquals(10, rfc7946Polygon.getBbox()[2], 0.001);
        assertEquals(10, rfc7946Polygon.getBbox()[3], 0.001);
    }

    @Test
    public void testCrossDeserialization() throws IOException {
        // Create a Polygon with a hole
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

        // Serialize with legacy mapper
        String legacyJson = legacyMapper.writeValueAsString(polygon);

        // Deserialize with RFC 7946 mapper
        Polygon crossPolygon1 = rfc7946Mapper.readValue(legacyJson, Polygon.class);
        assertEquals(2, crossPolygon1.getCoordinates().size());

        // Verify orientation was fixed
        assertTrue(GeoJsonUtils.isCounterClockwise(crossPolygon1.getExteriorRing()));
        assertFalse(GeoJsonUtils.isCounterClockwise(crossPolygon1.getInteriorRing(0)));

        // Serialize with RFC 7946 mapper
        String rfc7946Json = rfc7946Mapper.writeValueAsString(polygon);

        // Deserialize with legacy mapper
        Polygon crossPolygon2 = legacyMapper.readValue(rfc7946Json, Polygon.class);
        assertEquals(2, crossPolygon2.getCoordinates().size());
    }
}
