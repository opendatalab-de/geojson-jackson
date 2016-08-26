package org.geojson;

import java.util.List;

public class MultiLineString extends Geometry<List<Position>> {

	public MultiLineString() {
	}

	public MultiLineString(List<Position> line) {
		add(line);
	}

	@Override
	public <T> T accept(GeoJsonObjectVisitor<T> geoJsonObjectVisitor) {
		return geoJsonObjectVisitor.visit(this);
	}

	@Override
	public String toString() {
		return "MultiLineString{} " + super.toString();
	}
}
