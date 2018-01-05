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
		coordinates.add(polygon.getCoordinates());
		this.setBbox( accumulateBounds(
				this.getBbox(), polygon.calculateBounds() )) ;
		return this;
	}

	@Override
	public double[] calculateBounds()
	{
		double[] box = STARTING_BOUNDS.clone() ;
		for( List<List<LngLatAlt>> polygroup : this.getCoordinates() )
		{
			for( List<LngLatAlt> poly : polygroup )
				accumulateBounds( box, calculateBounds(poly) ) ;
		}
		this.setBbox(box) ;
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
