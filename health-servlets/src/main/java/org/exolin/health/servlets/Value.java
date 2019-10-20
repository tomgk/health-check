/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.exolin.health.servlets;

/**
 *
 * @author tomgk
 */
public class Value
{
    private final String value;
    private final Throwable exception;

    public Value(String value, Throwable exception)
    {
        this.value = value;
        this.exception = exception;
    }
    
    public static Value value(String value)
    {
        return new Value(value, null);
    }
    
    public static Value failed(Throwable exception)
    {
        return new Value(null, exception);
    }

    public String getValue()
    {
        return value;
    }

    public Throwable getException()
    {
        return exception;
    }
}
