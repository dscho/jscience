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
 * This class represents a luminous flux. The system unit for this quantity
 * is "cd·sr". By default, instances of this class are showed in Lumen ("lm").
 *
 * @author  <a href="mailto:jean-marie@dautelle.com">Jean-Marie Dautelle</a>
 * @version 1.0, October 24, 2004
 */
public class LuminousFlux extends Quantity {

    /**
     * Holds the associated unit.
     */
    private final static Unit<LuminousFlux> UNIT = SI.CANDELA
            .times(SI.STERADIAN);

    /**
     * Holds the factory for this class.
     */
    private final static Factory<LuminousFlux> FACTORY = new Factory<LuminousFlux>(
            UNIT) {
        protected LuminousFlux create() {
            return new LuminousFlux();
        }
    };

    /**
     * Represents a {@link LuminousFlux} amounting to nothing.
     */
    public final static LuminousFlux ZERO = Quantity.valueOf(0, UNIT);

    /**
     * Default constructor (allows for derivation).
     */
    protected LuminousFlux() {
    }

    /**
     * Shows {@link LuminousFlux} instances in the specified unit.
     *
     * @param unit the display unit for {@link LuminousFlux} instances.
     */
    public static void showAs(Unit unit) {
        QuantityFormat.show(LuminousFlux.class, unit);
    }

    private static final long serialVersionUID = 1L;

}
