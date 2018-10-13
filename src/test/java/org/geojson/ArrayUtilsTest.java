package org.geojson;

import static org.geojson.ArrayUtils.append;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class ArrayUtilsTest {
  @Test
  public void appends_elements_to_String_arrays() {
    String[] array = new String[]{"a"};
    assertThat(append(array, "b"), is(new String[]{"a", "b"}));
  }
}