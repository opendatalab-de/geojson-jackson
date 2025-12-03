package org.geojson.jackson;

import org.geojson.LngLatAlt;
import tools.jackson.core.JacksonException;
import tools.jackson.core.JsonParser;
import tools.jackson.core.JsonToken;
import tools.jackson.databind.DeserializationContext;
import tools.jackson.databind.ValueDeserializer;

import java.util.ArrayList;
import java.util.List;

public class LngLatAltDeserializer extends ValueDeserializer<LngLatAlt> {

    @Override
    public LngLatAlt deserialize(JsonParser jp, DeserializationContext ctxt) throws JacksonException {
        if (jp.isExpectedStartArrayToken()) {
            return deserializeArray(jp, ctxt);
        }
        throw (JacksonException) ctxt.reportInputMismatch(LngLatAlt.class, "Cannot deserialize value to %s", LngLatAlt.class.getSimpleName());
    }

    protected LngLatAlt deserializeArray(JsonParser jp, DeserializationContext ctxt) throws JacksonException {
        LngLatAlt node = new LngLatAlt();
        node.setLongitude(extractDouble(jp, ctxt, false));
        node.setLatitude(extractDouble(jp, ctxt, false));
        node.setAltitude(extractDouble(jp, ctxt, true));
        List<Double> additionalElementsList = new ArrayList<Double>();
        while (jp.hasCurrentToken() && jp.currentToken() != JsonToken.END_ARRAY) {
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

    private double extractDouble(JsonParser jp, DeserializationContext ctxt, boolean optional) throws JacksonException {
        JsonToken token = jp.nextToken();
        if (token == null) {
            if (optional)
                return Double.NaN;
            else
                throw (JacksonException) ctxt.reportInputMismatch(LngLatAlt.class, "Unexpected end-of-input when binding data into LngLatAlt");
        } else {
            switch (token) {
                case END_ARRAY:
                    if (optional)
                        return Double.NaN;
                    else
                        throw (JacksonException) ctxt.reportInputMismatch(LngLatAlt.class,"Unexpected end-of-input when binding data into LngLatAlt");
                case VALUE_NUMBER_FLOAT:
                    return jp.getDoubleValue();
                case VALUE_NUMBER_INT:
                    return jp.getLongValue();
                case VALUE_STRING:
                    return jp.getValueAsDouble();
                default:
                    throw (JacksonException) ctxt.reportInputMismatch(LngLatAlt.class,
                            "Unexpected token (" + token.name() + ") when binding data into LngLatAlt");
            }
        }
    }
}
