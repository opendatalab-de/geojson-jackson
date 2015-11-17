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

	/**
	 * An abstract adapter class for visiting GeoJson objects.
	 * The methods in this class are empty.
	 * This class exists as convenience for creating listener objects.
	 *
	 * @param <T> Return type of the visitor
   */
	class Adapter<T> implements GeoJsonObjectVisitor<T> {

		@Override
		public T visit(GeometryCollection geoJsonObject) {
			return null;
		}

		@Override
		public T visit(FeatureCollection geoJsonObject) {
			return null;
		}

		@Override
		public T visit(Point geoJsonObject) {
			return null;
		}

		@Override
		public T visit(Feature geoJsonObject) {
			return null;
		}

		@Override
		public T visit(MultiLineString geoJsonObject) {
			return null;
		}

		@Override
		public T visit(Polygon geoJsonObject) {
			return null;
		}

		@Override
		public T visit(MultiPolygon geoJsonObject) {
			return null;
		}

		@Override
		public T visit(MultiPoint geoJsonObject) {
			return null;
		}

		@Override
		public T visit(LineString geoJsonObject) {
			return null;
		}
	}
}
