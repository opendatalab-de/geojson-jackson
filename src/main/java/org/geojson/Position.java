package org.geojson;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.geojson.jackson.PositionDeserializer;
import org.geojson.jackson.PositionSerializer;

import java.io.Serializable;
import java.util.Arrays;

@JsonDeserialize(using = PositionDeserializer.class)
@JsonSerialize(using = PositionSerializer.class)
public class Position implements Serializable {

	public Double longitude;
	public Double latitude;
	public Double altitude;
	public Double[] additionalElements;

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Position)) return false;

		Position position = (Position) o;

		if (longitude != null ? !longitude.equals(position.longitude) : position.longitude != null) return false;
		if (latitude != null ? !latitude.equals(position.latitude) : position.latitude != null) return false;
		if (altitude != null ? !altitude.equals(position.altitude) : position.altitude != null) return false;
		// Probably incorrect - comparing Object[] arrays with Arrays.equals
		return Arrays.equals(additionalElements, position.additionalElements);

	}

	@Override
	public int hashCode() {
		int result = longitude != null ? longitude.hashCode() : 0;
		result = 31 * result + (latitude != null ? latitude.hashCode() : 0);
		result = 31 * result + (altitude != null ? altitude.hashCode() : 0);
		result = 31 * result + Arrays.hashCode(additionalElements);
		return result;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder("Position{");
		sb.append("longitude=").append(longitude);
		sb.append(", latitude=").append(latitude);
		sb.append(", altitude=").append(altitude);
		sb.append(", additionalElements=").append(Arrays.toString(additionalElements));
		sb.append('}');
		return sb.toString();
	}

}
