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
 * This class represents a distance traveled divided by the time of travel.
 * The system unit for this quantity is "m/s".
 *
 * @author  <a href="mailto:jean-marie@dautelle.com">Jean-Marie Dautelle</a>
 * @version 1.0, October 24, 2004
 */
public class Velocity extends Quantity {

    /**
     * Holds the system unit.
     */
    private final static Unit SYSTEM_UNIT = SI.METER.divide(SI.SECOND);

    /**
     * Holds the factory for this class.
     */
    private final static Factory FACTORY = new Factory(SYSTEM_UNIT) {
        protected Quantity newQuantity() {
             return new Velocity();
        }
    };

    /**
     * Represents a {@link Velocity} amounting to nothing.
     */
    public final static Velocity ZERO = (Velocity) valueOf(0, SYSTEM_UNIT);

    /**
     * Holds the speed of light in vacuum (exact).
     */
    public final static Velocity SPEED_OF_LIGHT
        = (Velocity) valueOf(299792458, SYSTEM_UNIT);

    /**
     * Default constructor (allows for derivation).
     */
    protected Velocity() {}

    /**
     * Returns the {@link Velocity} corresponding to the specified quantity.
     *
     * @param  quantity a quantity compatible with {@link Velocity}.
     * @return the specified quantity or a new {@link Velocity} instance.
     * @throws ConversionException if the current model does not allow the
     *         specified quantity to be converted to {@link Velocity}.
     */
    public static Velocity velocityOf(Quantity quantity) {
        return (Velocity) FACTORY.quantity(quantity);
    }

    /**
     * Shows {@link Velocity} instances in the specified unit.
     *
     * @param  unit the output unit for {@link Velocity} instances.
     * @see    Quantity#getOutputUnit
     */
    public static void showAs(Unit unit) {
        FACTORY.showInstancesAs(unit);
    }

    private static final long serialVersionUID = 2182679747189334410L;
}
