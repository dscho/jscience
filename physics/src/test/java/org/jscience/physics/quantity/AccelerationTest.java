/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2010 - JScience (http://jscience.org/)
 * All rights reserved.
 *
 * Permission to use, copy, modify, and distribute this software is
 * freely granted, provided that this notice is preserved.
 */
package org.jscience.physics.quantity;

import static org.jscience.physics.unit.SI.*;
import static org.jscience.physics.util.TestUtil.*;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Based on <b>Andrew Kennedy's Blog</b>, with assertions added.
 * 
 * @author  <a href="mailto:jsr275@catmedia.us">Werner Keil</a>
 * @version $Revision: 192 $, $Date: 2010-02-24 17:46:38 +0100 (mer., 24 févr. 2010) $
 * @see <a href="http://blogs.msdn.com/andrewkennedy/archive/2008/08/29/units-of-measure-in-f-part-one-introducing-units.aspx">Units of Measure in F#: Part One, Introducing Units</a>
 */
public class AccelerationTest {

    @Test
    public void testFallSpeed() {
    	double HEIGHT_AS_DOUBLE = 400.0d;
        QuantityFactory<Acceleration> accelerationFactory = QuantityFactory.getInstance(Acceleration.class);
        Acceleration gravityOnEarth = accelerationFactory.create(9.808, METRES_PER_SQUARE_SECOND);
        assertEquals("Can't get tuple element.", 9.808, gravityOnEarth.getValue());
        assertSame("Can't get tuple element.", METRES_PER_SQUARE_SECOND, gravityOnEarth.getUnit());
        assertEquals("Shall be equals to itself.", gravityOnEarth, gravityOnEarth);
        assertEquals("9.808 m/s²", gravityOnEarth.toString());

        QuantityFactory<Length> lengthFactory = QuantityFactory.getInstance(Length.class);
        Length heightOfBuilding = lengthFactory.create(HEIGHT_AS_DOUBLE, CENTI(METRE));
        assertEquals("Can't get tuple element.", HEIGHT_AS_DOUBLE, heightOfBuilding.getValue());
        assertEquals("Can't get tuple element.", CENTI(METRE), heightOfBuilding.getUnit());
        assertEquals("Shall be equals to itself.", heightOfBuilding, heightOfBuilding);
        assertEquals("400.0 cm", heightOfBuilding.toString());
        
        assertFalse("Expected non-equal.", gravityOnEarth.equals(heightOfBuilding));
        assertFalse("Expected different hash code", gravityOnEarth.hashCode() == heightOfBuilding.hashCode());

        // The following calculation implies a unit conversion from metres to centimetres.
        QuantityFactory<Velocity> velocityFactory = QuantityFactory.getInstance(Velocity.class);
        print("H: " + heightOfBuilding.toString());
        print("G: " + gravityOnEarth.toString());
        
        Velocity speedOfImpact = velocityFactory.create(
                Math.sqrt(2d * 
                gravityOnEarth.doubleValue(METRES_PER_SQUARE_SECOND) *
                heightOfBuilding.doubleValue(METRE)),
                METRES_PER_SECOND);
        assertEquals(8.8579907428265017350882265621754, speedOfImpact.getValue().doubleValue(), 1E-15);
        assertSame("Can't get tuple element.", METRES_PER_SECOND, speedOfImpact.getUnit());

        print(speedOfImpact.toString());
    }

}
