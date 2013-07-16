package org.geojson.jackson;

import java.util.Arrays;
import java.util.List;

import org.geojson.LngLatAlt;

public class MockData {

	public static final List<LngLatAlt> EXTERNAL = Arrays.asList(new LngLatAlt(100, 0), new LngLatAlt(101, 0),
			new LngLatAlt(101, 1), new LngLatAlt(100, 1), new LngLatAlt(100, 0));
	public static final List<LngLatAlt> INTERNAL = Arrays.asList(new LngLatAlt(100.2, 0.2), new LngLatAlt(100.8, 0.2),
			new LngLatAlt(100.8, 0.8), new LngLatAlt(100.2, 0.8), new LngLatAlt(100.2, 0.2));
}
