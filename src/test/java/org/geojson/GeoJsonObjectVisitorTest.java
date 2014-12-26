package org.geojson;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

@RunWith(Parameterized.class)
public class GeoJsonObjectVisitorTest {

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {new GeometryCollection()},
                {new FeatureCollection()},
                {new Point(12D, 13D)},
                {new Feature()},
                {new MultiLineString(Arrays.asList(new LngLatAlt(12D, 13D)))},
                {new Polygon()},
                {new MultiPolygon()},
                {new MultiPoint()},
                {new LineString()}
        });
    }

    private GeoJsonObjectVisitor<GeoJsonObject> instance = new GeoJsonObjectVisitor<GeoJsonObject>() {
        @Override
        public GeoJsonObject visit(GeometryCollection geoJsonObject) {
            Assert.assertEquals(GeometryCollection.class, geoJsonObject.getClass());
            return geoJsonObject;
        }

        @Override
        public GeoJsonObject visit(FeatureCollection geoJsonObject) {
            Assert.assertEquals(FeatureCollection.class, geoJsonObject.getClass());
            return geoJsonObject;
        }

        @Override
        public GeoJsonObject visit(Point geoJsonObject) {
            Assert.assertEquals(Point.class, geoJsonObject.getClass());
            return geoJsonObject;
        }

        @Override
        public GeoJsonObject visit(Feature geoJsonObject) {
            Assert.assertEquals(Feature.class, geoJsonObject.getClass());
            return geoJsonObject;
        }

        @Override
        public GeoJsonObject visit(MultiLineString geoJsonObject) {
            Assert.assertEquals(MultiLineString.class, geoJsonObject.getClass());
            return geoJsonObject;
        }

        @Override
        public GeoJsonObject visit(Polygon geoJsonObject) {
            Assert.assertEquals(Polygon.class, geoJsonObject.getClass());
            return geoJsonObject;
        }

        @Override
        public GeoJsonObject visit(MultiPolygon geoJsonObject) {
            Assert.assertEquals(MultiPolygon.class, geoJsonObject.getClass());
            return geoJsonObject;
        }

        @Override
        public GeoJsonObject visit(MultiPoint geoJsonObject) {
            Assert.assertEquals(MultiPoint.class, geoJsonObject.getClass());
            return geoJsonObject;
        }

        @Override
        public GeoJsonObject visit(LineString geoJsonObject) {
            Assert.assertEquals(LineString.class, geoJsonObject.getClass());
            return geoJsonObject;
        }
    };

    private final GeoJsonObject geoJsonObject;

    public GeoJsonObjectVisitorTest(GeoJsonObject geoJsonObject) {
        this.geoJsonObject = geoJsonObject;
    }

    @Test
    public void should_visit_right_class() {
        // When
        GeoJsonObject result = geoJsonObject.accept(this.instance);

        // Then
        Assert.assertEquals(geoJsonObject, result);
    }
}