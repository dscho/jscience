/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2010 - JScience (http://jscience.org/)
 * All rights reserved.
 *
 * Permission to use, copy, modify, and distribute this software is
 * freely granted, provided that this notice is preserved.
 */
package org.jscience.physics.unit;

import org.jscience.physics.unit.PhysicsUnit;
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
    PhysicsUnit<Dimensionless> one;

    /* (non-Javadoc)
     * @see junit.framework.TestCase#setUp()
     */
    @Before
    public void setUp() throws Exception {
//      super.setUp();

        one = PhysicsUnit.ONE;
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
     * Test method for {@link org.jscience.physics.unit.PhysicsUnit#toMetric()}.
     */
    @Test
    public void testToMetric() {
        PhysicsUnit<? extends Quantity> su = one.toMetric();
        assertTrue(su.isUnscaledMetric());
    }

    /**
     * Test method for {@link org.jscience.physics.unit.PhysicsUnit#getConverterTo}.
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
     * Test method for {@link org.jscience.physics.unit.PhysicsUnit#isMetric()}.
     */
    @Test
    public void testIsMetric() {
        boolean standard = one.isUnscaledMetric();
        assertTrue(standard);
    }

    /**
     * Test method for {@link org.jscience.physics.unit.PhysicsUnit#asType(java.lang.Class)}.
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
     * Test method for {@link org.jscience.physics.unit.PhysicsUnit#getDimension()}.
     */
    @Test
    public void testGetDimension() {
        one.getDimension();
    }

    /**
     * Test method for {@link org.jscience.physics.unit.PhysicsUnit#alternate(java.lang.String)}.
     */
    @Test
    public void testAlternate() {
        PhysicsUnit<? extends Quantity> alternate = one.alternate(null);
        assertNotNull(alternate);
    }

    /**
     * Test method for {@link org.jscience.physics.unit.PhysicsUnit#compound(org.jscience.physics.unit.PhysicsUnit)}.
     */
    /*public void testCompound() {
        PhysicsUnit<? extends Quantity> compound = one.compound(one);
        assertNotNull(compound);
    }*/

    /**
     * Test method for {@link org.jscience.physics.unit.PhysicsUnit#transform}.
     */
    @Test
    public void testTransform() {
        PhysicsUnit<? extends Quantity> result = one.transform(UnitConverter.IDENTITY);
        assertEquals(result, one);
    }

    /**
     * Test method for {@link org.jscience.physics.unit.PhysicsUnit#add(double)}.
     */
    @Test
    public void testAdd() {
        PhysicsUnit<? extends Quantity> result = one.add(10);
        assertNotSame(result, one);
    }

    /**
     * Test method for {@link org.jscience.physics.unit.PhysicsUnit#multiply(long)}.
     */
    @SuppressWarnings("unchecked")
	@Test
    public void testMultiplyLong() {
        PhysicsUnit<? extends Quantity> result = one.multiply(2L);
        assertNotSame(result, one);
    }

    /**
     * Test method for {@link org.jscience.physics.unit.PhysicsUnit#multiply(double)}.
     */
    @Test
    public void testMultiplyDouble() {
        PhysicsUnit<? extends Quantity> result = one.multiply(2.1);
        assertNotSame(result, one);
    }

    /**
     * Test method for {@link org.jscience.physics.unit.PhysicsUnit#multiply(org.jscience.physics.unit.PhysicsUnit)}.
     */
    @Test
    public void testMultiplyUnitOfQ() {
        PhysicsUnit<? extends Quantity> result = one.multiply(one);
        assertEquals(result, one);
    }

    /**
     * Test method for {@link org.jscience.physics.unit.PhysicsUnit#inverse()}.
     */
    @Test
    public void testInverse() {
        PhysicsUnit<? extends Quantity> result = one.inverse();
        assertEquals(result, one);
    }

    /**
     * Test method for {@link org.jscience.physics.unit.PhysicsUnit#divide(long)}.
     */
    @Test
    public void testDivideLong() {
        PhysicsUnit<? extends Quantity> result = one.divide(2L);
        assertNotSame(result, one);
    }

    /**
     * Test method for {@link org.jscience.physics.unit.PhysicsUnit#divide(double)}.
     */
    @Test
    public void testDivideDouble() {
        PhysicsUnit<? extends Quantity> result = one.divide(3.2);
        assertNotSame(result, one);
    }

    /**
     * Test method for {@link org.jscience.physics.unit.PhysicsUnit#divide(org.jscience.physics.unit.PhysicsUnit)}.
     */
    @Test
    public void testDivideUnitOfQ() {
        PhysicsUnit<? extends Quantity> result = one.divide(one);
        assertEquals(result, one);
    }

    /**
     * Test method for {@link org.jscience.physics.unit.PhysicsUnit#root(int)}.
     */
    @Test
    public void testRoot() {
        PhysicsUnit<? extends Quantity> root = one.root(2);
        assertEquals(root, one);
    }

    /**
     * Test method for {@link org.jscience.physics.unit.PhysicsUnit#pow(int)}.
     */
    @Test
    public void testPow() {
        PhysicsUnit<? extends Quantity> result = one.pow(10);
        assertEquals(result, one);
    }
}
