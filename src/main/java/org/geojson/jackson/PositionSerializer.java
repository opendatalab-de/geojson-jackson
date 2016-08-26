package org.geojson.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.geojson.Position;

import java.io.IOException;

public class PositionSerializer extends JsonSerializer<Position> {

	public static final long POW10[] = {1, 10, 100, 1000, 10000, 100000, 1000000, 10000000, 100000000, 1000000000};

	/**
	 * The following must convert double to String in a much more efficient way then Double.toString()
	 *
	 * @See http://stackoverflow.com/questions/10553710/fast-double-to-string-conversion-with-given-precision
	 * @param val
	 * @param precision
	 * @return
	 */
	protected static String fastDoubleToString(double val, int precision) {
		StringBuilder sb = new StringBuilder();
		if (val < 0) {
			sb.append('-');
			val = -val;
		}
		long exp = POW10[precision];
		long lval = (long)(val * exp + 0.5);
		sb.append(lval / exp).append('.');
		long fval = lval % exp;
		for (int p = precision - 1; p > 0 && fval < POW10[p] && fval>0; p--) {
			sb.append('0');
		}
		sb.append(fval);
		int i = sb.length()-1;
		while(sb.charAt(i)=='0' && sb.charAt(i-1)!='.')
		{
			sb.deleteCharAt(i);
			i--;
		}
		return sb.toString();
	}

	@Override
	public void serialize(Position value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonProcessingException {
		jgen.writeStartArray();
		jgen.writeNumber(fastDoubleToString(value.longitude, 9));
		jgen.writeNumber(fastDoubleToString(value.latitude, 9));
		serializeOptionalParams(value, jgen);
		jgen.writeEndArray();
	}

	private void serializeOptionalParams(Position value, JsonGenerator jgen) throws IOException {
		if (!hasAltitude(value)) return;
		jgen.writeNumber(value.altitude);
		serializeAdditionalElements(value.additionalElements, jgen);
	}

	private void serializeAdditionalElements(Double[] value, JsonGenerator jgen) throws IOException {
		if (value == null) return;
		for(Double d : value) {
			jgen.writeNumber(d);
		}
	}

	private boolean hasAltitude(Position value) {
		if (value == null) return false;
		return value.altitude != null;
	}
}
