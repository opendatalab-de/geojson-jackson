package org.geojson;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;

@JsonAutoDetect(getterVisibility = Visibility.PUBLIC_ONLY, setterVisibility = Visibility.PUBLIC_ONLY)
public abstract class Geometry<T> extends GeoJsonObject {

	protected List<T> coordinates = new ArrayList<T>();

	public Geometry() {
	}

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
