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
 * This class represents a distance traveled divided by the time of travel.
 * The system unit for this quantity is "m/s".
 *
 * @author  <a href="mailto:jean-marie@dautelle.com">Jean-Marie Dautelle</a>
 * @version 1.0, October 24, 2004
 */
public class Velocity extends Quantity {

    /**
     * Holds the associated unit.
     */
    private final static Unit<Velocity> UNIT = SI.METER_PER_SECOND;

    /**
     * Holds the factory for this class.
     */
    private final static Factory<Velocity> FACTORY = new Factory<Velocity>(
            UNIT) {
        protected Velocity create() {
            return new Velocity();
        }
    };

    /**
     * Represents a {@link Velocity} amounting to nothing.
     */
    public final static Velocity ZERO = Quantity.valueOf(0, UNIT);

    /**
     * Holds the speed of light in vacuum (exact).
     */
    public final static Velocity SPEED_OF_LIGHT = Quantity.valueOf(299792458, UNIT);

    /**
     * Default constructor (allows for derivation).
     */
    protected Velocity() {
    }

    /**
     * Shows {@link Velocity} instances in the specified unit.
     *
     * @param unit the display unit for {@link Velocity} instances.
     */
    public static void showAs(Unit unit) {
        QuantityFormat.show(Velocity.class, unit);
    }

    private static final long serialVersionUID = 1L;

}
