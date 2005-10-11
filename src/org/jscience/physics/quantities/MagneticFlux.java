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
 * This class represents a magnetic flux. The system unit for this quantity
 * is "m²·kg/(s²·A)". By default, instances of this class are showed in
 * Weber ("Wb").
 *
 * @author  <a href="mailto:jean-marie@dautelle.com">Jean-Marie Dautelle</a>
 * @version 1.0, October 24, 2004
 */
public class MagneticFlux extends Quantity {

    /**
     * Holds the associated unit.
     */
    private final static Unit<MagneticFlux> UNIT = SI.WEBER;

    /**
     * Holds the factory for this class.
     */
    private final static Factory<MagneticFlux> FACTORY = new Factory<MagneticFlux>(
            UNIT) {
        protected MagneticFlux create() {
            return new MagneticFlux();
        }
    }.useFor(UNIT.getBaseUnits());

    /**
     * Represents a {@link MagneticFlux} amounting to nothing.
     */
    public final static MagneticFlux ZERO = Quantity.valueOf(0, UNIT);

    /**
     * Default constructor (allows for derivation).
     */
    protected MagneticFlux() {
    }

    /**
     * Shows {@link MagneticFlux} instances in the specified unit.
     *
     * @param unit the display unit for {@link MagneticFlux} instances.
     */
    public static void showAs(Unit unit) {
        QuantityFormat.show(MagneticFlux.class, unit);
    }

    private static final long serialVersionUID = 1L;

}
