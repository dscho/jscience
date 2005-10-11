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
 * This class represents a radioactive activity. The system unit for
 * this quantity is "Bq".
 *
 * @author  <a href="mailto:jean-marie@dautelle.com">Jean-Marie Dautelle</a>
 * @version 1.0, October 24, 2004
 */
public class RadioactiveActivity extends Quantity {

    /**
     * Holds the associated unit.
     */
    private final static Unit<RadioactiveActivity> UNIT = SI.BECQUEREL;

    /**
     * Holds the factory for this class.
     */
    private final static Factory<RadioactiveActivity> FACTORY = new Factory<RadioactiveActivity>(
            UNIT) {
        protected RadioactiveActivity create() {
            return new RadioactiveActivity();
        }
    };

    /**
     * Represents a {@link RadioactiveActivity} amounting to nothing.
     */
    public final static RadioactiveActivity ZERO = Quantity.valueOf(0, UNIT);

    /**
     * Default constructor (allows for derivation).
     */
    protected RadioactiveActivity() {
    }

    /**
     * Shows {@link RadioactiveActivity} instances in the specified unit.
     *
     * @param unit the display unit for {@link RadioactiveActivity} instances.
     */
    public static void showAs(Unit unit) {
        QuantityFormat.show(RadioactiveActivity.class, unit);
    }

    private static final long serialVersionUID = 1L;

}
