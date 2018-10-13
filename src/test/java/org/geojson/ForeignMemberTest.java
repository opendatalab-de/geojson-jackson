package org.geojson;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

@RunWith(Parameterized.class)
public class ForeignMemberTest {
  @Parameterized.Parameter(value = 0)
  public String testName;

  @Parameterized.Parameter(value = 1)
  public GeoJsonObject gj;

  @Parameterized.Parameter(value = 2)
  public String reservedKey;

  @Parameterized.Parameters(name = "{0} - {2}")
  public static Collection<Object[]> data() {
    return Arrays.asList(new Object[][]{
        {"GeoJsonObject", abstractGeoJsonObjectFactory(), "crs"},
        {"GeoJsonObject", abstractGeoJsonObjectFactory(), "bbox"},
        {"GeoJsonObject", abstractGeoJsonObjectFactory(), "bbox"},
        {"Feature", new Feature(), "properties"},
        {"Feature", new Feature(), "geometry"},
        {"Feature", new Feature(), "id"},
        {"FeatureCollection", new FeatureCollection(), "features"},
        {"Geometry", abstractGeometryFactory(), "coordinates"},
        {"GeometryCollection", new GeometryCollection(), "geometries"},
        {"Point", new Point(), "coordinates"},
    });
  }

  @Test(expected = IllegalArgumentException.class)
  public void prevents_setting_foreign_members_with_reserved_keys() {
    gj.addForeignMember(reservedKey, Collections.<String, Object>emptyMap());
  }

  private static GeoJsonObject abstractGeoJsonObjectFactory() {
    return new GeoJsonObject() {
      @Override
      public <T> T accept(GeoJsonObjectVisitor<T> geoJsonObjectVisitor) {
        return null;
      }
    };
  }

  private static Geometry<Point> abstractGeometryFactory() {
    return new Geometry<Point>() {
      @Override
      public <T> T accept(GeoJsonObjectVisitor<T> geoJsonObjectVisitor) {
        return null;
      }
    };
  }
}
