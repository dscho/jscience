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
 * This class represents the capacity of a physical system to do work.
 * The system unit for this quantity is "m²·kg/s²".  By default, instances of
 * this class are showed in Joule ("J").
 *
 * @author  <a href="mailto:jean-marie@dautelle.com">Jean-Marie Dautelle</a>
 * @version 1.0, October 24, 2004
 */
public class Energy extends Quantity {

    /**
     * Holds the associated unit.
     */
    private final static Unit<Energy> UNIT = SI.JOULE;

    /**
     * Holds the factory for this class.
     */
    private final static Factory<Energy> FACTORY = new Factory<Energy>(
            UNIT) {
        protected Energy create() {
            return new Energy();
        }
    }.useFor(UNIT.getBaseUnits());

    /**
     * Represents a {@link Energy} amounting to nothing.
     */
    public final static Energy ZERO = Quantity.valueOf(0, UNIT);

    /**
     * Default constructor (allows for derivation).
     */
    protected Energy() {
    }

    /**
     * Shows {@link Energy} instances in the specified unit.
     *
     * @param unit the display unit for {@link Energy} instances.
     */
    public static void showAs(Unit unit) {
        QuantityFormat.show(Energy.class, unit);
    }

    private static final long serialVersionUID = 1L;
}
