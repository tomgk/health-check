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
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.exolin.health.servlets.HealthComponent;

/**
 *
 * @author tomgk
 */
public class HealthJSON
{
    private static final ObjectMapper mapper = new ObjectMapper();
    static
    {
        mapper.setSerializationInclusion(Include.NON_NULL);
    }
    
    public static void write(HealthComponent root, HttpServletRequest request, HttpServletResponse response) throws IOException
    {
        response.setContentType("application/json;charset=UTF-8");
        
        write(response.getWriter(), root);
    }
    
    static void write(Writer out, HealthComponent root) throws IOException
    {
        mapper.writeValue(out, new HealthComponentWrapper(root));
    }
}
