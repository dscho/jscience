/*
 * jScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2004 - The jScience Consortium (http://jscience.org/)
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation (http://www.gnu.org/copyleft/lesser.html); either version
 * 2.1 of the License, or any later version.
 */
package org.jscience.physics.units;
import javolution.realtime.LocalContext;

/**
 * <p> This class represents the building blocks on top of which all others
 *     units are created.</p>
 * <p> By default, base units are mutually independent. Although, in specialized
 *     context (e.g. relativistic context), conversions between base units
 *     is possible (Ref. {@link #setDimension}).</p>
 * <p> Examples of base units:
 *     <pre><code>
 *         METER = BaseUnit.getInstance("m");
 *         KILOGRAM = BaseUnit.getInstance("kg");
 *         SECOND = BaseUnit.getInstance("s");
 *         AMPERE = BaseUnit.getInstance("A");
 *     </code></pre></p>
 *
 * @author  <a href="mailto:jean-marie@dautelle.com">Jean-Marie Dautelle</a>
 * @version 1.0, October 24, 2004
 */
public class BaseUnit extends Unit {

    /**
     * Holds the dimension variable.
     */
    transient LocalContext.Variable dimensionVariable;

    /**
     * Holds the converter to dimension.
     */
    transient LocalContext.Variable toDimensionVariable;

    /**
     * Creates a base unit with the specified symbol.
     *
     * @param  symbol the symbol of this base unit.
     */
    protected BaseUnit(String symbol) {
        super(symbol);
    }

    /**
     * Returns the base unit with the specified symbol.
     * If the base unit does not already exist, then it is created.
     *
     * @param  symbol the base unit symbol.
     * @return the corresponding base unit.
     * @throws IllegalArgumentException if the specified symbol is currently
     *         associated to a different type of unit.
     */
    public static BaseUnit getInstance(String symbol) {
        BaseUnit newUnit = new BaseUnit(symbol);
        return (BaseUnit) getInstance(newUnit); // Ensures unicity.
    }

    /**
     * Returns the symbol for this base unit.
     *
     * @return this base unit's symbol.
     */
    public final String getSymbol() {
        return _symbol;
    }

    /**
     * Sets the dimension of this base unit (context-local).
     * By default a base unit's dimension is itself. It is possible to
     * set the dimension to any other unit. The only constraint being
     * that distinct dimensional units should be independent from each other
     * (e.g. if the dimensional unit for meter is "ns", then the dimensional
     *       unit for second should be "ns" as well). For example:<pre>
     * LocalContext.enter(); // Ensures that setting is local to current thread.
     * try {
     *     SI.METER.setDimension(SI.NANO(SI.SECOND),
     *                           new MultiplyConverter(1e9 / c));
     *     SI.SECOND.setDimension(SI.NANO(SI.SECOND),
     *                            new MultiplyConverter(1e9));
     *     // In this high-energy context, length and time are compatible,
     *     // they have the same "ns" dimensional unit.
     * } finally {
     *     LocalContext.exit();
     * }</pre>
     *
     * @param  dimension the unit identifying the new dimension of this
     *         base unit.
     * @param  toDimension the converter to the specified dimensional unit.
     * @see    LocalContext
     */
    public void setDimension(Unit dimension, Converter toDimension) {
        dimensionVariable.setValue(dimension);
        toDimensionVariable.setValue(toDimension);
        // Invalidate dimensions caches.
        Unit.DIMENSIONS_VALID.setValue(Boolean.FALSE);
    }

    // Implements abstract method.
    public Unit getSystemUnit() {
        return this; // Base units are system units.
    }

    // Implements abstract method.
    public boolean equals(Object that) {
        return (this == that)
                || ((that instanceof BaseUnit) && ((BaseUnit) that)._symbol
                        .equals(_symbol));
    }

    // Implements abstract method.
    int calculateHashCode() {
        return _symbol.hashCode();
    }

    // Implements abstract method.
    Unit getCtxDimension() {
        return (Unit) dimensionVariable.getValue();
    }

    // Implements abstract method.
    Converter getCtxToDimension() {
        return (Converter) toDimensionVariable.getValue();
    }

    private static final long serialVersionUID = -2744347203800613943L;
}