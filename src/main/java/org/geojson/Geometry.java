package org.geojson;

import java.util.ArrayList;
import java.util.List;

public abstract class Geometry<T> extends GeoJsonObject {

	public List<T> coordinates;

	public Geometry() { }

	public Geometry(T... elements) {
		for (T coordinate : elements) {
			add(coordinate);
		}
	}

	public Geometry<T> add(T elements) {
		if (coordinates == null) coordinates = new ArrayList<T>();
		coordinates.add(elements);
		return this;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Geometry)) return false;
		if (!super.equals(o)) return false;

		Geometry<?> geometry = (Geometry<?>) o;

		return coordinates != null ? coordinates.equals(geometry.coordinates) : geometry.coordinates == null;

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
