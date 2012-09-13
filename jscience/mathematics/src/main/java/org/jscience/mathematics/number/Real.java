/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2006 - JScience (http://jscience.org/)
 * All rights reserved.
 * 
 * Permission to use, copy, modify, and distribute this software is
 * freely granted, provided that this notice is preserved.
 */
package org.jscience.mathematics.number;

import java.io.IOException;
import java.math.BigDecimal;
import javolution.lang.MathLib;
import javolution.context.LocalContext;
import javolution.context.ObjectFactory;
import javolution.text.CharSet;
import javolution.text.Cursor;
import javolution.text.TextFormat;

/**
 * <p> This class represents a real number of arbitrary precision with 
 *     known/guaranteed uncertainty. A real number consists of a 
 *     {@link #getSignificand significand}, a maximum {@link #getError error} 
 *     (on the significand value) and a decimal {@link #getExponent exponent}: 
 *     (<code>(significand ± error) · 10<sup>exponent</sup></code>).</p>
 *     
 * <p> Reals number can be {@link #isExact exact} (e.g. integer values 
 *     scaled by a power of ten). Exactness is maintained for
 *     {@link org.jscience.mathematics.structure.Ring Ring} operations
 *     (e.g. addition, multiplication), but typically lost when  
 *     multiplicative {@link #inverse() inverses} are calculated. The minimum 
 *     precision (in digits) guaranteed when operating on exact numbers is
 *     given by the current {@link #getExactness exactness} value.
 *     [code]
 *           Real two = Real.valueOf(2); // Exact number.
 *           Real three = Real.valueOf(3); // Exact number.
 *           LocalContext.enter(); // Enter a local context to avoid impacting others threads.
 *           try {
 *                Real.setExactness(30); // Assumed exactness of at least 30 digits on exact numbers (e.g three = 3.00000000000000000000000000000)
 *                Real twoThird = two.divide(three); // The result is inexact.
 *                System.out.println(twoThird);
 *           } finally {
 *                LocalContext.exit(); // Reverts to previous settings.
 *           }
 *
 *           > (0.6666666666666666666666666666667 ± 1E-30)
 *     [/code]</p>
 * 
 * <p> The{@link #getPrecision precision} and/or {@link #getAccuracy
 *     accuracy} of any real number is available and <b>guaranteed</b> 
 *     (the true/exact value is always within the precision/accuracy range).</p>
 * 
 * <p> Operations on instances of this class are quite fast   
 *     as information substantially below the precision level (aka noise)
 *     is not processed/stored. There is no limit on a real precision
 *     but precision degenerates (due to numeric errors) and calculations 
 *     accelerate as more and more operations are performed.</p>
 *
 * <p> Instances of this class can be utilized to qualify number values 
 *     (e.g. {@link org.jscience.physics.amount.Constants physical constants})
 *     or to find approximate solutions to linear equations using the 
 *     {@link org.jscience.mathematics.vector.Matrix Matrix} class or.
 *     Then, the "qualified" result is indicative of possible system
 *     singularities.</p>
 *  
 * @author <a href="mailto:jean-marie@dautelle.com">Jean-Marie Dautelle</a>
 * @version 5.0, January 8, 2006
 * @see <a href="http://en.wikipedia.org/wiki/Real_number">
 *      Wikipedia: Real number</a>
 */
public final class Real extends FieldNumber<Real> {

    /**
     * Holds the default text format for real numbers. Digits below the error
     * level are not shown. For example:
     * [code]
     *    Real.valueOf(123, -1, 0); // "12.3" (exact)
     *    Real.valueOf(123456, 200, -4"); // "(12.35 ± 0.02)"
     * [/code]
     * 
     * @see TextFormat#getDefault
     */
    protected static final TextFormat<Real> TEXT_FORMAT = new TextFormat<Real>(Real.class) {

        @Override
        public Appendable format(Real real, Appendable out) throws IOException {
            if (real == Real.NaN)
                return out.append("NaN");
            Decimal value = Decimal.valueOf(real.getSignificand(), real.getExponent());
            if (real.isExact()) { // Same format as decimal.
                return Decimal.TEXT_FORMAT.format(value, out);
            }
            out.append("(");
            Decimal.TEXT_FORMAT.format(value, out);
            out.append(" ± ");
            Decimal error = Decimal.valueOf(real.getError(), real.getExponent());
            Decimal.TEXT_FORMAT.format(error, out);
            return out.append(")");
        }

        @Override
        public Real parse(CharSequence csq, Cursor cursor) throws IllegalArgumentException {
            if (cursor.skip("NaN", csq))
                return Real.NaN;
            if (cursor.skip('-', csq))
                return parse(csq, cursor).opposite();
            boolean isExact = !cursor.skip('(', csq);
            Decimal value = Decimal.TEXT_FORMAT.parse(csq, cursor);
            LargeInteger significand = value.getSignificand();
            int exponent = value.getExponent();
            if (isExact)
                return Real.valueOf(significand, exponent);
            cursor.skipAny(CharSet.WHITESPACES, csq);
            if (!cursor.skip('±', csq))
                throw new IllegalArgumentException("'±' expected");
            cursor.skipAny(CharSet.WHITESPACES, csq);
            Decimal error = Decimal.TEXT_FORMAT.parse(csq, cursor);
            if (error.isNegative())
                throw new IllegalArgumentException("Error cannot be negative");
            LargeInteger errorSignificand = error.getSignificand();
            int errorExponent = error.getExponent();
            // We have to ensure that value and error have the same exponent.
            if (exponent > errorExponent) {
                significand = significand.E(exponent - errorExponent);
                exponent = errorExponent;
            } else if (exponent < errorExponent) {
                errorSignificand = errorSignificand.E(errorExponent - exponent);
            }
            if (!isExact) {
                cursor.skipAny(CharSet.WHITESPACES, csq);
                if (!cursor.skip(')', csq))
                    throw new IllegalArgumentException("')' expected");
            }
            return Real.rangeOf(significand.minus(errorSignificand), significand.plus(errorSignificand), exponent);
        }
    };

    /**
     * Holds the factory constructing real instances.
     */
    private static final ObjectFactory<Real> FACTORY = new ObjectFactory<Real>() {

        protected Real create() {
            return new Real();
        }
    };

    /** 
     * Holds a Not-a-Number instance (infinite error). 
     */
    public static final Real NaN = new Real(LargeInteger.ZERO, 1, Integer.MAX_VALUE);

    /** 
     * Holds the exact ZERO instance. 
     */
    public static final Real ZERO = new Real(LargeInteger.ZERO, 0, 0);

    /** 
     * Holds the exact ONE instance. 
     */
    public static final Real ONE = new Real(LargeInteger.ONE, 0, 0);

    /**
     * Holds the number of digits used for error calculation (default 4).
     */
    private static final LocalContext.Reference<Integer> MAXIMUM_DIGITS_FOR_ERROR = new LocalContext.Reference<Integer>(4);

    /**
     * Holds the number of digits used when operating on exact digits (default 18).
     */
    private static final LocalContext.Reference<Integer> EXACTNESS = new LocalContext.Reference<Integer>(18);

    /**
     * The significand value.
     */
    private LargeInteger _significand;

    /**
     * The significand error (0 for exact number).
     */
    private int _error;

    /**
     * The decimal exponent.
     */
    private int _exponent;

    /**
     * Default constructor.
     */
    private Real() {
    }

    /**
     * Creates an exact real number always on the heap independently from the
     * current {@link javolution.context.AllocatorContext allocator context}.
     * To allow for custom object allocation policies, static factory methods
     * <code>valueOf(...)</code> are recommended.
     *
     *
     * @param significand this real significand.
     * @param exponent the decimal exponent.
     */
    public Real(LargeInteger significand, int exponent) {
        _significand = significand;
        _exponent = exponent;
        _error = 0;
    }

    /**
     * Creates a real number always on the heap independently from the
     * current {@link javolution.context.AllocatorContext allocator context}.
     * To allow for custom object allocation policies, static factory methods
     * <code>valueOf(...)</code> are recommended.
     *
     *
     * @param significand this real significand.
     * @param exponent the decimal exponent.
     * @param error the error on the significand or <code>0</code> if exact.
     * @throws IllegalArgumentException if <code>error &lt; 0</code>
     */
    public Real(LargeInteger significand, int exponent, int error) {
        if (error < 0)
            throw new IllegalArgumentException("Error cannot be negative");
        _significand = significand;
        _error = error;
        _exponent = exponent;
    }

    /**
     * Convenience method equivalent to
     * {@link #Real(org.jscience.mathematics.number.LargeInteger, int)
     *  Real(new LargeInteger(significand), exponent)}.
     *
     * @param significand the significand.
     * @param exponent the power of ten exponent.
     */
    public Real(long significand, int exponent) {
        this(new LargeInteger(significand), exponent);
    }

    /**
     * Convenience method equivalent to
     * {@link #Real(org.jscience.mathematics.number.LargeInteger, int, int)
     *  Real(new LargeInteger(significand),exponent, error)}.
     *
     * @param significand the significand.
     * @param exponent the power of ten exponent.
     * @param error the error on the significand or <code>0</code> if exact.
     * @throws IllegalArgumentException if <code>error &lt; 0</code>
     */
    public Real(long significand, int exponent, int error) {
        this(new LargeInteger(significand), exponent, error);
    }

    /**
     * Returns an exact real having the specified significand and exponent.
     *
     * @param significand this real significand.
     * @param exponent the decimal exponent.
     * @return <code>Real.valueOf(significand, exponent, 0)</code>
     */
    public static Real valueOf(LargeInteger significand, int exponent) {
        return Real.valueOf(significand, exponent, 0);
    }

    /**
     * Returns a real having the specified significand, exponent and
     * error. If the error is <code>0</code>, the real is assumed exact.
     * [code]
     *      // x = -12.3 exact
     *     Real x = Real.valueOf(-123, -1, 0);
     *
     *     // y = (0.0 ± 0.01)
     *     Real y = Real.valueOf(0, -2, 1);
     * [/code]
     * 
     * @param significand this real significand.
     * @param exponent the decimal exponent.
     * @param error the error on the significand or <code>0</code> if exact.
     * @return <code>(significand ± error)·10<sup>exponent</sup>)</code>
     * @throws IllegalArgumentException if <code>error < 0</code>
     */
    public static Real valueOf(LargeInteger significand, int exponent,
            int error) {
        if (error < 0)
            throw new IllegalArgumentException("Error cannot be negative");
        Real real = FACTORY.object();
        real._significand = significand;
        real._error = error;
        real._exponent = exponent;
        return real;
    }

    /**
     * Convenience method equivalent to
     * {@link #valueOf(org.jscience.mathematics.number.LargeInteger, int)
     * Real.valueOf(LargeInteger.valueOf(significand), exponent)}.
     *
     * @param significand this real significand.
     * @param exponent the decimal exponent.
     * @return <code>(significand)·10<sup>exponent</sup>)</code>
     */
    public static Real valueOf(long significand, int exponent) {
        return Real.valueOf(LargeInteger.valueOf(significand), exponent);
    }

    /**
     * Convenience method equivalent to
     * {@link #valueOf(org.jscience.mathematics.number.LargeInteger, int, int)
     * Real.valueOf(LargeInteger.valueOf(significand), exponent, error)}.
     *
     * @param significand this real significand.
     * @param exponent the decimal exponent.
     * @param error the maximum error on the significand.
     * @return <code>(significand ± error)·10<sup>exponent</sup>)</code>
     * @throws IllegalArgumentException if <code>error < 0</code>
     */
    public static Real valueOf(long significand, int exponent, int error) {
        return Real.valueOf(LargeInteger.valueOf(significand), exponent, error);
    }

    /**
     * Returns the real number for the specified character sequence.
     *
     * @param  csq the character sequence.
     * @return <code>TEXT_FORMAT.parse(csq)</code>.
     * @throws IllegalArgumentException if the character sequence does not
     *         contain a parsable number.
     * @see #TEXT_FORMAT
     */
    public static Real valueOf(CharSequence csq) {
        return TEXT_FORMAT.parse(csq);
    }

    /**
     * Returns the real number in the specified range.
     *
     * @param min the minimum significand value.
     * @param max the minimum significand value.
     * @param exponent the power of ten exponent multiplier.
     * @return the corresponding real number.
     */
    public static Real rangeOf(LargeInteger min, LargeInteger max, int exponent) {
        LargeInteger significand = min.plus(max).times2pow(-1);
        LargeInteger error = max.minus(significand);

        int maxErrorDigits = Real.getMaximumDigitsForError();
        int errorDigits = error.digitLength();
        if (errorDigits <= maxErrorDigits)
            return Real.valueOf(significand, exponent, error.intValue());
        int shift = errorDigits - maxErrorDigits;
        return Real.valueOf(significand.E(-shift), exponent + shift, error.E(-shift).intValue());
    }

    /**
     * Convenience method equivalent to
     * {@link #rangeOf(org.jscience.mathematics.number.LargeInteger, org.jscience.mathematics.number.LargeInteger, int)
     * Real.rangeOf(LargeInteger.valueOf(min), LargeInteger.valueOf(max), exponent)}.
     *
     * @param min the minimum significand value.
     * @param max the minimum significand value.
     * @param exponent the power of ten exponent multiplier.
     * @return the corresponding real number.
     */
    public static Real rangeOf(long min, long max, int exponent) {
        return Real.rangeOf(LargeInteger.valueOf(min), LargeInteger.valueOf(max), exponent);
    }

    /**
     * Returns the number of digits used for {@link #getError() error}
     * calculations (default <code>4</code>). Returns the
     * {@link javolution.context.LocalContext local} setting.
     * If a real number has an error too large then
     * the significand value is scaled to maintain the error in range.
     *
     * @return the maximum number of digits for error value.
     */
    public static int getMaximumDigitsForError() {
        return MAXIMUM_DIGITS_FOR_ERROR.get();
    }

    /**
     * Sets the maximum number of digits used  for {@link #getError() error}
     * calculations ({@link javolution.context.LocalContext local} setting).
     *
     * @param digits the number of digits for error value.
     * @throws IllegalArgumentException if
     *       <code>(digits &gt; 10) || (digits &lt;= 0)</code>
     */
    public static void setMaximumDigitsForError(int digits) {
        if ((digits <= 0) || (digits > 10))
            throw new IllegalArgumentException("digits: " + digits);
        MAXIMUM_DIGITS_FOR_ERROR.set(digits);
    }

    /**
     * Returns the exactness (in digits) of real number for which the error 
     * is zero (default <code>18</code> digits,
     * {@link javolution.context.LocalContext local} setting).
     *
     * @return the minimum number of digits considered exact when
     *         operating on exact number.
     */
    public static int getExactness() {
        return EXACTNESS.get();
    }

    /**
     * Sets the exactness (in digits) of real number for which the error
     * is zero {@link javolution.context.LocalContext local} setting).
     *
     * @return digits the minimum number of digits considered exact when
     *         operating on exact number.
     */
    public static void setExactness(int digits) {
        EXACTNESS.set(digits);
    }

    /**
     * Returns this real <a href="http://en.wikipedia.org/wiki/Significand">
     * significand</a> value.
     * 
     * @return the significand.
     */
    public LargeInteger getSignificand() {
        return _significand;
    }

    /**
     * Returns the maximum error (positive) on this real significand.
     * 
     * @return the maximum error on the significand.
     */
    public int getError() {
        return _error;
    }

    /**
     * Returns the power of ten exponent of this real number.
     * 
     * @return the exponent.
     */
    public int getExponent() {
        return _exponent;
    }

    /**
     * Indicates if this real number is exact
     * (<code>{@link #getError() error} == 0</code>).
     *
     * @return <code>getError() == 0</code>
     */
    public boolean isExact() {
        return _error == 0;
    }

    /**
     * Returns the number of decimal digits guaranteed exact which appear to
     * the right of the decimal point (absolute error).
     *
     * @return a measure of the absolute error of this real number.
     */
    public int getAccuracy() {
        if (isExact())
            return Integer.MAX_VALUE;
        if (this == NaN)
            return Integer.MIN_VALUE;
        return -_exponent - MathLib.digitLength(_error);
    }

    /**
     * Returns the total number of decimal digits guaranteed exact
     * (relative error).
     *
     * @return a measure of the relative error of this real number.
     */
    public final int getPrecision() {
        if (isExact())
            return Integer.MAX_VALUE;
        if (this == NaN)
            return Integer.MIN_VALUE;
        return _significand.digitLength() - MathLib.digitLength(_error);
    }

    /**
     * Indicates if this real is greater than zero.
     * 
     * @return <code>this &gt; 0</code>
     */
    public boolean isPositive() {
        return _significand.isPositive();
    }

    /**
     * Indicates if this real is less than zero.
     * 
     * @return <code>this &lt; 0</code>
     */
    public boolean isNegative() {
        return _significand.isNegative();
    }

    /**
     * Indicates if this real is Not-a-Number (unbounded value interval).
     * 
     * @return <code>true</code> if this number has unbounded value interval;
     *         <code>false</code> otherwise.
     */
    public boolean isNaN() {
        return this == NaN;
    }

    /**
     * Indicates if this real approximates the one specified. 
     * This method takes into account possible errors (e.g. numeric
     * errors) to make this determination.
     *  
     * <p>Note: This method returns <code>true</code> if <code>this</code> or 
     *          <code>that</code> {@link #isNaN} (basically Not-A-Number 
     *          approximates anything).</p>
     *
     * @param  that the real to compare with.
     * @return <code>this &asymp; that</code>
     */
    public boolean approximates(Real that) {
        Real diff = this.minus(that);
        if (diff == NaN)
            return false;
        return diff._significand.abs().compareTo(diff._error) <= 0;
    }

    /**
     * Returns the closest integer value to this real number.
     * 
     * @return this real rounded to the nearest integer.
     * @throws ArithmeticException if <code>this.isNaN()</code>
     */
    public LargeInteger round() {
        if (this == NaN)
            throw new ArithmeticException("Cannot convert NaN to integer value");
        LargeInteger half = LargeInteger.valueOf(5).E(-1 - _exponent);
        return isNegative() ? _significand.minus(half).times10pow(_exponent) : _significand.plus(half).times10pow(_exponent);
    }

    /**
     * Returns the minimum value this real can take (always an exact number).
     *
     * @return the minimum value.
     */
    public Real minimum() {
        return Real.valueOf(_significand.minus(_error), _exponent, 0);
    }

    /**
     * Returns the maximum value this real can take (always an exact number).
     *
     * @return the maximum value.
     */
    public Real maximum() {
        return Real.valueOf(_significand.plus(_error), _exponent, 0);
    }

    /**
     * Returns the square root of this real number, the more accurate is this
     * real number, the more accurate the square root.
     *
     * @return the positive square root of this real number.
     */
    public Real sqrt() {
        if (this == NaN)
            return NaN;
        if (this.equals(ZERO))
            return ZERO;
        if (this.equals(ONE))
            return ONE;
        if (this.isExact()) // Converts to inexact.
            return toInexact().sqrt();
        LargeInteger thisMin = this._significand.minus(this._error);
        LargeInteger thisMax = this._significand.plus(this._error);
        if (thisMin.isNegative())
            return NaN;
        // We scale appropriately
        int scale = _significand.digitLength();
        int exponent = _exponent - scale;
        if ((exponent & 1) == 1) { // Odd exponent.
            exponent--;
            scale++;
        }
        thisMin = thisMin.times10pow(scale);
        thisMax = thisMax.times10pow(scale);
        LargeInteger minSqrt = thisMin.sqrt();
        LargeInteger maxSqrt = thisMax.sqrt();
        return Real.rangeOf(minSqrt, maxSqrt, exponent >> 1);
    }

    // Implements GroupAdditive.
    public Real opposite() {
        if (this == NaN)
            return NaN;
        return Real.valueOf(_significand.opposite(), _exponent, _error);
    }

    // Implements GroupAdditive.
    public Real plus(Real that) {
        if ((this == NaN) || (that == NaN))
            return NaN;
        // Ensures the real with the smallest exponent is 'this'.
        // The others has its significand scaled to reduce its exponent to match.
        if (this._exponent > that._exponent)
            return that.plus(this);
        int shift = that._exponent - this._exponent; // >= 0
        LargeInteger thatSignificand = that._significand.E(shift);
        LargeInteger thatError = LargeInteger.valueOf(that._error).E(shift);

        LargeInteger min = this._significand.minus(this._error).plus(
                thatSignificand.minus(thatError));
        LargeInteger max = this._significand.plus(this._error).plus(
                thatSignificand.plus(thatError));
        return Real.rangeOf(min, max, this._exponent);
    }

    @Override
    public Real times(long multiplier) {
        return this.times(Real.valueOf(multiplier, 0, 0)); // Exact multiplier.
    }

    // Implements GroupMultiplicative
    public Real times(Real that) {
        if ((this == NaN) || (that == NaN))
            return NaN;
        long exp = ((long) this._exponent) + that._exponent;
        if (exp > Integer.MAX_VALUE || (exp < Integer.MIN_VALUE))
            return NaN; // Exponent overflow.
        LargeInteger thisMin = this._significand.minus(this._error);
        LargeInteger thisMax = this._significand.plus(this._error);
        LargeInteger thatMin = that._significand.minus(that._error);
        LargeInteger thatMax = that._significand.plus(that._error);
        LargeInteger min, max;
        if (thisMin.compareTo(thisMax.opposite()) > 0) {
            if (thatMin.compareTo(thatMax.opposite()) > 0) {
                min = thisMin.times(thatMin);
                max = thisMax.times(thatMax);
            } else {
                min = thisMax.times(thatMin);
                max = thisMin.times(thatMax);
            }
        } else {
            if (thatMin.compareTo(thatMax.opposite()) > 0) {
                min = thisMin.times(thatMax);
                max = thisMax.times(thatMin);
            } else {
                min = thisMax.times(thatMax);
                max = thisMin.times(thatMin);
            }
        }
        return Real.rangeOf(min, max, (int) exp);
    }

    @Override
    public Real divide(long divisor) {
        return this.divide(Real.valueOf(divisor, 0, 0)); // Exact divisor.
    }

    // Implements GroupMultiplicative
    public Real inverse() {
        if ((this == NaN) || (this == ZERO))
            return NaN;
        if (this.isExact())
            return this.toInexact().inverse();
        LargeInteger thisMin = this._significand.minus(this._error);
        LargeInteger thisMax = this._significand.plus(this._error);
        if (thisMin.isNegative() && thisMax.isPositive()) // Encompasses 0
            return NaN;
        int digits = MathLib.max(thisMin.digitLength(), thisMax.digitLength());
        long exp = ((long) -this._exponent) - digits - digits;
        if ((exp > Integer.MAX_VALUE || (exp < Integer.MIN_VALUE)))
            return NaN; // Exponent overflow.
        LargeInteger min = div(2 * digits, thisMax);
        LargeInteger max = div(2 * digits, thisMin).plus(1);
        return Real.rangeOf(min, max, (int) exp);
    }

    private static LargeInteger div(int exp, LargeInteger significand) {
        int expBitLength = (int) (exp * DIGITS_TO_BITS);
        int precision = expBitLength - significand.bitLength() + 1;
        LargeInteger reciprocal = significand.inverseScaled(precision);
        LargeInteger result = reciprocal.times10pow(exp);
        return result.shiftRight(expBitLength + 1);
    }
    private static final double DIGITS_TO_BITS = MathLib.LOG10 / MathLib.LOG2;

    // Implements abstract class Number.
    public Real abs() {
        return _significand.isNegative() ? this.opposite() : this;
    }

    // Implements abstract class Number.
    public long longValue() {
        return (long) doubleValue();
    }

    // Implements abstract class Number.
    public double doubleValue() {
        if (this == NaN)
            return Double.NaN;
        if (this == ZERO)
            return 0.0;
        // Shift the significand to a >18 digits integer (long compatible).
        int nbrDigits = _significand.digitLength();
        int digitShift = nbrDigits - 18;
        long reducedSignificand = _significand.times10pow(-digitShift).longValue();
        int exponent = _exponent + digitShift;
        return MathLib.toDoublePow10(reducedSignificand, exponent);
    }

    // Implements abstract class Number.
    public BigDecimal decimalValue() {
        return new BigDecimal(_significand.asBigInteger(), -_exponent);
    }

    // Implements abstract class Number.
    public int compareTo(Real that) {
        if (this.isNaN())
            return that.isNaN() ? 0 : 1;
        if (that.isNaN())
            return -1; // NaN is considered greater than !NaN

        // Ensures the real with the smallest exponent is 'this'.
        // The others has its significand scaled to reduce its exponent to match.
        if (this._exponent > that._exponent)
            return -that.compareTo(this);
        int shift = that._exponent - this._exponent; // >= 0
        LargeInteger thatSignificand = that._significand.E(shift);
        LargeInteger thatError = LargeInteger.valueOf(that._error).E(shift);

        // First we compare the significand.
        int status = this._significand.compareTo(thatSignificand);
        if (status != 0)
            return status;  // Distinct significand value.

        // Then if egality of significands we compare the error value.
        // Smallest error first.
        return -thatError.compareTo(this._error);
    }

    @Override
    public Real copy() {
        if (this == NaN)
            return NaN; // Maintains unicity.
        return Real.valueOf(_significand.copy(), _exponent, getError());
    }

    // Returns the Real equivalent to this one. But with an error
    // set from the current exactness.
    private Real toInexact() {
        int digits = _significand.digitLength();
        int scale = Real.getExactness() - digits + 1;
        return Real.valueOf(_significand.times10pow(scale), _exponent - scale, 1);
    }
    private static final long serialVersionUID = 1L;

}
