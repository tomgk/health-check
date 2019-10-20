/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.exolin.health.servlets;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Eine Komponente, deren Health-State abgefragt werden kann.
 * 
 * @author tomgk
 */
public interface HealthComponent
{
    /**
     * @return der Typ
     */
    String getType();

    /**
     * @return der Name (sollte mit {@link #getType()} 
     */
    default public String getName()
    {
        return null;
    }
    
    /**
     * @return die Versionsnummer der Komponente
     */
    default public String getVersion()
    {
        return null;
    }
    
    /**
     * @return der Subtyp
     */
    default public String getSubType()
    {
        return null;
    }
    
    /**
     * @return der Status
     */
    default String getStatus()
    {
        return null;
    }
    
    /**
     * @return Zeitpunkt, wann die Komponente gestartet wurde
     */
    default public Long getStartTime()
    {
        return null;
    }
    
    /**
     * @return wie lange der Start gebraucht hat
     */
    default public Long getStartDuration()
    {
        return null;
    }
    
    /**
     * @return aufgetretene Exception beim Start
     */
    default public Throwable getStartupException()
    {
        return null;
    }
    
    /**
     * @return Subkomponenten
     */
    default List<HealthComponent> getSubComponents()
    {
        return Collections.emptyList();
    }
    
    /**
     * @return zusätzliche, spezifische Eigenschaften
     */
    default Map<String, Value> getProperties()
    {
        return Collections.emptyMap();
    }
}
