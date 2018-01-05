package org.geojson;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class GeometryCollection extends GeoJsonObject implements Iterable<GeoJsonObject> {

	private List<GeoJsonObject> geometries = new ArrayList<GeoJsonObject>();

	/**
	 * {@inheritDoc}
	 * Note that this method will cause all subsidiary objects to update their
	 * bounding boxes as well.
	 */
	@Override
	public double[] calculateBounds()
	{
		double[] box = STARTING_BOUNDS.clone() ;
		for( GeoJsonObject geo : this.getGeometries() )
			GeoJsonObject.accumulateBounds( box, geo.calculateBounds() ) ;
		this.setBbox(box) ;
		return this.getBbox() ;
	}

	public List<GeoJsonObject> getGeometries() {
		return geometries;
	}

	public void setGeometries( List<GeoJsonObject> geometries )
	{
		this.geometries = geometries ;
		this.calculateBounds() ;
	}

	@Override
	public Iterator<GeoJsonObject> iterator() {
		return geometries.iterator();
	}

	public GeometryCollection add( GeoJsonObject geometry )
	{
		geometries.add(geometry) ;
		this.setBbox( accumulateBounds(
				this.getBbox(), geometry.calculateBounds() ) ) ;
		return this ;
	}

	@Override
	public <T> T accept(GeoJsonObjectVisitor<T> geoJsonObjectVisitor) {
		return geoJsonObjectVisitor.visit(this);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof GeometryCollection))
			return false;
		if (!super.equals(o))
			return false;
		GeometryCollection that = (GeometryCollection)o;
		return !(geometries != null ? !geometries.equals(that.geometries) : that.geometries != null);
	}

	@Override
	public int hashCode() {
		int result = super.hashCode();
		result = 31 * result + (geometries != null ? geometries.hashCode() : 0);
		return result;
	}

	@Override
	public String toString() {
		return "GeometryCollection{" + "geometries=" + geometries + "} " + super.toString();
	}
}
