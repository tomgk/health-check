package org.exolin.health.servlets;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author tomgk
 */
public interface Visualizer
{
    public void write(String url, HealthComponent root, HttpServletRequest request, HttpServletResponse response) throws IOException;
    
    public void writeNotFound(String url, String type, String name, HttpServletRequest request, HttpServletResponse response) throws IOException;

    public void showStatusAggregate(String url, Map<String, List<HealthComponent>> aggregated, HttpServletRequest request, HttpServletResponse response) throws IOException;
}
