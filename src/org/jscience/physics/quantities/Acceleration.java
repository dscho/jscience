/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2005 - JScience (http://jscience.org/)
 * All rights reserved.
 * 
 * Permission to use, copy, modify, and distribute this software is
 * freely granted, provided that this notice is preserved.
 */
package org.jscience.physics.quantities;

import static org.jscience.physics.units.SI.*;
import org.jscience.physics.units.Unit;

/**
 * This class represents the rate of change of velocity with respect to time.
 * The system unit for this quantity is "m/s²".
 *
 * @author  <a href="mailto:jean-marie@dautelle.com">Jean-Marie Dautelle</a>
 * @version 2.0, June 16, 2005
 */
public class Acceleration extends Quantity {

    /**
     * Holds the acceleration unit.
     */
    private final static Unit<Acceleration> UNIT = METER.divide(SECOND.pow(2));

    /**
     * Holds the factory for this class.
     */
    private final static Factory<Acceleration> FACTORY = new Factory<Acceleration>(
            UNIT) {
        protected Acceleration create() {
            return new Acceleration();
        }
    };

    /**
     * Represents a {@link Acceleration} amounting to nothing.
     */
    public final static Acceleration ZERO = Quantity.valueOf(0, UNIT);

    /**
     * Holds the standard acceleration of gravity (exact).
     */
    public final static Acceleration GRAVITY = Quantity.valueOf(9.80665, UNIT);

    /**
     * Default constructor (allows for derivation).
     */
    protected Acceleration() {
    }

    /**
     * Shows {@link Acceleration} instances in the specified unit.
     *
     * @param unit the display unit for {@link Acceleration} instances.
     */
    public static void showAs(Unit unit) {
        QuantityFormat.show(Acceleration.class, unit);
    }

    private static final long serialVersionUID = 1L;
}