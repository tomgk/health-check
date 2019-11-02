/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.exolin.health.servlets.html;

import org.exolin.health.servlets.LinkBuilder;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.text.StringEscapeUtils;
import org.exolin.health.servlets.HealthComponent;
import org.exolin.health.servlets.Value;
import org.exolin.health.servlets.Visualizer;

/**
 * 
 * 
 * @author tomgk
 */
public class HealthHTML implements Visualizer
{
    @Override
    public void writeNotFound(String url, String type, String name, HttpServletRequest request, HttpServletResponse response) throws IOException
    {
        try(PrintWriter out = response.getWriter())
        {
            response.setContentType("text/html;charset=UTF-8");
        
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<link rel=\"stylesheet\" href=\"https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css\" integrity=\"sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T\" crossorigin=\"anonymous\">");
            out.println("<title>Status</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<div class=\"container\">");
            out.println("<h1>Not found</h1>");
            out.println("<p>The specific service was not found</p>");
            out.println("</div>");
            out.println("</body>");
            out.println("</html>");
        }
    }
    
    private void writeTitle(PrintWriter out, HealthComponent component)
    {
        out.print("Status");

        if(component.getType() != null)
            out.print(" "+component.getType());
        if(component.getName()!= null)
            out.print(" "+component.getName());
    }
    
    @Override
    public void write(String url, HealthComponent component, HttpServletRequest request, HttpServletResponse response) throws IOException
    {
        try(PrintWriter out = response.getWriter())
        {
            response.setContentType("text/html;charset=UTF-8");
        
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<link rel=\"stylesheet\" href=\"https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css\" integrity=\"sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T\" crossorigin=\"anonymous\">");
            
            out.print("<title>");
            writeTitle(out, component);
            out.println("</title>");
            
            out.println("</head>");
            out.println("<body>");
            out.println("<div class=\"container\">");
            
            out.print("<h1>");
            writeTitle(out, component);
            out.println("</h1>");
            
            out.write("<table class=\"table\">");

            out.write("<tr><td>ContextPath</td><td>" + request.getContextPath()+"</td></tr>");

            out.write("</table>");
            
            LinkBuilder linkBuilder = new LinkBuilder(url, false);
            
            writeEntity(linkBuilder, out, component);
            
            List<HealthComponentWithParent> components = new ArrayList<>();
            addSubs(component, components);
            
            Map<String, List<HealthComponentWithParent>> byType = components.stream().collect(Collectors.groupingBy(c -> c.getComponent().getType(), Collectors.toList()));
            
            for(Map.Entry<String, List<HealthComponentWithParent>> entry: byType.entrySet())
            {
                if(entry.getValue().size() == 1)
                {
                    String name = entry.getValue().get(0).getComponent().getName();
                    
                    out.print("<h2>");
                    
                    if(name != null)
                        out.print("<a href=\""+linkBuilder.link(component.getType(), name)+"\">");
                    
                    out.print(entry.getKey());
                    
                    if(name != null)
                        out.print("</a>");
                    
                    out.print("</h2>");
                    
                    writeEntity(linkBuilder, out, entry.getValue().get(0).getComponent());
                }
                else
                {
                    Set<HealthComponent> parents = entry.getValue().stream().map(c -> c.getParent()).collect(Collectors.toSet());
                    
                    out.print("<h2>"+entry.getKey()+"s</h2>");
                    
                    if(parents.size() == 1)
                        out.print("<p>From "+ref(parents.iterator().next())+"</p>");
                    
                    writeList(linkBuilder, out, parents.size() != 1, entry.getValue());
                }
            }
            
            out.println("</div>");
            out.println("</body>");
            out.println("</html>");
        }
    }
    
    private static void writeRow(PrintWriter out, String name, String value)
    {
        if(value != null)
            out.println("<tr><td>"+name+"</td><td>"+value+"</td></tr>");
    }
    
    private static void writeEntity(LinkBuilder linkBuilder, PrintWriter out, HealthComponent component)
    {
        out.println("<table class=\"table\">");

        writeRow(out, "Type", component.getType());
        
        {
            String name = component.getName();
            if(name != null)
            {
                out.println("<tr>");
                out.println("<td><a href=\""+linkBuilder.link(component.getType(), name)+"\">Name</a></td>");
                out.println("<td>"+name+"</td>");
                out.println("</tr>");
            }
        }
        
        writeRow(out, "Version", component.getVersion());
        writeRow(out, "Sub-Type", component.getSubType());
        
        writeRow(out, "Start time", Formatter.formatTimestamp(component.getStartTime()));
        writeRow(out, "Startup time", Formatter.formatDuration(component.getStartDuration())+" ms");

        writeRow(out, "Status", component.getStatus());

        out.println("<tr><td>Startup Exception</td><td>");

        Throwable startUpException = component.getStartupException();
        if(startUpException != null)
            Formatter.writeException(out, startUpException);
        else
            out.print("No known exception<br>(either none or couldn't be determined)");

        out.println("</td></tr>");

        out.println("</table>");
    }

    protected static void writeList(LinkBuilder linkBuilder, PrintWriter out, boolean withParent, List<HealthComponentWithParent> components) throws IOException
    {
        boolean containsLocalName = components.stream().anyMatch(e -> e.getComponent().getName() != null);
        boolean containsLocalVersion = components.stream().anyMatch(e -> e.getComponent().getVersion() != null);
        boolean containsLocalType = components.stream().anyMatch(e -> e.getComponent().getSubType() != null);

        Set<String> allPropertiesKeys = new LinkedHashSet<>();
        components.forEach(component -> allPropertiesKeys.addAll(component.getComponent().getProperties().keySet()));

        out.print("<table class=\"table\">");
        out.print("<tr>");
        
        if(withParent)
            out.print("<th>Parent</th>");
        
        if(containsLocalName)
            out.print("<th>Name</th>");
        
        if(containsLocalVersion)
            out.print("<th>Version</th>");
        
        if(containsLocalType)
            out.print("<th>Type</th>");
        
        out.print("<th>Status</th>");
        for(String prop: allPropertiesKeys)
        {
            out.print("<th>");
            out.print(StringEscapeUtils.escapeHtml4(prop));
            out.print("</th>");
        }
        out.print("</tr>");
        
        for(HealthComponentWithParent subw: components)
        {
            HealthComponent sub = subw.getComponent();
            
            out.print("<tr>");
            
            if(withParent)
                out.print("<td>"+ref(subw.getParent())+"</td>");
            
            if(containsLocalName)
            {
                String name = sub.getName();
                
                out.print("<td>");
                
                if(name != null)
                    out.print("<a href=\""+linkBuilder.link(sub.getType(), name)+"\">");
                
                out.print(sub.getName());
                
                if(name != null)
                    out.print("</a>");
                
                out.print("</td>");
            }
            
            if(containsLocalVersion)
                out.print("<td>"+sub.getVersion()+"</td>");
            
            if(containsLocalType)
                out.print("<td>"+sub.getSubType()+"</td>");
            
            out.print("<td>"+sub.getStatus()+"</td>");

            Map<String, Value> properties = sub.getProperties();

            for(String prop: allPropertiesKeys)
            {
                out.print("<td>");
                Value value = properties.get(prop);

                if(value != null)
                {
                    if(value.getValue() != null)
                        out.print(StringEscapeUtils.escapeHtml4(value.getValue()));
                    else
                        Formatter.writeException(out, value.getException());
                }
                else if(properties.containsKey(prop))
                    out.print("<i>NULL</i>");
                else
                    out.print("<i>not present</i>");

                out.print("</td>");
            }

            out.print("</tr>");
        }
        out.print("</table>");
    }

    private static void addSubs(HealthComponent component, List<HealthComponentWithParent> components)
    {
        List<HealthComponent> subComponents = component.getSubComponents();
        subComponents.forEach(s -> components.add(new HealthComponentWithParent(component, s)));
        subComponents.forEach(s -> addSubs(s, components));
    }

    private static String ref(HealthComponent component)
    {
        List<String> parts = new ArrayList<>();
        parts.add(component.getType());
        parts.add(component.getName());
        parts.add(component.getVersion());
        parts.removeAll(Collections.singleton(null));
        
        if(parts.isEmpty())
            return "@"+System.identityHashCode(component);
        
        return String.join(" ", parts);
    }
}
