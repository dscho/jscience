/*
 * jScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2004 - The jScience Consortium (http://jscience.org/)
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation (http://www.gnu.org/copyleft/lesser.html); either version
 * 2.1 of the License, or any later version.
 */
package org.jscience.physics.quantities;
import org.jscience.physics.units.ConversionException;
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
     * Holds the factory for this class.
     */
    private final static Factory FACTORY = new Factory(SYSTEM_UNIT) {
        protected Quantity newQuantity() {
             return new LuminousIntensity();
        }
    };

    /**
     * Represents a {@link LuminousIntensity} amounting to nothing.
     */
    public final static LuminousIntensity ZERO
        = (LuminousIntensity) valueOf(0, SYSTEM_UNIT);

    /**
     * Default constructor (allows for derivation).
     */
    protected LuminousIntensity() {}

    /**
     * Returns the {@link LuminousIntensity} corresponding to the specified
     * quantity.
     *
     * @param  quantity a quantity compatible with {@link LuminousIntensity}.
     * @return the specified quantity or a new {@link LuminousIntensity} instance.
     * @throws ConversionException if the current model does not allow the
     *         specified quantity to be converted to {@link LuminousIntensity}.
     */
    public static LuminousIntensity luminousIntensityOf(Quantity quantity) {
        return (LuminousIntensity) FACTORY.quantity(quantity);
    }

    /**
     * Shows {@link LuminousIntensity} instances in the specified unit.
     *
     * @param  unit the output unit for {@link LuminousIntensity} instances.
     * @see    Quantity#getOutputUnit
     */
    public static void showAs(Unit unit) {
        FACTORY.showInstancesAs(unit);
    }

    private static final long serialVersionUID = 7831921387754179888L;
}
