/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2010 - JScience (http://jscience.org/)
 * All rights reserved.
 *
 * Permission to use, copy, modify, and distribute this software is
 * freely granted, provided that this notice is preserved.
 */
package org.jscience.physics.unit.format;

import java.math.BigInteger;
import org.jscience.physics.unit.converter.PhysicalUnitConverter;
import org.jscience.physics.unit.converter.RationalConverter;

/**
 * This class represents the prefixes recognized when parsing/formatting.
 *
 * @version 5.0, October 12, 2010
 */
public enum Prefix {

    YOTTA(new RationalConverter(BigInteger.TEN.pow(24), BigInteger.ONE)),
    ZETTA(new RationalConverter(BigInteger.TEN.pow(21), BigInteger.ONE)),
    EXA(new RationalConverter(BigInteger.TEN.pow(18), BigInteger.ONE)),
    PETA(new RationalConverter(BigInteger.TEN.pow(15), BigInteger.ONE)),
    TERA(new RationalConverter(BigInteger.TEN.pow(12), BigInteger.ONE)),
    GIGA(new RationalConverter(BigInteger.TEN.pow(9), BigInteger.ONE)),
    MEGA(new RationalConverter(BigInteger.TEN.pow(6), BigInteger.ONE)),
    KILO(new RationalConverter(BigInteger.TEN.pow(3), BigInteger.ONE)),
    HECTO(new RationalConverter(BigInteger.TEN.pow(2), BigInteger.ONE)),
    DEKA(new RationalConverter(BigInteger.TEN.pow(1), BigInteger.ONE)),
    DECI(new RationalConverter( BigInteger.ONE, BigInteger.TEN.pow(1))),
    CENTI(new RationalConverter( BigInteger.ONE, BigInteger.TEN.pow(2))),
    MILLI(new RationalConverter( BigInteger.ONE, BigInteger.TEN.pow(3))),
    MICRO(new RationalConverter( BigInteger.ONE, BigInteger.TEN.pow(6))),
    NANO(new RationalConverter( BigInteger.ONE, BigInteger.TEN.pow(9))),
    PICO(new RationalConverter( BigInteger.ONE, BigInteger.TEN.pow(12))),
    FEMTO(new RationalConverter( BigInteger.ONE, BigInteger.TEN.pow(15))),
    ATTO(new RationalConverter( BigInteger.ONE, BigInteger.TEN.pow(18))),
    ZEPTO(new RationalConverter( BigInteger.ONE, BigInteger.TEN.pow(21))),
    YOCTO(new RationalConverter( BigInteger.ONE, BigInteger.TEN.pow(24)));

    private final PhysicalUnitConverter _converter;

    /**
     * Creates a new prefix.
     *
     * @param converter the associated unit converter.
     */
    Prefix (PhysicalUnitConverter converter) {
        _converter = converter;
    }

    /**
     * Returns the corresponding unit converter.
     *
     * @return the unit converter.
     */
    public PhysicalUnitConverter getConverter() {
        return _converter;
    }
}
