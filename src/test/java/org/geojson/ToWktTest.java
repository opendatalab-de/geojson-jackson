package org.geojson;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class ToWktTest {

    @Test
    public void should_to_wkt_point() {
        Point point = new Point(10, 30);

        assertEquals(point.toWKT(), "POINT(10.0 30.0)");
    }

    @Test
    public void should_to_wkt_multipoint() {
        MultiPoint multiPoint = new MultiPoint();
        multiPoint.add(new LngLatAlt(10, 40));
        multiPoint.add(new LngLatAlt(40, 30));
        multiPoint.add(new LngLatAlt(20, 20));
        multiPoint.add(new LngLatAlt(30, 10));

        assertEquals(multiPoint.toWKT(), "MULTIPOINT(10.0 40.0,40.0 30.0,20.0 20.0,30.0 10.0)");
    }

    @Test
    public void should_to_wkt_linestring() {
        LineString lineString = new LineString();
        lineString.add(new LngLatAlt(30, 10));
        lineString.add(new LngLatAlt(10, 30));
        lineString.add(new LngLatAlt(40, 40));

        assertEquals(lineString.toWKT(), "LINESTRING(30.0 10.0,10.0 30.0,40.0 40.0)");
    }

    @Test
    public void should_to_wkt_multilinestring() {
        MultiLineString multiLineString = new MultiLineString();
        List<LngLatAlt> line = new ArrayList<LngLatAlt>();
        line.add(new LngLatAlt(10, 10));
        line.add(new LngLatAlt(20, 20));
        line.add(new LngLatAlt(10, 40));
        multiLineString.add(line);

        line = new ArrayList<LngLatAlt>();
        line.add(new LngLatAlt(40, 40));
        line.add(new LngLatAlt(30, 30));
        line.add(new LngLatAlt(40, 20));
        line.add(new LngLatAlt(30, 10));
        multiLineString.add(line);

        assertEquals(multiLineString.toWKT(), "MULTILINESTRING((10.0 10.0,20.0 20.0,10.0 40.0),(40.0 40.0,30.0 30.0,40.0 20.0,30.0 10.0))");
    }

    @Test
    public void should_to_wkt_polygon() {
        Polygon polygon = new Polygon();
        List<LngLatAlt> exteriorRing = new ArrayList<LngLatAlt>();
        exteriorRing.add(new LngLatAlt(30, 10));
        exteriorRing.add(new LngLatAlt(40, 40));
        exteriorRing.add(new LngLatAlt(20, 40));
        exteriorRing.add(new LngLatAlt(10, 20));
        exteriorRing.add(new LngLatAlt(30, 10));
        polygon.setExteriorRing(exteriorRing);

        assertEquals(polygon.toWKT(), "POLYGON((30.0 10.0,40.0 40.0,20.0 40.0,10.0 20.0,30.0 10.0))");

        polygon = new Polygon();
        exteriorRing = new ArrayList<LngLatAlt>();
        exteriorRing.add(new LngLatAlt(35, 10));
        exteriorRing.add(new LngLatAlt(45, 45));
        exteriorRing.add(new LngLatAlt(15, 40));
        exteriorRing.add(new LngLatAlt(10, 20));
        exteriorRing.add(new LngLatAlt(35, 10));
        polygon.setExteriorRing(exteriorRing);

        List<LngLatAlt> interiorRing = new ArrayList<LngLatAlt>();
        interiorRing.add(new LngLatAlt(20, 30));
        interiorRing.add(new LngLatAlt(35, 35));
        interiorRing.add(new LngLatAlt(30, 20));
        interiorRing.add(new LngLatAlt(20, 30));
        polygon.addInteriorRing(interiorRing);

        assertEquals(polygon.toWKT(), "POLYGON((35.0 10.0,45.0 45.0,15.0 40.0,10.0 20.0,35.0 10.0)," +
                "(20.0 30.0,35.0 35.0,30.0 20.0,20.0 30.0))");

    }

    @Test
    public void should_to_wkt_multipolygon() {
        MultiPolygon multiPolygon = new MultiPolygon();

        Polygon polygon = new Polygon();
        List<LngLatAlt> exteriorRing = new ArrayList<LngLatAlt>();
        exteriorRing.add(new LngLatAlt(30, 20));
        exteriorRing.add(new LngLatAlt(45, 40));
        exteriorRing.add(new LngLatAlt(10, 40));
        exteriorRing.add(new LngLatAlt(30, 20));
        polygon.setExteriorRing(exteriorRing);
        multiPolygon.add(polygon);

        polygon = new Polygon();
        exteriorRing = new ArrayList<LngLatAlt>();
        exteriorRing.add(new LngLatAlt(15, 5));
        exteriorRing.add(new LngLatAlt(40, 10));
        exteriorRing.add(new LngLatAlt(10, 20));
        exteriorRing.add(new LngLatAlt(5, 10));
        exteriorRing.add(new LngLatAlt(15, 5));
        polygon.setExteriorRing(exteriorRing);
        multiPolygon.add(polygon);

        assertEquals(multiPolygon.toWKT(), "MULTIPOLYGON(((30.0 20.0,45.0 40.0,10.0 40.0,30.0 20.0))," +
                "((15.0 5.0,40.0 10.0,10.0 20.0,5.0 10.0,15.0 5.0)))");

        multiPolygon = new MultiPolygon();

        polygon = new Polygon();
        exteriorRing = new ArrayList<LngLatAlt>();
        exteriorRing.add(new LngLatAlt(40, 40));
        exteriorRing.add(new LngLatAlt(20, 45));
        exteriorRing.add(new LngLatAlt(45, 30));
        exteriorRing.add(new LngLatAlt(40, 40));
        polygon.setExteriorRing(exteriorRing);
        multiPolygon.add(polygon);

        polygon = new Polygon();
        exteriorRing = new ArrayList<LngLatAlt>();
        exteriorRing.add(new LngLatAlt(20, 35));
        exteriorRing.add(new LngLatAlt(10, 30));
        exteriorRing.add(new LngLatAlt(10, 10));
        exteriorRing.add(new LngLatAlt(30, 5));
        exteriorRing.add(new LngLatAlt(45, 20));
        exteriorRing.add(new LngLatAlt(20, 35));
        polygon.setExteriorRing(exteriorRing);

        List<LngLatAlt> interiorRing = new ArrayList<LngLatAlt>();
        interiorRing.add(new LngLatAlt(30, 20));
        interiorRing.add(new LngLatAlt(20, 15));
        interiorRing.add(new LngLatAlt(20, 25));
        interiorRing.add(new LngLatAlt(30, 20));
        polygon.addInteriorRing(interiorRing);
        multiPolygon.add(polygon);

        assertEquals(multiPolygon.toWKT(), "MULTIPOLYGON(((40.0 40.0,20.0 45.0,45.0 30.0,40.0 40.0))," +
                "((20.0 35.0,10.0 30.0,10.0 10.0,30.0 5.0,45.0 20.0,20.0 35.0),(30.0 20.0,20.0 15.0,20.0 25.0,30.0 20.0)))");
    }

}
