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
 * This class represents a force applied uniformly over a surface.
 * The system unit for this quantity is "kg/(m·s²)". By default, instances of
 * this class are showed in Pascal ("Pa").
 *
 * @author  <a href="mailto:jean-marie@dautelle.com">Jean-Marie Dautelle</a>
 * @version 1.0, October 24, 2004
 */
public class Pressure extends Quantity {

    /**
     * Holds the associated unit.
     */
    private final static Unit<Pressure> UNIT = SI.PASCAL;

    /**
     * Holds the factory for this class.
     */
    private final static Factory<Pressure> FACTORY = new Factory<Pressure>(UNIT) {
        protected Pressure create() {
            return new Pressure();
        }
    }.useFor(UNIT.getBaseUnits());

    /**
     * Represents a {@link Pressure} amounting to nothing.
     */
    public final static Pressure ZERO = Quantity.valueOf(0, UNIT);

    /**
     * Default constructor (allows for derivation).
     */
    protected Pressure() {
    }

    /**
     * Shows {@link Pressure} instances in the specified unit.
     *
     * @param unit the display unit for {@link Pressure} instances.
     */
    public static void showAs(Unit unit) {
        QuantityFormat.show(Pressure.class, unit);
    }

    private static final long serialVersionUID = 1L;

}
