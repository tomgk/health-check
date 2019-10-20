/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.exolin.health.servlets;

import java.io.IOException;
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
    protected abstract HealthComponent getRoot();
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        HealthComponent root = getRoot();
        
        String type = request.getParameter("type");
        if("json".equals(type))
            HealthJSON.write(root, request, response);
        else
            HealthHTML.write(root, request, response);
    }
}
