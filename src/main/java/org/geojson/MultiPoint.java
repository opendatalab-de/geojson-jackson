package org.geojson;

public class MultiPoint extends Geometry<LngLatAlt> {

	public MultiPoint() {
	}

	public MultiPoint(LngLatAlt... points) {
		super(points);
	}

	@Override
	public double[] calculateBounds()
	{
		this.setBbox( calculateBounds( this.getCoordinates() )) ;
		return this.getBbox() ;
	}

	@Override
	public <T> T accept(GeoJsonObjectVisitor<T> geoJsonObjectVisitor) {
		return geoJsonObjectVisitor.visit(this);
	}

	@Override
	public String toString() {
		return "MultiPoint{} " + super.toString();
	}
}
