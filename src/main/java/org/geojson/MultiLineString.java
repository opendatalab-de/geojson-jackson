package org.geojson;

import java.util.List;

public class MultiLineString extends Geometry<List<LngLatAlt>> {

	public MultiLineString() {
	}

	public MultiLineString( List<LngLatAlt> line )
	{ this.add(line) ; }

	@Override
	public double[] calculateBounds()
	{
		if( this.getCoordinates().isEmpty() )
			this.setBbox(null) ;
		else
		{
			double[] box = STARTING_BOUNDS.clone() ;
			for( List<LngLatAlt> shape : this.getCoordinates() )
				accumulateBounds( box, calculateBounds(shape) ) ;
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
		return "MultiLineString{} " + super.toString();
	}
}
