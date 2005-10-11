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
 * <p> This class represents a compound unit. Compound units are used 
 *     to express quantites using multi-radix units. Instances of this
 *     class are created using the {@link Unit#compound} method.
 *     For example:<pre>
 *        Unit HOUR_MINUTE_SECOND 
 *            = NonSI.HOUR.compound(NonSI.MINUTE).compound(SI.SECOND);</pre>
 *     </p>
 *
 * @author  <a href="mailto:jean-marie@dautelle.com">Jean-Marie Dautelle</a>
 * @version 1.1, May 24, 2005
 * @see     Unit#compound
 */
public class CompoundUnit<Q extends Quantity> extends DerivedUnit<Q> {

    /**
     * Holds the main unit.
     */
    private final Unit<Q> _mainUnit;

    /**
     * Holds the sub-unit.
     */
    private final Unit<Q> _subUnit;

    /**
     * Creates a compound unit 
     *
     * @param  mainUnit the main unit.
     * @param  subUnit the sub-unit.
     */
    protected CompoundUnit(Unit<Q> mainUnit, Unit<Q> subUnit) {
        _mainUnit = mainUnit;
        _subUnit = subUnit;
    }

    /**
     * Returns the main unit of this compound unit.
     *
     * @return the main unit.
     */
    public Unit<Q> getMainUnit() {
        return _mainUnit;
    }

    /**
     * Returns the sub-unit of this compound unit.
     *
     * @return the sub-unit.
     */
    public Unit<Q> getSubUnit() {
        return _subUnit;
    }

    // Implements abstract method.
    protected boolean equalsImpl(Object that) {
        return (that instanceof CompoundUnit)
                && (((CompoundUnit) that)._mainUnit == _mainUnit)
                && ((CompoundUnit) that)._subUnit.equals(_subUnit);
    }

    // Implements abstract method.
    protected int hashCodeImpl() {
        return _mainUnit.hashCode() ^ _subUnit.hashCode();
    }

    // Implements abstract method.
    protected Unit<? super Q> getParentUnitImpl() {
        return _mainUnit.getParentUnit();
    }

    // Implements abstract method.
    protected Converter toParentUnitImpl() {
        return _mainUnit.toParentUnit();
    }

    private static final long serialVersionUID = 1L;
}