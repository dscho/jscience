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
 * This class represents the extent of something along its greatest dimension
 * or the extent of space between two objects or places. The system unit for
 * this quantity is "m" (Système International d'Unités).
 *
 * @author  <a href="mailto:jean-marie@dautelle.com">Jean-Marie Dautelle</a>
 * @version 1.0, October 24, 2004
 */
public class Length extends Quantity {

    /**
     * Holds the system unit.
     */
    private final static Unit SYSTEM_UNIT = SI.METER;

    /**
     * Holds the factory for this class.
     */
    private final static Factory FACTORY = new Factory(SYSTEM_UNIT) {
        protected Quantity newQuantity() {
             return new Length();
        }
    };

    /**
     * Represents a {@link Length} amounting to nothing.
     */
    public final static Length ZERO = (Length) valueOf(0, SYSTEM_UNIT);

    /**
     * Default constructor (allows for derivation).
     */
    protected Length() {}

    /**
     * Returns the {@link Length} corresponding to the specified quantity.
     *
     * @param  quantity a quantity compatible with {@link Length}.
     * @return the specified quantity or a new {@link Length} instance.
     * @throws ConversionException if the current model does not allow the
     *         specified quantity to be converted to {@link Length}.
     */
    public static Length lengthOf(Quantity quantity) {
        return (Length) FACTORY.quantity(quantity);
    }

    /**
     * Shows {@link Length} instances in the specified unit.
     *
     * @param  unit the output unit for {@link Length} instances.
     * @see    Quantity#getOutputUnit
     */
    public static void showAs(Unit unit) {
        FACTORY.showInstancesAs(unit);
    }

    /////////////////////
    // LENGTH SPECIFIC //
    /////////////////////

    /**
     * Returns the length of a circular arc.
     *
     * @param  radius the circle radius.
     * @param  theta the central angle.
     * @return the length of the specified circular arc.
     */
    public static Length arcOf(Length radius, Angle theta) {
        // Returns theta * radius
        return lengthOf(theta.multiply(radius));
    }

    private static final long serialVersionUID = 2110601334827305173L;
}
