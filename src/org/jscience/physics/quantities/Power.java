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
 * This class represents the rate at which work is done. The system unit
 * for this quantity is "m²·kg/s³". By default, instances of this class are
 * showed in Watt ("W").
 *
 * @author  <a href="mailto:jean-marie@dautelle.com">Jean-Marie Dautelle</a>
 * @version 1.0, October 24, 2004
 */
public class Power extends Quantity {

    /**
     * Holds the system unit.
     */
    private final static Unit SYSTEM_UNIT = SI.WATT.getDimension();

    /**
     * Holds the factory for this class.
     */
    private final static Factory FACTORY = new Factory(SYSTEM_UNIT) {
        protected Quantity newQuantity() {
             return new Power();
        }
    }.useFor(SI.WATT);

    /**
     * Represents a {@link Power} amounting to nothing.
     */
    public final static Power ZERO = (Power) valueOf(0, SYSTEM_UNIT);

    /**
     * Default constructor (allows for derivation).
     */
    protected Power() {}

    /**
     * Returns the {@link Power} corresponding to the specified quantity.
     *
     * @param  quantity a quantity compatible with {@link Power}.
     * @return the specified quantity or a new {@link Power} instance.
     * @throws ConversionException if the current model does not allow the
     *         specified quantity to be converted to {@link Power}.
     */
    public static Power powerOf(Quantity quantity) {
        return (Power) FACTORY.quantity(quantity);
    }

    /**
     * Shows {@link Power} instances in the specified unit.
     *
     * @param  unit the output unit for {@link Power} instances.
     * @see    Quantity#getOutputUnit
     */
    public static void showAs(Unit unit) {
        FACTORY.showInstancesAs(unit);
    }

    private static final long serialVersionUID = 2612241988994982119L;
}
