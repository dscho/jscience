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
 * This class represents an electric potential or electromotive force.
 * The system unit for this quantity is "m²·kg/(s³·A)".  By default, instances
 * of this class are showed in Volt ("V").
 *
 * @author  <a href="mailto:jean-marie@dautelle.com">Jean-Marie Dautelle</a>
 * @version 1.0, October 24, 2004
 */
public class ElectricPotential extends Quantity {

    /**
     * Holds the system unit.
     */
    private final static Unit SYSTEM_UNIT = SI.VOLT.getDimension();

    /**
     * Holds the factory for this class.
     */
    private final static Factory FACTORY = new Factory(SYSTEM_UNIT) {
        protected Quantity newQuantity() {
             return new ElectricPotential();
        }
    }.useFor(SI.VOLT);

    /**
     * Represents an {@link ElectricPotential} amounting to nothing.
     */
    public final static ElectricPotential ZERO
        = (ElectricPotential) valueOf(0, SYSTEM_UNIT);

    /**
     * Default constructor (allows for derivation).
     */
    protected ElectricPotential() {}

    /**
     * Returns the {@link ElectricPotential} corresponding to the specified
     * quantity.
     *
     * @param  quantity a quantity compatible with {@link ElectricPotential}.
     * @return the specified quantity or a new {@link ElectricPotential} instance.
     * @throws ConversionException if the current model does not allow the
     *         specified quantity to be converted to {@link ElectricPotential}.
     */
    public static ElectricPotential electricPotentialOf(Quantity quantity) {
        return (ElectricPotential) FACTORY.quantity(quantity);
    }

    /**
     * Shows {@link ElectricPotential} instances in the specified unit.
     *
     * @param  unit the output unit for {@link ElectricPotential} instances.
     * @see    Quantity#getOutputUnit
     */
    public static void showAs(Unit unit) {
        FACTORY.showInstancesAs(unit);
    }

    private static final long serialVersionUID = -3351269391643146413L;
}
