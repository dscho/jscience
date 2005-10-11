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
 * This class represents an electric capacitance. The system unit for this
 * quantity is "s<sup>4</sup>·A²/(m²·kg)".  By default, instances of this class
 * are showed in Farad ("F").
 *
 * @author  <a href="mailto:jean-marie@dautelle.com">Jean-Marie Dautelle</a>
 * @version 1.0, October 24, 2004
 */
public class ElectricCapacitance extends Quantity {

    /**
     * Holds the associated unit.
     */
    private final static Unit<ElectricCapacitance> UNIT = SI.FARAD;

    /**
     * Holds the factory for this class.
     */
    private final static Factory<ElectricCapacitance> FACTORY = new Factory<ElectricCapacitance>(
            UNIT) {
        protected ElectricCapacitance create() {
            return new ElectricCapacitance();
        }
    }.useFor(UNIT.getBaseUnits());

    /**
     * Represents a {@link ElectricCapacitance} amounting to nothing.
     */
    public final static ElectricCapacitance ZERO = Quantity.valueOf(0, UNIT);

    /**
     * Default constructor (allows for derivation).
     */
    protected ElectricCapacitance() {
    }

    /**
     * Shows {@link ElectricCapacitance} instances in the specified unit.
     *
     * @param unit the display unit for {@link ElectricCapacitance} instances.
     */
    public static void showAs(Unit unit) {
        QuantityFormat.show(ElectricCapacitance.class, unit);
    }

    private static final long serialVersionUID = 1L;

}