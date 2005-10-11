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
 * This class represents the rate at which work is done. The system unit
 * for this quantity is "m²·kg/s³". By default, instances of this class are
 * showed in Watt ("W").
 *
 * @author  <a href="mailto:jean-marie@dautelle.com">Jean-Marie Dautelle</a>
 * @version 1.0, October 24, 2004
 */
public class Power extends Quantity {

    /**
     * Holds the associated unit.
     */
    private final static Unit<Power> UNIT = SI.WATT;

    /**
     * Holds the factory for this class.
     */
    private final static Factory<Power> FACTORY = new Factory<Power>(UNIT) {
        protected Power create() {
            return new Power();
        }
    }.useFor(UNIT.getBaseUnits());

    /**
     * Represents a {@link Power} amounting to nothing.
     */
    public final static Power ZERO = Quantity.valueOf(0, UNIT);

    /**
     * Default constructor (allows for derivation).
     */
    protected Power() {
    }

    /**
     * Shows {@link Power} instances in the specified unit.
     *
     * @param unit the display unit for {@link Power} instances.
     */
    public static void showAs(Unit unit) {
        QuantityFormat.show(Power.class, unit);
    }

    private static final long serialVersionUID = 1L;

}
