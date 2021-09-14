package org.geojson;

public abstract class Geometry<T> extends GeoJsonObject {

	protected T coordinates;

	public Geometry() {
	}

	public Geometry(T coordinates) {
		this.coordinates = coordinates;
	}

	public T getCoordinates() {
		return coordinates;
	}

	public void setCoordinates(T coordinates) {
		this.coordinates = coordinates;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof Geometry)) {
			return false;
		}
		if (!super.equals(o)) {
			return false;
		}
		Geometry geometry = (Geometry)o;
		return !(coordinates != null ? !coordinates.equals(geometry.coordinates) : geometry.coordinates != null);
	}

	@Override
	public int hashCode() {
		int result = super.hashCode();
		result = 31 * result + (coordinates != null ? coordinates.hashCode() : 0);
		return result;
	}

	@Override
	public String toString() {
		return "Geometry{" + "coordinates=" + coordinates + "} " + super.toString();
	}
}
