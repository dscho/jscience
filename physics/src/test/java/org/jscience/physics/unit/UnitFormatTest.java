/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2010 - JScience (http://jscience.org/)
 * All rights reserved.
 *
 * Permission to use, copy, modify, and distribute this software is
 * freely granted, provided that this notice is preserved.
 */
package org.jscience.physics.unit;

import org.jscience.physics.unit.PhysicalUnit;
import org.jscience.physics.unit.format.UnitFormatImpl;
import static org.jscience.physics.unit.SI.*;
import static org.jscience.physics.unit.USCustomary.*;

import static org.jscience.physics.util.TestUtil.*;
import static org.junit.Assert.*;

import java.math.BigInteger;
import java.util.Locale;

import org.jscience.physics.quantity.Length;

import org.junit.Before;
import org.junit.Test;

/**
 * @author  <a href="mailto:jsr275@catmedia.us">Werner Keil</a>
 */
public class UnitFormatTest {
  private static final String COMPARISON_FOOT = "ft";
    private static final String COMPARISON_KM = "km";
    private static final Locale COMPARISON_LOCALE = Locale.UK;

    UnitFormatImpl format;
    PhysicalUnit<Length> cm;
    PhysicalUnit<Length> mm;
    PhysicalUnit<Length> foot;
    
    @Before
    public void setUp() throws Exception {
        //setName(UCUMFormatTest.class.getSimpleName());

        cm = CENTI(METRE);
        mm = MILLI(METRE);
        foot = FOOT;

//        print("Running " + getClass().getSimpleName() + "...");
    }

    protected void tearDown() throws Exception {
//        super.tearDown();
    }

    @Test
    public void testDefault() {
        format = UnitFormatImpl.getInstance();
        //format.format(unit, appendable);
        String formattedText = format.format(cm);
        print(formattedText);
        //System.out.println(unit2);
        formattedText = format.format(mm);
        print(formattedText);
        formattedText = format.format(foot);
        print(formattedText);
    }

    @Test
    public void testGetInstanceLocale() {
        format = UnitFormatImpl.getInstance(COMPARISON_LOCALE);
        String formattedText = format.format(cm);
        print(formattedText);
        //System.out.println(unit2);
        formattedText = format.format(mm);
        print(formattedText);
        formattedText = format.format(foot);
        print(formattedText);
        assertEquals(COMPARISON_FOOT, formattedText);
    }

    @Test
    public void testUSVolt() {
        print(ELECTRON_VOLT.getDimension().toString());
        print(ELECTRON_VOLT.toString());
    }
    
    @Test
    public void testSubMultiples() {
    	PhysicalUnit<Length> u = CENTI(METRE);
    	print(u.toString());
    }

    /**
     * Tests the {@link PhysicalUnit#toString()} method, which is backed by {@link UnitFormatImpl}.
     *
     * @see http://kenai.com/jira/browse/JSR_275-43
     */
    @Test
    public void testToString() {
        assertEquals("m", METRE.toString());
        // Multiples
        assertEquals(COMPARISON_KM, KILO(METRE).toString());
        assertEquals(COMPARISON_KM, METRE.multiply(1000).toString());
        assertEquals(COMPARISON_KM, METRE.multiply(1000d).toString());
        assertEquals("Tm", METRE.multiply(BigInteger.TEN.pow(12).longValue()).toString());
        // Submultiples
        assertEquals("cm", METRE.divide(100d).toString());
        assertEquals("mm", METRE.divide(1000d).toString());
        assertEquals("fm", METRE.divide(BigInteger.TEN.pow(15).longValue()).toString());
    }
}
