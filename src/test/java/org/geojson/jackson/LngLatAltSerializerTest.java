package org.geojson.jackson;

import org.geojson.LngLatAlt;
import org.junit.Assert;
import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

public class LngLatAltSerializerTest
{

    @Test
    public void testSerialization() throws Exception
    {
        LngLatAlt position = new LngLatAlt(49.43245, 52.42345, 120.34626);
        String correctJson = "[49.43245,52.42345,120.34626]";
        String producedJson = new ObjectMapper().writeValueAsString(position);
        Assert.assertEquals(correctJson, producedJson);
    }
}
