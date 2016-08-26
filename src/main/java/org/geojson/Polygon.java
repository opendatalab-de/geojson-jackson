package org.geojson;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Arrays;
import java.util.List;

public class Polygon extends Geometry<List<Position>> {

	public Polygon() {
	}

	public Polygon(List<Position> polygon) {
		add(polygon);
	}

	public Polygon(Position... polygon) {
		add(Arrays.asList(polygon));
	}

	void setExteriorRing(List<Position> points) {
		coordinates.add(0, points);
	}

	@JsonIgnore
	List<Position> getExteriorRing() {
		assertExteriorRing();
		return coordinates.get(0);
	}

	@JsonIgnore
	List<List<Position>> getInteriorRings() {
		assertExteriorRing();
		return coordinates.subList(1, coordinates.size());
	}

	List<Position> getInteriorRing(int index) {
		assertExteriorRing();
		return coordinates.get(1 + index);
	}

	public void addInteriorRing(List<Position> points) {
		assertExteriorRing();
		coordinates.add(points);
	}

	public void addInteriorRing(Position... points) {
		assertExteriorRing();
		coordinates.add(Arrays.asList(points));
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
