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
 * This class represents a radioactive activity. The system unit for
 * this quantity is "Bq".
 *
 * @author  <a href="mailto:jean-marie@dautelle.com">Jean-Marie Dautelle</a>
 * @version 1.0, October 24, 2004
 */
public class RadioactiveActivity extends Quantity {

    /**
     * Holds the system unit.
     */
    private final static Unit SYSTEM_UNIT = SI.BECQUEREL;

    /**
     * Holds the factory for this class.
     */
    private final static Factory FACTORY = new Factory(SYSTEM_UNIT) {
        protected Quantity newQuantity() {
             return new RadioactiveActivity();
        }
    };

    /**
     * Represents a {@link RadioactiveActivity} amounting to nothing.
     */
    public final static RadioactiveActivity ZERO
        = (RadioactiveActivity) valueOf(0, SYSTEM_UNIT);

    /**
     * Default constructor (allows for derivation).
     */
    protected RadioactiveActivity() {}

    /**
     * Returns the {@link RadioactiveActivity} corresponding to the specified
     * quantity.
     *
     * @param  quantity a quantity compatible with {@link RadioactiveActivity}.
     * @return the specified quantity or a new {@link RadioactiveActivity} instance.
     * @throws ConversionException if the current model does not allow the
     *         specified quantity to be converted to
     *         {@link RadioactiveActivity}.
     */
    public static RadioactiveActivity radioactiveActivityOf(Quantity quantity) {
        return (RadioactiveActivity) FACTORY.quantity(quantity);
    }

    /**
     * Shows {@link RadioactiveActivity} instances in the specified unit.
     *
     * @param  unit the output unit for {@link RadioactiveActivity} instances.
     * @see    Quantity#getOutputUnit
     */
    public static void showAs(Unit unit) {
        FACTORY.showInstancesAs(unit);
    }

    private static final long serialVersionUID = -100533533714319460L;
}
