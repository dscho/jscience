/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2005 - JScience (http://jscience.org/)
 * All rights reserved.
 * 
 * Permission to use, copy, modify, and distribute this software is
 * freely granted, provided that this notice is preserved.
 */
package org.jscience.physics.quantities;

import java.util.Date;

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
     * Holds the associated unit.
     */
    private final static Unit<Duration> UNIT = SI.SECOND;

    /**
     * Holds the factory for this class.
     */
    private final static Factory<Duration> FACTORY = new Factory<Duration>(UNIT) {
        protected Duration create() {
            return new Duration();
        }
    };

    /**
     * Represents a {@link Duration} amounting to nothing.
     */
    public final static Duration ZERO = Quantity.valueOf(0, UNIT);

    /**
     * Default constructor (allows for derivation).
     */
    protected Duration() {
    }

    /**
     * Shows {@link Duration} instances in the specified unit.
     *
     * @param unit the display unit for {@link Duration} instances.
     */
    public static void showAs(Unit unit) {
        QuantityFormat.show(Duration.class, unit);
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
        return Quantity.valueOf(from.getTime() - to.getTime(), 0.5, SI
                .MILLI(SI.SECOND));
    }

    /**
     * Returns the date after the specified date. How long after being
     * specified by this {@link Duration}.
     *
     * @param  date the date of origin.
     * @return <code>date + this</code>.
     */
    public Date addTo(Date date) {
        return new Date(date.getTime()
                + this.to(SI.MILLI(SI.SECOND)).longValue());
    }

    /**
     *  Returns the date before the specified date. How long before being
     *  specified by this {@link Duration}.
     *
     * @param  date the date of origin.
     * @return <code>date - this</code>
     */
    public Date subtractFrom(Date date) {
        return new Date(date.getTime()
                - this.to(SI.MILLI(SI.SECOND)).longValue());
    }

    private static final long serialVersionUID = 1L;
}
