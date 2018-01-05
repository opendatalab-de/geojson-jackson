package org.geojson;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class GeometryCollection extends GeoJsonObject implements Iterable<GeoJsonObject> {

	private List<GeoJsonObject> geometries = new ArrayList<GeoJsonObject>();

	public GeometryCollection() {}

	/**
	 * Constructs the collection with an initial list of geometries.
	 * @param geos the initial list of geometries to be added
	 * @since issue #45
	 */
	public GeometryCollection( List<GeoJsonObject> geos )
	{ this.setGeometries(geos) ; }

	public List<GeoJsonObject> getGeometries() {
		return geometries;
	}

	public void setGeometries( List<GeoJsonObject> geometries )
	{
		if( geometries == null ) this.geometries.clear() ;
		else this.geometries = geometries ;
		this.calculateBounds() ;
	}

	@Override
	public Iterator<GeoJsonObject> iterator() {
		return geometries.iterator();
	}

	public GeometryCollection add( GeoJsonObject geometry )
	{
		if( geometry == null ) return this ; // trivially
		geometries.add(geometry) ;
		this.setBbox( accumulateBounds(
				this.getBbox(), geometry.calculateBounds() ) ) ;
		return this ;
	}

	@Override
	public double[] calculateBounds()
	{
		if( geometries.isEmpty() )
			this.setBbox(null) ;
		else
		{
			double[] box = STARTING_BOUNDS.clone() ;
			for( GeoJsonObject geo : this.getGeometries() )
				GeoJsonObject.accumulateBounds( box, geo.getBbox() ) ;
			this.setBbox(box) ;
		}
		return this.getBbox() ;
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
