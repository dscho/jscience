/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2006 - JScience (http://jscience.org/)
 * All rights reserved.
 * 
 * Permission to use, copy, modify, and distribute this software is
 * freely granted, provided that this notice is preserved.
 */
package javax.measure;

import java.io.Serializable;
import javax.measure.quantity.Quantity;
import javax.measure.unit.Unit;

/**
 * This class represents the result of a measurement stated in a known unit.
 * This class can represents any kind of measure (scalar, vector, statistical).
 * A factory method for numerical quantities (<code>java.lang.Number</code>)
 * is {@link #valueOf provided}.  
 *  
 * @author  <a href="mailto:jean-marie@dautelle.com">Jean-Marie Dautelle</a>
 * @version 4.0, February 25, 2006
 */
public abstract class Measure<V, Q extends Quantity> implements Measurable<Q>, Serializable {

    /**
     * Holds the measurement value.
     */
    private V _value;

    /**
     * Holds the measurement unit.
     */
    private Unit<Q> _unit;

    /**
     * Creates a measure for the specified value stated in the specified unit.
     */
    protected Measure(V value, Unit<Q> unit) {
        _value = value;
        _unit = unit;
    }

    /**
     * Returns the measure for the specified numerical quantity.
     * 
     * @param value the measurement value.
     * @param unit the measurement unit.
     */
    public static <N extends Number, Q extends Quantity> 
            Measure<N, Q> valueOf(N value, Unit<Q> unit) {
        return new NumericMeasure<N, Q>(value, unit);
    }
    
    /**
     * Returns the measurement value of this measure.
     *    
     * @return the measurement value.
     */
    public final V getValue() {
        return _value;
    }

    /**
     * Returns the measurement unit of this measure.
     * 
     * @return the measurement unit.
     */
    public final Unit<Q> getUnit() {
        return _unit;
    }

    /**
     * Compares this measure with the one specified independently of the 
     * measurement units.
     * 
     * @param that the quantity to compare with.
     * @return negative integer, zero, or a positive integer as this quantity
     *         amount is less than, equal to, or greater than the specified 
     *         quantity.
     */
    public int compareTo(Measurable<Q> that) {
        return Double.compare(this.doubleValue(_unit), that.doubleValue(_unit));
    }
 
    /**
     * Compares this measure against the specified object for 
     * strict equality (same unit and amount).
     * To compare measures stated using different units the  
     * {@link #compareTo} method should be used. 
     *
     * @param  obj the object to compare with.
     * @return <code>true</code> if both objects are identical (same 
     *         unit and same amount); <code>false</code> otherwise.
     */
    public boolean equals(Object obj) {
        if (!(obj instanceof Measure)) return false;
        Measure that = (Measure) obj;
        return this._unit.equals(that._unit) && this._value.equals(that._value);
    }

    /**
     * Returns the hash code for this scalar.
     * 
     * @return the hash code value.
     */
    public int hashCode() {
        return _unit.hashCode() + _value.hashCode();
    }

    /**
     * Returns the <code>String</code> representation of this measure.
     * 
     * @return <code>this.getValue() + " " + this.getUnit()</code>
     */
    public String toString() {
        return this.getValue() + " " + this.getUnit();
    }
}