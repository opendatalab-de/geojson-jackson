package org.geojson;

/**
 * Visitor to handle all different types of {@link GeoJsonObject}.
 * 
 * @param <T>
 *            return type of the visitor.
 */
public interface GeoJsonObjectVisitor<T> {

	T visit(GeometryCollection geoJsonObject);

	T visit(FeatureCollection geoJsonObject);

	T visit(Point geoJsonObject);

	T visit(Feature geoJsonObject);

	T visit(MultiLineString geoJsonObject);

	T visit(Polygon geoJsonObject);

	T visit(MultiPolygon geoJsonObject);

	T visit(MultiPoint geoJsonObject);

	T visit(LineString geoJsonObject);
}
