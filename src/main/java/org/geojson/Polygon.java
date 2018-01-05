package org.geojson;

import java.util.Arrays;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class Polygon extends Geometry<List<LngLatAlt>>
{

	public Polygon() {}

	public Polygon(List<LngLatAlt> polygon)
	{ this.add(polygon) ; }

	public Polygon(LngLatAlt... polygon)
	{ this.add(Arrays.asList(polygon)) ; }

	public void setExteriorRing(List<LngLatAlt> points)
	{
		if( points == null ) return ; // trivially
		coordinates.add( 0, points ) ;
		this.setBbox( accumulateBounds( this.getBbox(),
				calculateBounds(points) )) ;
	}

	@JsonIgnore
	public List<LngLatAlt> getExteriorRing() {
		assertExteriorRing();
		return coordinates.get(0);
	}

	@JsonIgnore
	public List<List<LngLatAlt>> getInteriorRings() {
		assertExteriorRing();
		return coordinates.subList(1, coordinates.size());
	}

	public List<LngLatAlt> getInteriorRing(int index) {
		assertExteriorRing();
		return coordinates.get(1 + index);
	}

	public void addInteriorRing(List<LngLatAlt> points) {
		assertExteriorRing();
		coordinates.add(points);
	}

	public void addInteriorRing(LngLatAlt... points)
	{ this.addInteriorRing( Arrays.asList(points) ) ; }

	private void assertExteriorRing()
	{
		if (coordinates.isEmpty())
			throw new RuntimeException("No exterior ring defined.");
	}

	/**
	 * {@inheritDoc}
	 * Note that the various methods that add interior rings <i>do not</i>
	 * assert that those rings are indeed within the bounds of the current
	 * exterior ring. Thus the bounding box of the exterior ring might or might
	 * not enclose all of the rings that are considered "interior".
	 */
	@Override
	public double[] calculateBounds()
	{
		this.setBbox( calculateBounds( this.getExteriorRing() ) ) ;
		return this.getBbox() ;
	}

	@Override
	public <T> T accept(GeoJsonObjectVisitor<T> geoJsonObjectVisitor) {
		return geoJsonObjectVisitor.visit(this);
	}

	@Override
	public String toString() {
		return "Polygon{} " + super.toString();
	}
}
