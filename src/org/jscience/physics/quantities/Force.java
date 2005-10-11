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
 * This class represents a quantity that tends to produce an acceleration
 * of a body in the direction of its application. The system unit for
 * this quantity is "m·kg/s²".  By default, instances of this class are showed
 * in Newton ("N").
 *
 * @author  <a href="mailto:jean-marie@dautelle.com">Jean-Marie Dautelle</a>
 * @version 1.0, October 24, 2004
 */
public class Force extends Quantity {

    /**
     * Holds the associated unit.
     */
    private final static Unit<Force> UNIT = SI.NEWTON;

    /**
     * Holds the factory for this class.
     */
    private final static Factory<Force> FACTORY = new Factory<Force>(UNIT) {
        protected Force create() {
            return new Force();
        }
    }.useFor(UNIT.getBaseUnits());

    /**
     * Represents a {@link Force} amounting to nothing.
     */
    public final static Force ZERO = Quantity.valueOf(0, UNIT);

    /**
     * Default constructor (allows for derivation).
     */
    protected Force() {
    }

    /**
     * Shows {@link Force} instances in the specified unit.
     *
     * @param unit the display unit for {@link Force} instances.
     */
    public static void showAs(Unit unit) {
        QuantityFormat.show(Force.class, unit);
    }

    private static final long serialVersionUID = 1L;

}
