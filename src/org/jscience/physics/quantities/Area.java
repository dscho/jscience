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
 * This class represents the extent of a planar region or of the surface of
 * a solid measured in square units. The system unit for this quantity
 * is "m²".
 *
 * @author  <a href="mailto:jean-marie@dautelle.com">Jean-Marie Dautelle</a>
 * @version 1.0, October 24, 2004
 */
public class Area extends Quantity {

    /**
     * Holds the system unit.
     */
    private final static Unit SYSTEM_UNIT = SI.METER.pow(2);

    /**
     * Holds the factory for this class.
     */
    private final static Factory FACTORY = new Factory(SYSTEM_UNIT) {
        protected Quantity newQuantity() {
             return new Area();
        }
    };

    /**
     * Represents an {@link Area} amounting to nothing.
     */
    public final static Area ZERO = (Area) valueOf(0, SYSTEM_UNIT);

    /**
     * Default constructor (allows for derivation).
     */
    protected Area() {}

    /**
     * Returns the {@link Area} corresponding to the specified quantity.
     *
     * @param  quantity a quantity compatible with {@link Area}.
     * @return the specified quantity or a new {@link Area} instance.
     * @throws ConversionException if the current model does not allow the
     *         specified quantity to be converted to {@link Area}.
     */
    public static Area areaOf(Quantity quantity) {
        return (Area) FACTORY.quantity(quantity);
    }

    /**
     * Shows {@link Area} instances in the specified unit.
     *
     * @param  unit the output unit for {@link Area} instances.
     * @see    Quantity#getOutputUnit
     */
    public static void showAs(Unit unit) {
        FACTORY.showInstancesAs(unit);
    }

    ///////////////////
    // AREA SPECIFIC //
    ///////////////////

    /**
     * Returns the area of a circle sector (slice of a circle).
     *
     * @param  radius the circle radius.
     * @param  theta the central angle.
     * @return the area of the specified circle sector.
     */
    public static Area circleOf(Length radius, Angle theta) {
        // Returns (theta / 2.0) * radius^2
        return areaOf(theta.multiply(radius.pow(2.0)).divide(2.0));
    }

    private static final long serialVersionUID = 3375073435913259804L;
}
