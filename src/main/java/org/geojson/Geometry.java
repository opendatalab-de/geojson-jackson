package org.geojson;

import java.util.ArrayList;
import java.util.List;

public abstract class Geometry<T> extends GeoJsonObject {

	protected List<T> coordinates = new ArrayList<T>();

	public Geometry() {
	}

	@SafeVarargs
	public Geometry(T... elements) {
		for (T coordinate : elements) {
			coordinates.add(coordinate);
		}
	}

	public Geometry<T> add(T elements) {
		coordinates.add(elements);
		return this;
	}

	public List<T> getCoordinates() {
		return coordinates;
	}

	public void setCoordinates(List<T> coordinates) {
		this.coordinates = coordinates;
	}
}
