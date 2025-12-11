package org.geojson;

import java.util.List;

public class MultiPolygon extends Geometry<List<List<LngLatAlt>>> {

	public MultiPolygon() {
	}

	public MultiPolygon(Polygon polygon) {
		add(polygon);
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

    public String toWKT() {
        if (coordinates != null && coordinates.size() > 0) {
            String wkt = "MULTIPOLYGON(";
            boolean firstPolygon = true;
            for (List<List<LngLatAlt>> polygon : coordinates) {
                if (firstPolygon) {
                    firstPolygon = false;
                    wkt += "(";
                } else {
                    wkt += ",(";
                }
                boolean firstRing = true;
                for (List<LngLatAlt> ring : polygon) {
                    if (firstRing) {
                        firstRing = false;
                    } else {
                        wkt += ",";
                    }
                    wkt += "("+ toWKT(ring) + ")";
                }
                wkt += ")";
            }
            wkt += ")";
            return wkt;
        }
        return "";
    }
}
