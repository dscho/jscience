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


import java.util.Date;

import org.jscience.physics.units.ConversionException;
import org.jscience.physics.units.SI;
import org.jscience.physics.units.Unit;


/**
 * This class represents a period of existence or persistence. The system
 *  unit for this quantity is "s" (Système International d'Unités).
 *
 * @author  <a href="mailto:jean-marie@dautelle.com">Jean-Marie Dautelle</a>
 * @version 1.0, October 24, 2004
 */
public class Duration extends Quantity {

    /**
     * Holds the system unit.
     */
    private final static Unit SYSTEM_UNIT = SI.SECOND;

    /**
     * Holds the factory for this class.
     */
    private final static Factory FACTORY = new Factory(SYSTEM_UNIT) {
        protected Quantity newQuantity() {
             return new Duration();
        }
    };

    /**
     * Represents a {@link Duration} amounting to nothing.
     */
    public final static Duration ZERO = (Duration) valueOf(0, SYSTEM_UNIT);

    /**
     * Default constructor (allows for derivation).
     */
    protected Duration() {}

    /**
     * Returns the {@link Duration} corresponding to the specified quantity.
     *
     * @param  quantity a quantity compatible with {@link Duration}.
     * @return the specified quantity or a new {@link Duration} instance.
     * @throws ConversionException if the current model does not allow the
     *         specified quantity to be converted to {@link Duration}.
     */
    public static Duration durationOf(Quantity quantity) {
        return (Duration) FACTORY.quantity(quantity);
    }

    /**
     * Shows {@link Duration} instances in the specified unit.
     *
     * @param  unit the output unit for {@link Duration} instances.
     * @see    Quantity#getOutputUnit
     */
    public static void showAs(Unit unit) {
        FACTORY.showInstancesAs(unit);
    }

    ///////////////////////
    // DURATION SPECIFIC //
    ///////////////////////

    /**
     * Returns the {@link Duration} from the ellapsed time between two dates.
     *
     * @param  from the departure date.
     * @param  to the arrival date.
     * @return the ellapsed time between the specified dates.
     */
    public static Duration between(Date from, Date to) {
        return (Duration) valueOf(from.getTime() - to.getTime(), 0.5,
                                SI.MILLI(SI.SECOND));
    }

    /**
     * Returns the date after the specified date. How long after being
     * specified by this {@link Duration}.
     *
     * @param  date the date of origin.
     * @return <code>date + this</code>.
     */
    public Date addTo(Date date) {
        return new Date(date.getTime() + longValue(SI.MILLI(SI.SECOND)));
    }

    /**
     *  Returns the date before the specified date. How long before being
     *  specified by this {@link Duration}.
     *
     * @param  date the date of origin.
     * @return <code>date - this</code>
     */
    public Date subtractFrom(Date date) {
        return new Date(date.getTime() - longValue(SI.MILLI(SI.SECOND)));
    }

    private static final long serialVersionUID = -82507745696723925L;
}
