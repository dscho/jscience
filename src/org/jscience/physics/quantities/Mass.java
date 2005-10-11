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
 * This class represents the measure of the quantity of matter that a body
 * or an object contains. The mass of the body is not dependent on gravity
 * and therefore is different from but proportional to its weight.
 * The system unit for this quantity is "kg" (Système International d'Unités).
 *
 * @author  <a href="mailto:jean-marie@dautelle.com">Jean-Marie Dautelle</a>
 * @version 1.0, October 24, 2004
 */
public class Mass extends Quantity {

    /**
     * Holds the associated unit.
     */
    private final static Unit<Mass> UNIT = SI.KILOGRAM;

    /**
     * Holds the factory for this class.
     */
    private final static Factory<Mass> FACTORY = new Factory<Mass>(
            UNIT) {
        protected Mass create() {
            return new Mass();
        }
    };

    /**
     * Represents a {@link Mass} amounting to nothing.
     */
    public final static Mass ZERO = Quantity.valueOf(0, UNIT);

    /**
     * Holds the electron rest mass.
     */
    public final static Mass ELECTRON
        = Quantity.valueOf(9.10938188e-31, 0.00000072e-31, SI.KILOGRAM);

    /**
     * Holds the proton rest mass.
     */
    public final static Mass PROTON
        = Quantity.valueOf(1.67262158e-27, 0.00000013e-27, SI.KILOGRAM);

    /**
     * Holds the neutron rest mass.
     */
    public final static Mass NEUTRON
        = Quantity.valueOf(1.67492716e-27, 0.00000013e-27, SI.KILOGRAM);

    /**
     * Holds the deuteron rest mass.
     */
    public final static Mass DEUTERON
        = Quantity.valueOf(3.34358309e-27, 0.00000026e-27, SI.KILOGRAM);

    /**
     * Holds the muon rest mass.
     */
    public final static Mass MUON
        = Quantity.valueOf(1.88353109e-28, 0.00000016e-28, SI.KILOGRAM);

    /**
     * Default constructor (allows for derivation).
     */
    protected Mass() {
    }

    /**
     * Shows {@link Mass} instances in the specified unit.
     *
     * @param unit the display unit for {@link Mass} instances.
     */
    public static void showAs(Unit unit) {
        QuantityFormat.show(Mass.class, unit);
    }

    private static final long serialVersionUID = 1L;

}
