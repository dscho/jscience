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
 * This class represents the measure of the quantity of matter that a body
 * or an object contains. The mass of the body is not dependent on gravity
 * and therefore is different from but proportional to its weight.
 * The system unit for this quantity is "kg" (Système International d'Unités).
 *
 * @author  <a href="mailto:jean-marie@dautelle.com">Jean-Marie Dautelle</a>
 * @version 1.0, October 24, 2004
 */
public class Mass extends Quantity {

    /**
     * Holds the system unit.
     */
    private final static Unit SYSTEM_UNIT = SI.KILOGRAM;

    /**
     * Holds the factory for this class.
     */
    private final static Factory FACTORY = new Factory(SYSTEM_UNIT) {
        protected Quantity newQuantity() {
             return new Mass();
        }
    };

    /**
     * Represents a {@link Mass} amounting to nothing.
     */
    public final static Mass ZERO = (Mass) valueOf(0, SYSTEM_UNIT);

    /**
     * Holds the electron rest mass.
     */
    public final static Mass ELECTRON
        = (Mass) valueOf(9.10938188e-31, 0.00000072e-31, SI.KILOGRAM);

    /**
     * Holds the proton rest mass.
     */
    public final static Mass PROTON
        = (Mass) valueOf(1.67262158e-27, 0.00000013e-27, SI.KILOGRAM);

    /**
     * Holds the neutron rest mass.
     */
    public final static Mass NEUTRON
        = (Mass) valueOf(1.67492716e-27, 0.00000013e-27, SI.KILOGRAM);

    /**
     * Holds the deuteron rest mass.
     */
    public final static Mass DEUTERON
        = (Mass) valueOf(3.34358309e-27, 0.00000026e-27, SI.KILOGRAM);

    /**
     * Holds the muon rest mass.
     */
    public final static Mass MUON
        = (Mass) valueOf(1.88353109e-28, 0.00000016e-28, SI.KILOGRAM);

    /**
     * Default constructor (allows for derivation).
     */
    protected Mass() {}

    /**
     * Returns the {@link Mass} corresponding to the specified quantity.
     *
     * @param  quantity a quantity compatible with {@link Mass}.
     * @return the specified quantity or a new {@link Mass} instance.
     * @throws ConversionException if the current model does not allow the
     *         specified quantity to be converted to {@link Mass}.
     */
    public static Mass massOf(Quantity quantity) {
        return (Mass) FACTORY.quantity(quantity);
    }

    /**
     * Shows {@link Mass} instances in the specified unit.
     *
     * @param  unit the output unit for {@link Mass} instances.
     * @see    Quantity#getOutputUnit
     */
    public static void showAs(Unit unit) {
        FACTORY.showInstancesAs(unit);
    }

    private static final long serialVersionUID = -7800432954954604437L;
}
