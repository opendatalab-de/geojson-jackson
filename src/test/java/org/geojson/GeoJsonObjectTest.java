package org.geojson;

/**
 * Provides constants used in other test classes. Eventually, this could also
 * exercise the concrete methods provided by {@link GeoJsonObject} itself.
 * @since issue #45
 */
public class GeoJsonObjectTest
{
	/**
	 * Tolerance of {@code assertEquals} evaluations against double-precision
	 * floating point numbers, such as those used in the bounding box.
	 */
	public static final double TOLERANCE = 0.0000001d ;
}
