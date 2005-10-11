/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2005 - JScience (http://jscience.org/)
 * All rights reserved.
 * 
 * Permission to use, copy, modify, and distribute this software is
 * freely granted, provided that this notice is preserved.
 */
package org.jscience.physics.units;

import java.io.Serializable;

import javolution.lang.Text;
import javolution.util.FastMap;

/**
 * <p> This class represents an unit dimension. Two units <code>u1</code>
 *     and <code>u2</code> are {@link Unit#isCompatible compatible} if and 
 *     only if <code>(u1.getDimension() == u2.getDimension())</code></p>
 *
 * @author  <a href="mailto:jean-marie@dautelle.com">Jean-Marie Dautelle</a>
 * @version 1.1 May 24, 2005
 * @see Unit#getDimension
 */
public final class Dimension implements Serializable {

	/**
     * Holds the dimension instances (pseudo-unit to dimension mapping).
     */
    private static FastMap<Unit, Dimension> COLLECTION 
         = new FastMap<Unit, Dimension>();

    /**
     * Holds dimensionless.
     */
    public static final Dimension NONE = new Dimension(Unit.ONE);
    
    /**
     * Holds length dimension (L).
     */
    public static final Dimension LENGTH = new Dimension('L');

    /**
     * Holds mass dimension (M).
     */
    public static final Dimension MASS = new Dimension('M');

    /**
     * Holds time dimension (T).
     */
    public static final Dimension TIME = new Dimension('T');
    
    /**
     * Holds electric current dimension (I).
     */
    public static final Dimension ELECTRIC_CURRENT = new Dimension('I');

    /**
     * Holds temperature dimension (θ).
     */
    public static final Dimension TEMPERATURE = new Dimension('θ');

    /**
     * Holds amount of substance dimension (N).
     */
    public static final Dimension AMOUNT_OF_SUBSTANCE = new Dimension('N');

    /**
     * Holds luminous intensity dimension (J).
     */
    public static final Dimension LUMINOUS_INTENSITY = new Dimension('J');

    /**
     * Holds the pseudo unit associated to this dimension.
     */
    final Unit _pseudoUnit;

    /**
     * Creates a new dimension associated to the specified symbol.
     * 
     * @param symbol the symbol for this dimension.
     * @throws IllegalArgumentException if the specified symbol is already 
     *         associated to an existing dimension.
     */
    public Dimension(char symbol) {
        _pseudoUnit = new BaseUnit("[" + symbol + "]", this, Converter.IDENTITY);
        synchronized (COLLECTION) {
            if (COLLECTION.containsKey(_pseudoUnit)) 
                throw new IllegalArgumentException("Symbol: " + symbol + 
                        " associate to existing dimension");
            COLLECTION.put(_pseudoUnit, this);
        }
    }
    
    /**
     * Creates a dimension having the specified pseudo-unit.
     * 
     * @param pseudoUnit the pseudo-unit identifying this dimension.
     */
    private Dimension(Unit pseudoUnit) {
        _pseudoUnit = pseudoUnit;
    }

    /**
     * Returns the product of this dimension with the one specified.
     *
     * @param  that the dimension multiplicand.
     * @return <code>this * that</code>
     */
    public final Dimension multiply(Dimension that) {
        Unit pseudoUnit = this._pseudoUnit.times(that._pseudoUnit);
        Dimension d = COLLECTION.get(pseudoUnit);
        if (d != null) return d;
        synchronized (COLLECTION) { // Performs synchronized check.
            d = COLLECTION.get(pseudoUnit);
            if (d != null) return d;
            d = new Dimension(pseudoUnit);
            COLLECTION.put(pseudoUnit, d);
            return d;
        }
    }
    
    /**
     * Returns the quotient of this dimension with the one specified.
     *
     * @param  that the dimension divisor.
     * @return <code>this / that</code>
     */
    public final Dimension divide(Dimension that) {
        Unit pseudoUnit = this._pseudoUnit.divide(that._pseudoUnit);
        Dimension d = COLLECTION.get(pseudoUnit);
        if (d != null) return d;
        synchronized (COLLECTION) { // Performs synchronized check.
            d = COLLECTION.get(pseudoUnit);
            if (d != null) return d;
            d = new Dimension(pseudoUnit);
            COLLECTION.put(pseudoUnit, d);
            return d;
        }
    }

    /**
     * Returns this dimension raised to an exponent.
     *
     * @param  n the exponent.
     * @return the result of raising this dimension to the exponent.
     */
    public final Dimension pow(int n) {
        Unit pseudoUnit = this._pseudoUnit.pow(n);
        Dimension d = COLLECTION.get(pseudoUnit);
        if (d != null) return d;
        synchronized (COLLECTION) { // Performs synchronized check.
            d = COLLECTION.get(pseudoUnit);
            if (d != null) return d;
            d = new Dimension(pseudoUnit);
            COLLECTION.put(pseudoUnit, d);
            return d;
        }
    }

    /**
     * Returns the given root of this dimension.
     *
     * @param  n the root's order.
     * @return the result of taking the given root of this dimension.
     * @throws ArithmeticException if <code>n == 0</code>.
     */
    public final Dimension root(int n) {
        Unit pseudoUnit = this._pseudoUnit.root(n);
        Dimension d = COLLECTION.get(pseudoUnit);
        if (d != null) return d;
        synchronized (COLLECTION) { // Performs synchronized check.
            d = COLLECTION.get(pseudoUnit);
            if (d != null) return d;
            d = new Dimension(pseudoUnit);
            COLLECTION.put(pseudoUnit, d);
            return d;
        }
    }
    
    /**
     * Returns the textual representation of this dimension.
     *
     * @return the text representation of this dimension.
     */
    public Text toText() {
        return UnitFormat.current().format(this._pseudoUnit);
    }

    /**
     * Returns the standard <code>String</code> representation of this
     * dimenstion.
     *
     * @return <code>toText().toString();</code>
     */
    public final String toString() {
        return this.toText().toString();
    }
    
    /**
     * Overrides <code>readResolve()</code> to ensure that deserialization
     * maintains dimension unicity.
     *
     * @return a new dimension or an existing dimension.
     */
    protected Object readResolve() {
        Dimension d = COLLECTION.get(_pseudoUnit);
        if (d != null) return d;
        synchronized (COLLECTION) { // Performs synchronized check.
            d = COLLECTION.get(_pseudoUnit);
            if (d != null) return d;
            COLLECTION.put(_pseudoUnit, this);
            return this;
        }
    }

    private static final long serialVersionUID = 1L;
}