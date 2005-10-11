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
 * This class represents a measure of data amount.
 * The system unit for this quantity is "bit". This quantity is dimensionless.
 *
 * @author  <a href="mailto:jean-marie@dautelle.com">Jean-Marie Dautelle</a>
 * @version 1.0, October 24, 2004
 */
public class DataAmount extends Dimensionless {

    /**
     * Holds the associated unit.
     */
    private final static Unit<DataAmount> UNIT = SI.BIT;

    /**
     * Holds the factory for this class.
     */
    private final static Factory<DataAmount> FACTORY = new Factory<DataAmount>(
            UNIT) {
        protected DataAmount create() {
            return new DataAmount();
        }
    };

    /**
     * Represents a {@link DataAmount} amounting to nothing.
     */
    public final static DataAmount ZERO = Quantity.valueOf(0, UNIT);

    /**
     * Default constructor (allows for derivation).
     */
    protected DataAmount() {
    }

    /**
     * Shows {@link DataAmount} instances in the specified unit.
     *
     * @param unit the display unit for {@link DataAmount} instances.
     */
    public static void showAs(Unit unit) {
        QuantityFormat.show(DataAmount.class, unit);
    }

    private static final long serialVersionUID = 1L;

}
