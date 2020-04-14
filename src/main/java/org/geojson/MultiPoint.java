package org.geojson;

import java.util.List;

public class MultiPoint extends Geometry<LngLatAlt> {

	public MultiPoint() {
	}

	public MultiPoint(LngLatAlt... points) {
		super(points);
	}

	@Override
	public <T> T accept(GeoJsonObjectVisitor<T> geoJsonObjectVisitor) {
		return geoJsonObjectVisitor.visit(this);
	}

	@Override
	public String toString() {
		return "MultiPoint{} " + super.toString();
	}

    public String toWKT() {
        if (coordinates != null && coordinates.size() > 0) {
            return "MULTIPOINT("+ toWKT(coordinates) +  ")";
        }
        return "";
    }
}
