/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.exolin.health.servlets.json;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.util.AbstractMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import org.exolin.health.servlets.HealthComponent;
import org.exolin.health.servlets.LinkBuilder;
import org.exolin.health.servlets.Value;

/**
 *
 * @author tomgk
 */
@JsonPropertyOrder({
    "type",
    "name",
    "version",
    "subType",
    "url",
    "status",
    "specificStatus",
    "properties",
    "startTime",
    "startDuration",
    "startupException",
    "subComponents"
})
public class HealthComponentWrapper
{
    private final HealthComponent component;
    private final LinkBuilder linkBuilder;

    public HealthComponentWrapper(HealthComponent component, LinkBuilder linkBuilder)
    {
        this.component = Objects.requireNonNull(component, "component");
        this.linkBuilder = Objects.requireNonNull(linkBuilder, "linkBuilder");
    }
    
    public String getUrl()
    {
        String name = getName();
        if(name == null)
            return null;
        
        String type = getType();
        
        return linkBuilder.link(type, name);
    }

    public String getType()
    {
        return component.getType();
    }

    public String getName()
    {
        return component.getName();
    }

    public String getVersion()
    {
        return component.getVersion();
    }

    public String getSubType()
    {
        return component.getSubType();
    }

    public String getStatus()
    {
        return component.getStatus().name().toLowerCase();
    }

    public String getSpecificStatus()
    {
        return component.getSpecificStatus();
    }

    public Long getStartTime()
    {
        return component.getStartTime();
    }

    public Long getStartDuration()
    {
        return component.getStartDuration();
    }

    public EceptionWrapper getStartupException()
    {
        return EceptionWrapper.wrap(component.getStartupException());
    }

    public List<HealthComponentWrapper> getSubComponents()
    {
        List<HealthComponent> subComponents = component.getSubComponents();

        //Damit nicht angezeigt
        if(subComponents.isEmpty())
            return null;

        return subComponents.stream().map(sub -> new HealthComponentWrapper(sub, linkBuilder)).collect(Collectors.toList());
    }

    public Map<String, ValueWrapper> getProperties()
    {
        Map<String, Value> properties = component.getProperties();

        //Damit nicht angezeigt
        if(properties.isEmpty())
            return null;

        return properties.entrySet()
                .stream()
                .map(e -> new AbstractMap.SimpleImmutableEntry<>(e.getKey(), new ValueWrapper(e.getValue())))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }
}
