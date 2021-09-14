package org.geojson;

import java.util.Arrays;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class Polygon extends Geometry<List<LngLatAlt>> {

	public Polygon() {
	}

	public Polygon(List<LngLatAlt> polygon) {
		add(polygon);
	}

	public Polygon(LngLatAlt... polygon) {
		add(Arrays.asList(polygon));
	}

	public void setExteriorRing(List<LngLatAlt> points) {
		if (coordinates.isEmpty()) {
			coordinates.add(0, points);
		} else {
			coordinates.set(0, points);
		}
	}

	@JsonIgnore
	public List<LngLatAlt> getExteriorRing() {
		assertExteriorRing();
		return coordinates.get(0);
	}

	@JsonIgnore
	public List<List<LngLatAlt>> getInteriorRings() {
		assertExteriorRing();
		return coordinates.subList(1, coordinates.size());
	}

	public List<LngLatAlt> getInteriorRing(int index) {
		assertExteriorRing();
		return coordinates.get(1 + index);
	}

	public void addInteriorRing(List<LngLatAlt> points) {
		assertExteriorRing();
		coordinates.add(points);
	}

	public void addInteriorRing(LngLatAlt... points) {
		assertExteriorRing();
		coordinates.add(Arrays.asList(points));
	}

	private void assertExteriorRing() {
		if (coordinates.isEmpty())
			throw new RuntimeException("No exterior ring definied");
	}

	@Override
	public <T> T accept(GeoJsonObjectVisitor<T> geoJsonObjectVisitor) {
		return geoJsonObjectVisitor.visit(this);
	}

	@Override
	public String toString() {
		return "Polygon{} " + super.toString();
	}

    public String toWKT() {
        List<LngLatAlt> exteriorRing = this.getExteriorRing();
        if (exteriorRing != null) {
            String wkt = "POLYGON(";
            wkt += "(" + toWKT(exteriorRing) + ")";
            List<List<LngLatAlt>> holes = this.getInteriorRings();
            if (holes != null && holes.size() > 0) {
                for (List<LngLatAlt> hole : holes) {
                    wkt += ",(" + toWKT(hole) + ")";
                }
            }
            wkt += ")";
            return wkt;
        }
        return "";
    }
}
