package org.geojson;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;

import java.io.Serializable;
import java.util.Arrays;

@JsonTypeInfo(property = "type", use = Id.NAME)
@JsonSubTypes({
	@Type(Feature.class),
		@Type(Polygon.class),
		@Type(MultiPolygon.class),
		@Type(FeatureCollection.class),
		@Type(Point.class),
		@Type(MultiPoint.class),
		@Type(MultiLineString.class),
		@Type(LineString.class),
		@Type(GeometryCollection.class)
})
@JsonInclude(Include.NON_NULL)
public abstract class GeoJsonObject implements Serializable {

	public Crs crs;
	public double[] bbox;

	public abstract <T> T accept(GeoJsonObjectVisitor<T> geoJsonObjectVisitor);

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof GeoJsonObject)) return false;

		GeoJsonObject that = (GeoJsonObject) o;

		if (crs != null ? !crs.equals(that.crs) : that.crs != null) return false;
		return Arrays.equals(bbox, that.bbox);

	}

	@Override
	public int hashCode() {
		int result = crs != null ? crs.hashCode() : 0;
		result = 31 * result + Arrays.hashCode(bbox);
		return result;
	}

	@Override
	public String toString() {
		return "GeoJsonObject{}";
	}
}
