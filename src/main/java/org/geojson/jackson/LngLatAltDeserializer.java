package org.geojson.jackson;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.geojson.LngLatAlt;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LngLatAltDeserializer extends JsonDeserializer<LngLatAlt> {

    @Override
    public LngLatAlt deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
        if (jp.isExpectedStartArrayToken()) {
            return deserializeArray(jp, ctxt);
        }
        throw ctxt.mappingException(LngLatAlt.class);
    }

    protected LngLatAlt deserializeArray(JsonParser jp, DeserializationContext ctxt) throws IOException {
        LngLatAlt node = new LngLatAlt();
        node.setLongitude(extractDouble(jp, ctxt, false));
        node.setLatitude(extractDouble(jp, ctxt, false));
        node.setAltitude(extractDouble(jp, ctxt, true));
        List<Double> additionalElementsList = new ArrayList<Double>();
        while (jp.hasCurrentToken() && jp.getCurrentToken() != JsonToken.END_ARRAY) {
            double element = extractDouble(jp, ctxt, true);
            if (!Double.isNaN(element)) {
                additionalElementsList.add(element);
            }
        }
        double[] additionalElements = new double[additionalElementsList.size()];
        for (int i = 0; i < additionalElements.length; i++) {
            additionalElements[i] = additionalElementsList.get(i);
        }
        node.setAdditionalElements(additionalElements);
        return node;
    }

    private double extractDouble(JsonParser jp, DeserializationContext ctxt, boolean optional) throws IOException {
        JsonToken token = jp.nextToken();
        if (token == null) {
            if (optional)
                return Double.NaN;
            else
                throw ctxt.mappingException("Unexpected end-of-input when binding data into LngLatAlt");
        } else {
            switch (token) {
                case END_ARRAY:
                    if (optional)
                        return Double.NaN;
                    else
                        throw ctxt.mappingException("Unexpected end-of-input when binding data into LngLatAlt");
                case VALUE_NUMBER_FLOAT:
                    return jp.getDoubleValue();
                case VALUE_NUMBER_INT:
                    return jp.getLongValue();
                case VALUE_STRING:
                    return jp.getValueAsDouble();
                default:
                    throw ctxt.mappingException(
                            "Unexpected token (" + token.name() + ") when binding data into LngLatAlt");
            }
        }
    }
}
