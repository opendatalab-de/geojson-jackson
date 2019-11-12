package org.geojson;

public class Point extends GeoJsonObject {

	private LngLatAlt coordinates = null ;

	public Point() {}

	public Point(LngLatAlt coordinates)
	{ this.setCoordinates(coordinates) ; }

	public Point(double longitude, double latitude)
	{ this.setCoordinates( new LngLatAlt(longitude, latitude) ) ; }

	public Point(double longitude, double latitude, double altitude)
	{ this.setCoordinates( new LngLatAlt(longitude, latitude, altitude) ) ; }

	public Point(double longitude, double latitude, double altitude, double... additionalElements)
	{
		this.setCoordinates(
			new LngLatAlt( longitude, latitude, altitude, additionalElements ));
	}

	@Override
	public double[] calculateBounds()
	{
		if( this.coordinates == null )
			this.setBbox(null) ;
		else
		{
			this.setBbox( new double[]
			{
				coordinates.getLongitude(),
				coordinates.getLatitude(),
				coordinates.getLongitude(),
				coordinates.getLatitude()
			});
		}
		return this.getBbox() ;
	}

	public LngLatAlt getCoordinates() {
		return coordinates;
	}

	public void setCoordinates( LngLatAlt coordinates )
	{
		this.coordinates = coordinates ;
		this.calculateBounds() ;
	}

	@Override
	public <T> T accept(GeoJsonObjectVisitor<T> geoJsonObjectVisitor) {
		return geoJsonObjectVisitor.visit(this);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof Point)) {
			return false;
		}
		if (!super.equals(o)) {
			return false;
		}
		Point point = (Point)o;
		return !(coordinates != null ? !coordinates.equals(point.coordinates) : point.coordinates != null);
	}

	@Override
	public int hashCode() {
		int result = super.hashCode();
		result = 31 * result + (coordinates != null ? coordinates.hashCode() : 0);
		return result;
	}

	@Override
	public String toString() {
		return "Point{" + "coordinates=" + coordinates + "} " + super.toString();
	}
}
