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
 * This class represents a force applied uniformly over a surface.
 * The system unit for this quantity is "kg/(m·s²)". By default, instances of
 * this class are showed in Pascal ("Pa").
 *
 * @author  <a href="mailto:jean-marie@dautelle.com">Jean-Marie Dautelle</a>
 * @version 1.0, October 24, 2004
 */
public class Pressure extends Quantity {

    /**
     * Holds the system unit.
     */
    private final static Unit SYSTEM_UNIT = SI.PASCAL.getDimension();

    /**
     * Holds the factory for this class.
     */
    private final static Factory FACTORY = new Factory(SYSTEM_UNIT) {
        protected Quantity newQuantity() {
             return new Pressure();
        }
    }.useFor(SI.PASCAL);

    /**
     * Represents a {@link Pressure} amounting to nothing.
     */
    public final static Pressure ZERO = (Pressure) valueOf(0, SYSTEM_UNIT);

    /**
     * Holds the standard atmosphere pressure (exact).
     */
    public final static Pressure STANDARD
        = (Pressure) valueOf(101325, SYSTEM_UNIT);

    /**
     * Default constructor (allows for derivation).
     */
    protected Pressure() {}

    /**
     * Returns the {@link Pressure} corresponding to the specified quantity.
     *
     * @param  quantity a quantity compatible with {@link Pressure}.
     * @return the specified quantity or a new {@link Pressure} instance.
     * @throws ConversionException if the current model does not allow the
     *         specified quantity to be converted to {@link Pressure}.
     */
    public static Pressure pressureOf(Quantity quantity) {
        return (Pressure) FACTORY.quantity(quantity);
    }

    /**
     * Shows {@link Pressure} instances in the specified unit.
     *
     * @param  unit the output unit for {@link Pressure} instances.
     * @see    Quantity#getOutputUnit
     */
    public static void showAs(Unit unit) {
        FACTORY.showInstancesAs(unit);
    }

    private static final long serialVersionUID = -4550646071797887005L;
}
