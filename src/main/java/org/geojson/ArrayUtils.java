package org.geojson;

final class ArrayUtils {
  static String[] append(String[] array, String... components) {
    String[] tempArray = new String[array.length + components.length];
    System.arraycopy(array, 0, tempArray, 0, array.length);
    System.arraycopy(components, 0, tempArray, array.length, components.length);
    return tempArray;
  }
}
