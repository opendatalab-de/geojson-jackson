package org.geojson;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Test;

public class ToStringTest {

	@Test
	public void itShouldToStringCrs() throws Exception {
		assertEquals("Crs{type='name', properties={}}", new Crs().toString());
	}

	@Test
	public void itShouldToStringFeature() throws Exception {
		assertEquals("Feature{geometry=null, id='null'}", new Feature().toString());
	}

	@Test
	public void itShouldToStringFeatureCollection() throws Exception {
		assertEquals("FeatureCollection{features=[]}", new FeatureCollection().toString());
	}

	@Test
	public void itShouldToStringPoint() throws Exception {
		Point geometry = new Point(10, 20);
		assertEquals(
				"Point{coordinates=LngLatAlt{longitude=10.0, latitude=20.0, altitude=null}} GeoJsonObject{properties={}}",
				geometry.toString());
	}

	@Test
	public void itShouldToStringPolygon() throws Exception {
		Polygon geometry = new Polygon(new LngLatAlt(10, 20), new LngLatAlt(30, 40), new LngLatAlt(10, 40),
				new LngLatAlt(10, 20));
		assertEquals(
				"Polygon{} Geometry{coordinates=[[LngLatAlt{longitude=10.0, latitude=20.0, altitude=null}, "
						+ "LngLatAlt{longitude=30.0, latitude=40.0, altitude=null}, LngLatAlt{longitude=10.0, latitude=40.0, altitude=null}, "
						+ "LngLatAlt{longitude=10.0, latitude=20.0, altitude=null}]]} GeoJsonObject{properties={}}",
				geometry.toString());
	}

	@Test
	public void itShouldToStringMultiPolygon() throws Exception {
		MultiPolygon geometry = new MultiPolygon(new Polygon(new LngLatAlt(10, 20), new LngLatAlt(30, 40),
				new LngLatAlt(10, 40), new LngLatAlt(10, 20)));
		geometry.add(new Polygon(new LngLatAlt(5, 20), new LngLatAlt(30, 40), new LngLatAlt(10, 40), new LngLatAlt(5,
				20)));
		assertEquals("MultiPolygon{} Geometry{coordinates=[[[LngLatAlt{longitude=10.0, latitude=20.0, altitude=null}, "
				+ "LngLatAlt{longitude=30.0, latitude=40.0, altitude=null}, "
				+ "LngLatAlt{longitude=10.0, latitude=40.0, altitude=null}, "
				+ "LngLatAlt{longitude=10.0, latitude=20.0, altitude=null}]], "
				+ "[[LngLatAlt{longitude=5.0, latitude=20.0, altitude=null}, "
				+ "LngLatAlt{longitude=30.0, latitude=40.0, altitude=null}, "
				+ "LngLatAlt{longitude=10.0, latitude=40.0, altitude=null}, "
				+ "LngLatAlt{longitude=5.0, latitude=20.0, altitude=null}]]]} GeoJsonObject{properties={}}",
				geometry.toString());
	}

	@Test
	public void itShouldToStringLineString() throws Exception {
		LineString geometry = new LineString(new LngLatAlt(49, 9), new LngLatAlt(41, 1));
		assertEquals("LineString{} MultiPoint{} Geometry{coordinates=["
				+ "LngLatAlt{longitude=49.0, latitude=9.0, altitude=null}, "
				+ "LngLatAlt{longitude=41.0, latitude=1.0, altitude=null}]} GeoJsonObject{properties={}}",
				geometry.toString());
	}

	@Test
	public void itShouldToStringMultiLineString() throws Exception {
		MultiLineString geometry = new MultiLineString(Arrays.asList(new LngLatAlt(49, 9), new LngLatAlt(41, 1)));
		geometry.add(Arrays.asList(new LngLatAlt(10, 20), new LngLatAlt(30, 40)));
		assertEquals("MultiLineString{} Geometry{coordinates=[[LngLatAlt{longitude=49.0, latitude=9.0, altitude=null}, "
				+ "LngLatAlt{longitude=41.0, latitude=1.0, altitude=null}], "
				+ "[LngLatAlt{longitude=10.0, latitude=20.0, altitude=null}, "
				+ "LngLatAlt{longitude=30.0, latitude=40.0, altitude=null}]]} GeoJsonObject{properties={}}",
				geometry.toString());
	}
}
