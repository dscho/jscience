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
 * This class represents the degree of hotness or coldness of a body or
 * an environment. The system unit for this quantity is "K"
 * (Système International d'Unités).
 *
 * @author  <a href="mailto:jean-marie@dautelle.com">Jean-Marie Dautelle</a>
 * @version 1.0, October 24, 2004
 */
public class Temperature extends Quantity {

    /**
     * Holds the system unit.
     */
    private final static Unit SYSTEM_UNIT = SI.KELVIN;

    /**
     * Holds the factory for this class.
     */
    private final static Factory FACTORY = new Factory(SYSTEM_UNIT) {
        protected Quantity newQuantity() {
             return new Temperature();
        }
    };

    /**
     * Represents a {@link Temperature} amounting to nothing.
     */
    public final static Temperature ZERO
        = (Temperature) valueOf(0, SYSTEM_UNIT);

    /**
     * Holds the standard temperature (0°C).
     */
    public final static Temperature STANDARD
        = (Temperature) valueOf(273.15, SI.KELVIN);

    /**
     * Default constructor (allows for derivation).
     */
    protected Temperature() {}

    /**
     * Returns the {@link Temperature} corresponding to the specified quantity.
     *
     * @param  quantity a quantity compatible with {@link Temperature}.
     * @return the specified quantity or a new {@link Temperature} instance.
     * @throws ConversionException if the current model does not allow the
     *         specified quantity to be converted to {@link Temperature}.
     */
    public static Temperature temperatureOf(Quantity quantity) {
        return (Temperature) FACTORY.quantity(quantity);
    }

    /**
     * Shows {@link Temperature} instances in the specified unit.
     *
     * @param  unit the output unit for {@link Temperature} instances.
     * @see    Quantity#getOutputUnit
     */
    public static void showAs(Unit unit) {
        FACTORY.showInstancesAs(unit);
    }

    private static final long serialVersionUID = -2273050199169159604L;
}
