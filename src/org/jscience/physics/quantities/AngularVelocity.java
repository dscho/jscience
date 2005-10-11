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
 * This class represents the rate of change of angular displacement
 * with respect to time. The system unit for this quantity is "rad/s".
 *
 * @author  <a href="mailto:jean-marie@dautelle.com">Jean-Marie Dautelle</a>
 * @version 1.0, October 24, 2004
 */
public class AngularVelocity extends Quantity {

    /**
     * Holds the associated unit.
     */
    private final static Unit<AngularVelocity> UNIT = SI.RADIAN.divide(SI.SECOND);

    /**
     * Holds the factory for this class.
     */
    private final static Factory<AngularVelocity> FACTORY = new Factory<AngularVelocity>(
            UNIT) {
        protected AngularVelocity create() {
            return new AngularVelocity();
        }
    };

    /**
     * Represents a {@link AngularVelocity} amounting to nothing.
     */
    public final static AngularVelocity ZERO = Quantity.valueOf(0, UNIT);

    /**
     * Default constructor (allows for derivation).
     */
    protected AngularVelocity() {
    }

    /**
     * Shows {@link AngularVelocity} instances in the specified unit.
     *
     * @param unit the display unit for {@link AngularVelocity} instances.
     */
    public static void showAs(Unit unit) {
        QuantityFormat.show(AngularVelocity.class, unit);
    }

    private static final long serialVersionUID = 1L;

}
