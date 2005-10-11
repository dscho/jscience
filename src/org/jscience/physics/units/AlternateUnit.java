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

/**
 * <p> This class represents an alternate unit. Alternate units are used
 *     in expressions to distinguish between quantities of a different nature
 *     but of the same dimensions (e.g. angle <code>rad</code>, 
 *     angular acceleration <code>rad/s²</code>).</p>
 * <p> Instances of this class are created using the {@link Unit#alternate}
 *     method. For example:<pre>
 *         Unit<Angle> RADIAN = (Unit<Angle>) SI.ONE.alternate("rad");
 *     </pre></p>
 *
 * @author  <a href="mailto:jean-marie@dautelle.com">Jean-Marie Dautelle</a>
 * @version 1.1, May 24, 2005
 */
class AlternateUnit<Q extends Quantity> extends DerivedUnit<Q> {

    /**
     * Creates an alternate unit for the specified unit identified by the 
     * specified symbol. 
     *
     * @param  symbol the symbol for this alternate unit.
     * @param  baseUnits the base units from which this unit is derived from.
     * @param  toBaseUnits the converter to the base unit.
     */
     protected AlternateUnit(String symbol, Unit<? super Q> parentUnit, 
             Converter toParentUnit) {
        _symbol = symbol;
        _parentUnit = parentUnit;
        _toParentUnit = toParentUnit;
    }

    /**
     * Returns the symbol for this alternate unit.
     *
     * @return this alternate unit symbol.
     */
    public final String getSymbol() {
        return _symbol;
    }

    // Implements abstract method.
    protected boolean equalsImpl(Object that) {
        return (that instanceof AlternateUnit) &&
           ((AlternateUnit)that)._symbol.equals(_symbol) &&
           (((AlternateUnit)that)._parentUnit == _parentUnit)&&
           (((AlternateUnit)that)._toParentUnit == _toParentUnit);
    }

    // Implements abstract method.
    protected int hashCodeImpl() { 
        return _symbol.hashCode();
    }

    // Implements abstract method.
    protected final Unit<? super Q> getParentUnitImpl() {
        return _parentUnit;
    }
    
    // Implements abstract method.
    protected final Converter toParentUnitImpl() {
        return _toParentUnit;
    }

   private static final long serialVersionUID = 1L;
}