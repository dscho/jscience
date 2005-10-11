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
 * <p> This class represents a unit derived from another unit using
 *     a {@link Converter}.</p>
 * <p> Examples of transformed units:
 *     <pre><code>
 *         CELSIUS = KELVIN.add(273.15); // Use label from unit database.
 *         MILLISECOND = SECOND.multiply(1e-3); // Use label from unit database.
 *     </code></pre></p>
 * <p> Transformed units have no intrinsic symbol. But like any other units,
 *     they may have labels attached to them:
 *     <pre><code>
 *         FOOT = METER.multiply(0.3048).label("ft");
 *         CENTIMETER = METER.multiply(0.01).label"cm");
 *         CALENDAR_YEAR = DAY.multiply(365).label("year");
 *     </code></pre>
 *     or aliases:
 *     <pre><code>
 *         KELVIN.add(273.15).alias("Celsius");
 *         METER.multiply(0.01).alias("centimeter");
 *         METER.multiply(0.01).alias("centimetre");
 *     </code></pre>
 *
 * @author  <a href="mailto:jean-marie@dautelle.com">Jean-Marie Dautelle</a>
 * @version 1.1, May  24, 2004
 * @see     Unit#plus(double)
 * @see     Unit#times(double)
 * @see     Unit#transform(Converter)
 * @see     UnitFormat
 */
class TransformedUnit<Q extends Quantity> extends DerivedUnit<Q> {

    /**
     * Creates a transformed unit derived from the specified unit using
     * the specified converter.
     *
     * @param  sourceUnit the units from which this unit is derived.
     * @param  toParentUnit the converter to the parent unit.
     */
    protected TransformedUnit(Unit<? super Q> parentUnit, Converter toParentUnit) {
        while (parentUnit instanceof TransformedUnit) {
            toParentUnit = parentUnit._toParentUnit.concatenate(toParentUnit);
            parentUnit = parentUnit._parentUnit;
        }
        _parentUnit = parentUnit;
        _toParentUnit = toParentUnit;
    }

    // Implements abstract method.
    protected boolean equalsImpl(Object that) {
        return (that instanceof TransformedUnit)
                && (((TransformedUnit) that)._parentUnit == _parentUnit)
                && ((TransformedUnit) that)._toParentUnit.equals(_toParentUnit);
    }

    // Implements abstract method.
    protected int hashCodeImpl() {
        return _parentUnit.hashCode() + _toParentUnit.hashCode();
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