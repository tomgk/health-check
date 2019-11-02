package org.exolin.health.servlets;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 *
 * @author tomgk
 */
public class LinkBuilder
{
    private final String url;
    private final boolean json;

    public LinkBuilder(String url, boolean json)
    {
        this.url = url;
        this.json = json;
    }

    public String link(String type, String name)
    {
        String serviceId = type+":"+name;
        
        try{
            return url+"?"+(json?"type=json&":"")+"service="+URLEncoder.encode(serviceId, "UTF-8");
        }catch(UnsupportedEncodingException e){
            throw new RuntimeException(e);
        }
    }
}
