/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2005 - JScience (http://jscience.org/)
 * All rights reserved.
 * 
 * Permission to use, copy, modify, and distribute this software is
 * freely granted, provided that this notice is preserved.
 */
package org.jscience.physics.quantities;

import org.jscience.physics.units.Unit;
import static org.jscience.physics.units.SI.*;

/**
 * This class represents the number of elementary entities (molecules, for
 * example) of a substance. The system unit for this quantity is "mol"
 * (Système International d'Unités).
 *
 * @author  <a href="mailto:jean-marie@dautelle.com">Jean-Marie Dautelle</a>
 * @version 2.0, June 12, 2005
 */
public class AmountOfSubstance extends Quantity {

    /**
     * Holds the acceleration unit.
     */
    private final static Unit<AmountOfSubstance> UNIT = MOLE;

    /**
     * Holds the factory for this class.
     */
    private final static Factory<AmountOfSubstance> FACTORY = new Factory<AmountOfSubstance>(
            UNIT) {
        protected AmountOfSubstance create() {
            return new AmountOfSubstance();
        }
    };

    /**
     * Represents a {@link AmountOfSubstance} amounting to nothing.
     */
    public final static AmountOfSubstance ZERO = Quantity.valueOf(0, UNIT);

    /**
     * Default constructor (allows for derivation).
     */
    protected AmountOfSubstance() {
    }

    /**
     * Shows {@link AmountOfSubstance} instances in the specified unit.
     *
     * @param unit the display unit for {@link AmountOfSubstance} instances.
     */
    public static void showAs(Unit unit) {
        QuantityFormat.show(AmountOfSubstance.class, unit);
    }

    private static final long serialVersionUID = 1L;
}
