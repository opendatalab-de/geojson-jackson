package org.geojson;

import java.util.*;

public abstract class Geometry<T> extends GeoJsonObject {

	protected List<T> coordinates = new ArrayList<T>();

	public Geometry() {	}

	public Geometry( List<T> coords )
	{ this.setCoordinates(coords) ; }

	public Geometry( T... elements )
	{ this.setCoordinates( Arrays.asList(elements) ) ; }

	/**
	 * Adds some set of coordinates to the geometry.
	 * Implies recalculation of the feature's bounding box.
	 * @param elements the elements to be added
	 * @return (fluid)
	 */
	public Geometry<T> add( T elements )
	{
		if( elements == null ) return this ; // trivially
		coordinates.add(elements) ;
		if( GeoJsonObject.class.isAssignableFrom( elements.getClass() ) )
		{ // Recalculate more cheaply by simply adding this element.
			this.setBbox( accumulateBounds( this.getBbox(),
					((GeoJsonObject)(elements)).getBbox() )) ;
		}
		else this.calculateBounds() ;
		return this;
	}

	public List<T> getCoordinates()
	{ return coordinates ; }

	/**
	 * Sets the list of coordinates that make up the geometry.
 	 * Implies a recalculation of the bounding box around the geometry.
	 * @param coordinates the new list of coordinates
	 */
	public void setCoordinates( List<T> coordinates )
	{
		if( coordinates == null ) this.coordinates.clear() ;
		else this.coordinates = coordinates ;
		this.calculateBounds() ;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof Geometry)) {
			return false;
		}
		if (!super.equals(o)) {
			return false;
		}
		Geometry geometry = (Geometry)o;
		return !(coordinates != null ? !coordinates.equals(geometry.coordinates) : geometry.coordinates != null);
	}

	@Override
	public int hashCode() {
		int result = super.hashCode();
		result = 31 * result + (coordinates != null ? coordinates.hashCode() : 0);
		return result;
	}

	@Override
	public String toString() {
		return "Geometry{" + "coordinates=" + coordinates + "} " + super.toString();
	}
}
