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
 * This class represents an illuminance. The system unit for this quantity
 * is "cd·sr/m²".  By default, instances of this class are showed in Lux ("lx").
 *
 * @author  <a href="mailto:jean-marie@dautelle.com">Jean-Marie Dautelle</a>
 * @version 6.0, June 13, 2004
 */
public class Illuminance extends Quantity {

    /**
     * Holds the system unit.
     */
    private final static Unit SYSTEM_UNIT = SI.CANDELA.multiply(SI.STERADIAN)
            .divide(SI.METER.pow(2));

    /**
     * Holds the factory for this class.
     */
    private final static Factory FACTORY = new Factory(SYSTEM_UNIT) {
        protected Quantity newQuantity() {
            return new Illuminance();
        }
    }.useFor(SI.LUX);

    /**
     * Represents an {@link Illuminance} amounting to nothing.
     */
    public final static Illuminance ZERO = (Illuminance) valueOf(0, SYSTEM_UNIT);

    /**
     * Default constructor (allows for derivation).
     */
    protected Illuminance() {
    }

    /**
     * Returns the {@link Illuminance} corresponding to the specified quantity.
     *
     * @param  quantity a quantity compatible with {@link Illuminance}.
     * @return the specified quantity or a new {@link Illuminance} instance.
     * @throws ConversionException if the current model does not allow the
     *         specified quantity to be converted to {@link Illuminance}.
     */
    public static Illuminance illuminanceOf(Quantity quantity) {
        return (Illuminance) FACTORY.quantity(quantity);
    }

    /**
     * Shows {@link Illuminance} instances in the specified unit.
     *
     * @param  unit the output unit for {@link Illuminance} instances.
     * @see    Quantity#getOutputUnit
     */
    public static void showAs(Unit unit) {
        FACTORY.showInstancesAs(unit);
    }

    private static final long serialVersionUID = 3559730295234637650L;
}