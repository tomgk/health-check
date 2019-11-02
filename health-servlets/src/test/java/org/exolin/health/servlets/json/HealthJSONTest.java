/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.exolin.health.servlets.json;

import java.io.StringWriter;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.exolin.health.servlets.HealthComponent;
import org.exolin.health.servlets.Value;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 *
 * @author tomgk
 */
public class HealthJSONTest
{
    private static final String URL = "http://exolin.test/status";
    
    static class HealthComponentRoot implements HealthComponent
    {
        @Override
        public String getType()
        {
            return "root";
        }
    }
    
    @Test
    public void testWriteMinimal() throws Exception
    {
        StringWriter out = new StringWriter();
        HealthJSON.write(URL, out, new HealthComponentRoot());
        
        assertEquals("{\"type\":\"root\"}", out.toString());
    }
    
    static class HealthComponentRoot2 implements HealthComponent
    {
        @Override
        public String getType()
        {
            return "root";
        }

        @Override
        public String getName()
        {
            return "a name";
        }

        @Override
        public String getVersion()
        {
            return "3.2";
        }

        @Override
        public String getSubType()
        {
            return "rooty";
        }

        @Override
        public String getStatus()
        {
            return "running";
        }

        @Override
        public Long getStartTime()
        {
            return 1234l;
        }

        @Override
        public Long getStartDuration()
        {
            return 5678l;
        }

        @Override
        public Throwable getStartupException()
        {
            return null;
        }

        @Override
        public List<HealthComponent> getSubComponents()
        {
            return Arrays.asList(() -> "sub-thingy");
        }

        @Override
        public Map<String, Value> getProperties()
        {
            return Collections.singletonMap("something", Value.value("value of it"));
        }
    }
    
    @Test
    public void testWriteAll() throws Exception
    {
        StringWriter out = new StringWriter();
        HealthJSON.write(URL, out, new HealthComponentRoot2());
        
        assertEquals("{"
                + "\"type\":\"root\","
                + "\"name\":\"a name\","
                + "\"version\":\"3.2\","
                + "\"subType\":\"rooty\","
                + "\"url\":\"http://exolin.test/status?type=json&service=root%3Aa+name\","
                + "\"status\":\"running\","
                + "\"properties\":{\"something\":{\"value\":\"value of it\"}},"
                + "\"startTime\":1234,"
                + "\"startDuration\":5678,"
                + "\"subComponents\":[{\"type\":\"sub-thingy\"}"
                + "]}", out.toString());
    }
}
