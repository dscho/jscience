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
 * This class represents the angle formed by three or more planes intersecting
 * at a common point. The system unit for this quantity is "sr".
 * This quantity is dimensionless.
 *
 * @author  <a href="mailto:jean-marie@dautelle.com">Jean-Marie Dautelle</a>
 * @version 1.0, October 24, 2004
 */
public class SolidAngle extends Quantity {

    /**
     * Holds the system unit.
     */
    private final static Unit SYSTEM_UNIT = SI.STERADIAN;

    /**
     * Holds the factory for this class.
     */
    private final static Factory FACTORY = new Factory(SYSTEM_UNIT) {
        protected Quantity newQuantity() {
             return new SolidAngle();
        }
    };

    /**
     * Represents a {@link SolidAngle} amounting to nothing.
     */
    public final static SolidAngle ZERO = (SolidAngle) valueOf(0, SYSTEM_UNIT);

    /**
     * Default constructor (allows for derivation).
     */
    protected SolidAngle() {}

    /**
     * Returns the {@link SolidAngle} corresponding to the specified quantity.
     *
     * @param  quantity a quantity compatible with {@link SolidAngle}.
     * @return the specified quantity or a new {@link SolidAngle} instance.
     * @throws ConversionException if the current model does not allow the
     *         specified quantity to be converted to {@link SolidAngle}.
     */
    public static SolidAngle solidAngleOf(Quantity quantity) {
        return (SolidAngle) FACTORY.quantity(quantity);
    }

    /**
     * Shows {@link SolidAngle} instances in the specified unit.
     *
     * @param  unit the output unit for {@link SolidAngle} instances.
     * @see    Quantity#getOutputUnit
     */
    public static void showAs(Unit unit) {
        FACTORY.showInstancesAs(unit);
    }

    private static final long serialVersionUID = -8484596247656862314L;
}
