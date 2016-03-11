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

	@Test
	public void should_LngLatAlt_equals_with_additional_elements() {
		LngLatAlt first = new LngLatAlt(14.D, 14.D, 15D, 16D, 17D);
		LngLatAlt second = new LngLatAlt(14.D, 14.D, 15D, 16D, 17D);
		Assert.assertEquals(second, first);
		Assert.assertEquals(second.hashCode(), first.hashCode());
	}

	@Test
	public void should_LngLatAlt_equals_with_additional_elements_and_null() {
		LngLatAlt first = new LngLatAlt(14.D, 14.D, 15D, 16D, 17D);
		LngLatAlt second = new LngLatAlt(14.D, 14.D, 15D, 16D, 17D);
		Assert.assertEquals(second, first);
		Assert.assertEquals(second.hashCode(), first.hashCode());
	}

	@Test
	public void should_not_LngLatAlt_equals_without_additional_elements() {
		LngLatAlt first = new LngLatAlt(14.D, 14.D, 15D, 16D, 17D);
		LngLatAlt second = new LngLatAlt(14.D, 14.D, 15D);
		Assert.assertNotEquals(second, first);
		Assert.assertNotEquals(second.hashCode(), first.hashCode());
	}

	@Test
	public void should_not_LngLatAlt_equals_with_additional_elements_in_different_order() {
		LngLatAlt first = new LngLatAlt(14.D, 14.D, 15D, 16D, 17D);
		LngLatAlt second = new LngLatAlt(14.D, 14.D, 15D, 17D, 16D);
		Assert.assertNotEquals(second, first);
		Assert.assertNotEquals(second.hashCode(), first.hashCode());
	}

	@Test
	public void should_not_LngLatAlt_equals_with_additional_elements_and_different_size() {
		LngLatAlt first = new LngLatAlt(14.D, 14.D, 15D, 16D, 17D);
		LngLatAlt second = new LngLatAlt(14.D, 14.D, 15D, 16D, 17D, 18D);
		Assert.assertNotEquals(second, first);
		Assert.assertNotEquals(second.hashCode(), first.hashCode());
	}
}