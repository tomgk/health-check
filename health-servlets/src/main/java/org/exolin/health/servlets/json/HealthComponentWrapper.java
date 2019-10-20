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
import java.util.stream.Collectors;
import org.exolin.health.servlets.HealthComponent;
import org.exolin.health.servlets.Value;

/**
 *
 * @author tomgk
 */
@JsonPropertyOrder({
    "type",
    "localName",
    "localVersion",
    "localType",
    "status",
    "properties",
    "startTime",
    "startDuration",
    "startupException",
    "subComponents"
})
public class HealthComponentWrapper
{
    private final HealthComponent component;

    public HealthComponentWrapper(HealthComponent component)
    {
        this.component = component;
    }

    public String getType()
    {
        return component.getType();
    }

    public String getLocalName()
    {
        return component.getName();
    }

    public String getLocalVersion()
    {
        return component.getVersion();
    }

    public String getLocalType()
    {
        return component.getSubType();
    }

    public String getStatus()
    {
        return component.getStatus();
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

        return subComponents.stream().map(HealthComponentWrapper::new).collect(Collectors.toList());
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
