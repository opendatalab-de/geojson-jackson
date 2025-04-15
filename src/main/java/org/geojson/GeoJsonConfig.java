package org.geojson;

/**
 * Configuration for GeoJSON processing.
 * Controls compliance with different GeoJSON specifications and processing behavior.
 */
public class GeoJsonConfig {

    private static final GeoJsonConfig INSTANCE = new GeoJsonConfig();

    /**
     * Whether to enforce RFC 7946 compliance.
     * When true, certain validations and behaviors will follow the RFC 7946 specification.
     * When false, the library will follow the 2008 GeoJSON specification.
     */
    private boolean rfc7946Compliance = false;

    /**
     * Whether to validate polygon ring orientation according to the right-hand rule.
     * RFC 7946 requires exterior rings to be counterclockwise and interior rings to be clockwise.
     */
    private boolean validatePolygonOrientation = false;

    /**
     * Whether to warn when CRS is used in RFC 7946 mode.
     * RFC 7946 removed support for custom coordinate reference systems.
     */
    private boolean warnOnCrsUse = true;

    /**
     * Whether to automatically cut geometries that cross the antimeridian.
     * RFC 7946 recommends cutting geometries that cross the antimeridian into multiple parts.
     */
    private boolean cutAntimeridian = false;

    /**
     * Whether to automatically fix polygon ring orientation.
     * If true, rings will be reversed if they don't follow the right-hand rule.
     * If false, an exception will be thrown for invalid orientation when validation is enabled.
     */
    private boolean autoFixPolygonOrientation = false;

    /**
     * Private constructor to prevent instantiation.
     */
    private GeoJsonConfig() {
    }

    /**
     * Get the singleton instance.
     */
    public static GeoJsonConfig getInstance() {
        return INSTANCE;
    }

    /**
     * Configure for RFC 7946 compliance.
     */
    public static void useRfc7946() {
        GeoJsonConfig config = getInstance();
        config.setRfc7946Compliance(true);
        config.setValidatePolygonOrientation(true);
        config.setCutAntimeridian(true);
    }

    /**
     * Configure for 2008 GeoJSON specification (legacy mode).
     */
    public static void useLegacyMode() {
        GeoJsonConfig config = getInstance();
        config.setRfc7946Compliance(false);
        config.setValidatePolygonOrientation(false);
        config.setCutAntimeridian(false);
    }

    public boolean isRfc7946Compliance() {
        return rfc7946Compliance;
    }

    public GeoJsonConfig setRfc7946Compliance(boolean rfc7946Compliance) {
        this.rfc7946Compliance = rfc7946Compliance;
        return this;
    }

    public boolean isValidatePolygonOrientation() {
        return validatePolygonOrientation;
    }

    public GeoJsonConfig setValidatePolygonOrientation(boolean validatePolygonOrientation) {
        this.validatePolygonOrientation = validatePolygonOrientation;
        return this;
    }

    public boolean isWarnOnCrsUse() {
        return warnOnCrsUse;
    }

    public GeoJsonConfig setWarnOnCrsUse(boolean warnOnCrsUse) {
        this.warnOnCrsUse = warnOnCrsUse;
        return this;
    }

    public boolean isCutAntimeridian() {
        return cutAntimeridian;
    }

    public GeoJsonConfig setCutAntimeridian(boolean cutAntimeridian) {
        this.cutAntimeridian = cutAntimeridian;
        return this;
    }

    public boolean isAutoFixPolygonOrientation() {
        return autoFixPolygonOrientation;
    }

    public GeoJsonConfig setAutoFixPolygonOrientation(boolean autoFixPolygonOrientation) {
        this.autoFixPolygonOrientation = autoFixPolygonOrientation;
        return this;
    }
}
