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
 * This class represents an electric charge. The system unit for this
 * quantity is "s·A". By default, instances of this class are showed in
 * Coulomb ("C").
 *
 * @author  <a href="mailto:jean-marie@dautelle.com">Jean-Marie Dautelle</a>
 * @version 1.0, October 24, 2004
 */
public class ElectricCharge extends Quantity {

    /**
     * Holds the associated unit.
     */
    private final static Unit<ElectricCharge> UNIT = SI.COULOMB;

    /**
     * Holds the factory for this class.
     */
    private final static Factory<ElectricCharge> FACTORY = new Factory<ElectricCharge>(
            UNIT) {
        protected ElectricCharge create() {
            return new ElectricCharge();
        }
    }.useFor(UNIT.getBaseUnits());

    /**
     * Represents a {@link ElectricCharge} amounting to nothing.
     */
    public final static ElectricCharge ZERO = Quantity.valueOf(0, UNIT);

    /**
     * Holds the elementary charge (positron charge).
     */
    public final static ElectricCharge ELEMENTARY = Quantity.valueOf(
            1.602176462e-19, 0.000000063e-19, UNIT);

    /**
     * Default constructor (allows for derivation).
     */
    protected ElectricCharge() {
    }

    /**
     * Shows {@link ElectricCharge} instances in the specified unit.
     *
     * @param unit the display unit for {@link ElectricCharge} instances.
     */
    public static void showAs(Unit unit) {
        QuantityFormat.show(ElectricCharge.class, unit);
    }

    private static final long serialVersionUID = 1L;

}
