/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.exolin.health.servlets.html;

import org.exolin.health.servlets.HealthComponent;

/**
 *
 * @author tomgk
 */
public class HealthComponentWithParent
{
    private final HealthComponent parent;
    private final HealthComponent component;

    public HealthComponentWithParent(HealthComponent parent, HealthComponent component)
    {
        this.parent = parent;
        this.component = component;
    }

    public HealthComponent getComponent()
    {
        return component;
    }

    public HealthComponent getParent()
    {
        return parent;
    }
}
