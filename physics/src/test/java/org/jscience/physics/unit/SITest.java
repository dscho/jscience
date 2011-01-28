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
import java.util.Map;
import static org.jscience.physics.util.TestUtil.print;
import static org.jscience.physics.unit.SI.SI;
import static org.jscience.physics.unit.SI.METRE;
import static org.jscience.physics.unit.USCustomary.LITER;
import static org.jscience.physics.unit.USCustomary.METER;

import org.jscience.physics.quantity.Volume;

import org.junit.Test;

import junit.framework.TestCase;

/**
 * PhysicsUnit test for class org.jscience.physics.unit.SI
 * @author  <a href="mailto:jsr275@catmedia.us">Werner Keil</a>
 */
public class SITest extends TestCase {

    public SITest(String testName) {
        super(testName);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public static void main(String[] args) {
        new SITest("").testGetInstance();
    }
    /**
     * Test of getInstance method, of class SI.
     */
    @Test
    public void testGetInstance() {
//        print("getInstance: " + USCustomary.GALLON_UK.divide(8) + " (" +
//              USCustomary.GALLON_UK.divide(8).getDimension().toString() + ")");
        SI result = SI.getInstance();

        // Checks SI contains the 7 SI base units.
        assertTrue(result.getUnits().contains(PhysicsUnit.valueOf("m")));
        assertTrue(result.getUnits().contains(PhysicsUnit.valueOf("kg")));
        assertTrue(result.getUnits().contains(PhysicsUnit.valueOf("s")));
        assertTrue(result.getUnits().contains(PhysicsUnit.valueOf("mol")));
        assertTrue(result.getUnits().contains(PhysicsUnit.valueOf("K")));
        assertTrue(result.getUnits().contains(PhysicsUnit.valueOf("cd")));
        assertTrue(result.getUnits().contains(PhysicsUnit.valueOf("A")));

        print(PhysicsUnit.valueOf("m").getDimension().toString());
    }

    @Test
    public void testSIvsUS() {
    	assertEquals(METRE, METER);
    }
    
	public void testVolume() {
		print("ML: ");
		PhysicsUnit<Volume> MILLILITER = MILLI(LITER);
		print(MILLILITER);
	}
}
