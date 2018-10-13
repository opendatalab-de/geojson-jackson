package org.geojson;

import static org.geojson.ArrayUtils.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class GeometryCollection extends GeoJsonObject implements Iterable<GeoJsonObject> {
	private static final String[] RESERVED_KEYS = append(GeoJsonObject.RESERVED_KEYS, "geometries");

	@Override
	protected List<String> getReservedKeys() {
		return Arrays.asList(RESERVED_KEYS);
	}

	private List<GeoJsonObject> geometries = new ArrayList<GeoJsonObject>();

	public List<GeoJsonObject> getGeometries() {
		return geometries;
	}

	public void setGeometries(List<GeoJsonObject> geometries) {
		this.geometries = geometries;
	}

	@Override
	public Iterator<GeoJsonObject> iterator() {
		return geometries.iterator();
	}

	public GeometryCollection add(GeoJsonObject geometry) {
		geometries.add(geometry);
		return this;
	}

	@Override
	public <T> T accept(GeoJsonObjectVisitor<T> geoJsonObjectVisitor) {
		return geoJsonObjectVisitor.visit(this);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof GeometryCollection))
			return false;
		if (!super.equals(o))
			return false;
		GeometryCollection that = (GeometryCollection)o;
		return !(geometries != null ? !geometries.equals(that.geometries) : that.geometries != null);
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
