GeoJson POJOs for Jackson
=========================

A small package of all GeoJson POJOs (Plain Old Java Objects) for serializing and 
deserializing of objects via JSON Jackson Parser. This libary conforms to the 2008 GeoJSON specification.

Usage
-----

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

Maven Central
-------------

You can find the library in the Maven Central Repository.

```xml
<dependency>
 <groupId>de.grundid.opendatalab</groupId>
 <artifactId>geojson-jackson</artifactId>
 <version>1.14</version>
</dependency>
```
		
