package org.geojson;


public class MultiPoint extends Geometry<LngLatAlt> {

	public MultiPoint() {
	}

	public MultiPoint(LngLatAlt... points) {
		super(points);
	}
}
