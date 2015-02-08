package org.geojson;

public class Point extends GeoJsonObject {

	private LngLatAlt coordinates;

	public Point() {
	}

	public Point(LngLatAlt coordinates) {
		this.coordinates = coordinates;
	}

	public Point(double longitude, double latitude) {
		coordinates = new LngLatAlt(longitude, latitude);
	}

	public Point(double longitude, double latitude, double altitude) {
		coordinates = new LngLatAlt(longitude, latitude, altitude);
	}

	public LngLatAlt getCoordinates() {
		return coordinates;
	}

	public void setCoordinates(LngLatAlt coordinates) {
		this.coordinates = coordinates;
	}

	@Override
	public <T> T accept(GeoJsonObjectVisitor<T> geoJsonObjectVisitor) {
		return geoJsonObjectVisitor.visit(this);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof Point)) {
			return false;
		}
		if (!super.equals(o)) {
			return false;
		}
		Point point = (Point)o;
		return !(coordinates != null ? !coordinates.equals(point.coordinates) : point.coordinates != null);
	}

	@Override
	public int hashCode() {
		int result = super.hashCode();
		result = 31 * result + (coordinates != null ? coordinates.hashCode() : 0);
		return result;
	}

	@Override
	public String toString() {
		return "Point{" + "coordinates=" + coordinates + "} " + super.toString();
	}
}
