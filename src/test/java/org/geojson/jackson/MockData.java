package org.geojson.jackson;

import org.geojson.Position;

import java.util.Arrays;
import java.util.List;

import static org.geojson.PositionFactory.create;

public class MockData {

	public static final List<Position> EXTERNAL = Arrays.asList(create(100, 0), create(101, 0),
			create(101, 1), create(100, 1), create(100, 0));
	public static final List<Position> INTERNAL = Arrays.asList(create(100.2, 0.2), create(100.8, 0.2),
			create(100.8, 0.8), create(100.2, 0.8), create(100.2, 0.2));
}
