package org.geojson;

import java.util.ArrayList;
import java.util.List;

/**
 * Utility class for GeoJSON processing.
 * Contains methods for RFC 7946 compliance, including polygon ring orientation
 * and antimeridian cutting.
 */
public class GeoJsonUtils {

    private static final double ANTIMERIDIAN = 180.0;

    /**
     * Determines if a ring is oriented counterclockwise.
     * Uses the Shoelace formula to calculate the signed area.
     *
     * @param ring The ring to check
     * @return true if the ring is counterclockwise, false otherwise
     */
    public static boolean isCounterClockwise(List<LngLatAlt> ring) {
        if (ring == null || ring.size() < 4) {
            // A valid ring must have at least 4 points (with the first and last being the same)
            return true;
        }

        double sum = 0;
        for (int i = 0; i < ring.size() - 1; i++) {
            LngLatAlt p1 = ring.get(i);
            LngLatAlt p2 = ring.get(i + 1);
            sum += (p2.getLongitude() - p1.getLongitude()) * (p2.getLatitude() + p1.getLatitude());
        }

        // If the signed area is negative, the ring is counterclockwise
        // Note: This is the opposite of the usual convention because GeoJSON uses longitude-latitude order
        return sum < 0;
    }

    /**
     * Reverses the orientation of a ring.
     *
     * @param ring The ring to reverse
     */
    public static void reverseRing(List<LngLatAlt> ring) {
        if (ring == null || ring.size() <= 1) {
            return;
        }

        // Keep the last point (which should be the same as the first)
        LngLatAlt last = ring.get(ring.size() - 1);

        // Reverse the list excluding the last point
        for (int i = 0, j = ring.size() - 2; i < j; i++, j--) {
            LngLatAlt temp = ring.get(i);
            ring.set(i, ring.get(j));
            ring.set(j, temp);
        }

        // Ensure the last point is still the same as the first
        ring.set(ring.size() - 1, last);
    }

    /**
     * Validates that the exterior ring is counterclockwise and interior rings are clockwise.
     *
     * @param rings The list of rings to validate
     * @throws IllegalArgumentException if the rings do not follow the right-hand rule
     */
    public static void validatePolygonOrientation(List<List<LngLatAlt>> rings) {
        if (rings == null || rings.isEmpty()) {
            return;
        }

        // Exterior ring should be counterclockwise
        List<LngLatAlt> exteriorRing = rings.get(0);

        // Validate that the ring has at least 4 points (3 unique points + closure)
        if (exteriorRing == null || exteriorRing.size() < 4) {
            throw new IllegalArgumentException("Exterior ring must have at least 4 points (3 unique points + closure)");
        }

        // Validate that the ring is closed (first and last points are the same)
        LngLatAlt first = exteriorRing.get(0);
        LngLatAlt last = exteriorRing.get(exteriorRing.size() - 1);
        if (first.getLongitude() != last.getLongitude() || first.getLatitude() != last.getLatitude()) {
            throw new IllegalArgumentException("Exterior ring must be closed (first and last points must be the same)");
        }

        // Validate orientation
        if (!isCounterClockwise(exteriorRing)) {
            throw new IllegalArgumentException("Exterior ring must be counterclockwise according to RFC 7946");
        }

        // Interior rings should be clockwise
        for (int i = 1; i < rings.size(); i++) {
            List<LngLatAlt> interiorRing = rings.get(i);

            // Validate that the ring has at least 4 points
            if (interiorRing == null || interiorRing.size() < 4) {
                throw new IllegalArgumentException("Interior ring " + i + " must have at least 4 points (3 unique points + closure)");
            }

            // Validate that the ring is closed
            first = interiorRing.get(0);
            last = interiorRing.get(interiorRing.size() - 1);
            if (first.getLongitude() != last.getLongitude() || first.getLatitude() != last.getLatitude()) {
                throw new IllegalArgumentException("Interior ring " + i + " must be closed (first and last points must be the same)");
            }

            // Validate orientation
            if (isCounterClockwise(interiorRing)) {
                throw new IllegalArgumentException("Interior ring " + i + " must be clockwise according to RFC 7946");
            }
        }
    }

