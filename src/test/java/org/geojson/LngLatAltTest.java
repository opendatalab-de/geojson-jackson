package org.geojson;

import org.junit.Assert;
import org.junit.Test;

public class LngLatAltTest {

	@Test
	public void should_LngLatAlt_equals_without_alt() {
		LngLatAlt first = new LngLatAlt(14.D, 13.D);
		LngLatAlt second = new LngLatAlt(14.D, 13.D);
		Assert.assertEquals(second, first);
	}

	@Test
	public void should_LngLatAlt_equals_with_alt() {
		LngLatAlt first = new LngLatAlt(14.D, 13.D, 15D);
		LngLatAlt second = new LngLatAlt(14.D, 13.D, 15D);
		Assert.assertEquals(second, first);
	}

	@Test
	public void should_not_LngLatAlt_equals_with_alt() {
		LngLatAlt first = new LngLatAlt(14.D, 13.D, 15D);
		LngLatAlt second = new LngLatAlt(14.D, 13.D, 16D);
		Assert.assertNotEquals(second, first);
	}

	@Test
	public void should_not_LngLatAlt_equals_without_alt() {
		LngLatAlt first = new LngLatAlt(14.D, 14.D, 15D);
		LngLatAlt second = new LngLatAlt(14.D, 13.D, 16D);
		Assert.assertNotEquals(second, first);
	}
}