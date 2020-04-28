package org.geojson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class MultiPoint extends Geometry<List<LngLatAlt>> {

	public MultiPoint() {
		this(Collections.<LngLatAlt>emptyList());
	}

	public MultiPoint(LngLatAlt... points) {
		this(Arrays.asList(points));
	}

	private MultiPoint(List<LngLatAlt> points) {
		super(new ArrayList<LngLatAlt>(points));
	}

	public MultiPoint add(LngLatAlt point) {
		coordinates.add(point);
		return this;
	}

		@Override
	public <T> T accept(GeoJsonObjectVisitor<T> geoJsonObjectVisitor) {
		return geoJsonObjectVisitor.visit(this);
	}

	@Override
	public String toString() {
		return "MultiPoint{} " + super.toString();
	}
}