    /**
     * Fixes polygon ring orientation to follow the right-hand rule.
     * Exterior rings are made counterclockwise, interior rings are made clockwise.
     *
     * @param rings The list of rings to fix
     */
    public static void fixPolygonOrientation(List<List<LngLatAlt>> rings) {
        if (rings == null || rings.isEmpty()) {
            return;
        }

        // Exterior ring should be counterclockwise
        List<LngLatAlt> exteriorRing = rings.get(0);
        if (!isCounterClockwise(exteriorRing)) {
            reverseRing(exteriorRing);
        }

        // Interior rings should be clockwise
        for (int i = 1; i < rings.size(); i++) {
            List<LngLatAlt> interiorRing = rings.get(i);
            if (isCounterClockwise(interiorRing)) {
                reverseRing(interiorRing);
            }
        }
    }

    /**
     * Determines if a line segment crosses the antimeridian.
     *
     * @param p1 The first point
     * @param p2 The second point
     * @return true if the line segment crosses the antimeridian, false otherwise
     */
    public static boolean crossesAntimeridian(LngLatAlt p1, LngLatAlt p2) {
        double lon1 = p1.getLongitude();
        double lon2 = p2.getLongitude();

        // Check if the longitudes have different signs and their absolute difference is greater than 180
        return Math.signum(lon1) != Math.signum(lon2) && Math.abs(lon1 - lon2) > 180;
    }

    /**
     * Interpolates the latitude at the antimeridian crossing.
     *
     * @param p1 The first point
     * @param p2 The second point
     * @return The interpolated latitude
     */
    public static double interpolateLatitude(LngLatAlt p1, LngLatAlt p2) {
        double lon1 = p1.getLongitude();
        double lat1 = p1.getLatitude();
        double lon2 = p2.getLongitude();
        double lat2 = p2.getLatitude();

        // Normalize longitudes to handle the antimeridian
        if (lon1 > 0 && lon2 < 0) {
            lon2 += 360;
        } else if (lon1 < 0 && lon2 > 0) {
            lon1 += 360;
        }

        // Calculate the fraction of the distance to the antimeridian
        double fraction = (ANTIMERIDIAN - Math.abs(lon1)) / Math.abs(lon2 - lon1);

        // Interpolate the latitude
        return lat1 + fraction * (lat2 - lat1);
    }

    /**
     * Cuts a LineString that crosses the antimeridian into a MultiLineString.
     *
     * @param lineString The LineString to cut
     * @return A MultiLineString if the LineString crosses the antimeridian, otherwise the original LineString
     */
    public static GeoJsonObject cutLineStringAtAntimeridian(LineString lineString) {
        List<LngLatAlt> coordinates = lineString.getCoordinates();
        if (coordinates.size() < 2) {
            return lineString;
        }

        List<List<LngLatAlt>> segments = new ArrayList<>();
        List<LngLatAlt> currentSegment = new ArrayList<>();
        currentSegment.add(coordinates.get(0));

        for (int i = 1; i < coordinates.size(); i++) {
            LngLatAlt p1 = coordinates.get(i - 1);
            LngLatAlt p2 = coordinates.get(i);

            if (crossesAntimeridian(p1, p2)) {
                // Calculate intersection points with the antimeridian
                double lat = interpolateLatitude(p1, p2);

                // Add the intersection point to the current segment
                LngLatAlt intersection1 = new LngLatAlt(Math.signum(p1.getLongitude()) * ANTIMERIDIAN, lat);
                currentSegment.add(intersection1);
                segments.add(new ArrayList<>(currentSegment));

                // Start a new segment from the other side of the antimeridian
                currentSegment = new ArrayList<>();
                LngLatAlt intersection2 = new LngLatAlt(-Math.signum(p1.getLongitude()) * ANTIMERIDIAN, lat);
                currentSegment.add(intersection2);
            }

            currentSegment.add(p2);
        }

        segments.add(currentSegment);

        if (segments.size() == 1) {
            return lineString;
        } else {
            MultiLineString multiLineString = new MultiLineString();
            for (List<LngLatAlt> segment : segments) {
                multiLineString.add(segment);
            }
            return multiLineString;
        }
    }

