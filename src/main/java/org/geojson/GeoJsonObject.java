package org.geojson;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

@JsonTypeInfo(property = "type", use = Id.NAME)
@JsonSubTypes({ @Type(Feature.class), @Type(Polygon.class), @Type(MultiPolygon.class), @Type(FeatureCollection.class),
		@Type(Point.class), @Type(MultiPoint.class), @Type(MultiLineString.class), @Type(LineString.class),
                @Type(GeometryCollection.class) })
@JsonInclude(Include.NON_NULL)
public abstract class GeoJsonObject implements Serializable
{
	private Crs crs;
	private double[] bbox;

	/**
	 * Starting bounds to guarantee that the various {@code calculateBounds}
	 * methods will work out correctly.
	 */
	protected static final double[] STARTING_BOUNDS =
		{
			Double.MAX_VALUE, Double.MAX_VALUE,
			Double.MIN_VALUE, Double.MIN_VALUE
		} ;

	/**
	 * Calculates the bounding box around a list of points.
	 * @param points a list of points that compose a polygon
	 * @return a bounding box
	 */
	public static double[] calculateBounds( List<LngLatAlt> points )
	{
		double[] box = STARTING_BOUNDS.clone() ;
		for( LngLatAlt point : points )
		{
			double longitude = point.getLongitude() ;
			double latitude = point.getLatitude() ;
			if( Double.compare( longitude, box[0] ) < 0 )
				box[0] = longitude ;
			if( Double.compare( latitude, box[1] ) < 0 )
				box[1] = latitude ;
			if( Double.compare( longitude, box[2] ) > 0 )
				box[2] = longitude ;
			if( Double.compare( latitude, box[3] ) > 0 )
				box[3] = latitude ;
		}
		return box ;
	}

	/**
	 * Given a "current" bounding box, recalculates that box to account for an
	 * additional data point.
	 * @param currentBox the current bounding box
	 * @param newData a new data point
	 * @return the updated bounding box
	 */
	@SuppressWarnings("UnusedReturnValue")
	public static double[] accumulateBounds( double[] currentBox, double[] newData )
	{
		if( currentBox == null ) currentBox = STARTING_BOUNDS.clone() ;
		if( Double.compare( newData[0], currentBox[0] ) < 0 )
			currentBox[0] = newData[0] ;
		if( Double.compare( newData[1], currentBox[1] ) < 0 )
			currentBox[1] = newData[1] ;
		if( Double.compare( newData[2], currentBox[2] ) > 0 )
			currentBox[2] = newData[2] ;
		if( Double.compare( newData[3], currentBox[3] ) > 0 )
			currentBox[3] = newData[3] ;
		return currentBox ;
	}

	public Crs getCrs() {
		return crs;
	}

	public void setCrs(Crs crs) {
		this.crs = crs;
	}

	@JsonIgnore
	public double[] getBbox() {
		return bbox;
	}

	@JsonIgnore
	public void setBbox(double[] bbox) {
		this.bbox = bbox;
	}

	/**
	 * Calculates a bounding box around the object, and writes its coordinates
	 * back to the internal bounding box.
	 * @return the new bounding box (as from {@link #getBbox}.
	 * @since issue #45 (zerobandwidth-net issue #1)
	 */
	public abstract double[] calculateBounds() ;

	public abstract <T> T accept(GeoJsonObjectVisitor<T> geoJsonObjectVisitor);

	@Override public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		GeoJsonObject that = (GeoJsonObject)o;
		if (crs != null ? !crs.equals(that.crs) : that.crs != null)
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
