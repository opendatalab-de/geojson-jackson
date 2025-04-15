### Note: This is a continuation of opendatalab-de/geojson-jackson

GeoJson POJOs for Jackson
=========================

A small package of all GeoJson POJOs (Plain Old Java Objects) for serializing and
deserializing of objects via JSON Jackson Parser. This library supports both the 2008 GeoJSON specification and the newer RFC 7946 standard (published in 2016).

GeoJSON Standards Support
------------------------

This library supports two GeoJSON standards:

1. **2008 GeoJSON Specification** (default for backward compatibility)
   - Supports custom Coordinate Reference Systems (CRS)
   - No specific requirements for polygon ring orientation
   - No handling of geometries crossing the antimeridian

2. **RFC 7946 Specification** (modern standard, opt-in)
   - All coordinates use WGS 84 as the coordinate reference system
   - Polygon rings must follow the right-hand rule (exterior rings counterclockwise, interior rings clockwise)
   - Geometries crossing the antimeridian should be cut into multiple parts
   - Media type is "application/geo+json"

If migrating from 2008 to RFC 7946, refer to the [Migration Guide](RFC_7946_MIGRATION_GUIDE.md).

Usage
-----

### Basic Usage (2008 GeoJSON Specification)

If you know what kind of object you expect from a GeoJson file you can directly read it like this:

```java
FeatureCollection featureCollection =
	new ObjectMapper().readValue(inputStream, FeatureCollection.class);
```

If you want to read any GeoJson file read the value as GeoJsonObject and then test for the contents via instanceOf:

```java
GeoJsonObject object = new ObjectMapper().readValue(inputStream, GeoJsonObject.class);
if (object instanceof Polygon) {
	...
} else if (object instanceof Feature) {
	...
}
```
and so on.

Or you can use the GeoJsonObjectVisitor to visit the right method:

```java
GeoJsonObject object = new ObjectMapper().readValue(inputStream, GeoJsonObject.class);
object.accept(visitor);
```

Writing Json is even easier. You just have to create the GeoJson objects and pass them to the Jackson ObjectMapper.

```java
FeatureCollection featureCollection = new FeatureCollection();
featureCollection.add(new Feature());

String json= new ObjectMapper().writeValueAsString(featureCollection);
```

### RFC 7946 Compliance

To enable RFC 7946 compliance, use the `GeoJsonMapper` with RFC 7946 mode enabled:

```java
// Create a mapper with RFC 7946 compliance enabled
GeoJsonMapper mapper = new GeoJsonMapper(true);

// Use it like a regular ObjectMapper
FeatureCollection featureCollection = mapper.readValue(inputStream, FeatureCollection.class);
String json = mapper.writeValueAsString(featureCollection);

// Process a GeoJSON object according to RFC 7946 recommendations
GeoJsonObject processed = mapper.process(geoJsonObject);
```

Or configure the global settings directly:

```java
// Enable RFC 7946 compliance globally
GeoJsonConfig.useRfc7946();

// Use a regular ObjectMapper
ObjectMapper mapper = new ObjectMapper();
FeatureCollection featureCollection = mapper.readValue(inputStream, FeatureCollection.class);
String json = mapper.writeValueAsString(featureCollection);
```

### Customizing RFC 7946 Behavior

You can customize the RFC 7946 compliance behavior using the `GeoJsonConfig` class:

```java
// Get the global configuration instance
GeoJsonConfig config = GeoJsonConfig.getInstance();

// Enable RFC 7946 compliance
config.

setRfc7946Compliance(true)
// Validate polygon ring orientation
      .

setValidatePolygonOrientation(true)
// Automatically fix polygon orientation instead of throwing exceptions
      .

setAutoFixPolygonOrientation(true)
// Enable antimeridian cutting
      .

setCutAntimeridian(true)
// Disable warnings when CRS is used
      .

setWarnOnCrsUse(false);
```

### Polygon Ring Orientation

RFC 7946 requires that polygon rings follow the right-hand rule (exterior rings counterclockwise, interior rings clockwise). This is validated automatically
when RFC 7946 compliance is enabled.

You can choose to either validate orientation (which throws exceptions for invalid rings) or automatically fix orientation:

```java
// Validate orientation (throws exceptions for invalid rings)
GeoJsonConfig.getInstance()
    .

setValidatePolygonOrientation(true)
    .

setAutoFixPolygonOrientation(false);

// Auto-fix orientation (silently fixes invalid rings)
GeoJsonConfig.

getInstance()
    .

setValidatePolygonOrientation(true)
    .

setAutoFixPolygonOrientation(true);
```

### Antimeridian Cutting

RFC 7946 recommends cutting geometries that cross the antimeridian. You can enable this feature:

```java
// Enable antimeridian cutting
GeoJsonConfig.getInstance().

setCutAntimeridian(true);

// Process a GeoJSON object (will cut geometries if enabled)
GeoJsonObject processed = GeoJsonUtils.process(geoJsonObject);
```

Maven Central
-------------

Currently this fork is not hosted on maven central. Release can be downloaded as a jar file.