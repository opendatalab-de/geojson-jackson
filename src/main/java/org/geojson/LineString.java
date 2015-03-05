package org.geojson;

import java.util.List;

public class LineString extends MultiPoint {

	public LineString() {
	}

	public LineString(LngLatAlt... points) {
		super(points);
	}

	@Override
	public <T> T accept(GeoJsonObjectVisitor<T> geoJsonObjectVisitor) {
		return geoJsonObjectVisitor.visit(this);
	}

	@Override
	public String toString() {
		return "LineString{} " + super.toString();
	}

    public String toWKT() {
        if (coordinates != null && coordinates.size() > 0) {
            return "LINESTRING(" + toWKT(coordinates) + ")";
        }
        return null;
    }
}
