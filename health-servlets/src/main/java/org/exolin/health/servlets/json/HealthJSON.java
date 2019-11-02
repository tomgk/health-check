/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.exolin.health.servlets.json;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.Writer;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.exolin.health.servlets.HealthComponent;
import org.exolin.health.servlets.LinkBuilder;
import org.exolin.health.servlets.Visualizer;

/**
 *
 * @author tomgk
 */
public class HealthJSON implements Visualizer
{
    private static final ObjectMapper mapper = new ObjectMapper();
    static
    {
        mapper.setSerializationInclusion(Include.NON_NULL);
    }
    
    @Override
    public void write(String url, HealthComponent component, HttpServletRequest request, HttpServletResponse response) throws IOException
    {
        response.setContentType("application/json;charset=UTF-8");
        
        write(url, response.getWriter(), component);
    }
    
    static void write(String url, Writer out, HealthComponent component) throws IOException
    {
        LinkBuilder linkBuilder = new LinkBuilder(url, true);
        mapper.writeValue(out, new HealthComponentWrapper(component, linkBuilder));
    }

    @Override
    public void writeNotFound(String url, String type, String name, HttpServletRequest request, HttpServletResponse response) throws IOException
    {
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        
        mapper.writeValue(response.getWriter(), Collections.singletonMap("error", "not found: service[type="+type+", name="+name+"]"));
    }

    @Override
    public void showStatusAggregate(String url, Map<String, List<HealthComponent>> aggregated, HttpServletRequest request, HttpServletResponse response) throws IOException
    {
        LinkBuilder linkBuilder = new LinkBuilder(url, true);
        
        Map<String, List<HealthComponentShortInfo>> info = new LinkedHashMap<>();
        
        for(Map.Entry<String, List<HealthComponent>> e: aggregated.entrySet())
        {
            info.put(e.getKey(), e.getValue().stream().map(c -> new HealthComponentShortInfo(c, linkBuilder)).collect(Collectors.toList()));
        }
        
        mapper.writeValue(response.getWriter(), info);
    }
}
