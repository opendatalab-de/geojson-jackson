package org.geojson;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MultiLineString extends Geometry<List<List<LngLatAlt>>> {

	public MultiLineString() {
		super(new ArrayList<List<LngLatAlt>>());
	}

	public MultiLineString(List<LngLatAlt> line) {
		super(new ArrayList<List<LngLatAlt>>(Collections.singletonList(line)));
	}

	public MultiLineString add(List<LngLatAlt> line) {
		coordinates.add(line);
		return this;
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
