/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.exolin.health.servlets;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.exolin.health.servlets.html.HealthHTML;
import org.exolin.health.servlets.json.HealthJSON;

/**
 *
 * @author tomgk
 */
public abstract class HealthServlet extends HttpServlet
{
    private final HealthJSON jsonVisualizer = new HealthJSON();
    private final HealthHTML htmlVisualizer = new HealthHTML();
    
    protected abstract HealthComponent getRoot();
    
    private Visualizer getVisualizerFor(HttpServletRequest request)
    {
        String responseType = request.getParameter("type");
        
        if("json".equals(responseType))
            return jsonVisualizer;
        else if("html".equals(responseType))
            return htmlVisualizer;
        
        for(Accept.ContentType c: Accept.parse(responseType))
        {
            if(c.matches("text/html"))
                return htmlVisualizer;
            if(c.matches("application/json"))
                return jsonVisualizer;
        }
        
        return htmlVisualizer;
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        Visualizer visualizer = getVisualizerFor(request);
        
        String url = request.getRequestURL().toString();  //ist ohne Query
        
        HealthComponent root = getRoot();
        HealthComponent c;
        
        String service = request.getParameter("service");
        if(service != null)
        {
            int p = service.indexOf(':');
            if(p == -1)
            {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "service parameter must have the form of type:name");
                return;
            }
            
            String type = service.substring(0, p);
            String name = service.substring(p+1);

            c = Finder.find(root, type, name);
            
            if(c == null)
            {
                visualizer.writeNotFound(url, type, name, request, response);
                return;
            }
        }
        else
            c = root;
        
        visualizer.write(url, c, request, response);
    }
}
