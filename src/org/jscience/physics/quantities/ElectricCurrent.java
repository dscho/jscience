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
 * This class represents the amount of electric charge flowing past
 * a specified circuit point per unit time. The system unit for
 * this quantity is "A" (Système International d'Unités).
 *
 * @author  <a href="mailto:jean-marie@dautelle.com">Jean-Marie Dautelle</a>
 * @version 1.0, October 24, 2004
 */
public class ElectricCurrent extends Quantity {

    /**
     * Holds the associated unit.
     */
    private final static Unit<ElectricCurrent> UNIT = SI.AMPERE;

    /**
     * Holds the factory for this class.
     */
    private final static Factory<ElectricCurrent> FACTORY = new Factory<ElectricCurrent>(
            UNIT) {
        protected ElectricCurrent create() {
            return new ElectricCurrent();
        }
    };

    /**
     * Represents a {@link ElectricCurrent} amounting to nothing.
     */
    public final static ElectricCurrent ZERO = Quantity.valueOf(0, UNIT);

    /**
     * Default constructor (allows for derivation).
     */
    protected ElectricCurrent() {
    }

    /**
     * Shows {@link ElectricCurrent} instances in the specified unit.
     *
     * @param unit the display unit for {@link ElectricCurrent} instances.
     */
    public static void showAs(Unit unit) {
        QuantityFormat.show(ElectricCurrent.class, unit);
    }

    private static final long serialVersionUID = 1L;

}
