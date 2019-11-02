package org.exolin.health.servlets;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 *
 * @author tomgk
 */
public class Accept
{
    public static class ContentType
    {
        private final String category;
        private final String sub;
        private final double q;

        public ContentType(String category, String sub, double q)
        {
            this.category = category;
            this.sub = sub;
            this.q = q;
        }

        public double getQ()
        {
            return q;
        }
        
        public boolean matches(String contentType)
        {
            if(category.equals("*") && sub.equals("*"))
                return true;
            
            int p = contentType.indexOf('/');
            if(p == -1)
                throw new IllegalArgumentException("No slash in: "+contentType);
            
            String category = contentType.substring(0, p);
            String sub      = contentType.substring(p+1);
            
            return (this.category.equals("*") || this.category.equals(category)) ||
                   (this.sub.equals("*")      || this.sub.equals(category));
        }

        @Override
        public String toString()
        {
            return category+"/"+sub+";q="+q;
        }
    }
    
    private final List<ContentType> acceptedTypes;

    public Accept(List<ContentType> acceptedTypes)
    {
        this.acceptedTypes = acceptedTypes;
    }
    
    public static List<ContentType> parse(String accept)
    {
        if(accept == null || accept.isEmpty())
            return Collections.emptyList();
        
        String[] types = accept.split(",");
        
        List<ContentType> ct = new ArrayList<>();
        for(String type: types)
        {
            String[] parts = type.split(";");
            double q = 1;
            
            String contentType = parts[0];
            //@0 = content type
            for(int i=1;i<parts.length;++i)
                if(parts[i].startsWith("q="))
                    q = Double.parseDouble(parts[i].substring("q=".length()));
            
            int p = contentType.indexOf('/');
            if(p == -1)
                throw new IllegalArgumentException("No slash in: "+contentType+"; Accept: "+accept);
            
            String category = contentType.substring(0, p);
            String sub      = contentType.substring(p+1);
            
            ct.add(new ContentType(category, sub, q));
        }
        
        ct.sort(Comparator.comparing(ContentType::getQ).reversed());
        
        return ct;
    }
    
    public static void main(String[] args)
    {
        System.out.println(Accept.parse("text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3"));
    }
}
