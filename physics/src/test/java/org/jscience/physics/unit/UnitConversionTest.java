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
import org.jscience.physics.unit.USCustomary;
import org.unitsofmeasurement.unit.UnitConverter;
import static org.jscience.physics.util.TestUtil.*;
import static org.junit.Assert.*;

import org.jscience.physics.quantity.Length;
import org.jscience.physics.quantity.QuantityFactory;
import static org.jscience.physics.unit.SI.*;

import org.junit.Test;

/**
 * @author  <a href="mailto:jsr275@catmedia.us">Werner Keil</a>
 */
public class UnitConversionTest {
    static final PhysicalUnit<Length> unit1 = SI.METRE;
    static final PhysicalUnit<Length> unit2 = SI.CENTI(METRE);
    static final PhysicalUnit<Length> unit3 = USCustomary.FOOT;
    
    protected void setUp() throws Exception {
//        super.setUp();
    }

    protected void tearDown() throws Exception {
//        super.tearDown();
    }

    @Test
    public void testConvert() {
        try {
            //unitConverter = unit1.getConverterTo(unit2);
            Length m1 = QuantityFactory.getInstance(Length.class).create(23, unit1);
            //reset = true;

            UnitConverter converter = unit1.getConverterTo(unit2);
//            Length m2 = m1.to(unit2, MathContext.DECIMAL32);
            Length m2 = QuantityFactory.getInstance(Length.class).create(converter.convert(23), unit2);
      //      assertEquals(2300d, m2.doubleValue(unit2));
            String operation;
            if (isTestOutput()) {
                operation = m1 + " -> " + unit2;
                String number = m2.toString();
                StringBuilder build = new StringBuilder(operation);
                build.append(": ");
                build.append(number);
                print(build.toString());
            }
            
            converter = unit2.getConverterTo(unit3);
            if (isTestOutput()) {
                operation = unit2 + " -> " + unit3;
                print(operation);
            }
            double foot1 = converter.convert(m2.getValue().doubleValue());
            double foot2 = m2.doubleValue(unit3);
            assertEquals(Double.valueOf(foot1), Double.valueOf(foot2));
            print(foot2);
        } catch (Exception e) {
            //reset = true;
            fail(e.getMessage());
        }
    }
}
