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
 * This class represents an electric inductance. The system unit for this
 * quantity is "m²·kg/(s²·A²)".  By default, instances of this class are showed
 * in Henry ("H").
 *
 * @author  <a href="mailto:jean-marie@dautelle.com">Jean-Marie Dautelle</a>
 * @version 1.0, October 24, 2004
 */
public class ElectricInductance extends Quantity {

    /**
     * Holds the system unit.
     */
    private final static Unit SYSTEM_UNIT = SI.HENRY.getDimension();

    /**
     * Holds the factory for this class.
     */
    private final static Factory FACTORY = new Factory(SYSTEM_UNIT) {
        protected Quantity newQuantity() {
             return new ElectricInductance();
        }
    }.useFor(SI.HENRY);

    /**
     * Represents an {@link ElectricInductance} amounting to nothing.
     */
    public final static ElectricInductance ZERO
        = (ElectricInductance) valueOf(0, SYSTEM_UNIT);

    /**
     * Default constructor (allows for derivation).
     */
    protected ElectricInductance() {}

    /**
     * Returns the {@link ElectricInductance} corresponding to the specified
     * quantity.
     *
     * @param  quantity a quantity compatible with {@link ElectricInductance}.
     * @return the specified quantity or a new {@link ElectricInductance} instance.
     * @throws ConversionException if the current model does not allow the
     *         specified quantity to be converted to {@link ElectricInductance}.
     */
    public static ElectricInductance electricInductanceOf(Quantity quantity) {
        return (ElectricInductance) FACTORY.quantity(quantity);
    }

    /**
     * Shows {@link ElectricInductance} instances in the specified unit.
     *
     * @param  unit the output unit for {@link ElectricInductance} instances.
     * @see    Quantity#getOutputUnit
     */
    public static void showAs(Unit unit) {
        FACTORY.showInstancesAs(unit);
    }

    private static final long serialVersionUID = -5818060993834087737L;
}
