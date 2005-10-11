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
 * This class represents an illuminance. The system unit for this quantity
 * is "cd·sr/m²".  By default, instances of this class are showed in Lux ("lx").
 *
 * @author  <a href="mailto:jean-marie@dautelle.com">Jean-Marie Dautelle</a>
 * @version 6.0, June 13, 2004
 */
public class Illuminance extends Quantity {

    /**
     * Holds the associated unit.
     */
    private final static Unit<Illuminance> UNIT = SI.LUX;

    /**
     * Holds the factory for this class.
     */
    private final static Factory<Illuminance> FACTORY = new Factory<Illuminance>(
            UNIT) {
        protected Illuminance create() {
            return new Illuminance();
        }
    }.useFor(UNIT.getBaseUnits());

    /**
     * Represents a {@link Illuminance} amounting to nothing.
     */
    public final static Illuminance ZERO = Quantity.valueOf(0, UNIT);

    /**
     * Default constructor (allows for derivation).
     */
    protected Illuminance() {
    }

    /**
     * Shows {@link Illuminance} instances in the specified unit.
     *
     * @param unit the display unit for {@link Illuminance} instances.
     */
    public static void showAs(Unit unit) {
        QuantityFormat.show(Illuminance.class, unit);
    }

    private static final long serialVersionUID = 1L;

}