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
 * This class represents the extent of something along its greatest dimension
 * or the extent of space between two objects or places. The system unit for
 * this quantity is "m" (Système International d'Unités).
 *
 * @author  <a href="mailto:jean-marie@dautelle.com">Jean-Marie Dautelle</a>
 * @version 1.0, October 24, 2004
 */
public class Length extends Quantity {

    /**
     * Holds the associated unit.
     */
    private final static Unit<Length> UNIT = SI.METER;

    /**
     * Holds the factory for this class.
     */
    private final static Factory<Length> FACTORY = new Factory<Length>(UNIT) {
        protected Length create() {
            return new Length();
        }
    };

    /**
     * Represents a {@link Length} amounting to nothing.
     */
    public final static Length ZERO = Quantity.valueOf(0, UNIT);

    /**
     * Default constructor (allows for derivation).
     */
    protected Length() {
    }

    /**
     * Shows {@link Length} instances in the specified unit.
     *
     * @param unit the display unit for {@link Length} instances.
     */
    public static void showAs(Unit unit) {
        QuantityFormat.show(Length.class, unit);
    }

    /////////////////////
    // LENGTH SPECIFIC //
    /////////////////////

    /**
     * Returns the length of a circular arc.
     *
     * @param  radius the circle radius.
     * @param  theta the central angle.
     * @return the length of the specified circular arc.
     */
    public static Length valueOf(Length radius, Angle theta) {
        return theta.times(radius).to(UNIT);
    }

    private static final long serialVersionUID = 1L;
}
