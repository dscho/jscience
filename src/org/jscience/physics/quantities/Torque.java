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
 * This class represents the moment of a force. The system unit for this
 * quantity is "N·m".
 * <p> Note: The Newton-metre ("N·m") is also a way of exressing a Joule (unit
 *     of energy). However, torque is not energy. So, to avoid confusion, we
 *     will use the units "N·m" for torque and not "J". This distinction occurs
 *     due to the scalar nature of energy and the vector nature of torque.</p>
 *
 * @author  <a href="mailto:jean-marie@dautelle.com">Jean-Marie Dautelle</a>
 * @version 1.0, October 24, 2004
 */
public class Torque extends Quantity {

    /**
     * Holds the system unit.
     */
    private final static Unit SYSTEM_UNIT = SI.NEWTON.multiply(SI.METER);

    /**
     * Holds the factory for this class.
     */
    private final static Factory FACTORY = new Factory(SYSTEM_UNIT) {
        protected Quantity newQuantity() {
             return new Torque();
        }
    };

    /**
     * Represents a {@link Torque} amounting to nothing.
     */
    public final static Torque ZERO = (Torque) valueOf(0, SYSTEM_UNIT);

    /**
     * Default constructor (allows for derivation).
     */
    protected Torque() {}

    /**
     * Returns the {@link Torque} corresponding to the specified quantity.
     *
     * @param  quantity a quantity compatible with {@link Torque}.
     * @return the specified quantity or a new {@link Torque} instance.
     * @throws ConversionException if the current model does not allow the
     *         specified quantity to be converted to {@link Torque}.
     */
    public static Torque torqueOf(Quantity quantity) {
        return (Torque) FACTORY.quantity(quantity);
    }

    /**
     * Shows {@link Torque} instances in the specified unit.
     *
     * @param  unit the output unit for {@link Torque} instances.
     * @see    Quantity#getOutputUnit
     */
    public static void showAs(Unit unit) {
        FACTORY.showInstancesAs(unit);
    }

    private static final long serialVersionUID = -2265853161174238042L;
}
