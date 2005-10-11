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
 * This class represents a mass per unit volume of a substance under
 * specified conditions of pressure and temperature. The system unit for
 * this quantity is "kg/m³".
 *
 * @author  <a href="mailto:jean-marie@dautelle.com">Jean-Marie Dautelle</a>
 * @version 1.0, October 24, 2004
 */
public class VolumetricDensity extends Quantity {

    /**
     * Holds the associated unit.
     */
    private final static Unit<VolumetricDensity> UNIT = SI.KILOGRAM
            .divide(SI.METER.pow(3));

    /**
     * Holds the factory for this class.
     */
    private final static Factory<VolumetricDensity> FACTORY = new Factory<VolumetricDensity>(
            UNIT) {
        protected VolumetricDensity create() {
            return new VolumetricDensity();
        }
    };

    /**
     * Represents a {@link VolumetricDensity} amounting to nothing.
     */
    public final static VolumetricDensity ZERO = Quantity.valueOf(0, UNIT);

    /**
     * Default constructor (allows for derivation).
     */
    protected VolumetricDensity() {
    }

    /**
     * Shows {@link VolumetricDensity} instances in the specified unit.
     *
     * @param unit the display unit for {@link VolumetricDensity} instances.
     */
    public static void showAs(Unit unit) {
        QuantityFormat.show(VolumetricDensity.class, unit);
    }

    private static final long serialVersionUID = 1L;
}
