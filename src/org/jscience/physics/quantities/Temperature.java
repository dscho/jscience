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
 * This class represents the degree of hotness or coldness of a body or
 * an environment. The system unit for this quantity is "K"
 * (Système International d'Unités).
 *
 * @author  <a href="mailto:jean-marie@dautelle.com">Jean-Marie Dautelle</a>
 * @version 1.0, October 24, 2004
 */
public class Temperature extends Quantity {

    /**
     * Holds the associated unit.
     */
    private final static Unit<Temperature> UNIT = SI.KELVIN;

    /**
     * Holds the factory for this class.
     */
    private final static Factory<Temperature> FACTORY = new Factory<Temperature>(
            UNIT) {
        protected Temperature create() {
            return new Temperature();
        }
    };

    /**
     * Represents a {@link Temperature} amounting to nothing.
     */
    public final static Temperature ZERO = Quantity.valueOf(0, UNIT);

    /**
     * Holds the standard temperature (0°C).
     */
    public final static Temperature STANDARD = Quantity.valueOf(273.15,
            SI.KELVIN);

    /**
     * Default constructor (allows for derivation).
     */
    protected Temperature() {
    }

    /**
     * Shows {@link Temperature} instances in the specified unit.
     *
     * @param unit the display unit for {@link Temperature} instances.
     */
    public static void showAs(Unit unit) {
        QuantityFormat.show(Temperature.class, unit);
    }

    private static final long serialVersionUID = 1L;

}
