package org.geojson;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Polygon extends Geometry<List<List<LngLatAlt>>> {

	public Polygon() {
		super(new ArrayList<List<LngLatAlt>>());
	}

	public Polygon(List<LngLatAlt> polygon) {
		super(new ArrayList<List<LngLatAlt>>(Collections.singletonList(polygon)));
	}

	public Polygon(LngLatAlt... polygon) {
		this(Arrays.asList(polygon));
	}

	public void setExteriorRing(List<LngLatAlt> points) {
		if (coordinates.isEmpty()) {
			coordinates.add(0, points);
		} else {
			coordinates.set(0, points);
		}
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
	}

	public void addInteriorRing(LngLatAlt... points) {
		assertExteriorRing();
		coordinates.add(Arrays.asList(points));
	}

	private void assertExteriorRing() {
		if (coordinates.isEmpty()) {
			throw new RuntimeException("No exterior ring definied");
		}
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
