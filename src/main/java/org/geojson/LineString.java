package org.geojson;


public class LineString extends MultiPoint {

	public LineString() {
	}

	public LineString(LngLatAlt... points) {
		super(points);
	}

	@Override
	public String toString() {
		return "LineString{} " + super.toString();
	}
}
