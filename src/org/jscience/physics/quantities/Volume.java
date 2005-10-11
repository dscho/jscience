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
 * This class represents the amount of space occupied by a three-dimensional
 * object or region of space, expressed in cubic units. The system unit for
 * this quantity is "m³".
 *
 * @author  <a href="mailto:jean-marie@dautelle.com">Jean-Marie Dautelle</a>
 * @version 1.0, October 24, 2004
 */
public class Volume extends Quantity {

    /**
     * Holds the associated unit.
     */
    private final static Unit<Volume> UNIT = SI.CUBIC_METER;

    /**
     * Holds the factory for this class.
     */
    private final static Factory<Volume> FACTORY = new Factory<Volume>(UNIT) {
        protected Volume create() {
            return new Volume();
        }
    };

    /**
     * Represents a {@link Volume} amounting to nothing.
     */
    public final static Volume ZERO = Quantity.valueOf(0, UNIT);

    /**
     * Default constructor (allows for derivation).
     */
    protected Volume() {
    }

    /**
     * Shows {@link Volume} instances in the specified unit.
     *
     * @param unit the display unit for {@link Volume} instances.
     */
    public static void showAs(Unit unit) {
        QuantityFormat.show(Volume.class, unit);
    }

    private static final long serialVersionUID = 1L;

}
