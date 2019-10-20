/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.exolin.health.servlets.html;

import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

/**
 *
 * @author tomgk
 */
public class Formatter
{
    static void writeException(PrintWriter out, Throwable e)
    {
        out.print("<pre>");
        e.printStackTrace(out);
        out.print("</pre>");
    }
    
    static String formatTimestamp(Long timestamp)
    {
        if(timestamp == null)
            return "NULL";
        
        return LocalDateTime.ofEpochSecond(timestamp/1000, (int)(timestamp%1000*1000000), ZoneOffset.UTC).toString()+" UTC";
    }
    
    static String formatDuration(Long duration)
    {
        if(duration == null)
            return "NULL";
        
        if(duration < 1000)
            return duration+" ms";
        
        else
            return duration/1000+" s, "+duration%1000+" ms";
    }
}
