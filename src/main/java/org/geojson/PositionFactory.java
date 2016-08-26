package org.geojson;

public class PositionFactory {

    public static Position create(double longitude, double latitude) {
        Position position = new Position();
        position.longitude = longitude;
        position.latitude = latitude;
        return position;
    }

    public static Position create(double longitude, double latitude, double altitude) {
        Position position = create(longitude, latitude);
        position.altitude = altitude;
        return position;
    }

    /**
     * Construct a Position with additional elements.
     * The specification allows for any number of additional elements in a position, after lng, lat, alt.
     * http://geojson.org/geojson-spec.html#positions
     * @param longitude The longitude.
     * @param latitude The latitude.
     * @param altitude The altitude.
     * @param additionalElements The additional elements.
     */
    public static Position create(double longitude, double latitude, double altitude, Double... additionalElements) {
        Position position = create(longitude, latitude);
        position.altitude = altitude;

        checkAltitudeAndAdditionalElements(position);
        setAdditionalElements(position, additionalElements);
        return position;
    }

    private static boolean hasAltitude(Position position) {
        return !Double.isNaN(position.altitude);
    }

    private static boolean hasAdditionalElements(Position position) {
        if (position == null) return false;
        Double[] elements = position.additionalElements;
        if (elements == null) return false;
        return elements.length > 0;
    }

    private static void setAdditionalElements(Position position, Double... additionalElements) {
        if (additionalElements == null) return;
        position.additionalElements = additionalElements;

        for(double element : additionalElements) {
            if (Double.isNaN(element)) {
                throw new IllegalArgumentException("No additional elements may be NaN.");
            }
            if (Double.isInfinite(element)) {
                throw new IllegalArgumentException("No additional elements may be infinite.");
            }
        }

        checkAltitudeAndAdditionalElements(position);
    }

    private static void checkAltitudeAndAdditionalElements(Position position) {
        if (!hasAltitude(position) && hasAdditionalElements(position)) {
            throw new IllegalArgumentException("Additional Elements are only valid if Altitude is also provided.");
        }
    }
}
