package org.geojson.jackson;

import org.junit.Assert;
import org.junit.Test;

public class PositionSerializerTest {

	@Test
	public void itShouldFastSerialize() throws Exception {
		String value = PositionSerializer.fastDoubleToString(49.43245, 9);
		Assert.assertEquals("49.43245", value);
	}
}