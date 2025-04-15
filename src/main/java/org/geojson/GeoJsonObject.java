package org.geojson;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;

/**
 * Base class for all GeoJSON objects.
 * <p>
 * This library supports both the 2008 GeoJSON specification and RFC 7946 (published in 2016).
 * <p>
 * According to RFC 7946, all GeoJSON objects use WGS 84 as the coordinate reference system.
 * The "crs" member is deprecated in RFC 7946 but supported for backward compatibility.
 */
@JsonTypeInfo(property = "type", use = Id.NAME)
@JsonSubTypes({ @Type(Feature.class), @Type(Polygon.class), @Type(MultiPolygon.class), @Type(FeatureCollection.class),
		@Type(Point.class), @Type(MultiPoint.class), @Type(MultiLineString.class), @Type(LineString.class),
                @Type(GeometryCollection.class) })
@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class GeoJsonObject implements Serializable {

	/**
	 * The coordinate reference system.
	 *
	 * @deprecated The "crs" member is deprecated in RFC 7946. All GeoJSON coordinates should use WGS 84.
	 */
	@Deprecated
	private Crs crs;

	/**
	 * The bounding box of the object.
	 * The value of the bbox member is an array of length 2*n where n is the number of dimensions.
	 * For 2D coordinates, the bbox is [west, south, east, north].
	 */
	private double[] bbox;

	/**
	 * Get the coordinate reference system.
	 *
	 * @return The CRS
	 * @deprecated The "crs" member is deprecated in RFC 7946. All GeoJSON coordinates should use WGS 84.
	 */
	@Deprecated
	public Crs getCrs() {
		if (crs != null && GeoJsonConfig.getInstance().isRfc7946Compliance() &&
				GeoJsonConfig.getInstance().isWarnOnCrsUse()) {
			System.err.println("Warning: The 'crs' member is deprecated in RFC 7946. All GeoJSON coordinates should use WGS 84.");
		}
		return crs;
	}

	/**
	 * Set the coordinate reference system.
	 * @param crs The CRS to set
	 * @deprecated The "crs" member is deprecated in RFC 7946. All GeoJSON coordinates should use WGS 84.
	 */
	@Deprecated
	public void setCrs(Crs crs) {
		if (crs != null && GeoJsonConfig.getInstance().isRfc7946Compliance() &&
				GeoJsonConfig.getInstance().isWarnOnCrsUse()) {
			System.err.println("Warning: The 'crs' member is deprecated in RFC 7946. All GeoJSON coordinates should use WGS 84.");
		}
		this.crs = crs;
	}

	/**
	 * Get the bounding box.
	 * @return The bounding box
	 */
	public double[] getBbox() {
		return bbox;
	}

	/**
	 * Set the bounding box.
	 * @param bbox The bounding box to set
	 */
	public void setBbox(double[] bbox) {
		this.bbox = bbox;
	}

	public abstract <T> T accept(GeoJsonObjectVisitor<T> geoJsonObjectVisitor);

	@Override public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		GeoJsonObject that = (GeoJsonObject)o;
		if (!Objects.equals(crs, that.crs))
			return false;
		return Arrays.equals(bbox, that.bbox);
	}

	@Override public int hashCode() {
		int result = crs != null ? crs.hashCode() : 0;
		result = 31 * result + (bbox != null ? Arrays.hashCode(bbox) : 0);
		return result;
	}

	@Override
	public String toString() {
		return "GeoJsonObject{}";
	}
}
