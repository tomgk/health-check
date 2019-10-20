/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.exolin.health.servlets.html;

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

/**
 * 
 * 
 * @author tomgk
 */
public class HealthHTML
{
    public static void write(HealthComponent root, HttpServletRequest request, HttpServletResponse response) throws IOException
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
            out.println("<h1>Status</h1>");
            
            out.write("<table class=\"table\">");

            out.write("<tr><td>ContextPath</td><td>" + request.getContextPath()+"</td></tr>");

            out.write("</table>");
            
            writeEntity(out, root);
            
            List<HealthComponentWithParent> components = new ArrayList<>();
            addSubs(root, components);
            
            Map<String, List<HealthComponentWithParent>> byType = components.stream().collect(Collectors.groupingBy(c -> c.getComponent().getType(), Collectors.toList()));
            
            for(Map.Entry<String, List<HealthComponentWithParent>> entry: byType.entrySet())
            {
                if(entry.getValue().size() == 1)
                {
                    out.print("<h2>"+entry.getKey()+"</h2>");
                    writeEntity(out, entry.getValue().get(0).getComponent());
                }
                else
                {
                    Set<HealthComponent> parents = entry.getValue().stream().map(c -> c.getParent()).collect(Collectors.toSet());
                    
                    out.print("<h2>"+entry.getKey()+"s</h2>");
                    
                    if(parents.size() == 1)
                        out.print("<p>From "+ref(parents.iterator().next())+"</p>");
                    
                    writeList(out, parents.size() != 1, entry.getValue());
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
    
    private static void writeEntity(PrintWriter out, HealthComponent component)
    {
        out.println("<table class=\"table\">");

        writeRow(out, "Type", component.getType());
        writeRow(out, "Name", component.getName());
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

    protected static void writeList(PrintWriter out, boolean withParent, List<HealthComponentWithParent> components) throws IOException
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
                out.print("<td>"+sub.getName()+"</td>");
            
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
