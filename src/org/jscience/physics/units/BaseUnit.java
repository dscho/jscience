/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2005 - JScience (http://jscience.org/)
 * All rights reserved.
 * 
 * Permission to use, copy, modify, and distribute this software is
 * freely granted, provided that this notice is preserved.
 */
package org.jscience.physics.units;
import org.jscience.physics.quantities.Quantity;

import javolution.realtime.LocalReference;

/**
 * <p> This class represents the building blocks on top of which all others
 *     units are created.</p>
 * <p> Base units are typically mutually independent. Although, in specialized
 *     context (e.g. relativistic context), conversions between base units
 *     is possible (Ref. {@link #setDimension}).</p>
 * <p> Examples of base units:
 *     <pre><code>
 *         METER = new BaseUnit&lt;Length>("m", Dimension.LENGTH, Converter.IDENTITY);
 *         KILOGRAM = new BaseUnit&lt;Mass>("kg", Dimension.MASS, Converter.IDENTITY);
 *         SECOND = new BaseUnit&lt;Duration>("s", Dimension.TIME, Converter.IDENTITY);
 *     </code></pre></p>
 *
 * @author  <a href="mailto:jean-marie@dautelle.com">Jean-Marie Dautelle</a>
 * @version 1.1, May 24, 2005
 */
public class BaseUnit<Q extends Quantity> extends Unit<Q> {

    /**
     * Holds the local dimension.
     */
    final LocalReference<Dimension> _dimension;

    /**
     * Holds the local transform.
     */
    final LocalReference<Converter> _transform;

    /**
     * Creates a new base unit having the specified symbol.
     *
     * @param symbol the symbol of this base unit.
     * @param dimension the default dimension for this unit.
     * @param transform the default intrinsic dimensional transform.
     * @throws IllegalArgumentException if the specified symbol is already 
     *         used.
     */
    public BaseUnit(String symbol, Dimension dimension, Converter transform) {
        _symbol = symbol;
        _dimension = new LocalReference<Dimension>(dimension);
        _transform = new LocalReference<Converter>(transform);
        getInstance(this); // Ensures unicity.
    }

    /**
     * Returns the symbol for this base unit.
     *
     * @return this base unit symbol.
     */
    public final String getSymbol() {
        return _symbol;
    }

    /**
     * Sets the {@link javolution.realtime.LocalContext local} dimension of 
     * this base unit. For example:<pre>
     * LocalContext.enter(); // Ensures that setting is local to current thread.
     * try {
     *     SI.METER.setDimension(TIME, new MultiplyConverter(1e9 / c));
     *     // In this high-energy context, length and time are compatible,
     *     // they have the same "ns" dimensional unit.
     * } finally {
     *     LocalContext.exit();
     * }</pre>
     *
     * @param  dimension the unit identifying the new dimension of this
     *         base unit.
     * @param  transform the dimensional intrinsic transform for this 
     *         base unit (typically a multiply converter).
     */
    public void setDimension(Dimension dimension, Converter transform) {
        _dimension.set(dimension);
        _transform.set(transform);
    }

   // Implements abstract method.
    protected boolean equalsImpl(Object that) {
        return (that instanceof BaseUnit) && 
                ((BaseUnit) that)._symbol.equals(_symbol); 
    }

    // Implements abstract method.
    protected int hashCodeImpl() {
        return _symbol.hashCode();
    }    

    // Implements abstract method.
    protected Unit<Q> getParentUnitImpl() {
        return this;
    }
    
    // Implements abstract method.
    protected Converter toParentUnitImpl() {
        return Converter.IDENTITY;
    }
    
    private static final long serialVersionUID = 1L;
}