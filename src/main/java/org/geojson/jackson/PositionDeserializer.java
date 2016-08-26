package org.geojson.jackson;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.geojson.Position;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PositionDeserializer extends JsonDeserializer<Position> {

	@Override
	public Position deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
		if (jp.isExpectedStartArrayToken()) {
			return deserializeArray(jp, ctxt);
		}
		throw ctxt.mappingException("Could not deserialize Position");
	}

	protected Position deserializeArray(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
		Position node = new Position();
		node.longitude = extractDouble(jp, ctxt, false);
		node.latitude = extractDouble(jp, ctxt, false);
		node.altitude = extractDouble(jp, ctxt, true);

		List<Double> additionalElementsList = new ArrayList<Double>();
		while (jp.hasCurrentToken() && jp.getCurrentToken() != JsonToken.END_ARRAY) {
			Double element = extractDouble(jp, ctxt, true);
			if (element != null && !Double.isNaN(element)) {
				additionalElementsList.add(element);
			}
		}

		Double[] additionalElements = new Double[additionalElementsList.size()];
		for(int i = 0; i < additionalElements.length; i++) {
			additionalElements[i] = additionalElementsList.get(i);
		}
		if (additionalElements.length != 0) node.additionalElements = additionalElements;

		return node;
	}

	private Double extractDouble(JsonParser jp, DeserializationContext ctxt, boolean optional) throws JsonParseException, IOException {
		JsonToken token = jp.nextToken();
		if (token == null) {
			if (optional)
				return null;
			else
				throw ctxt.mappingException("Unexpected end-of-input when binding data into Position");
		}
		else {
			switch (token) {
				case END_ARRAY:
					if (optional)
						return null;
					else
						throw ctxt.mappingException("Unexpected end-of-input when binding data into Position");
				case VALUE_NUMBER_FLOAT:
					return jp.getDoubleValue();
				case VALUE_NUMBER_INT:
					return new Double(jp.getLongValue());
				default:
					throw ctxt.mappingException("Unexpected token (" + token.name() + ") when binding data into Position ");
			}
		}
	}
}
