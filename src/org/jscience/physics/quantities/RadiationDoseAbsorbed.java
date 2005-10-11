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
 * This class represents the amount of energy deposited per unit of
 * mass. The system unit for this quantity is "Gy".
 *
 * @author  <a href="mailto:jean-marie@dautelle.com">Jean-Marie Dautelle</a>
 * @version 1.0, October 24, 2004
 */
public class RadiationDoseAbsorbed extends Quantity {

    /**
     * Holds the associated unit.
     */
    private final static Unit<RadiationDoseAbsorbed> UNIT = SI.GRAY;

    /**
     * Holds the factory for this class.
     */
    private final static Factory<RadiationDoseAbsorbed> FACTORY = new Factory<RadiationDoseAbsorbed>(
            UNIT) {
        protected RadiationDoseAbsorbed create() {
            return new RadiationDoseAbsorbed();
        }
    };

    /**
     * Represents a {@link RadiationDoseAbsorbed} amounting to nothing.
     */
    public final static RadiationDoseAbsorbed ZERO = Quantity.valueOf(0, UNIT);

    /**
     * Default constructor (allows for derivation).
     */
    protected RadiationDoseAbsorbed() {
    }

    /**
     * Shows {@link RadiationDoseAbsorbed} instances in the specified unit.
     *
     * @param unit the display unit for {@link RadiationDoseAbsorbed} instances.
     */
    public static void showAs(Unit unit) {
        QuantityFormat.show(RadiationDoseAbsorbed.class, unit);
    }

    private static final long serialVersionUID = 1L;

}
