/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.exolin.health.servlets.json;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 *
 * @author tomgk
 */
@JsonPropertyOrder({
    "name",
    "message"
})
public class EceptionWrapper
{
    private final Throwable exception;

    private EceptionWrapper(Throwable exception)
    {
        this.exception = exception;
    }

    static EceptionWrapper wrap(Throwable exception)
    {
        return exception != null ? new EceptionWrapper(exception) : null;
    }

    public String getType()
    {
        return exception.getClass().getName();
    }

    public String getMessage()
    {
        return exception.getMessage();
    }
}
