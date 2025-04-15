package org.geojson;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * A custom ObjectMapper for GeoJSON processing.
 * Provides options for RFC 7946 compliance.
 */
public class GeoJsonMapper extends ObjectMapper {

    /**
     * Creates a new GeoJsonMapper with default settings.
     * By default, this uses the 2008 GeoJSON specification for backward compatibility.
     */
    public GeoJsonMapper() {
        super();
    }

    /**
     * Creates a new GeoJsonMapper with RFC 7946 compliance enabled.
     *
     * @param rfc7946Compliance Whether to enable RFC 7946 compliance
     */
    public GeoJsonMapper(boolean rfc7946Compliance) {
        this(rfc7946Compliance, false);
    }

    /**
     * Creates a new GeoJsonMapper with RFC 7946 compliance enabled.
     *
     * @param rfc7946Compliance          Whether to enable RFC 7946 compliance
     * @param validatePolygonOrientation Whether to validate polygon orientation
     */
    public GeoJsonMapper(boolean rfc7946Compliance, boolean validatePolygonOrientation) {
        super();
        if (rfc7946Compliance) {
            GeoJsonConfig.useRfc7946();
            // Override polygon orientation validation if needed
            GeoJsonConfig.getInstance().setValidatePolygonOrientation(validatePolygonOrientation);
        } else {
            GeoJsonConfig.useLegacyMode();
        }
    }

    /**
     * Process a GeoJSON object according to RFC 7946 recommendations.
     * This includes:
     * - Cutting geometries that cross the antimeridian
     * - Fixing polygon ring orientation
     *
     * @param object The GeoJSON object to process
     * @return The processed GeoJSON object
     */
    public GeoJsonObject process(GeoJsonObject object) {
        return GeoJsonUtils.process(object);
    }
}
