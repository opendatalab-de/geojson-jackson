package org.geojson;

import java.util.List;

public class MultiPolygon extends Geometry<List<List<LngLatAlt>>> {

	public MultiPolygon() {
	}

	public MultiPolygon(Polygon polygon) {
		add(polygon);
	}

	public MultiPolygon add(Polygon polygon)
	{
		if( polygon == null ) return this ; // trivially
		coordinates.add(polygon.getCoordinates());
		this.setBbox( accumulateBounds( this.getBbox(), polygon.getBbox() )) ;
		return this;
	}

	@Override
	public double[] calculateBounds()
	{
		if( this.coordinates.isEmpty() )
			this.setBbox(null) ;
		else
		{
			double[] box = STARTING_BOUNDS.clone() ;
			for( List<List<LngLatAlt>> polygroup : this.getCoordinates() )
			{
				for( List<LngLatAlt> poly : polygroup )
					accumulateBounds( box, calculateBounds(poly) ) ;
			}
			this.setBbox(box) ;
		}
		return this.getBbox() ;
	}

	@Override
	public <T> T accept(GeoJsonObjectVisitor<T> geoJsonObjectVisitor) {
		return geoJsonObjectVisitor.visit(this);
	}

	@Override
	public String toString() {
		return "MultiPolygon{} " + super.toString();
	}
}
