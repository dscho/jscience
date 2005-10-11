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
 * This class represents an Electric Resistance.
 * The system unit for this quantity is "m²·kg/(s³·A²)".  By default, instances
 * of this class are showed in Ohm ("Ohm").
 *
 * @author  <a href="mailto:jean-marie@dautelle.com">Jean-Marie Dautelle</a>
 * @version 1.0, October 24, 2004
 */
public class ElectricResistance extends Quantity {

    /**
     * Holds the associated unit.
     */
    private final static Unit<ElectricResistance> UNIT = SI.OHM;

    /**
     * Holds the factory for this class.
     */
    private final static Factory<ElectricResistance> FACTORY = new Factory<ElectricResistance>(
            UNIT) {
        protected ElectricResistance create() {
            return new ElectricResistance();
        }
    }.useFor(UNIT.getBaseUnits());

    /**
     * Represents a {@link ElectricResistance} amounting to nothing.
     */
    public final static ElectricResistance ZERO = Quantity.valueOf(0, UNIT);

    /**
     * Default constructor (allows for derivation).
     */
    protected ElectricResistance() {
    }

    /**
     * Shows {@link ElectricResistance} instances in the specified unit.
     *
     * @param unit the display unit for {@link ElectricResistance} instances.
     */
    public static void showAs(Unit unit) {
        QuantityFormat.show(ElectricResistance.class, unit);
    }

    private static final long serialVersionUID = 1L;

}
