package org.geojson;

public class PointFactory {

    public static Point create(Position coordinates) {
        Point point = new Point();
        point.coordinates = coordinates;
        return point;
    }

    public static Point create(double longitude, double latitude) {
        Point point = new Point();
        point.coordinates = PositionFactory.create(longitude, latitude);
        return point;
    }

    public static Point create(double longitude, double latitude, double altitude) {
        Point point = new Point();
        point.coordinates = PositionFactory.create(longitude, latitude, altitude);
        return point;
    }

    public static Point create(double longitude, double latitude, double altitude, Double... additionalElements) {
        Point point = new Point();
        point.coordinates = PositionFactory.create(longitude, latitude, altitude, additionalElements);
        return point;
    }
}
