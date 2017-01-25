package org.geojson.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.geojson.LngLatAlt;

import java.io.IOException;

public class LngLatAltSerializer extends JsonSerializer<LngLatAlt> {

	public static final long POW10[] = {1, 10, 100, 1000, 10000, 100000, 1000000, 10000000, 100000000, 1000000000};

	@Override
	public void serialize(LngLatAlt value, JsonGenerator jgen, SerializerProvider provider) throws IOException,
			JsonProcessingException {
		jgen.writeStartArray();
		jgen.writeNumber(value.getLongitude());
		jgen.writeNumber(value.getLatitude());
		if (value.hasAltitude()) {
			jgen.writeNumber(value.getAltitude());

			for(double d : value.getAdditionalElements()) {
				jgen.writeNumber(d);
			}
		}
		jgen.writeEndArray();
	}
}
