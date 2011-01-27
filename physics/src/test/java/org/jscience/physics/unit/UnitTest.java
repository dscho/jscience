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
import org.jscience.physics.unit.SI;
import org.unitsofmeasurement.unit.UnitConverter;
import static org.junit.Assert.*;
import static org.jscience.physics.util.TestUtil.*;

import org.jscience.physics.quantity.Quantity;
import org.jscience.physics.quantity.Dimensionless;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;


/**
 * @author  <a href="mailto:jsr275@catmedia.us">Werner Keil</a>
 */
public class UnitTest {
    PhysicalUnit<Dimensionless> one;

    /* (non-Javadoc)
     * @see junit.framework.TestCase#setUp()
     */
    @Before
    public void setUp() throws Exception {
//      super.setUp();

        one = PhysicalUnit.ONE;
    }

    /* (non-Javadoc)
     * @see junit.framework.TestCase#tearDown()
     */
    @After
    public void tearDown() throws Exception {
//      super.tearDown();

        one = null;
    }

    /**
     * Test method for {@link org.jscience.physics.unit.PhysicalUnit#toMetric()}.
     */
    @Test
    public void testToMetric() {
        PhysicalUnit<? extends Quantity> su = one.toMetric();
        assertTrue(su.isUnscaledMetric());
    }

    /**
     * Test method for {@link org.jscience.physics.unit.PhysicalUnit#getConverterTo}.
     */
    @Test
    public void testConverterToSI() {
        Double factor = new Double(10);
        UnitConverter converter = one.getConverterTo(one);
        Double result = converter.convert(factor.doubleValue());
        assertEquals(result, factor);
        print(result.toString());
    }

    /**
     * Test method for {@link org.jscience.physics.unit.PhysicalUnit#isMetric()}.
     */
    @Test
    public void testIsMetric() {
        boolean standard = one.isUnscaledMetric();
        assertTrue(standard);
    }

    /**
     * Test method for {@link org.jscience.physics.unit.PhysicalUnit#asType(java.lang.Class)}.
     */
  @Test
  public void testAsType() {
      one.asType(Dimensionless.class);
      try {
          SI.METRE.asType(Dimensionless.class);
          fail("Should have raised ClassCastException");
      } catch (ClassCastException e) {
          assertTrue(true);
      }
  }

    /**
     * Test method for {@link org.jscience.physics.unit.PhysicalUnit#getDimension()}.
     */
    @Test
    public void testGetDimension() {
        one.getDimension();
    }

    /**
     * Test method for {@link org.jscience.physics.unit.PhysicalUnit#alternate(java.lang.String)}.
     */
    @Test
    public void testAlternate() {
        PhysicalUnit<? extends Quantity> alternate = one.alternate(null);
        assertNotNull(alternate);
    }

    /**
     * Test method for {@link org.jscience.physics.unit.PhysicalUnit#compound(org.jscience.physics.unit.PhysicalUnit)}.
     */
    /*public void testCompound() {
        PhysicalUnit<? extends Quantity> compound = one.compound(one);
        assertNotNull(compound);
    }*/

    /**
     * Test method for {@link org.jscience.physics.unit.PhysicalUnit#transform}.
     */
    @Test
    public void testTransform() {
        PhysicalUnit<? extends Quantity> result = one.transform(UnitConverter.IDENTITY);
        assertEquals(result, one);
    }

    /**
     * Test method for {@link org.jscience.physics.unit.PhysicalUnit#add(double)}.
     */
    @Test
    public void testAdd() {
        PhysicalUnit<? extends Quantity> result = one.add(10);
        assertNotSame(result, one);
    }

    /**
     * Test method for {@link org.jscience.physics.unit.PhysicalUnit#multiply(long)}.
     */
    @SuppressWarnings("unchecked")
	@Test
    public void testMultiplyLong() {
        PhysicalUnit<? extends Quantity> result = one.multiply(2L);
        assertNotSame(result, one);
    }

    /**
     * Test method for {@link org.jscience.physics.unit.PhysicalUnit#multiply(double)}.
     */
    @Test
    public void testMultiplyDouble() {
        PhysicalUnit<? extends Quantity> result = one.multiply(2.1);
        assertNotSame(result, one);
    }

    /**
     * Test method for {@link org.jscience.physics.unit.PhysicalUnit#multiply(org.jscience.physics.unit.PhysicalUnit)}.
     */
    @Test
    public void testMultiplyUnitOfQ() {
        PhysicalUnit<? extends Quantity> result = one.multiply(one);
        assertEquals(result, one);
    }

    /**
     * Test method for {@link org.jscience.physics.unit.PhysicalUnit#inverse()}.
     */
    @Test
    public void testInverse() {
        PhysicalUnit<? extends Quantity> result = one.inverse();
        assertEquals(result, one);
    }

    /**
     * Test method for {@link org.jscience.physics.unit.PhysicalUnit#divide(long)}.
     */
    @Test
    public void testDivideLong() {
        PhysicalUnit<? extends Quantity> result = one.divide(2L);
        assertNotSame(result, one);
    }

    /**
     * Test method for {@link org.jscience.physics.unit.PhysicalUnit#divide(double)}.
     */
    @Test
    public void testDivideDouble() {
        PhysicalUnit<? extends Quantity> result = one.divide(3.2);
        assertNotSame(result, one);
    }

    /**
     * Test method for {@link org.jscience.physics.unit.PhysicalUnit#divide(org.jscience.physics.unit.PhysicalUnit)}.
     */
    @Test
    public void testDivideUnitOfQ() {
        PhysicalUnit<? extends Quantity> result = one.divide(one);
        assertEquals(result, one);
    }

    /**
     * Test method for {@link org.jscience.physics.unit.PhysicalUnit#root(int)}.
     */
    @Test
    public void testRoot() {
        PhysicalUnit<? extends Quantity> root = one.root(2);
        assertEquals(root, one);
    }

    /**
     * Test method for {@link org.jscience.physics.unit.PhysicalUnit#pow(int)}.
     */
    @Test
    public void testPow() {
        PhysicalUnit<? extends Quantity> result = one.pow(10);
        assertEquals(result, one);
    }
}
