/*
 * jScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2004 - The jScience Consortium (http://jscience.org/)
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation (http://www.gnu.org/copyleft/lesser.html); either version
 * 2.1 of the License, or any later version.
 */
package org.jscience.mathematics.numbers;


import java.io.IOException;

import javolution.realtime.LocalContext;
import javolution.util.MathLib;
import javolution.lang.Text;
import javolution.lang.TextBuilder;
import javolution.lang.TypeFormat;
import javolution.xml.XmlElement;
import javolution.xml.XmlFormat;

import org.jscience.mathematics.matrices.Matrix;
import org.jscience.mathematics.matrices.Operable;

/**
 * <p> This class represents a real number of arbitrary precision with 
 *     known/guaranteed uncertainty. A real number consists of a 
 *     {@link #getMantissa mantissa}, a maximum {@link #getError error} 
 *     (on the mantissa) and a decimal {@link #getExponent exponent}: 
 *     (<code>(mantissa ± error) · 10<sup>exponent</sup></code>).<p>
 * 
 * <p> The decimal representations of real instances are indicative of
 *     their precision as only exact digits are written out.
 *     For example, the string <code>"2.000"</code> represents a real 
 *     value of <code>(2.0 ± 0.001)</code>. 
 *     The {@link #getPrecision precision} or {@link #getAccuracy accuracy}
 *     of any real number is available and <b>guaranteed</b> 
 *     (the true/exact value is always within the precision/accuracy range).</p>
 * 
 * <p> Operations on instances of this class are quite fast   
 *     as information substantially below the precision level (aka noise)
 *     is not processed/stored. There is no limit on a real precision
 *     but precision degenerates (due to numeric errors) and calculations 
 *     accelerate as more and more operations are performed.</p>
 * 
 * <p> Instances of this class can be utilized to find approximate 
 *     solutions to linear equations using the {@link Matrix} class for which
 *     high-precision reals is often required, the primitive type
 *     <code>double</code> being not accurate enough to resolve equations 
 *     when the matrix's size exceeds 100x100. Furthermore, even for small 
 *     matrices the "qualified" result is indicative of possible system 
 *     singularities.</p>
 *  
 * @author <a href="mailto:jean-marie@dautelle.com">Jean-Marie Dautelle</a>
 * @version 1.0, October 24, 2004
 */
public final class Real extends RealtimeNumber implements Comparable {

    /**
     * Holds the default XML representation for real numbers.
     * This representation consists of a simple <code>value</code> attribute
     * holding its textual representation.
     * 
     * @see #valueOf(CharSequence)
     * @see #toText
     */
    protected static final XmlFormat REAL_XML = new XmlFormat(Real.class) {
        public void format(Object obj, XmlElement xml) {
            xml.setAttribute("value", ((Real) obj).toText());
        }

        public Object parse(XmlElement xml) {
            return Real.valueOf(xml.getAttribute("value"));
        }
    };

    /** 
     * Holds a Not-a-Number instance (infinite error). 
     */
    public static final Real NaN = new Real();

    /**
     * Holds the factory constructing rational instances.
     */
    private static final Factory FACTORY = new Factory() {

        public Object create() {
            return new Real();
        }
    };

    /**
     * The mantissa value.
     */
    private LargeInteger _mantissa = LargeInteger.ZERO;

    /**
     * The mantissa  error or <code>null</code> if NaN
     */
    private LargeInteger _error;

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
     * Returns a real having the specified mantissa, error and exponent values.
     * For example:<pre>
     *     // x = 0.0 ± 0.01  ("0.00" or "0E-2")
     *     Real x = Real.valueOf(LargeInteger.ZERO,
     *                           LargeInteger.ONE, -2);

