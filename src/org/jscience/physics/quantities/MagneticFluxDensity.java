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
 * This class represents a magnetic flux density. The system unit for this
 * quantity is "kg/(s²·A)". By default, instances of this class are showed in
 * Tesla ("T").
 *
 * @author  <a href="mailto:jean-marie@dautelle.com">Jean-Marie Dautelle</a>
 * @version 1.0, October 24, 2004
 */
public class MagneticFluxDensity extends Quantity {

    /**
     * Holds the associated unit.
     */
    private final static Unit<MagneticFluxDensity> UNIT = SI.TESLA;

    /**
     * Holds the factory for this class.
     */
    private final static Factory<MagneticFluxDensity> FACTORY = new Factory<MagneticFluxDensity>(
            UNIT) {
        protected MagneticFluxDensity create() {
            return new MagneticFluxDensity();
        }
    }.useFor(UNIT.getBaseUnits());

    /**
     * Represents a {@link MagneticFluxDensity} amounting to nothing.
     */
    public final static MagneticFluxDensity ZERO = Quantity.valueOf(0, UNIT);

    /**
     * Default constructor (allows for derivation).
     */
    protected MagneticFluxDensity() {
    }

    /**
     * Shows {@link MagneticFluxDensity} instances in the specified unit.
     *
     * @param unit the display unit for {@link MagneticFluxDensity} instances.
     */
    public static void showAs(Unit unit) {
        QuantityFormat.show(MagneticFluxDensity.class, unit);
    }

    private static final long serialVersionUID = 1L;

}
