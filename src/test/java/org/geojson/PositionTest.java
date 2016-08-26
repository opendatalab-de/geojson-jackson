package org.geojson;

import org.junit.Assert;
import org.junit.Test;

public class PositionTest {

	@Test
	public void should_LngLatAlt_equals_without_alt() {
		Position first = PositionFactory.create(14.D, 13.D);
		Position second = PositionFactory.create(14.D, 13.D);
		Assert.assertEquals(second, first);
	}

	@Test
	public void should_LngLatAlt_equals_with_alt() {
		Position first = PositionFactory.create(14.D, 13.D, 15D);
		Position second = PositionFactory.create(14.D, 13.D, 15D);
		Assert.assertEquals(second, first);
	}

	@Test
	public void should_not_LngLatAlt_equals_with_alt() {
		Position first = PositionFactory.create(14.D, 13.D, 15D);
		Position second = PositionFactory.create(14.D, 13.D, 16D);
		Assert.assertNotEquals(second, first);
	}

	@Test
	public void should_not_LngLatAlt_equals_without_alt() {
		Position first = PositionFactory.create(14.D, 14.D, 15D);
		Position second = PositionFactory.create(14.D, 13.D, 16D);
		Assert.assertNotEquals(second, first);
	}

	@Test
	public void should_LngLatAlt_equals_with_additional_elements() {
		Position first = PositionFactory.create(14.D, 14.D, 15D, 16D, 17D);
		Position second = PositionFactory.create(14.D, 14.D, 15D, 16D, 17D);
		Assert.assertEquals(second, first);
		Assert.assertEquals(second.hashCode(), first.hashCode());
	}

	@Test
	public void should_LngLatAlt_equals_with_additional_elements_and_null() {
		Position first = PositionFactory.create(14.D, 14.D, 15D, 16D, 17D);
		Position second = PositionFactory.create(14.D, 14.D, 15D, 16D, 17D);
		Assert.assertEquals(second, first);
		Assert.assertEquals(second.hashCode(), first.hashCode());
	}

	@Test
	public void should_not_LngLatAlt_equals_without_additional_elements() {
		Position first = PositionFactory.create(14.D, 14.D, 15D, 16D, 17D);
		Position second = PositionFactory.create(14.D, 14.D, 15D);
		Assert.assertNotEquals(second, first);
		Assert.assertNotEquals(second.hashCode(), first.hashCode());
	}

	@Test
	public void should_not_LngLatAlt_equals_with_additional_elements_in_different_order() {
		Position first = PositionFactory.create(14.D, 14.D, 15D, 16D, 17D);
		Position second = PositionFactory.create(14.D, 14.D, 15D, 17D, 16D);
		Assert.assertNotEquals(second, first);
		Assert.assertNotEquals(second.hashCode(), first.hashCode());
	}

	@Test
	public void should_not_LngLatAlt_equals_with_additional_elements_and_different_size() {
		Position first = PositionFactory.create(14.D, 14.D, 15D, 16D, 17D);
		Position second = PositionFactory.create(14.D, 14.D, 15D, 16D, 17D, 18D);
		Assert.assertNotEquals(second, first);
		Assert.assertNotEquals(second.hashCode(), first.hashCode());
	}

	@Test
	public void should_LngLatAlt_throw_if_alt_not_specified_in_constructor() {
		try {
			PositionFactory.create(14.D, 14.D, Double.NaN, 16D, 17D);
			Assert.fail("Additional elements are not allowed if altitude is Nan.");
		} catch (IllegalArgumentException e) {
			Assert.assertTrue("Expected exception.", true);
		}
	}

	@Test
	public void should_LngLatAlt_throw_if_alt_set_to_Nan_with_additional_elements() {
		try {
			PositionFactory.create(14.D, 14.D, Double.NaN, 16D, 17D);
			Assert.fail("Additional elements are not allowed if altitude is Nan.");
		} catch (IllegalArgumentException e) {
			Assert.assertTrue("Expected exception.", true);
		}
	}

	private Double[] generateAdditionalElements(Double ... elements) {
		return elements;
	}

	@Test
	public void should_LngLatAlt_throw_if_additional_elements_set_with_Nan_alt() {
		try {
			PositionFactory.create(14.D, 14.D, Double.NaN, generateAdditionalElements(42.D));
			Assert.fail("Additional elements are not allowed if altitude is Nan.");
		} catch (IllegalArgumentException e) {
			Assert.assertTrue("Expected exception.", true);
		}
	}

	@Test
	public void should_LngLatAlt_throw_if_any_additional_elements_constructed_to_Nan() {
		try {
			PositionFactory.create(14.D, 14.D, 15.D, 16.D, Double.NaN, 17.D);
			Assert.fail("Additional elements are not allowed to be Nan.");
		} catch (IllegalArgumentException e) {
			Assert.assertTrue("Expected exception.", true);
		}
	}

	@Test
	public void should_LngLatAlt_throw_if_any_additional_elements_constructed_to_Positive_Infinity() {
		try {
			PositionFactory.create(14.D, 14.D, 15.D, 16.D, Double.POSITIVE_INFINITY, 17.D);
			Assert.fail("Additional elements are not allowed to be positive infinity.");
		} catch (IllegalArgumentException e) {
			Assert.assertTrue("Expected exception.", true);
		}
	}

	@Test
	public void should_LngLatAlt_throw_if_any_additional_elements_constructed_to_Negative_Infinity() {
		try {
			PositionFactory.create(14.D, 14.D, 15.D, 16.D, Double.NEGATIVE_INFINITY, 17.D);
			Assert.fail("Additional elements are not allowed to be positive infinity.");
		} catch (IllegalArgumentException e) {
			Assert.assertTrue("Expected exception.", true);
		}
	}

	@Test
	public void should_LngLatAlt_throw_if_any_additional_elements_set_to_Nan() {
		try {
			PositionFactory.create(14.D, 14.D, 15.D, generateAdditionalElements(16.D, Double.NaN, 17.D));
			Assert.fail("Additional elements are not allowed to be Nan.");
		} catch (IllegalArgumentException e) {
			Assert.assertTrue("Expected exception.", true);
		}
	}

	@Test
	public void should_LngLatAlt_throw_if_any_additional_elements_set_to_Positive_Infinity() {
		try {
			PositionFactory.create(14.D, 14.D, 15.D, generateAdditionalElements(16.D, Double.POSITIVE_INFINITY, 17.D));
			Assert.fail("Additional elements are not allowed to be positive infinity.");
		} catch (IllegalArgumentException e) {
			Assert.assertTrue("Expected exception.", true);
		}
	}

	@Test
	public void should_LngLatAlt_throw_if_any_additional_elements_set_to_Negative_Infinity() {
		try {
			PositionFactory.create(14.D, 14.D, 15.D, generateAdditionalElements(16.D, Double.NEGATIVE_INFINITY, 17.D));
			Assert.fail("Additional elements are not allowed to be positive infinity.");
		} catch (IllegalArgumentException e) {
			Assert.assertTrue("Expected exception.", true);
		}
	}
}