     *      // y = -12.3 ± 0.005 ("-12300±5E-3") 
     *     Real y = Real.valueOf(LargeInteger.valueOf("-12300"), 
     *                           LargeInteger.valueOf("5"), -3);
     * 
     *      // The real 1.0 with an accuracy of 100 digits. 
     *     Real one = Real.valueOf(LargeInteger.ONE.E(100), 
     *                             LargeInteger.ONE, -100);
     * </pre>
     * 
     * @param mantissa this real mantissa.
     * @param error the maximum error on the mantissa.
     * @param exponent the decimal exponent.
     * @return <code>(mantissa ± error)·10<sup>exponent</sup>)</code>
     * @throws IllegalArgumentException if <code>error <= 0</code>
     */
    public static Real valueOf(LargeInteger mantissa, LargeInteger error,
            int exponent) {
        if (error.isPositive()) {
            Real real = (Real) FACTORY.object();
            real._mantissa = mantissa;
            real._error = error;
            real._exponent = exponent;
            return real.scale();
        } else {
            throw new IllegalArgumentException("error: " + error
                    + " should be greater than zero");
        }
    }

    /**
     * Returns the real for the specified character sequence.
     * If the precision is not specified (using the <code>±</code> symbol), 
     * the number of digits is characteristic of the precision.
     * Example of valid character sequences for <code>1.2 ± 0.001</code>:<ul>
     * <li>"1.200"</li>
     * <li>"1200±1E-3" (no decimal point allowed here)</li></ul>
     * 
     * @param  chars the character sequence.
     * @return the corresponding real number.
     * @throws NumberFormatException if the character sequence does not contain
     *         a parsable real.
     */
    public static Real valueOf(CharSequence chars) throws NumberFormatException {
        if ((chars.length() == 3) && (TypeFormat.indexOf("NaN", chars, 0) == 0)) {
            return NaN;
        }
        int exponentIndex = TypeFormat.indexOf("E", chars, 0);
        if (exponentIndex >= 0) {
            Real r = valueOf(chars.subSequence(0, exponentIndex));
            int exponent = TypeFormat.parseInt(chars.subSequence(
                    exponentIndex + 1, chars.length()));
            r._exponent += exponent;
            return r.scale();
        }
        Real real = (Real) FACTORY.object();
        int errorIndex = TypeFormat.indexOf("±", chars, 0);
        if (errorIndex >= 0) {
            real._mantissa = LargeInteger.valueOf(chars.subSequence(0,
                    errorIndex));
            real._error = LargeInteger.valueOf(chars.subSequence(
                    exponentIndex + 1, chars.length()));
            if (!real._error.isPositive()) {
                throw new NumberFormatException(chars
                        + " not parsable (error must be greater than 0)");
            }
            real._exponent = 0;
            return real.scale();
        }
        int decimalPointIndex = TypeFormat.indexOf(".", chars, 0);
        if (decimalPointIndex >= 0) {
            LargeInteger integer = LargeInteger.valueOf(chars.subSequence(0,
                    decimalPointIndex));
            LargeInteger fraction = LargeInteger.valueOf(chars.subSequence(
                    decimalPointIndex + 1, chars.length()));
            int fractionDigits = chars.length() - decimalPointIndex - 1;
            real._mantissa = integer.E(fractionDigits).add(fraction);
            real._error = LargeInteger.ONE;
            real._exponent = -fractionDigits;
            return real.scale();
        } else {
            real._mantissa = LargeInteger.valueOf(chars);
            real._error = LargeInteger.ONE;
            real._exponent = 0;
            return real.scale();
        }
    }

    /**
     * Converts a <code>double</code> value to a real instance.
     * The error is derived from the inexact representation of
     * <code>double</code> values intrinsic to the 64 bits IEEE 754 format. 
     * 
     * @param doubleValue the <code>double</code> value to convert.
     */
    public static Real valueOf(double doubleValue) {
        long bits = Double.doubleToRawLongBits(doubleValue);
        boolean isNegative = (bits & 0x8000000000000000L) != 0L;
        int exponent = (int) ((bits & 0x7ff0000000000000L) >> 52);
        long mantissa = (bits & 0x000fffffffffffffL) | 0x0010000000000000L;
        if (exponent != 0x7ff) {
            Real unscaledMantissa = isNegative ? valueOf(LargeInteger
                    .valueOf(-mantissa), LargeInteger.ONE, 0) : valueOf(
                    LargeInteger.valueOf(mantissa), LargeInteger.ONE, 0);
            int pow2 = exponent - 1023 - 52;
            // Calculates the scale factor with at least 52 bits accuracy.
            Real scale = valueOf(E18.shiftLeft(MathLib.abs(pow2)),
                    LargeInteger.ONE, -18);
            return (pow2 >= 0)
                    ? unscaledMantissa.multiply(scale).scale()
                    : unscaledMantissa.divide(scale).scale();
        } else {
            return NaN;
        }
    }
    private static final LargeInteger E18 // > 52 bits precision.
    = LargeInteger.ONE.E(18);

    /**
     * Converts an integer value to a real instance. 
     * The error is determined  by the local {@link #getIntegerAccuracy integer 
     * accuracy} setting.
     * 
     * @param  integer the integer value to convert.
     * @return a real number corresponding to the specified integer with an
     *         an accuracy determined by the local integer accuracy setting.
     */
    public static Real valueOf(long integer) {
        int accuracy = ((Number) INTEGER_ACCURACY.getValue()).intValue();
        return valueOf(LargeInteger.valueOf(integer).E(accuracy),
                LargeInteger.ONE, -accuracy);
    }

    /**
     * Converts an integer value to a real instance. 
     * The error is determined  by the local {@link #getIntegerAccuracy integer 
     * accuracy} setting.
     * 
     * @param  integer the integer value to convert.
     * @return a real number corresponding to the specified integer with an
     *         an accuracy determined by the local integer accuracy setting.
     */
    public static Real valueOf(LargeInteger integer) {
        int accuracy = ((Number) INTEGER_ACCURACY.getValue()).intValue();
        return valueOf(integer.E(accuracy), LargeInteger.ONE, -accuracy);
    }

    /**
     * Returns the {@link LocalContext local} number of decimal zeros
     * in the fraction part for reals created from integer values
     * (default <code>18</code> zeros).
     * 
     * @return the accuracy of reals created from integer values.
     * @see   #setIntegerAccuracy
     */
    public static int getIntegerAccuracy() {
        return ((Number)INTEGER_ACCURACY.getValue()).intValue();
    }
    private static final LocalContext.Variable INTEGER_ACCURACY 
        = new LocalContext.Variable(new Integer(18));

    /**
     * Sets the {@link LocalContext local} number of decimal zeros
     * in the fraction part for reals created from integer values.
     * 
     * @param accuracy the accuracy of reals created from integer values.
     */
    public static void setIntegerAccuracy(int accuracy) {
        INTEGER_ACCURACY.setValue(Integer32.valueOf(accuracy));
    }

    /**
     * Returns this real's mantissa.
     * 
     * @return the mantissa.
     */
    public LargeInteger getMantissa() {
        return _mantissa;
    }

    /**
     * Returns the maximum error (positive) on this real's mantissa.
     * This methods returns <code>null</code> if this {@link #isNaN}.
     * 
     * @return the maximum error on the mantissa or <code>null</code> if 
     *         <code>this.isNaN()</code>
     */
    public LargeInteger getError() {
        return _error;
    }

    /**
     * Returns the exponent of the power of 10 multiplier.
     * 
     * @return the decimal exponent.
     */
    public int getExponent() {
        return _exponent;
    }

    /**
     * Returns the number of decimal digits guaranteed exact which appear to
     * the right of the decimal point (absolute error).
     *
     * @return a measure of the absolute error of this real number.
     */
    public int getAccuracy() {
        if (_error != null) {
            int errorDigits = (int) (_error.bitLength() * BITS_TO_DIGITS + 1);
            return -_exponent - errorDigits;
        } else {
            return Integer.MIN_VALUE;
        }
    }

    /**
     * Returns the total number of decimal digits guaranteed exact
     * (relative error).
     *
     * @return a measure of the relative error of this real number.
     */
    public final int getPrecision() {
        if (_error != null) {
            return (int) ((_mantissa.bitLength() - _error.bitLength())
                    * BITS_TO_DIGITS + 1);
        } else {
            return Integer.MIN_VALUE;
        }
    }

    /**
     * Indicates if this real is greater than zero.
     * 
     * @return <code>this > 0</code>
     */
    public boolean isPositive() {
        return _mantissa.isPositive();
    }

    /**
     * Indicates if this real is less than zero.
     * 
     * @return <code>this < 0</code>
     */
    public boolean isNegative() {
        return _mantissa.isNegative();
    }

    /**
     * Indicates if this real is Not-a-Number (unbounded value interval).
     * 
     * @return <code>true</code> if this number has unbounded value interval;
     *         <code>false</code> otherwise.
     */
    public boolean isNaN() {
        return this._error == null;
    }

    /**
     * Indicates if this real is approximately equals to the one 
     * specified. This method takes into account possible errors (e.g. numeric
     * errors) to make this determination.
     *  
     * <p>Note: This method returns <code>false</code> if <code>this</code> or 
     *          <code>that</code> {@link #isNaN}.</p>
     *
     * @param  that the real to compare with.
     * @return <code>this &asymp; that</code>
     */
    public boolean approxEquals(Real that) {
        Real diff = this.subtract(that);
        if (diff._error != null) {
            return diff._error.isLargerThan(diff._mantissa);
        } else { // NaN
            return false;
        }
    }

    /**
     * Converts this real to a {@link LargeInteger} instance. 
     * Any fractional part of this real is discarded.
     * 
     * @return the integer part of this real.
     * @throws ArithmeticException if <code>this.isNaN()</code>
     */
    public LargeInteger toLargeInteger() {
        if (_error != null) {
            return _mantissa.E(_exponent);
        } else { // NaN
            throw new ArithmeticException("Cannot convert NaN to integer value");
        }
    }

    /**
     * Returns the negation of this real number.
     * 
     * @return <code>-this</code>.
     */
    public Real negate() {
        Real real = (Real) FACTORY.object();
        real._mantissa = _mantissa.negate();
        real._exponent = _exponent;
        real._error = _error;
        return real;
    }

    /**
     * Returns the sum of this real number with the one specified.
     * 
     * @param that the real to be added.
     * @return <code>this + that</code>.
     */
    public Real add(Real that) {
        if ((this._error == null) || (that._error == null)) {
            return NaN;
        } else if (this._exponent == that._exponent) {
            Real real = (Real) FACTORY.object();
            real._exponent = _exponent;
            real._mantissa = this._mantissa.add(that._mantissa);
            real._error = this._error.add(that._error).add(LargeInteger.ONE);
            return real.scale();
        } else if (this._exponent > that._exponent) {
            return this.add(that.scale(this._exponent));
        } else {
            return that.add(this.scale(that._exponent));
        }
    }

    /**
     * Returns the difference between this real number and the one
     * specified.
     * 
     * @param that the real to be subtracted.
     * @return <code>this - that</code>.
     */
    public Real subtract(Real that) {
        return this.add(that.negate());
    }

    /**
     * Returns the product of this real number with the one specified.
     * 
     * @param that the real multiplier.
     * @return <code>this * that</code>.
     */
    public Real multiply(Real that) {
        long exp = ((long) this._exponent) + that._exponent;
        if ((this._error == null) || (that._error == null)
                || (exp > Integer.MAX_VALUE || (exp < Integer.MIN_VALUE))) {
            return NaN;
        }
        LargeInteger thisMin = this._mantissa.subtract(this._error);
        LargeInteger thisMax = this._mantissa.add(this._error);
        LargeInteger thatMin = that._mantissa.subtract(that._error);
        LargeInteger thatMax = that._mantissa.add(that._error);
        LargeInteger min, max;
        if (thisMin.compareTo(thisMax.negate()) > 0) {
            if (thatMin.compareTo(thatMax.negate()) > 0) {
                min = thisMin.multiply(thatMin);
                max = thisMax.multiply(thatMax);
            } else {
                min = thisMax.multiply(thatMin);
                max = thisMin.multiply(thatMax);
            }
        } else {
            if (thatMin.compareTo(thatMax.negate()) > 0) {
                min = thisMin.multiply(thatMax);
                max = thisMax.multiply(thatMin);
            } else {
                min = thisMax.multiply(thatMax);
                max = thisMin.multiply(thatMin);
            }
        }
        Real real = (Real) FACTORY.object();
        real._exponent = (int) exp;
        real._mantissa = min.add(max).shiftRight(1);
        real._error = max.subtract(min).add(LargeInteger.ONE);
        return real.scale();
    }

    /**
     * Returns this real number divided by the one specified.
     * 
     * @param that the real divisor.
     * @return <code>this / that</code>.
     * @throws ArithmeticException if <code>that.equals(ZERO)</code>
     */
    public Real divide(Real that) {
        return this.multiply(that.inverse());
    }

    /**
     * Returns the inverse of this real number.
     *
     * @return <code>1 / this</code>.
     */
    public final Real inverse() {
        if (this._error == null) {
            return NaN;
        }
        LargeInteger thisMin = this._mantissa.subtract(this._error);
        LargeInteger thisMax = this._mantissa.add(this._error);
        if (thisMin.isNegative() && thisMax.isPositive()) { // Encompasses 0
            return NaN;
        }
        int maxBits = thisMin.isLargerThan(thisMax)
                ? thisMin.bitLength()
                : thisMax.bitLength();
        int digits = (int) (maxBits * BITS_TO_DIGITS + 1);
        long exp = ((long) -this._exponent) - digits - digits;
        if ((exp > Integer.MAX_VALUE || (exp < Integer.MIN_VALUE))) {
            return NaN; // Exponent overflow.
        }
        LargeInteger min = div(2 * digits, thisMax);
        LargeInteger max = div(2 * digits, thisMin);
        Real real = (Real) FACTORY.object();
        real._exponent = (int) exp;
        real._mantissa = min.add(max).shiftRight(1);
        real._error = max.subtract(min).add(LargeInteger.ONE);
        return real.scale();

    }
    private static LargeInteger div(int exp, LargeInteger mantissa) {
        int expBitLength = (int) (exp * DIGITS_TO_BITS);
        int precision = expBitLength - mantissa.bitLength() + 1;
        LargeInteger reciprocal = mantissa.inverseScaled(precision);
        LargeInteger result = reciprocal.E(exp);
        return result.shiftRight(expBitLength + 1);
    }
    private static final double BITS_TO_DIGITS = MathLib.LOG2 / MathLib.LOG10;
    private static final double DIGITS_TO_BITS = 1.0 / BITS_TO_DIGITS;

    /**
     * Returns the absolute value of this real number.
     * 
     * @return <code>abs(this)</code>.
     */
    public Real abs() {
        return _mantissa.isNegative() ? this.negate() : this;
    }

    /**
     * Returns the decimal text representation of this number.
     *
     * @return the text representation of this number.
     */
    public Text toText() {
        try {
            TextBuilder tb = TextBuilder.newInstance();
            appendTo(tb);
            return tb.toText();
        } catch (IOException ioError) {
            throw new InternalError(); // Should never get there.
        }
    }

    /**
     * Appends the decimal text representation of this real number to the
     * <code>TextBuilder</code> argument. Only digits guaranteed to be exact
     * are written.
     * 
     * @param tb the <code>TextBuilder</code> to append.
     * @return the specified <code>Appendable</code>.
     * @throws IOException if an I/O exception occurs.
     */
    TextBuilder appendTo(TextBuilder a) throws IOException {
        if (this.isNaN()) {
            return a.append("NaN");
        }
        int errorDigits = (int) (_error.bitLength() * BITS_TO_DIGITS + 1);
        LargeInteger m = (_mantissa.isPositive()) ? _mantissa.add(FIVE
                .E(errorDigits - 1)) : _mantissa.add(MINUS_FIVE
                .E(errorDigits - 1));
        m = m.E(-errorDigits);
        int exp = _exponent + errorDigits;
        TextBuilder chars = TextBuilder.newInstance();
        m.appendTo(chars, 10);
        int digits = (m.isNegative()) ? chars.length() - 1 : chars.length();
        if (digits > 1) {
            if ((exp < 0) && (-exp < digits)) {
                chars.insert(chars.length() + exp, '.');
            } else { // Scientific notation.
                chars.insert(chars.length() - digits + 1, '.');
                chars.append('E');
                TypeFormat.format(exp + digits - 1, chars);
            }
        } else {
            chars.append('E');
            TypeFormat.format(exp, chars);
        }
        a.append(chars);
        return a;
    }
    private static final LargeInteger FIVE = (LargeInteger) LargeInteger
            .valueOf(5).moveHeap();
    private static final LargeInteger MINUS_FIVE = (LargeInteger) LargeInteger
            .valueOf(-5).moveHeap();

    /**
     * Compares this real number against the specified object.
     * 
     * @param that the object to compare with.
     * @return <code>true</code> if the objects are the same;
     *         <code>false</code> otherwise.
     */
    public boolean equals(Object that) {
        if (that instanceof Real) {
            Real thatReal = (Real) that;
            return this._mantissa.equals(thatReal._mantissa)
                    && (this._exponent == thatReal._exponent);
        } else {
            return false;
        }
    }

    /**
     * Returns the hash code for this real number.
     * 
     * @return the hash code value.
     */
    public int hashCode() {
        return _mantissa.hashCode() + _exponent * 31;
    }

    /**
     * Returns the value of this real number as an <code>int</code>.
     * 
     * @return the numeric value represented by this real after conversion
     *         to type <code>int</code>.
     */
    public int intValue() {
        return (int) doubleValue();
    }

    /**
     * Returns the value of this real number as a <code>long</code>.
     * 
     * @return the numeric value represented by this real after conversion
     *         to type <code>long</code>.
     */
    public long longValue() {
        return (long) doubleValue();
    }

    /**
     * Returns the value of this real number as a <code>float</code>.
     * 
     * @return the numeric value represented by this real after conversion
     *         to type <code>float</code>.
     */
    public float floatValue() {
        return (float) doubleValue();
    }

    /**
     * Returns the value of this real number as a <code>double</code>.
     * 
     * @return the numeric value represented by this real after conversion
     *         to type <code>double</code>.
     */
    public double doubleValue() {
        return _mantissa.doubleValue() * MathLib.pow(10, _exponent);
    }

    /**
     * Compares two real numbers numerically.
     * 
     * @param that the real to compare with.
     * @return -1, 0 or 1 as this real is numerically less than, equal to,
     *         or greater than <code>that</code>.
     * @throws ClassCastException <code>that</code> is not a {@link Real}.
     */
    public int compareTo(Object that) {
        Real diff = this.subtract((Real) that);
        if (diff.isPositive()) {
            return 1;
        } else if (diff.isNegative()) {
            return -1;
        } else {
            return 0;
        }
    }

    // Implements Operable.
    public Operable plus(Operable that) {
        return this.add((Real) that);
    }

    // Implements Operable.
    public Operable opposite() {
        return this.negate();
    }

    // Implements Operable.
    public Operable times(Operable that) {
        return this.multiply((Real) that);
    }

    // Implements Operable.
    public Operable reciprocal() {
        return this.inverse();
    }


    // Moves additional real-time members.
    public void move(ContextSpace cs) {
        super.move(cs);
        _mantissa.move(cs);
        if (_error != null) {
            _error.move(cs);
        }
    }

    /**
     * Returns this real equivalent to this real but having the specified
     * exponent.
     *
     * @param  exponent the exponent.
     */
    private Real scale(int exponent) {
        int e = this._exponent - exponent;
        Real real = (Real) FACTORY.object();
        real._mantissa = this._mantissa.E(e);
        real._error = this._error.E(e).add(LargeInteger.ONE);
        real._exponent = exponent;
        return real;
    }

    /**
     * Returns this real at the closest normalized scale.
     */
    private Real scale() {
        int residual = this._exponent & 0x7;
        if (residual != 0) { // Ensures that exponent is a multiplier of 8.
            return this.scale(this._exponent - residual).scale();
        } else if (_error.isLargerThan(MAX_ERROR)) {
            return this.scale(this._exponent + 8).scale();
        } else {
            return this;
        }
    }
    private static final LargeInteger MAX_ERROR = (LargeInteger) LargeInteger.ONE
            .E(16).moveHeap();

    private static final long serialVersionUID = 3833184726813784121L;
}