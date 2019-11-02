package org.exolin.health.servlets;

import java.util.Arrays;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests für {@link Finder}
 * 
 * @author tomgk
 */
public class FinderTest
{
    static class HealthComponentImpl implements HealthComponent
    {
        private final String type;
        private final String name;
        private final List<HealthComponent> subComponents;

        public HealthComponentImpl(String type, String name, HealthComponent...subComponents)
        {
            this.type = type;
            this.name = name;
            this.subComponents = Arrays.asList(subComponents);
        }

        @Override
        public String getType()
        {
            return type;
        }

        @Override
        public String getName()
        {
            return name;
        }

        @Override
        public List<HealthComponent> getSubComponents()
        {
            return subComponents;
        }
    }
    
    HealthComponentImpl sub1, sub2;

    HealthComponentImpl root = new HealthComponentImpl("root", "xyz",
            sub1 = new HealthComponentImpl("sub", "name1",
                    sub2 = new HealthComponentImpl("sub", "name2"),
                    new HealthComponentImpl("sub", null)  //Namenslose sollten zu keinem Problem führen
            )
    );
    
    @Test
    public void testFind_Root()
    {
        assertSame(root, Finder.find(root, "root", "xyz"));
    }
    
    @Test
    public void testFind_Level1()
    {
        assertSame(sub1, Finder.find(root, "sub", "name1"));
    }
    
    @Test
    public void testFind_Level2()
    {
        assertSame(sub2, Finder.find(root, "sub", "name2"));
    }
    
    @Test
    public void testFind_NotFound()
    {
        assertNull(Finder.find(root, "sub", "name3"));
    }
}
