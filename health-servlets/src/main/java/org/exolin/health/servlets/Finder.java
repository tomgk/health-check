package org.exolin.health.servlets;

/**
 *
 * @author tomgk
 */
public class Finder
{
    public static HealthComponent find(HealthComponent current, String type, String name)
    {
        System.out.println("Current: "+current.getType()+":"+current.getName());
        
        if(type.equals(current.getType()) && name.equals(current.getName()))
            return current;
        
        for(HealthComponent c: current.getSubComponents())
        {
            HealthComponent s = find(c, type, name);
            if(s != null)
                return s;
        }

        return null;
    }
}
