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
 * This class represents an electric charge. The system unit for this
 * quantity is "s·A". By default, instances of this class are showed in
 * Coulomb ("C").
 *
 * @author  <a href="mailto:jean-marie@dautelle.com">Jean-Marie Dautelle</a>
 * @version 1.0, October 24, 2004
 */
public class ElectricCharge extends Quantity {

    /**
     * Holds the system unit.
     */
    private final static Unit SYSTEM_UNIT = SI.COULOMB.getDimension();

    /**
     * Holds the factory for this class.
     */
    private final static Factory FACTORY = new Factory(SYSTEM_UNIT) {
        protected Quantity newQuantity() {
             return new ElectricCharge();
        }
    }.useFor(SI.COULOMB);

    /**
     * Represents an {@link ElectricCharge} amounting to nothing.
     */
    public final static ElectricCharge ZERO
        = (ElectricCharge) valueOf(0, SYSTEM_UNIT);

    /**
     * Holds the elementary charge (positron charge).
     */
    public final static ElectricCharge ELEMENTARY
        = (ElectricCharge) valueOf(1.602176462e-19, 0.000000063e-19,
                                   SYSTEM_UNIT);

    /**
     * Default constructor (allows for derivation).
     */
    protected ElectricCharge() {}

    /**
     * Returns the {@link ElectricCharge} corresponding to the specified
     * quantity.
     *
     * @param  quantity a quantity compatible with {@link ElectricCharge}.
     * @return the specified quantity or a new {@link ElectricCharge} instance.
     * @throws ConversionException if the current model does not allow the
     *         specified quantity to be converted to {@link ElectricCharge}.
     */
    public static ElectricCharge electricChargeOf(Quantity quantity) {
        return (ElectricCharge) FACTORY.quantity(quantity);
    }

    /**
     * Shows {@link ElectricCharge} instances in the specified unit.
     *
     * @param  unit the output unit for {@link ElectricCharge} instances.
     * @see    Quantity#getOutputUnit
     */
    public static void showAs(Unit unit) {
        FACTORY.showInstancesAs(unit);
    }

    private static final long serialVersionUID = -4126331108929046917L;
}
