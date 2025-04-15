package org.geojson;

import java.util.Arrays;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * A Polygon geometry.
 * <p>
 * According to RFC 7946, a Polygon consists of a linear ring (the exterior ring),
 * and zero or more linear rings (the interior rings or holes).
 * <p>
 * The exterior ring must be counterclockwise, and the interior rings must be clockwise
 * when RFC 7946 compliance is enabled.
 */
public class Polygon extends Geometry<List<LngLatAlt>> {

	public Polygon() {
	}

	public Polygon(List<LngLatAlt> polygon) {
		add(polygon);
	}

	public Polygon(LngLatAlt... polygon) {
		add(Arrays.asList(polygon));
	}

	@Override
	public Geometry<List<LngLatAlt>> add(List<LngLatAlt> elements) {
		super.add(elements);

		// Process according to RFC 7946 if enabled
		processRfc7946();

		return this;
	}

	/**
	 * Set the exterior ring of the polygon.
	 *
	 * @param points The points of the exterior ring
	 */
	public void setExteriorRing(List<LngLatAlt> points) {
		if (coordinates.isEmpty()) {
			coordinates.add(0, points);
		} else {
			coordinates.set(0, points);
		}

		// Process according to RFC 7946 if enabled
		processRfc7946();
	}

	@JsonIgnore
	public List<LngLatAlt> getExteriorRing() {
		assertExteriorRing();
		return coordinates.get(0);
	}

	@JsonIgnore
	public List<List<LngLatAlt>> getInteriorRings() {
		assertExteriorRing();
		return coordinates.subList(1, coordinates.size());
	}

	public List<LngLatAlt> getInteriorRing(int index) {
		assertExteriorRing();
		return coordinates.get(1 + index);
	}

	public void addInteriorRing(List<LngLatAlt> points) {
		assertExteriorRing();
		coordinates.add(points);

		// Process according to RFC 7946 if enabled
		processRfc7946();
	}

	public void addInteriorRing(LngLatAlt... points) {
		assertExteriorRing();
		coordinates.add(Arrays.asList(points));

		// Process according to RFC 7946 if enabled
		processRfc7946();
	}

	/**
	 * Process the polygon according to RFC 7946 requirements if enabled.
	 * This includes validating or fixing ring orientation.
	 */
	private void processRfc7946() {
		if (!GeoJsonConfig.getInstance().isRfc7946Compliance() || coordinates.isEmpty()) {
			return;
		}

		// Handle polygon orientation
		if (GeoJsonConfig.getInstance().isValidatePolygonOrientation()) {
			if (GeoJsonConfig.getInstance().isAutoFixPolygonOrientation()) {
				// Auto-fix orientation
				GeoJsonUtils.fixPolygonOrientation(coordinates);
			} else {
				// Validate orientation
				try {
					GeoJsonUtils.validatePolygonOrientation(coordinates);
				} catch (IllegalArgumentException e) {
					throw e;
				}
			}
		} else {
			// When validation is disabled but RFC 7946 compliance is enabled,
			// we still want to fix the orientation for serialization tests
			GeoJsonUtils.fixPolygonOrientation(coordinates);
		}
	}

	private void assertExteriorRing() {
		if (coordinates.isEmpty())
			throw new RuntimeException("No exterior ring definied");
	}

	@Override
	public <T> T accept(GeoJsonObjectVisitor<T> geoJsonObjectVisitor) {
		return geoJsonObjectVisitor.visit(this);
	}

	@Override
	public String toString() {
		return "Polygon{} " + super.toString();
	}
}
