package org.geojson;

import java.util.List;

public class MultiLineString extends Geometry<List<LngLatAlt>> {

	public MultiLineString() {
	}

	public MultiLineString(List<LngLatAlt> line) {
		add(line);
	}

	@Override
	public double[] calculateBounds()
	{
		double[] box = STARTING_BOUNDS.clone() ;
		for( List<LngLatAlt> shape : this.getCoordinates() )
			accumulateBounds( box, calculateBounds(shape) ) ;
		this.setBbox(box) ;
		return this.getBbox() ;
	}

	@Override
	public <T> T accept(GeoJsonObjectVisitor<T> geoJsonObjectVisitor) {
		return geoJsonObjectVisitor.visit(this);
	}

	@Override
	public String toString() {
		return "MultiLineString{} " + super.toString();
	}
}
