package org.geojson;

import org.junit.Test;

import java.util.Arrays;

import static org.geojson.PositionFactory.create;
import static org.junit.Assert.assertEquals;

public class ToStringTest {

	@Test
	public void itShouldToStringCrs() throws Exception {
		assertEquals("Crs{type='name', properties=null}", new Crs().toString());
	}

	@Test
	public void itShouldToStringFeature() throws Exception {
		assertEquals("Feature{properties=null, geometry=null, id='null'}", new Feature().toString());
	}

	@Test
	public void itShouldToStringFeatureCollection() throws Exception {
		assertEquals("FeatureCollection{features=null}", new FeatureCollection().toString());
	}

	@Test
	public void itShouldToStringPoint() throws Exception {
		Point geometry = PointFactory.create(10, 20);
		assertEquals(
				"Point{coordinates=Position{longitude=10.0, latitude=20.0, altitude=null, additionalElements=null}} GeoJsonObject{}",
				geometry.toString());
	}

	@Test
	public void itShouldToStringPointWithAdditionalElements() {
		Point geometry = PointFactory.create(10, 20, 30, 40D, 50D);
		assertEquals(
				"Point{coordinates=Position{longitude=10.0, latitude=20.0, altitude=30.0, additionalElements=[40.0, 50.0]}} GeoJsonObject{}",
				geometry.toString());
	}

	@Test
	public void itShouldToStringPointWithAdditionalElementsAndIgnoreNulls() {
		Point geometry = PointFactory.create(10, 20, 30, 40D, 50D);
		assertEquals(
				"Point{coordinates=Position{longitude=10.0, latitude=20.0, altitude=30.0, additionalElements=[40.0, 50.0]}} GeoJsonObject{}",
				geometry.toString());
	}

	@Test
	public void itShouldToStringPolygon() throws Exception {
		Polygon geometry = new Polygon(create(10, 20), create(30, 40), create(10, 40),
				create(10, 20));
		assertEquals(
				"Polygon{} Geometry{coordinates=[[Position{longitude=10.0, latitude=20.0, altitude=null, additionalElements=null}, "
						+ "Position{longitude=30.0, latitude=40.0, altitude=null, additionalElements=null}, Position{longitude=10.0, latitude=40.0, altitude=null, additionalElements=null}, "
						+ "Position{longitude=10.0, latitude=20.0, altitude=null, additionalElements=null}]]} GeoJsonObject{}",
				geometry.toString());
	}

	@Test
	public void itShouldToStringMultiPolygon() throws Exception {
		MultiPolygon geometry = new MultiPolygon(new Polygon(create(10, 20), create(30, 40),
				create(10, 40), create(10, 20)));
		geometry.add(new Polygon(create(5, 20), create(30, 40), create(10, 40), create(5,
				20)));
		assertEquals("MultiPolygon{} Geometry{coordinates=[[[Position{longitude=10.0, latitude=20.0, altitude=null, additionalElements=null}, "
				+ "Position{longitude=30.0, latitude=40.0, altitude=null, additionalElements=null}, "
				+ "Position{longitude=10.0, latitude=40.0, altitude=null, additionalElements=null}, "
				+ "Position{longitude=10.0, latitude=20.0, altitude=null, additionalElements=null}]], "
				+ "[[Position{longitude=5.0, latitude=20.0, altitude=null, additionalElements=null}, "
				+ "Position{longitude=30.0, latitude=40.0, altitude=null, additionalElements=null}, "
				+ "Position{longitude=10.0, latitude=40.0, altitude=null, additionalElements=null}, "
						+ "Position{longitude=5.0, latitude=20.0, altitude=null, additionalElements=null}]]]} GeoJsonObject{}",
				geometry.toString());
	}

	@Test
	public void itShouldToStringLineString() throws Exception {
		LineString geometry = new LineString(create(49, 9), create(41, 1));
		assertEquals("LineString{} MultiPoint{} Geometry{coordinates=["
				+ "Position{longitude=49.0, latitude=9.0, altitude=null, additionalElements=null}, "
						+ "Position{longitude=41.0, latitude=1.0, altitude=null, additionalElements=null}]} GeoJsonObject{}",
				geometry.toString());
	}

	@Test
	public void itShouldToStringMultiLineString() throws Exception {
		MultiLineString geometry = new MultiLineString(Arrays.asList(create(49, 9), create(41, 1)));
		geometry.add(Arrays.asList(create(10, 20), create(30, 40)));
		assertEquals("MultiLineString{} Geometry{coordinates=[[Position{longitude=49.0, latitude=9.0, altitude=null, additionalElements=null}, "
				+ "Position{longitude=41.0, latitude=1.0, altitude=null, additionalElements=null}], "
				+ "[Position{longitude=10.0, latitude=20.0, altitude=null, additionalElements=null}, "
						+ "Position{longitude=30.0, latitude=40.0, altitude=null, additionalElements=null}]]} GeoJsonObject{}",
				geometry.toString());
	}
}
