package org.geojson.jackson;

import org.geojson.LngLatAlt;
import tools.jackson.core.JacksonException;
import tools.jackson.core.JsonGenerator;
import tools.jackson.databind.SerializationContext;
import tools.jackson.databind.ValueSerializer;

public class LngLatAltSerializer extends ValueSerializer<LngLatAlt> {

    @Override
    public void serialize(LngLatAlt value, JsonGenerator jgen, SerializationContext provider) throws JacksonException {
        jgen.writeStartArray();
        jgen.writeNumber(value.getLongitude());
        jgen.writeNumber(value.getLatitude());
        if (value.hasAltitude()) {
            jgen.writeNumber(value.getAltitude());

            for (double d : value.getAdditionalElements()) {
                jgen.writeNumber(d);
            }
        }
        jgen.writeEndArray();
    }
}
