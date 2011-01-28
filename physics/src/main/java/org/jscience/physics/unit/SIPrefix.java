/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2010 - JScience (http://jscience.org/)
 * All rights reserved.
 *
 * Permission to use, copy, modify, and distribute this software is
 * freely granted, provided that this notice is preserved.
 */
package org.jscience.physics.unit;

import java.math.BigInteger;
import org.jscience.physics.unit.converter.RationalConverter;
import org.unitsofmeasurement.quantity.Quantity;

/**
 * <p> This class provides support for the 20 SI prefixes used in the metric
 *     system (decimal multiples and submultiples of SI units).
 *     For example:<pre><code>
 *     import static org.jscience.physics.unit.SI.*;  // Static import.
 *     import static org.jscience.physics.unit.SIPrefix.*; // Static import.
 *     import org.unitsofmeasurement.quantity.*;
 *     ...
 *     PhysicsUnit<Pressure> HECTOPASCAL = HECTO(PASCAL);
 *     PhysicsUnit<Length> KILOMETRE = KILO(METRE);
 *     </code></pre>
 * </p>
 *
 * @noextend This class is not intended to be extended by clients.
 * @see <a href="http://en.wikipedia.org/wiki/SI_prefix">Wikipedia: SI Prefix</a>
 *
 * @author <a href="mailto:jean-marie@dautelle.com">Jean-Marie Dautelle</a>
 * @version 5.0, October 12, 2010
 */
public final class SIPrefix {

    /**
     * Default constructor (private).
     */
    private SIPrefix() {
    }

    /**
     * Returns the specified unit multiplied by the factor
     * <code>10<sup>24</sup></code>
     *
     * @param <Q> The type of the quantity measured by the unit.
     * @param unit any unit.
     * @return <code>unit.times(1e24)</code>.
     */
    public static <Q extends Quantity<Q>> PhysicsUnit<Q> YOTTA(PhysicsUnit<Q> unit) {
        return unit.transform(E24);
    }

    /**
     * Returns the specified unit multiplied by the factor
     * <code>10<sup>21</sup></code>
     *
     * @param <Q> The type of the quantity measured by the unit.
     * @param unit any unit.
     * @return <code>unit.times(1e21)</code>.
     */
    public static <Q extends Quantity<Q>> PhysicsUnit<Q> ZETTA(PhysicsUnit<Q> unit) {
        return unit.transform(E21);
    }

    /**
     * Returns the specified unit multiplied by the factor
     * <code>10<sup>18</sup></code>
     *
     * @param <Q> The type of the quantity measured by the unit.
     * @param unit any unit.
     * @return <code>unit.times(1e18)</code>.
     */
    public static <Q extends Quantity<Q>> PhysicsUnit<Q> EXA(PhysicsUnit<Q> unit) {
        return unit.transform(E18);
    }

    /**
     * Returns the specified unit multiplied by the factor
     * <code>10<sup>15</sup></code>
     *
     * @param <Q> The type of the quantity measured by the unit.
     * @param unit any unit.
     * @return <code>unit.times(1e15)</code>.
     */
    public static <Q extends Quantity<Q>> PhysicsUnit<Q> PETA(PhysicsUnit<Q> unit) {
        return unit.transform(E15);
    }

    /**
     * Returns the specified unit multiplied by the factor
     * <code>10<sup>12</sup></code>
     *
     * @param <Q> The type of the quantity measured by the unit.
     * @param unit any unit.
     * @return <code>unit.times(1e12)</code>.
     */
    public static <Q extends Quantity<Q>> PhysicsUnit<Q> TERA(PhysicsUnit<Q> unit) {
        return unit.transform(E12);
    }

    /**
     * Returns the specified unit multiplied by the factor
     * <code>10<sup>9</sup></code>
     *
     * @param <Q> The type of the quantity measured by the unit.
     * @param unit any unit.
     * @return <code>unit.times(1e9)</code>.
     */
    public static <Q extends Quantity<Q>> PhysicsUnit<Q> GIGA(PhysicsUnit<Q> unit) {
        return unit.transform(E9);
    }

    /**
     * Returns the specified unit multiplied by the factor
     * <code>10<sup>6</sup></code>
     *
     * @param <Q> The type of the quantity measured by the unit.
     * @param unit any unit.
     * @return <code>unit.times(1e6)</code>.
     */
    public static <Q extends Quantity<Q>> PhysicsUnit<Q> MEGA(PhysicsUnit<Q> unit) {
        return unit.transform(E6);
    }

    /**
     * Returns the specified unit multiplied by the factor
     * <code>10<sup>3</sup></code>
     *
     * @param <Q> The type of the quantity measured by the unit.
     * @param unit any unit.
     * @return <code>unit.times(1e3)</code>.
     */
    public static <Q extends Quantity<Q>> PhysicsUnit<Q> KILO(PhysicsUnit<Q> unit) {
        return unit.transform(E3);
    }

    /**
     * Returns the specified unit multiplied by the factor
     * <code>10<sup>2</sup></code>
     *
     * @param <Q> The type of the quantity measured by the unit.
     * @param unit any unit.
     * @return <code>unit.times(1e2)</code>.
     */
    public static <Q extends Quantity<Q>> PhysicsUnit<Q> HECTO(PhysicsUnit<Q> unit) {
        return unit.transform(E2);
    }

    /**
     * Returns the specified unit multiplied by the factor
     * <code>10<sup>1</sup></code>
     *
     * @param <Q> The type of the quantity measured by the unit.
     * @param unit any unit.
     * @return <code>unit.times(1e1)</code>.
     */
    public static <Q extends Quantity<Q>> PhysicsUnit<Q> DEKA(PhysicsUnit<Q> unit) {
        return unit.transform(E1);
    }

    /**
     * Returns the specified unit multiplied by the factor
     * <code>10<sup>-1</sup></code>
     *
     * @param <Q> The type of the quantity measured by the unit.
     * @param unit any unit.
     * @return <code>unit.times(1e-1)</code>.
     */
    public static <Q extends Quantity<Q>> PhysicsUnit<Q> DECI(PhysicsUnit<Q> unit) {
        return unit.transform(Em1);
    }

    /**
     * Returns the specified unit multiplied by the factor
     * <code>10<sup>-2</sup></code>
     *
     * @param <Q> The type of the quantity measured by the unit.
     * @param unit any unit.
     * @return <code>unit.times(1e-2)</code>.
     */
    public static <Q extends Quantity<Q>> PhysicsUnit<Q> CENTI(PhysicsUnit<Q> unit) {
        return unit.transform(Em2);
    }

    /**
     * Returns the specified unit multiplied by the factor
     * <code>10<sup>-3</sup></code>
     *
     * @param <Q> The type of the quantity measured by the unit.
     * @param unit any unit.
     * @return <code>unit.times(1e-3)</code>.
     */
    public static <Q extends Quantity<Q>> PhysicsUnit<Q> MILLI(PhysicsUnit<Q> unit) {
        return unit.transform(Em3);
    }

    /**
     * Returns the specified unit multiplied by the factor
     * <code>10<sup>-6</sup></code>
     *
     * @param <Q> The type of the quantity measured by the unit.
     * @param unit any unit.
     * @return <code>unit.times(1e-6)</code>.
     */
    public static <Q extends Quantity<Q>> PhysicsUnit<Q> MICRO(PhysicsUnit<Q> unit) {
        return unit.transform(Em6);
    }

    /**
     * Returns the specified unit multiplied by the factor
     * <code>10<sup>-9</sup></code>
     *
     * @param <Q> The type of the quantity measured by the unit.
     * @param unit any unit.
     * @return <code>unit.times(1e-9)</code>.
     */
    public static <Q extends Quantity<Q>> PhysicsUnit<Q> NANO(PhysicsUnit<Q> unit) {
        return unit.transform(Em9);
    }

    /**
     * Returns the specified unit multiplied by the factor
     * <code>10<sup>-12</sup></code>
     *
     * @param <Q> The type of the quantity measured by the unit.
     * @param unit any unit.
     * @return <code>unit.times(1e-12)</code>.
     */
    public static <Q extends Quantity<Q>> PhysicsUnit<Q> PICO(PhysicsUnit<Q> unit) {
        return unit.transform(Em12);
    }

    /**
     * Returns the specified unit multiplied by the factor
     * <code>10<sup>-15</sup></code>
     *
     * @param <Q> The type of the quantity measured by the unit.
     * @param unit any unit.
     * @return <code>unit.times(1e-15)</code>.
     */
    public static <Q extends Quantity<Q>> PhysicsUnit<Q> FEMTO(PhysicsUnit<Q> unit) {
        return unit.transform(Em15);
    }

    /**
     * Returns the specified unit multiplied by the factor
     * <code>10<sup>-18</sup></code>
     *
     * @param <Q> The type of the quantity measured by the unit.
     * @param unit any unit.
     * @return <code>unit.times(1e-18)</code>.
     */
    public static <Q extends Quantity<Q>> PhysicsUnit<Q> ATTO(PhysicsUnit<Q> unit) {
        return unit.transform(Em18);
    }

    /**
     * Returns the specified unit multiplied by the factor
     * <code>10<sup>-21</sup></code>
     *
     * @param <Q> The type of the quantity measured by the unit.
     * @param unit any unit.
     * @return <code>unit.times(1e-21)</code>.
     */
    public static <Q extends Quantity<Q>> PhysicsUnit<Q> ZEPTO(PhysicsUnit<Q> unit) {
        return unit.transform(Em21);
    }

    /**
     * Returns the specified unit multiplied by the factor
     * <code>10<sup>-24</sup></code>
     *
     * @param <Q> The type of the quantity measured by the unit.
     * @param unit any unit.
     * @return <code>unit.times(1e-24)</code>.
     */
    public static <Q extends Quantity<Q>> PhysicsUnit<Q> YOCTO(PhysicsUnit<Q> unit) {
        return unit.transform(Em24);
    }

    // Holds prefix converters (optimization).
    static final RationalConverter E24 = new RationalConverter(
            BigInteger.TEN.pow(24), BigInteger.ONE);

    static final RationalConverter E21 = new RationalConverter(
            BigInteger.TEN.pow(21), BigInteger.ONE);

    static final RationalConverter E18 = new RationalConverter(
            BigInteger.TEN.pow(18), BigInteger.ONE);

    static final RationalConverter E15 = new RationalConverter(
            BigInteger.TEN.pow(15), BigInteger.ONE);

    static final RationalConverter E12 = new RationalConverter(
            BigInteger.TEN.pow(12), BigInteger.ONE);

    static final RationalConverter E9 = new RationalConverter(
            BigInteger.TEN.pow(9), BigInteger.ONE);

    static final RationalConverter E6 = new RationalConverter(
            BigInteger.TEN.pow(6), BigInteger.ONE);

    static final RationalConverter E3 = new RationalConverter(
            BigInteger.TEN.pow(3), BigInteger.ONE);

    static final RationalConverter E2 = new RationalConverter(
            BigInteger.TEN.pow(2), BigInteger.ONE);

    static final RationalConverter E1 = new RationalConverter(
            BigInteger.TEN.pow(1), BigInteger.ONE);

    static final RationalConverter Em1 = new RationalConverter(
            BigInteger.ONE, BigInteger.TEN.pow(1));

    static final RationalConverter Em2 = new RationalConverter(
            BigInteger.ONE, BigInteger.TEN.pow(2));

    static final RationalConverter Em3 = new RationalConverter(
            BigInteger.ONE, BigInteger.TEN.pow(3));

    static final RationalConverter Em6 = new RationalConverter(
            BigInteger.ONE, BigInteger.TEN.pow(6));

    static final RationalConverter Em9 = new RationalConverter(
            BigInteger.ONE, BigInteger.TEN.pow(9));

    static final RationalConverter Em12 = new RationalConverter(
            BigInteger.ONE, BigInteger.TEN.pow(12));

    static final RationalConverter Em15 = new RationalConverter(
            BigInteger.ONE, BigInteger.TEN.pow(15));

    static final RationalConverter Em18 = new RationalConverter(
            BigInteger.ONE, BigInteger.TEN.pow(18));

    static final RationalConverter Em21 = new RationalConverter(
            BigInteger.ONE, BigInteger.TEN.pow(21));

    static final RationalConverter Em24 = new RationalConverter(
            BigInteger.ONE, BigInteger.TEN.pow(24));
}
