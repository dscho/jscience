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
 * This class represents the speed of data-transmission.
 * The system unit for this quantity is "bit/s".
 *
 * @author  <a href="mailto:jean-marie@dautelle.com">Jean-Marie Dautelle</a>
 * @version 1.0, October 24, 2004
 */
public class DataRate extends Quantity {

    /**
     * Holds the associated unit.
     */
    private final static Unit<DataRate> UNIT = SI.BIT.divide(SI.SECOND);

    /**
     * Holds the factory for this class.
     */
    private final static Factory<DataRate> FACTORY = new Factory<DataRate>(
            UNIT) {
        protected DataRate create() {
            return new DataRate();
        }
    };

    /**
     * Represents a {@link DataRate} amounting to nothing.
     */
    public final static DataRate ZERO = Quantity.valueOf(0, UNIT);

    /**
     * Default constructor (allows for derivation).
     */
    protected DataRate() {
    }

    /**
     * Shows {@link DataRate} instances in the specified unit.
     *
     * @param unit the display unit for {@link DataRate} instances.
     */
    public static void showAs(Unit unit) {
        QuantityFormat.show(DataRate.class, unit);
    }

    private static final long serialVersionUID = 1L;

}