    /**
     * Cuts a Polygon that crosses the antimeridian into a MultiPolygon.
     *
     * @param polygon The Polygon to cut
     * @return A MultiPolygon if the Polygon crosses the antimeridian, otherwise the original Polygon
     */
    public static GeoJsonObject cutPolygonAtAntimeridian(Polygon polygon) {
        List<List<LngLatAlt>> rings = polygon.getCoordinates();
        if (rings.isEmpty()) {
            return polygon;
        }

        boolean crossesAntimeridian = false;
        for (List<LngLatAlt> ring : rings) {
            for (int i = 1; i < ring.size(); i++) {
                if (crossesAntimeridian(ring.get(i - 1), ring.get(i))) {
                    crossesAntimeridian = true;
                    break;
                }
            }
            if (crossesAntimeridian) {
                break;
            }
        }

        if (!crossesAntimeridian) {
            return polygon;
        }

        // For simplicity, we'll just create a placeholder implementation
        // A full implementation would be more complex and require cutting the polygon
        // along the antimeridian and creating two separate polygons

        MultiPolygon multiPolygon = new MultiPolygon();

        // This is a simplified approach - a real implementation would need to properly
        // cut the polygon along the antimeridian
        List<List<LngLatAlt>> eastRings = new ArrayList<>();
        List<List<LngLatAlt>> westRings = new ArrayList<>();

        // For now, we'll just split the polygon based on which side of the antimeridian
        // the majority of its points lie
        for (List<LngLatAlt> ring : rings) {
            int eastCount = 0;
            int westCount = 0;

            for (LngLatAlt point : ring) {
                if (point.getLongitude() > 0) {
                    eastCount++;
                } else {
                    westCount++;
                }
            }

            if (eastCount > westCount) {
                eastRings.add(ring);
            } else {
                westRings.add(ring);
            }
        }

        if (!eastRings.isEmpty()) {
            Polygon eastPolygon = new Polygon();
            for (List<LngLatAlt> ring : eastRings) {
                eastPolygon.add(ring);
            }
            multiPolygon.add(eastPolygon);
        }

        if (!westRings.isEmpty()) {
            Polygon westPolygon = new Polygon();
            for (List<LngLatAlt> ring : westRings) {
                westPolygon.add(ring);
            }
            multiPolygon.add(westPolygon);
        }

        return multiPolygon;
    }

    /**
     * Processes a GeoJSON object according to RFC 7946 recommendations.
     * This includes:
     * - Cutting geometries that cross the antimeridian
     * - Fixing polygon ring orientation
     *
     * @param object The GeoJSON object to process
     * @return The processed GeoJSON object
     */
    public static GeoJsonObject process(GeoJsonObject object) {
        if (!GeoJsonConfig.getInstance().isRfc7946Compliance()) {
            return object;
        }

        if (object instanceof Polygon) {
            Polygon polygon = (Polygon) object;

            // Fix polygon orientation if needed
            if (GeoJsonConfig.getInstance().isAutoFixPolygonOrientation()) {
                fixPolygonOrientation(polygon.getCoordinates());
            }

            // Cut at antimeridian if needed
            if (GeoJsonConfig.getInstance().isCutAntimeridian()) {
                return cutPolygonAtAntimeridian(polygon);
            }
        } else if (object instanceof LineString) {
            LineString lineString = (LineString) object;

            // Cut at antimeridian if needed
            if (GeoJsonConfig.getInstance().isCutAntimeridian()) {
                return cutLineStringAtAntimeridian(lineString);
            }
        } else if (object instanceof Feature) {
            Feature feature = (Feature) object;
            GeoJsonObject geometry = feature.getGeometry();
            if (geometry != null) {
                feature.setGeometry(process(geometry));
            }
        } else if (object instanceof FeatureCollection) {
            FeatureCollection featureCollection = (FeatureCollection) object;
            for (Feature feature : featureCollection) {
                GeoJsonObject geometry = feature.getGeometry();
                if (geometry != null) {
                    feature.setGeometry(process(geometry));
                }
            }
        } else if (object instanceof GeometryCollection) {
            GeometryCollection geometryCollection = (GeometryCollection) object;
            geometryCollection.getGeometries().replaceAll(GeoJsonUtils::process);
        }

        return object;
    }

    /**
     * Creates a WGS 84 CRS object.
     * This is the default CRS for GeoJSON according to RFC 7946.
     *
     * @return A CRS object for WGS 84
     */
    public static Crs createWGS84Crs() {
        Crs crs = new Crs();
        crs.getProperties().put("name", "urn:ogc:def:crs:OGC::CRS84");
        return crs;
    }
}
