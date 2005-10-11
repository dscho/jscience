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
 * This class represents an electric inductance. The system unit for this
 * quantity is "m²·kg/(s²·A²)".  By default, instances of this class are showed
 * in Henry ("H").
 *
 * @author  <a href="mailto:jean-marie@dautelle.com">Jean-Marie Dautelle</a>
 * @version 1.0, October 24, 2004
 */
public class ElectricInductance extends Quantity {

    /**
     * Holds the associated unit.
     */
    private final static Unit<ElectricInductance> UNIT = SI.HENRY;

    /**
     * Holds the factory for this class.
     */
    private final static Factory<ElectricInductance> FACTORY = new Factory<ElectricInductance>(
            UNIT) {
        protected ElectricInductance create() {
            return new ElectricInductance();
        }
    }.useFor(UNIT.getBaseUnits());

    /**
     * Represents a {@link ElectricInductance} amounting to nothing.
     */
    public final static ElectricInductance ZERO = Quantity.valueOf(0, UNIT);

    /**
     * Default constructor (allows for derivation).
     */
    protected ElectricInductance() {
    }

    /**
     * Shows {@link ElectricInductance} instances in the specified unit.
     *
     * @param unit the display unit for {@link ElectricInductance} instances.
     */
    public static void showAs(Unit unit) {
        QuantityFormat.show(ElectricInductance.class, unit);
    }

    private static final long serialVersionUID = 1L;

}
