package org.geojson;

import java.util.ArrayList;
import java.util.List;

public class MultiPolygon extends Geometry<List<List<LngLatAlt>>> {

	private List<Polygon> polygons = new ArrayList<Polygon>();
	
	public MultiPolygon() {
	}

	public MultiPolygon(Polygon polygon) {
		add(polygon);
	}

	public MultiPolygon add(Polygon polygon) {
		polygons.add(polygon);
		coordinates.add(polygon.getCoordinates());
		return this;
	}
	
	public List<Polygon> getPolygons()
	{
		return this.polygons;
	}
}
