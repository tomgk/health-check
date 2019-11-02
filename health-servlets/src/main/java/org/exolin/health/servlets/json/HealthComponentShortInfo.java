package org.exolin.health.servlets.json;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.exolin.health.servlets.HealthComponent;
import org.exolin.health.servlets.LinkBuilder;

/**
 *
 * @author tomgk
 */
@JsonPropertyOrder({
    "type",
    "name",
    "url"
})
public class HealthComponentShortInfo
{
    private final HealthComponent component;
    private final LinkBuilder linkBuilder;

    public HealthComponentShortInfo(HealthComponent component, LinkBuilder linkBuilder)
    {
        this.component = component;
        this.linkBuilder = linkBuilder;
    }

    public String getType()
    {
        return component.getType();
    }
    
    public String getName()
    {
        return component.getName();
    }
    
    public String getUrl()
    {
        String name = component.getName();
        
        if(name == null)
            return null;
        
        return linkBuilder.link(component.getType(), name);
    }
}
