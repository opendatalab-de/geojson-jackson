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
	public String toString() {
		return "Point{" +
			"coordinates=" + coordinates +
			"} " + super.toString();
	}
}