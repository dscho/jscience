/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2006 - JScience (http://jscience.org/)
 * All rights reserved.
 * 
 * Permission to use, copy, modify, and distribute this software is
 * freely granted, provided that this notice is preserved.
 */
package javax.units;

import javax.quantities.Quantity;
import javax.units.converters.UnitConverter;

/**
 * <p> This class represents the multi-radix units (e.g.&nbsp;"hour:min:sec"). 
 *     Instances of this class are created using the {@link Unit#compound
 *     Unit.compound} method.</p>
 *      
 * <p> Examples of compound units:[code]
 *     Unit<Duration> HOUR_MINUTE_SECOND = HOUR.compound(MINUTE).compound(SECOND);
 *     Unit<Angle> DEGREE_MINUTE_ANGLE = DEGREE_ANGLE.compound(MINUTE_ANGLE);
 *     [/code]</p>
 *
 * @author  <a href="mailto:jean-marie@dautelle.com">Jean-Marie Dautelle</a>
 * @version 1.2, February 9, 2006
 */
public final class CompoundUnit<Q extends Quantity> extends DerivedUnit<Q> {

    /**
     * Holds the first unit (cannot be a compound unit)
     */
    private final Unit<Q> _first;

    /**
     * Holds the next units.
     */
    private final Unit<Q> _next;

    /**
     * Creates a compound unit from the specified units. 
     *
     * @param  unit the first unit.
     * @param  next the next unit(s)
     * @throws IllegalArgumentException if both units do not have equals 
     *         base units.
     */
    CompoundUnit(Unit<Q> first, Unit<Q> next) {
        if (!first.getBaseUnits().equals(next.getBaseUnits())) 
            throw new IllegalArgumentException(
                    "Both units do not have equals base units");
        if (first instanceof CompoundUnit) {
            CompoundUnit<Q> firstUnit = (CompoundUnit<Q>) first;
            _first = firstUnit._first;
            _next = new CompoundUnit<Q>(firstUnit._next, next);
        } else {
            _first = first;
            _next = next;
        }
    }

    /**
     * Returns the first unit of this compound unit (never a compound unit)
     *
     * @return the first unit.
     */
    public Unit<Q> getFirst() {
        return _first;
    }

    /**
     * Returns the next unit(s) of this compound unit (can be a compound unit).
     *
     * @return the next units.
     */
    public Unit<Q> getNext() {
        return _next;
    }

    /**
     * Indicates if this compound unit is considered equals to the specified 
     * object (both are compound units with same composing units in the 
     * same order).
     *
     * @param  that the object to compare for equality.
     * @return <code>true</code> if <code>this</code> and <code>that</code>
     *         are considered equals; <code>false</code>otherwise. 
     */
    public boolean equals(Object that) {
        if (this == that) return true;
        if (!(that instanceof CompoundUnit)) return false;
        CompoundUnit thatUnit = (CompoundUnit) that; 
        return  this._first.equals(thatUnit._first) && 
        this._next.equals(thatUnit._next);
    }

    // Implements abstract method.
    public int hashCode() {
        return _first.hashCode() ^ _next.hashCode();
    }

    // Implements abstract method.
    public Unit<? super Q> getBaseUnits() {
        return _first.getBaseUnits();  // equals(_next.getBaseUnits())
    }

    // Implements abstract method.
    public UnitConverter toBaseUnits() {
        return _first.toBaseUnits();
    }

    private static final long serialVersionUID = 8226634084067305310L;
}