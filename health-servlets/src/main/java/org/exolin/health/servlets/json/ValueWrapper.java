/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.exolin.health.servlets.json;

import org.exolin.health.servlets.Value;

/**
 *
 * @author tomgk
 */
public class ValueWrapper
{
    private final Value value;

    public ValueWrapper(Value value)
    {
        this.value = value;
    }

    public String getValue()
    {
        return value.getValue();
    }

    public EceptionWrapper getException()
    {
        return EceptionWrapper.wrap(value.getException());
    }
}
