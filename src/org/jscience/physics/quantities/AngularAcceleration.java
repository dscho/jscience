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
 * This class represents the rate of change of angular velocity with respect
 * to time. The system unit for this quantity is "rad/s²".
 *
 * @author  <a href="mailto:jean-marie@dautelle.com">Jean-Marie Dautelle</a>
 * @version 1.0, October 24, 2004
 */
public class AngularAcceleration extends Quantity {

    /**
     * Holds the associated unit.
     */
    private final static Unit<AngularAcceleration> 
         SYSTEM_UNIT = SI.RADIAN.divide(SI.SECOND.pow(2));

    /**
     * Holds the associated unit.
     */
    private final static Unit<AngularAcceleration> UNIT = SI.RADIAN.divide(SI.SECOND.pow(2));

    /**
     * Holds the factory for this class.
     */
    private final static Factory<AngularAcceleration> FACTORY = new Factory<AngularAcceleration>(
            UNIT) {
        protected AngularAcceleration create() {
            return new AngularAcceleration();
        }
    };

    /**
     * Represents a {@link AngularAcceleration} amounting to nothing.
     */
    public final static AngularAcceleration ZERO = Quantity.valueOf(0, UNIT);

    /**
     * Default constructor (allows for derivation).
     */
    protected AngularAcceleration() {
    }

    /**
     * Shows {@link AngularAcceleration} instances in the specified unit.
     *
     * @param unit the display unit for {@link AngularAcceleration} instances.
     */
    public static void showAs(Unit unit) {
        QuantityFormat.show(AngularAcceleration.class, unit);
    }

    private static final long serialVersionUID = 1L;
}
