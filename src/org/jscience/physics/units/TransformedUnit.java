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
 * @version 1.0, October 24, 2004
 * @see     Unit#add(double)
 * @see     Unit#multiply(double)
 * @see     UnitFormat
 */
public class TransformedUnit extends DerivedUnit {

    /**
     * Holds the system unit.
     */
    private final Unit _systemUnit;

    /**
     * Holds the converter to the system unit.
     */
    private final Converter _toSystem;

    /**
     * Creates a transformed unit derived from the specified unit using
     * the specified converter.
     *
     * @param  systemUnit the system unit from which this unit is transformed.
     * @param  toSystem the converter to the system unit.
     */
    private TransformedUnit(Unit systemUnit, Converter toSystem) {
        super(null);
        _systemUnit = systemUnit;
        _toSystem = toSystem;
    }

    /**
     * Returns the unit derived from the specified unit using the specified
     * converter.
     *
     * @param  parent the unit from which this unit is derived.
     * @param  toParent the converter to the parent unit.
     * @return the corresponding derived unit.
     */
    public static Unit getInstance(Unit parent, Converter toParent) {
        Unit systemUnit = parent.getSystemUnit();
        TransformedUnit newUnit = new TransformedUnit(
            systemUnit,
            parent.getConverterTo(systemUnit).concatenate(toParent));
        return (TransformedUnit) getInstance(newUnit); // Ensures unicity.
    }

    // Implements abstract method.
    public Unit getSystemUnit() {
        return _systemUnit;
    }

    // Implements abstract method.
    public boolean equals(Object that) {
        return (this == that) || (
            (that instanceof TransformedUnit) &&
            (((TransformedUnit)that)._systemUnit == _systemUnit) &&
            ((TransformedUnit)that)._toSystem.equals(_toSystem));
    }

    // Implements abstract method.
    int calculateHashCode() {
        return _systemUnit.hashCode() + _toSystem.hashCode();
    }

    // Implements abstract method.
    Unit getCtxDimension() {
        return _systemUnit.getCtxDimension();
    }

    // Implements abstract method.
    Converter getCtxToDimension() {
        return _systemUnit.getCtxToDimension().concatenate(_toSystem);
    }

    private static final long serialVersionUID = 3137995610399363562L;
}