package org.geojson;

import java.util.*;

public class FeatureCollection extends GeoJsonObject implements Iterable<Feature> {

	private List<Feature> features = new ArrayList<Feature>();

	public List<Feature> getFeatures() {
		return features;
	}

	/**
	 * Sets a new list of features for the collection.
	 * Implies a recalculation of the bounding box around the collection.
	 * @param features the new list of features
	 */
	public void setFeatures( List<Feature> features )
	{
		if( features == null ) this.features.clear() ;
		else this.features = features ;
		this.calculateBounds() ;
	}

	@Override
	public double[] calculateBounds()
	{
		if( this.features.isEmpty() )
			this.setBbox(null) ;
		else
		{
			double[] box = STARTING_BOUNDS.clone() ;
			for( Feature f : this.getFeatures() )
				accumulateBounds( box, f.getBbox() ) ;
			this.setBbox(box) ;
		}
		return this.getBbox() ;
	}

	/**
	 * Adds a feature to the collection.
	 * Implies a recalculation of the bounding box around the collection.
	 * @param feature the feature to be added
	 * @return (fluid)
	 */
	public FeatureCollection add( Feature feature )
	{
		if( feature == null ) return this ; // trivially
		features.add(feature) ;
		this.setBbox( accumulateBounds(
				this.getBbox(), feature.calculateBounds() )) ;
		return this ;
	}

	/**
	 * Adds a collection of features to this collection.
	 * Implies a recalculation of the bounding box around the collection.
	 * @param features the features to be added
	 */
	public void addAll( Collection<Feature> features )
	{
		if( features == null || features.isEmpty() ) return ; // trivially
		this.features.addAll(features) ;
		this.calculateBounds() ;
	}

	@Override
	public Iterator<Feature> iterator() {
		return features.iterator();
	}

	@Override
	public <T> T accept(GeoJsonObjectVisitor<T> geoJsonObjectVisitor) {
		return geoJsonObjectVisitor.visit(this);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof FeatureCollection))
			return false;
		FeatureCollection features1 = (FeatureCollection)o;
		return features.equals(features1.features);
	}

	@Override
	public int hashCode() {
		return features.hashCode();
	}

	@Override
	public String toString() {
		return "FeatureCollection{" + "features=" + features + '}';
	}
}
