package org.exolin.health.servlets;

import java.util.List;

/**
 *
 * @author tomgk
 * @deprecated currently not used
 */
public class SyntheticListOfType implements HealthComponent
{
    private final String elementType;
    private final List<HealthComponent> components;

    public SyntheticListOfType(String elementType, List<HealthComponent> components)
    {
        this.elementType = elementType;
        this.components = components;
    }

    @Override
    public String getType()
    {
        return "List("+elementType+")";
    }
    
    @Override
    public List<HealthComponent> getSubComponents()
    {
        return components;
    }
}
