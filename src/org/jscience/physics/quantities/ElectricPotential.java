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
 * This class represents an electric potential or electromotive force.
 * The system unit for this quantity is "m²·kg/(s³·A)".  By default, instances
 * of this class are showed in Volt ("V").
 *
 * @author  <a href="mailto:jean-marie@dautelle.com">Jean-Marie Dautelle</a>
 * @version 1.0, October 24, 2004
 */
public class ElectricPotential extends Quantity {

    /**
     * Holds the associated unit.
     */
    private final static Unit<ElectricPotential> UNIT = SI.VOLT;

    /**
     * Holds the factory for this class.
     */
    private final static Factory<ElectricPotential> FACTORY = new Factory<ElectricPotential>(
            UNIT) {
        protected ElectricPotential create() {
            return new ElectricPotential();
        }
    }.useFor(UNIT.getBaseUnits());

    /**
     * Represents a {@link ElectricPotential} amounting to nothing.
     */
    public final static ElectricPotential ZERO = Quantity.valueOf(0, UNIT);

    /**
     * Default constructor (allows for derivation).
     */
    protected ElectricPotential() {
    }

    /**
     * Shows {@link ElectricPotential} instances in the specified unit.
     *
     * @param unit the display unit for {@link ElectricPotential} instances.
     */
    public static void showAs(Unit unit) {
        QuantityFormat.show(ElectricPotential.class, unit);
    }

    private static final long serialVersionUID = 1L;

}
