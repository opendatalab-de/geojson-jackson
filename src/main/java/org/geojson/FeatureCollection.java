package org.geojson;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class FeatureCollection extends GeoJsonObject {

	public List<Feature> features;

	public FeatureCollection add(Feature feature) {
		initFeatures();
		features.add(feature);
		return this;
	}

	private void initFeatures() {
		if(features == null) features = new ArrayList<Feature>();
	}

	public void addAll(Collection<Feature> features) {
		initFeatures();
		this.features.addAll(features);
	}

	@Override
	public <T> T accept(GeoJsonObjectVisitor<T> geoJsonObjectVisitor) {
		return geoJsonObjectVisitor.visit(this);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof FeatureCollection)) return false;
		if (!super.equals(o)) return false;

		FeatureCollection that = (FeatureCollection) o;

		return features != null ? features.equals(that.features) : that.features == null;

	}

	@Override
	public int hashCode() {
		int result = super.hashCode();
		result = 31 * result + (features != null ? features.hashCode() : 0);
		return result;
	}

	@Override
	public String toString() {
		return "FeatureCollection{" + "features=" + features + '}';
	}
}
