/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2005 - JScience (http://jscience.org/)
 * All rights reserved.
 * 
 * Permission to use, copy, modify, and distribute this software is
 * freely granted, provided that this notice is preserved.
 */
package org.jscience.physics.quantities;

import org.jscience.physics.units.SI;
import org.jscience.physics.units.Unit;

/**
 * This class represents the angle formed by three or more planes intersecting
 * at a common point. The system unit for this quantity is "sr".
 * This quantity is dimensionless.
 *
 * @author  <a href="mailto:jean-marie@dautelle.com">Jean-Marie Dautelle</a>
 * @version 1.0, October 24, 2004
 */
public class SolidAngle extends Dimensionless {

    /**
     * Holds the associated unit.
     */
    private final static Unit<SolidAngle> UNIT = SI.STERADIAN;

    /**
     * Holds the factory for this class.
     */
    private final static Factory<SolidAngle> FACTORY = new Factory<SolidAngle>(
            UNIT) {
        protected SolidAngle create() {
            return new SolidAngle();
        }
    };

    /**
     * Represents a {@link SolidAngle} amounting to nothing.
     */
    public final static SolidAngle ZERO = Quantity.valueOf(0, UNIT);

    /**
     * Default constructor (allows for derivation).
     */
    protected SolidAngle() {
    }

    /**
     * Shows {@link SolidAngle} instances in the specified unit.
     *
     * @param unit the display unit for {@link SolidAngle} instances.
     */
    public static void showAs(Unit unit) {
        QuantityFormat.show(SolidAngle.class, unit);
    }

    private static final long serialVersionUID = 1L;

}
