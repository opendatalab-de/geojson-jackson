package org.geojson;

import java.util.List;

public class MultiPolygon extends Geometry<List<List<LngLatAlt>>> {

	public MultiPolygon() {
	}

	public MultiPolygon(Polygon polygon) {
		add(polygon);
	}

	public MultiPolygon add(Polygon polygon) {
		coordinates.add(polygon.getCoordinates());
		return this;
	}

	@Override
	public double[] calculateBounds()
	{
		double[] box = { 0.0d, 0.0d, 0.0d, 0.0d } ;
		for( List<List<LngLatAlt>> polygroup : this.getCoordinates() )
		{
			for( List<LngLatAlt> poly : polygroup )
			{
				double[] polybox = GeoJsonObject.calculateBounds( poly ) ;
				GeoJsonObject.accumulateBounds( box, polybox ) ;
			}
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
