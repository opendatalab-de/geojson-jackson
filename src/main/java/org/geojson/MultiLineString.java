package org.geojson;

import java.util.List;

public class MultiLineString extends Geometry<List<LngLatAlt>> {

	public MultiLineString() {
	}

	public MultiLineString(List<LngLatAlt> line) {
		add(line);
	}

	@Override
	public String toString() {
		return "MultiLineString{} " + super.toString();
	}
}
