package org.geojson.jackson;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.geojson.LngLatAlt;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by babbleshack on 27/11/16.
 */
public class LngLatAltDeserializerTest {
    @Test
    public void deserializeMongoLngLatAlt() throws Exception {
        LngLatAlt lngLatAlt = new LngLatAlt(10D, 15D, 5);
        String lngLatAltJson = new ObjectMapper().writeValueAsString(lngLatAlt);
        lngLatAltJson.replace("10.0", "\"10.0\"");
        lngLatAltJson.replace("15.0", "\"15.0\"");
        LngLatAlt lngLatAlt1 = new ObjectMapper().readValue(lngLatAltJson, LngLatAlt.class);
        Assert.assertTrue(lngLatAlt.equals(lngLatAlt));
    }
}
