# Migration Guide: Moving to RFC 7946 Compliance

This guide helps you migrate your application from using the 2008 GeoJSON specification to the newer RFC 7946 standard.

## Overview of Changes in RFC 7946

RFC 7946 (published in 2016) made several significant changes to the GeoJSON format:

1. **Coordinate Reference System (CRS)**:
    - The 2008 specification allowed custom CRS definitions.
    - RFC 7946 mandates that all GeoJSON coordinates use WGS 84 (longitude, latitude in decimal degrees).
    - The `crs` member has been removed from the specification.

2. **Polygon Ring Orientation**:
    - RFC 7946 requires that polygon rings follow the right-hand rule:
        - Exterior rings must be counterclockwise.
        - Interior rings (holes) must be clockwise.

3. **Antimeridian Cutting**:
    - Geometries that cross the antimeridian (180° longitude) should be cut into multiple parts.

4. **Media Type**:
    - The media type for GeoJSON is now "application/geo+json" (previously often used as "application/json").

## Migration Steps

### Step 1: Enable RFC 7946 Compliance

Use the `GeoJsonMapper` with RFC 7946 mode enabled or configure the global settings directly:

```java
// Using the GeoJsonMapper
GeoJsonMapper mapper = new GeoJsonMapper(true);

// Or configure global settings directly
GeoJsonConfig.

useRfc7946();
```

### Step 2: Remove Custom CRS Definitions

If your application uses custom CRS definitions, you'll need to:

1. Convert coordinates to WGS 84 before creating GeoJSON objects.
2. Remove any code that sets the `crs` property on GeoJSON objects.

Example:

```java
// Before:
Point point = new Point(100, 0);
Crs crs = new Crs();
crs.

getProperties().

put("name","EPSG:3857");
point.

setCrs(crs);

// After:
// Convert coordinates to WGS 84 first, then:
Point point = new Point(longitude, latitude);
// No CRS setting
```

### Step 3: Fix Polygon Ring Orientation

When RFC 7946 compliance is enabled, polygon rings must follow the right-hand rule. The library will validate this automatically, but you may need to fix your
existing code:

```java
// Create a polygon with counterclockwise exterior ring (valid)
Polygon polygon = new Polygon(
                new LngLatAlt(0, 0),
                new LngLatAlt(1, 0),
                new LngLatAlt(1, 1),
                new LngLatAlt(0, 1),
                new LngLatAlt(0, 0)
        );
```

If you need to fix ring orientation programmatically, you can use the `GeoJsonUtils` utility:

```java
List<LngLatAlt> ring = polygon.getExteriorRing();
if(!GeoJsonUtils.

isCounterClockwise(ring)){
        GeoJsonUtils.

reverseRing(ring);
}
```

Alternatively, you can configure the library to automatically fix polygon orientation:

```java
GeoJsonConfig.getInstance()
    .

setValidatePolygonOrientation(true)
    .

setAutoFixPolygonOrientation(true);
```

### Step 4: Handle Antimeridian Crossing

For geometries that cross the antimeridian (180° longitude), use the `GeoJsonUtils` class or the `GeoJsonMapper`:

```java
// Enable antimeridian cutting
GeoJsonConfig.getInstance().

setCutAntimeridian(true);

// Process a GeoJSON object using GeoJsonUtils
GeoJsonObject processed = GeoJsonUtils.process(geoJsonObject);

// Or use the GeoJsonMapper
GeoJsonMapper mapper = new GeoJsonMapper(true);
GeoJsonObject processed = mapper.process(geoJsonObject);
```

### Step 5: Update Media Type

If your application specifies media types for HTTP requests or responses, update to the new media type:

```
Content-Type: application/geo+json
```

## Backward Compatibility

This library maintains backward compatibility with the 2008 GeoJSON specification by default. RFC 7946 compliance is opt-in, so your existing code should
continue to work without changes.

If you need to support both standards, you can create separate mappers:

```java
// For 2008 GeoJSON specification
GeoJsonMapper legacyMapper = new GeoJsonMapper(false);

// For RFC 7946
GeoJsonMapper rfc7946Mapper = new GeoJsonMapper(true);
```

## Common Issues

### CRS Warnings

When using RFC 7946 compliance mode, you'll see warnings if you use the deprecated `crs` property. To disable these warnings:

```java
GeoJsonConfig.getInstance()
    .

setRfc7946Compliance(true)
    .

setWarnOnCrsUse(false);
```

### Polygon Validation Errors

If you get `IllegalArgumentException` errors about polygon ring orientation, you need to fix the orientation of your rings. See Step 3 above.

### Global Configuration

The GeoJsonConfig is a singleton with global state. If you're in a multi-threaded environment and need different configurations for different threads, you
should be careful about changing the configuration. Consider using separate instances of GeoJsonMapper instead:

```java
// Create mappers with different configurations
GeoJsonMapper legacyMapper = new GeoJsonMapper(false);
GeoJsonMapper rfc7946Mapper = new GeoJsonMapper(true);

// Use the appropriate mapper for each task
GeoJsonObject legacyObject = legacyMapper.readValue(inputStream, GeoJsonObject.class);
GeoJsonObject rfc7946Object = rfc7946Mapper.readValue(inputStream, GeoJsonObject.class);
```
