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
 * This class represents the luminous flux density per solid angle as measured
 * in a given direction relative to the emitting source. The system unit
 * for this quantity is "cd" (Système International d'Unités).
 *
 * @author  <a href="mailto:jean-marie@dautelle.com">Jean-Marie Dautelle</a>
 * @version 1.0, October 24, 2004
 */
public class LuminousIntensity extends Quantity {

    /**
     * Holds the system unit.
     */
    private final static Unit SYSTEM_UNIT = SI.CANDELA;

    /**
     * Holds the associated unit.
     */
    private final static Unit<LuminousIntensity> UNIT = SI.CANDELA;

    /**
     * Holds the factory for this class.
     */
    private final static Factory<LuminousIntensity> FACTORY = new Factory<LuminousIntensity>(
            UNIT) {
        protected LuminousIntensity create() {
            return new LuminousIntensity();
        }
    };

    /**
     * Represents a {@link LuminousIntensity} amounting to nothing.
     */
    public final static LuminousIntensity ZERO = Quantity.valueOf(0, UNIT);

    /**
     * Default constructor (allows for derivation).
     */
    protected LuminousIntensity() {
    }

    /**
     * Shows {@link LuminousIntensity} instances in the specified unit.
     *
     * @param unit the display unit for {@link LuminousIntensity} instances.
     */
    public static void showAs(Unit unit) {
        QuantityFormat.show(LuminousIntensity.class, unit);
    }

    private static final long serialVersionUID = 1L;

}
