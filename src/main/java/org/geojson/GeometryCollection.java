package org.geojson;

import java.util.ArrayList;
import java.util.List;

public class GeometryCollection extends GeoJsonObject {

	public List<GeoJsonObject> geometries;

	public GeometryCollection add(GeoJsonObject geometry) {
		if (geometries == null) geometries = new ArrayList<GeoJsonObject>();
		geometries.add(geometry);
		return this;
	}

	@Override
	public <T> T accept(GeoJsonObjectVisitor<T> geoJsonObjectVisitor) {
		return geoJsonObjectVisitor.visit(this);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof GeometryCollection)) return false;
		if (!super.equals(o)) return false;

		GeometryCollection that = (GeometryCollection) o;

		return geometries != null ? geometries.equals(that.geometries) : that.geometries == null;

	}

	@Override
	public int hashCode() {
		int result = super.hashCode();
		result = 31 * result + (geometries != null ? geometries.hashCode() : 0);
		return result;
	}

	@Override
	public String toString() {
		return "GeometryCollection{" + "geometries=" + geometries + "} " + super.toString();
	}
}
