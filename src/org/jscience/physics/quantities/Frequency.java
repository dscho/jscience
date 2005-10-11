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
 * This class represents the number of times a specified phenomenon occurs
 * within a specified interval. The system unit for this quantity is "1/s".
 * By default, instances of this quantity are showed in Hertz ("Hz").
 *
 *
 * @author  <a href="mailto:jean-marie@dautelle.com">Jean-Marie Dautelle</a>
 * @version 1.0, October 24, 2004
 */
public class Frequency extends Quantity {

    /**
     * Holds the associated unit.
     */
    private final static Unit<Frequency> UNIT = SI.HERTZ;

    /**
     * Holds the factory for this class.
     */
    private final static Factory<Frequency> FACTORY = new Factory<Frequency>(
            UNIT) {
        protected Frequency create() {
            return new Frequency();
        }
    }.useFor(UNIT.getBaseUnits());

    /**
     * Represents a {@link Frequency} amounting to nothing.
     */
    public final static Frequency ZERO = Quantity.valueOf(0, UNIT);

    /**
     * Default constructor (allows for derivation).
     */
    protected Frequency() {
    }

    /**
     * Shows {@link Frequency} instances in the specified unit.
     *
     * @param unit the display unit for {@link Frequency} instances.
     */
    public static void showAs(Unit unit) {
        QuantityFormat.show(Frequency.class, unit);
    }

    private static final long serialVersionUID = 1L;

}
