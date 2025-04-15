package org.geojson;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class GeoJsonMapperTest {

    private GeoJsonMapper legacyMapper;
    private GeoJsonMapper rfc7946Mapper;

    @Before
    public void setUp() {
        legacyMapper = new GeoJsonMapper(false);
        rfc7946Mapper = new GeoJsonMapper(true);
    }

    @After
    public void tearDown() {
        // Reset to default configuration after each test
        GeoJsonConfig.useLegacyMode();
    }

    @Test
    public void testLegacyMapper() throws IOException {
        // Create a point with CRS
        Point point = new Point(100, 0);
        Crs crs = new Crs();
        crs.getProperties().put("name", "EPSG:4326");
        point.setCrs(crs);

        // Serialize and deserialize with legacy mapper
        String json = legacyMapper.writeValueAsString(point);
        Point deserializedPoint = legacyMapper.readValue(json, Point.class);

        // The CRS should be preserved
        assertNotNull(deserializedPoint.getCrs());
        assertEquals("EPSG:4326", deserializedPoint.getCrs().getProperties().get("name"));
    }

    @Test
    public void testRfc7946Mapper() throws IOException {
        // Create a polygon with clockwise exterior ring (would be invalid, but will be fixed)
        Polygon polygon = new Polygon(
                new LngLatAlt(0, 0),
                new LngLatAlt(0, 1),
                new LngLatAlt(1, 1),
                new LngLatAlt(1, 0),
                new LngLatAlt(0, 0)
        );

        // The polygon should have been fixed by the RFC 7946 mapper
        assertTrue(GeoJsonUtils.isCounterClockwise(polygon.getExteriorRing()));

        // Serialize and deserialize with RFC 7946 mapper
        String json = rfc7946Mapper.writeValueAsString(polygon);
        Polygon deserializedPolygon = rfc7946Mapper.readValue(json, Polygon.class);

        // The polygon orientation should be preserved
        assertTrue(GeoJsonUtils.isCounterClockwise(deserializedPolygon.getExteriorRing()));
    }

    @Test
    public void testProcessMethod() {
        // Create a LineString that crosses the antimeridian
        LineString lineString = new LineString(
                new LngLatAlt(170, 45),
                new LngLatAlt(-170, 45)
        );

        // Process the LineString with the RFC 7946 mapper
        GeoJsonObject processed = rfc7946Mapper.process(lineString);

        // The result should be a MultiLineString
        assertTrue(processed instanceof MultiLineString);
        MultiLineString multiLineString = (MultiLineString) processed;

        // It should have two segments
        assertEquals(2, multiLineString.getCoordinates().size());
    }
}
