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
 * This class represents a catalytic activity. The system unit for this quantity
 * is "mol/s". By default, instances of this class are showed in Katal ("kat").
 *
 * @author  <a href="mailto:jean-marie@dautelle.com">Jean-Marie Dautelle</a>
 * @version 1.0, October 24, 2004
 */
public class CatalyticActivity extends Quantity {

    /**
     * Holds the associated unit.
     */
    private final static Unit<CatalyticActivity> UNIT = SI.KATAL;

    /**
     * Holds the factory for this class.
     */
    private final static Factory<CatalyticActivity> FACTORY = new Factory<CatalyticActivity>(
            UNIT) {
        protected CatalyticActivity create() {
            return new CatalyticActivity();
        }
    }.useFor(UNIT.getBaseUnits());

    /**
     * Represents a {@link CatalyticActivity} amounting to nothing.
     */
    public final static CatalyticActivity ZERO = Quantity.valueOf(0, UNIT);

    /**
     * Default constructor (allows for derivation).
     */
    protected CatalyticActivity() {
    }

    /**
     * Shows {@link CatalyticActivity} instances in the specified unit.
     *
     * @param unit the display unit for {@link CatalyticActivity} instances.
     */
    public static void showAs(Unit unit) {
        QuantityFormat.show(CatalyticActivity.class, unit);
    }

    private static final long serialVersionUID = 1L;

}