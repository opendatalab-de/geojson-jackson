package org.geojson;

import java.util.List;

public class MultiLineString extends Geometry<List<LngLatAlt>> {

	public MultiLineString() {
	}

	public MultiLineString(List<LngLatAlt> line) {
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

    public String toWKT() {
        if (coordinates != null && coordinates.size() > 0) {
            String wkt = "MULTILINESTRING(";
            boolean first = true;
            for (List<LngLatAlt> line : coordinates) {
                if (first) {
                    first = false;
                } else {
                    wkt += ",";
                }
                wkt += "(" + toWKT(line) + ")";
            }
            wkt += ")";
            return wkt;
        }
        return null;
    }
}
