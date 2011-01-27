/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2007 - JScience (http://jscience.org/)
 * All rights reserved.
 * 
 * Permission to use, copy, modify, and distribute this software is
 * freely granted, provided that this notice is preserved.
 */
package org.jscience.mathematics.number;

import java.io.IOException;
import java.math.BigDecimal;
import javolution.context.LocalContext;
import javolution.context.ObjectFactory;
import javolution.lang.MathLib;
import javolution.text.CharSet;
import javolution.text.Cursor;
import javolution.text.TextBuilder;
import javolution.text.TextFormat;
import javolution.text.TypeFormat;

/**
 * <p> This class represents an integer that is scaled by a power of ten 
 *     scaling factor (decimal fixed point).</p>
 *     
 * <p> Unlike fixed-size implementations, this implementation is based
 *     on {@link LargeInteger} and has no upbound limit (and no overflow).</p>
 *
 * <p> The number of fractional digits by default is <code>18</code>.
 *     This number is adjustable and context-based (can be made local to the
 *     current thread using Javolution context).
 *     [code]
 *         LocalContext.enter();
 *         try {
 *              FixedPoint.setFractionalDigits(30); // 100 digits fractional part.
 *              FixedPoint two = FixedPoint.valueOf(2);
 *              System.out.println(two.sqrt());
 *         } finally {
 *              LocalContext.exit(); // Reverts to previous settings.
 *         }
 *
 *         >   1.41421356237309504880168872420
 *     [/code]</p>
 * 
 * <p> Instances of this class are immutable and can be used to find  
 *     accurate solutions to linear equations with the {@link 
 *     org.jscience.mathematics.vector.Matrix Matrix} class.</p>
 * 
 * @author <a href="mailto:jean-marie@dautelle.com">Jean-Marie Dautelle</a>
 * @version 5.0, November 20, 200(
 * @see <a href="http://en.wikipedia.org/wiki/Fixed-point_arithmetic">
 *      Wikipedia: Fixed-point arithmetic</a>
 */
public final class FixedPoint extends FieldNumber<FixedPoint> {

    /**
     * Holds the default text format for fixed-point numbers (for example
     * "-123.456"). The number of  fractional digits shown is given by
     * {@link #getFractionalDigits()}. When writting a fixed point number,
     * the decimal point always separates the integral from the fractional
     * part (no scientific notation).
     *
     * @see TextFormat#getDefault
     */
    protected static final TextFormat<FixedPoint> TEXT_FORMAT = new TextFormat<FixedPoint>(FixedPoint.class) {

        @Override
        public Appendable format(FixedPoint fp, Appendable out) throws IOException {
            if (fp == FixedPoint.NaN)
                return out.append("NaN");
            if (fp.isNegative()) {
                out.append('-');
            }
            LargeInteger li = fp._significand.abs();
            TextBuilder tmp = TextBuilder.newInstance();
            try {
                LargeInteger.TEXT_FORMAT.format(li, tmp);
                int fractionIndex = tmp.length() + fp._exponent;
                while (fractionIndex <= 0) { // Prepends zeros.
                    tmp.insert(0, "0");
                    fractionIndex++;
                }
                while (fractionIndex >= tmp.length()) { // Appends zeros.
                    tmp.append('0');
                }
                tmp.insert(fractionIndex, "."); // Dot nicely located between the digits.
                return out.append(tmp);
            } finally {
                TextBuilder.recycle(tmp);
            }
        }

        @Override
        public FixedPoint parse(CharSequence csq, Cursor cursor) throws IllegalArgumentException {
            if (cursor.skip("NaN", csq))
                return FixedPoint.NaN;
            if (cursor.skip('-', csq))
                return parse(csq, cursor).opposite();
            LargeInteger significand = LargeInteger.TEXT_FORMAT.parse(csq, cursor);
            LargeInteger fraction = LargeInteger.ZERO;
            int fractionDigits = 0;
            if (cursor.skip('.', csq)) {
                while (cursor.skip('0', csq)) {
                    fractionDigits++;
                }
                fraction = LargeInteger.TEXT_FORMAT.parse(csq, cursor);
                if (!LargeInteger.ZERO.equals(fraction))
                    fractionDigits += fraction.digitLength();
            }
            int exponent = cursor.skip(CharSet.valueOf('E', 'e'), csq) ? TypeFormat.parseInt(csq, 10, cursor) : 0;
            return FixedPoint.valueOf(significand.E(fractionDigits).plus(fraction), exponent - fractionDigits);
        }
    };

    /**
     * Holds the factory constructing floating point instances.
     */
    private static final ObjectFactory<FixedPoint> FACTORY = new ObjectFactory<FixedPoint>() {

        protected FixedPoint create() {
            return new FixedPoint();
        }
    };

    /**
     * The floating point instance representing the additive identity.
     */
    public static final FixedPoint ZERO = new FixedPoint(LargeInteger.ZERO, 0);

    /**
     * The floating point instance representing the multiplicative identity.
     */
    public static final FixedPoint ONE = new FixedPoint(LargeInteger.ONE, 0);

    /** 
     * The Not-a-Number instance (unique). 
     */
    public static final FixedPoint NaN = new FixedPoint(LargeInteger.ZERO, Integer.MAX_VALUE);

    /**
     * Holds the number of fractional digits used during operations.
     */
    private static final LocalContext.Reference<Integer> FRACTIONAL_DIGITS = new LocalContext.Reference<Integer>(18);

    /**
     * Holds the scaled value.
     */
    private LargeInteger _significand;

    /**
     * Holds the power of 10 exponent.
     */
    private int _exponent;

    /**
     * Default constructor. 
     */
    private FixedPoint() {
    }

    /**
     * Creates a fixed point number always on the heap independently from the
     * current {@link javolution.context.AllocatorContext allocator context}.
     * To allow for custom object allocation policies, static factory methods
     * <code>valueOf(...)</code> are recommended.
     * 
     * @param significand the scaled value.
     * @param exponent the power of ten exponent.
     */
    public FixedPoint(LargeInteger significand, int exponent) {
        _significand = significand;
        _exponent = exponent;
    }

    /**
     * Convenience method equivalent to
     * {@link #FixedPoint(org.jscience.mathematics.number.LargeInteger, int)
     * FixedPoint(new LargeInteger(significand), error)}.
     *
     * @param significand the significand.
     * @param exponent the power of ten exponent.
     */
    public FixedPoint(long significand, int exponent) {
        this(new LargeInteger(significand), exponent);
    }

    /**
     * Returns the fixed-point number for the specified scaled value and
     * power of ten exponent.
     * [code]
     *     FixedPoint pi = FixedPoint.valueOf(314, -2); // 3.14
     * [/code]
     *
     * @param significand the scaled value.
     * @param exponent the power of ten exponent.
     * @return the fixed point number <code>(significand · 10<sup>pow10</sup></code>
     */
    public static FixedPoint valueOf(LargeInteger significand, int exponent) {
        FixedPoint fp = FACTORY.object();
        fp._significand = significand;
        fp._exponent = exponent;
        return fp;
    }

    /**
     * Convenience method equivalent to
     * {@link #valueOf(org.jscience.mathematics.number.LargeInteger, int)
     * FixedPoint.valueOf(LargeInteger.valueOf(significand), exponent)
     * 
     * @param significand the scaled value.
     * @param exponent the power of ten exponent.
     * @return the fixed point number <code>(significand · 10<sup>pow10</sup></code>
     */
    public static FixedPoint valueOf(long significand, int exponent) {
        return FixedPoint.valueOf(LargeInteger.valueOf(significand), exponent);
    }

    /**
     * Convenience method equivalent to
     * {@link #valueOf(org.jscience.mathematics.number.LargeInteger, int)
     * FixedPoint.valueOf(value, 0) }
     *
     * @param value the integral value.
     * @return the fixed point number <code>(value)</code>
     */
    public static FixedPoint valueOf(LargeInteger value) {
        return FixedPoint.valueOf(value, 0);
    }

    /**
     * Convenience method equivalent to
     * {@link #valueOf(org.jscience.mathematics.number.LargeInteger, int)
     * FixedPoint.valueOf(LargeInteger.valueOf(value), 0) }
     *
     * @param value the integral value.
     * @return the fixed point number <code>(value)</code>
     */
    public static FixedPoint valueOf(long value) {
        return FixedPoint.valueOf(LargeInteger.valueOf(value), 0);
    }

    /**
     * Returns the fixed point number for the specified character sequence.
     *
     * @param  csq the character sequence.
     * @return <code>TEXT_FORMAT.parse(csq)</code>.
     * @throws IllegalArgumentException if the character sequence does not
     *         contain a parsable number.
     * @see #TEXT_FORMAT
     */
    public static FixedPoint valueOf(CharSequence csq) {
        return TEXT_FORMAT.parse(csq);
    }

    /**
     * Returns the {@link javolution.context.LocalContext local} number of
     * fractional digits used during calculations (default <code>18</code>).
     * 
     * @return the number of fractional digits.
     */
    public static int getFractionalDigits() {
        return FRACTIONAL_DIGITS.get();
    }

    /**
     * Sets the {@link javolution.context.LocalContext local} number of
     * fractional digits used during calculations.
     * 
     * @param fractionalDigits the number of fractional digits.
     */
    public static void setFractionalDigits(int fractionalDigits) {
        FRACTIONAL_DIGITS.set(fractionalDigits);
    }

    /**
     * Returns the <a href="http://en.wikipedia.org/wiki/Significand">
     * significand</a> value.
     *
     * @return the significand.
     */
    public LargeInteger getSignificand() {
        return _significand;
    }

    /**
     * Returns the power of ten exponent.
     *
     * @return the exponent.
     */
    public int getExponent() {
        return _exponent;
    }

    /**
     * Indicates if this fixed point number is equal to zero.
     *
     * @return <code>this == 0</code>
     */
    public boolean isZero() {
        return _significand.isZero() && (this != NaN);
    }

    /**
     * Indicates if this fixed point number is greater than zero.
     *
     * @return <code>this &gt; 0</code>
     */
    public boolean isPositive() {
        return _significand.isPositive();
    }

    /**
     * Indicates if this fixed point number is less than zero.
     *
     * @return <code>this &lt; 0</code>
     */
    public boolean isNegative() {
        return _significand.isNegative();
    }

    /**
     * Indicates if this fixed point is Not-a-Number.
     *
     * @return <code>true</code> if this number has unbounded value;
     *         <code>false</code> otherwise.
     */
    public boolean isNaN() {
        return this == NaN;
    }

    /**
     * Returns the closest fixed-point that is less than or equal to this
     * fixed point and is equal to a mathematical integer.
     *
     * @return  a fixed point that less than or equal to this fixed point
     *          and is equal to a mathematical integer.
     */
    public FixedPoint floor() {
        if (this == NaN)
            return NaN;
        LargeInteger integralPart = _significand.E(_exponent);
        return FixedPoint.valueOf(isNegative() ? integralPart.minus(LargeInteger.ONE) : integralPart);
    }

    /**
     * Returns the closest fixed-point that is greater than or equal to this
     * fixed point and is equal to a mathematical integer.
     *
     * @return  a fixed point that greater than or equal to this fixed point
     *          and is equal to a mathematical integer.
     */
    public FixedPoint ceil() {
        if (this == NaN)
            return NaN;
        LargeInteger integralPart = _significand.E(_exponent);
        return FixedPoint.valueOf(isNegative() ? integralPart : integralPart.plus(LargeInteger.ONE));
    }

    /**
     * Returns the closest integer value to this fixed-point number.
     *
     * @return <code>(LargeInteger) (this + 0.5).floor() </code>
     * @throws ArithmeticException if this fixed point {@link #isNaN()}.
     */
    public LargeInteger round() {
        if (this == NaN)
            throw new ArithmeticException("Cannot convert NaN to integer value");
        FixedPoint fp = this.plus(FixedPoint.valueOf(5, -1)).floor();
        return fp._significand.E(fp._exponent);
    }

    /**
     * Returns the square root of this fixed point number.
     * If this fixed point is negative {@link #NaN} is returned.
     *
     * @return the positive square root of this fixed point number.
     */
    public FixedPoint sqrt() {
        if ((this == NaN) | this.isNegative())
            return NaN;
        int newExponent = -FixedPoint.getFractionalDigits();
        LargeInteger thisScaledValue = rescale(this._significand, this._exponent, newExponent);
        return FixedPoint.valueOf(thisScaledValue.E(-newExponent).sqrt(), newExponent);
    }

    // Implements GroupAdditive.
    public FixedPoint opposite() {
        if (this == NaN)
            return NaN;
        return FixedPoint.valueOf(_significand.opposite(), _exponent);
    }

    // Implements GroupAdditive.
    public FixedPoint plus(FixedPoint that) {
        if ((this == NaN) | (that == NaN))
            return NaN;
        int newExponent = -FixedPoint.getFractionalDigits();
        LargeInteger thisScaledValue = rescale(this._significand, this._exponent, newExponent);
        LargeInteger thatScaledValue = rescale(that._significand, that._exponent, newExponent);
        return FixedPoint.valueOf(thisScaledValue.plus(thatScaledValue), newExponent);
    }

    @Override
    public FixedPoint times(long multiplier) {
        return this.times(FixedPoint.valueOf(multiplier));
    }

    // Implements GroupMultiplicative.
    public FixedPoint times(FixedPoint that) {
        if ((this == NaN) | (that == NaN))
            return NaN;
        int newExponent = -FixedPoint.getFractionalDigits();
        LargeInteger thisScaledValue = rescale(this._significand, this._exponent, newExponent);
        LargeInteger thatScaledValue = rescale(that._significand, that._exponent, newExponent);
        return FixedPoint.valueOf(thisScaledValue.times(thatScaledValue).E(newExponent), newExponent);
    }

    // Implements GroupMultiplicative
    public FixedPoint inverse() {
        if (_significand.isZero())
            return NaN;
        int newExponent = -FixedPoint.getFractionalDigits();
        LargeInteger thisScaledValue = rescale(this._significand, this._exponent, newExponent);
        return FixedPoint.valueOf(LargeInteger.ONE.E(-newExponent << 1).divide(thisScaledValue), newExponent);
    }

    @Override
    public FixedPoint divide(long n) {
        return this.divide(FixedPoint.valueOf(n));
    }

    @Override
    public FixedPoint divide(FixedPoint that) {
        if ((this.isNaN()) | (that._significand.isZero()))
            return NaN;
        int newExponent = -FixedPoint.getFractionalDigits();
        LargeInteger thisScaledValue = rescale(this._significand, this._exponent, newExponent);
        LargeInteger thatScaledValue = rescale(that._significand, that._exponent, newExponent);
        return FixedPoint.valueOf(thisScaledValue.E(-newExponent).divide(thatScaledValue), newExponent);
    }

    // Implements abstract class Number.
    public FixedPoint abs() {
        return this._significand.isNegative() ? this.opposite() : this;
    }

    // Implements abstract class Number.
    public long longValue() {
        if (this == NaN)
            return Long.MAX_VALUE;
        return _significand.E(_exponent).longValue();
    }

    // Implements abstract class Number.
    public double doubleValue() {
        if (this == NaN)
            return Double.NaN;
        if (this._significand.isZero())
            return 0.0;
        // Shift the significand to a 18 digits integer (long compatible).
        int nbrDigits = _significand.digitLength();
        int digitShift = nbrDigits - 18;
        long reducedSignificand = _significand.E(-digitShift).longValue();
        int exponent = _exponent + digitShift;
        return MathLib.toDoublePow10(reducedSignificand, exponent);
    }

    // Implements abstract class Number.
    public BigDecimal decimalValue() {
        return new BigDecimal(_significand.asBigInteger(), -_exponent);
    }

    // Implements abstract class Number.
    public int compareTo(FixedPoint that) {
        if (this.isNaN())
            return that.isNaN() ? 0 : 1;
        if (that.isNaN())
            return -1; // NaN is considered greater than !NaN

        int newExponent = MathLib.min(this._exponent, that._exponent);
        LargeInteger thisScaledValue = rescale(this._significand, this._exponent, newExponent);
        LargeInteger thatScaledValue = rescale(that._significand, that._exponent, newExponent);
        return thisScaledValue.compareTo(thatScaledValue);
    }

    // Implements abstract class Number.
    public FixedPoint copy() {
        if (this == NaN)
            return NaN; // Maintains unicity.
        return FixedPoint.valueOf(_significand, _exponent);
    }

    /** Rescales the specified significand value, round-off when necessary. */
    private static LargeInteger rescale(LargeInteger significand, int exponent, int newExponent) {
        int digitShift = exponent - newExponent;
        if (digitShift == 0)
            return significand;
        if (digitShift > 0)
            return significand.E(digitShift);
        // Else we need to round-off to the closest integer value.
        return significand.isNegative() ? significand.minus(LargeInteger.valueOf(5).E((-digitShift) - 1)).E(digitShift) : significand.plus(LargeInteger.valueOf(5).E((-digitShift) - 1)).E(digitShift);
    }
    private static final long serialVersionUID = 1L;

}
