package org.geojson;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MultiPolygon extends Geometry<List<List<List<LngLatAlt>>>> {

	public MultiPolygon() {
		this(Collections.<List<List<LngLatAlt>>>emptyList());
	}

	public MultiPolygon(Polygon polygon) {
		this(Collections.singletonList(polygon.getCoordinates()));
	}

	private MultiPolygon(List<List<List<LngLatAlt>>> elements) {
		super(new ArrayList<List<List<LngLatAlt>>>(elements));
	}

	public MultiPolygon add(Polygon polygon) {
		coordinates.add(polygon.getCoordinates());
		return this;
	}

	@Override
	public <T> T accept(GeoJsonObjectVisitor<T> geoJsonObjectVisitor) {
		return geoJsonObjectVisitor.visit(this);
	}

	@Override
	public String toString() {
		return "MultiPolygon{} " + super.toString();
	}
}
