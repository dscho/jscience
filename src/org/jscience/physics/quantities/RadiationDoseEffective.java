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
 * This class represents the effective (or "equivalent") dose of radiation
 * received by a human or some other living organism. The system unit for
 * this quantity is "Sv".
 *
 * @author  <a href="mailto:jean-marie@dautelle.com">Jean-Marie Dautelle</a>
 * @version 1.0, October 24, 2004
 */
public class RadiationDoseEffective extends Quantity {

    /**
     * Holds the associated unit.
     */
    private final static Unit<RadiationDoseEffective> UNIT = SI.SIEVERT;

    /**
     * Holds the factory for this class.
     */
    private final static Factory<RadiationDoseEffective> FACTORY = new Factory<RadiationDoseEffective>(
            UNIT) {
        protected RadiationDoseEffective create() {
            return new RadiationDoseEffective();
        }
    };

    /**
     * Represents a {@link RadiationDoseEffective} amounting to nothing.
     */
    public final static RadiationDoseEffective ZERO = Quantity.valueOf(0, UNIT);

    /**
     * Default constructor (allows for derivation).
     */
    protected RadiationDoseEffective() {
    }

    /**
     * Shows {@link RadiationDoseEffective} instances in the specified unit.
     *
     * @param unit the display unit for {@link RadiationDoseEffective} instances.
     */
    public static void showAs(Unit unit) {
        QuantityFormat.show(RadiationDoseEffective.class, unit);
    }

    private static final long serialVersionUID = 1L;

}
