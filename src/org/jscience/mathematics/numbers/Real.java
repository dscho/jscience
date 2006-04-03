/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2006 - JScience (http://jscience.org/)
 * All rights reserved.
 * 
 * Permission to use, copy, modify, and distribute this software is
 * freely granted, provided that this notice is preserved.
 */
package org.jscience.mathematics.numbers;

import org.jscience.mathematics.structures.Field;

import javolution.lang.MathLib;
import javolution.lang.Text;
import javolution.lang.TypeFormat;
import javolution.xml.XmlElement;
import javolution.xml.XmlFormat;

/**
 * <p> This class represents a real number of arbitrary precision with 
 *     known/guaranteed uncertainty. A real number consists of a 
 *     {@link #getMantissa mantissa}, a maximum {@link #getError error} 
 *     (on the mantissa) and a decimal {@link #getExponent exponent}: 
 *     (<code>(mantissa ± error) · 10<sup>exponent</sup></code>).
 *     Except for {@link #ZERO}, reals number are never exact (tests for 
 *     equality should employ the {@link #approximates} method).<p>
 * 
 * <p> The decimal representations of real instances are indicative of
 *     their precision as only exact digits are written out.
 *     For example, the string <code>"2.000"</code> represents a real 
 *     value of <code>(2.0 ± 0.001)</code>. 
 *     The {@link #getPrecision precision} and {@link #getAccuracy accuracy}
 *     of any real number are available and <b>guaranteed</b> 
 *     (the true/exact value is always within the precision/accuracy range).</p>
 * 
 * <p> Operations on instances of this class are quite fast   
 *     as information substantially below the precision level (aka noise)
 *     is not processed/stored. There is no limit on a real precision
 *     but precision degenerates (due to numeric errors) and calculations 
 *     accelerate as more and more operations are performed.</p>
 * 
 * <p> Instances of this class can be utilized to find approximate 
 *     solutions to linear equations using the 
 *     {@link org.jscience.mathematics.vectors.Matrix Matrix} class for which
 *     high-precision reals is often required, the primitive type
 *     <code>double</code> being not accurate enough to resolve equations 
 *     when the matrix's size exceeds 100x100. Furthermore, even for small 
 *     matrices the "qualified" result is indicative of possible system 
 *     singularities.</p>
 *  
 * @author <a href="mailto:jean-marie@dautelle.com">Jean-Marie Dautelle</a>
 * @version 3.0, February 13, 2006
 * @see <a href="http://en.wikipedia.org/wiki/Real_number">
 *      Wikipedia: Real number</a>
 */
public final class Real extends Number<Real> implements Field<Real> {

    /**
     * Holds the default XML representation for real numbers.
     * This representation consists of a simple <code>value</code> attribute
     * holding the {@link #toText() textual} representation.
     */
    public static final XmlFormat<Real> XML = new XmlFormat<Real>(Real.class) {
        public void format(Real r, XmlElement xml) {
            xml.setAttribute("value", r.toText());
        }

        public Real parse(XmlElement xml) {
            return Real.valueOf(xml.getAttribute("value"));
        }
    };

    /** 
     * Holds a Not-a-Number instance (infinite error). 
     */
    public static final Real NaN = new Real();
    static {
        NaN._mantissa = LargeInteger.ZERO;
    }

    /** 
     * Holds ZERO (the only real number being exact). 
     */
    public static final Real ZERO = new Real();
    static {
        ZERO._mantissa = LargeInteger.ZERO;
        ZERO._error = LargeInteger.ZERO;
    }

    /**
     * The mantissa value.
     */
    private LargeInteger _mantissa;

    /**
     * The mantissa  error or <code>null</code> if NaN.
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
            Real real = FACTORY.object();
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
        Text txt = Text.valueOf(chars); // TODO Use TextFormat...
        if ((txt.length() == 3) && (txt.indexOf("NaN", 0) == 0)) {
            return NaN;
        }
        int exponentIndex = txt.indexOf("E",  0);
        if (exponentIndex >= 0) {
            Real r = valueOf(txt.subtext(0, exponentIndex));
            int exponent = TypeFormat.parseInt(txt.subtext(
                    exponentIndex + 1, txt.length()));
            r._exponent += exponent;
            return r.scale();
        }
        Real real = FACTORY.object();
        int errorIndex = txt.indexOf("±", 0);
        if (errorIndex >= 0) {
            real._mantissa = LargeInteger.valueOf(txt.subtext(0,
                    errorIndex));
            real._error = LargeInteger.valueOf(txt.subtext(
                    exponentIndex + 1, txt.length()));
            if (!real._error.isPositive()) {
                throw new NumberFormatException(chars
                        + " not parsable (error must be greater than 0)");
            }
            real._exponent = 0;
            return real.scale();
        }
        int decimalPointIndex = txt.indexOf(".", 0);
        if (decimalPointIndex >= 0) {
            LargeInteger integer = LargeInteger.valueOf(txt.subtext(0,
                    decimalPointIndex));
            LargeInteger fraction = LargeInteger.valueOf(txt.subtext(
                    decimalPointIndex + 1, txt.length()));
            int fractionDigits = chars.length() - decimalPointIndex - 1;
            real._mantissa = integer.E(fractionDigits).plus(fraction);
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
        if (doubleValue == 0.0) return ZERO;
        long bits = Double.doubleToRawLongBits(doubleValue);
        boolean isNegative = (bits & 0x8000000000000000L) != 0L;
        int exponent = (int) ((bits & 0x7ff0000000000000L) >> 52);
        long mantissa = (bits & 0x000fffffffffffffL) | 0x0010000000000000L;
        if (exponent == 0x7ff)
            return NaN;
        // real = pow2Mantissa * expFactor  
        Real pow2Mantissa = valueOf(LargeInteger.valueOf(isNegative ? - mantissa : mantissa), LargeInteger.ONE, 0);
        Real expFactor = FACTORY.object();
        expFactor._error = LargeInteger.ZERO;
        expFactor._exponent = -18;
        int pow2 = exponent - 1023 - 52;
        if (pow2 < 0) {
            expFactor._mantissa = E18.shiftLeft(-pow2);
            return pow2Mantissa.divide(expFactor);
        } else {
            expFactor._mantissa = E18.shiftLeft(pow2);
            return pow2Mantissa.times(expFactor);
        }
    }
    private static final LargeInteger E18 
        = LargeInteger.ONE.E(18).moveHeap(); // > 52 bits precision.

    /**
     * Converts an integer value to a real instance. 
     * 
     * @param  integer the integer value to convert.
     * @param  accuracy the number of decimal zeros in the fraction part.
     * @return the real number corresponding to the specified integer with
     *         the specified number of decimal zero.
     */
    public static Real valueOf(long integer, int accuracy) {
        return Real.valueOf(LargeInteger.valueOf(integer).E(accuracy),
                LargeInteger.ONE, -accuracy);
    }

    /**
     * Converts an large integer value to a real instance. 
     * 
     * @param  integer the integer value to convert.
     * @param  accuracy the number of decimal zeros in the fraction part.
     * @return the real number corresponding to the specified integer with
     *         the specified number of decimal zero.
     */
    public static Real valueOf(LargeInteger integer, int accuracy) {
        return valueOf(integer.E(accuracy), LargeInteger.ONE, -accuracy);
    }

    /**
     * Returns this real mantissa.
     * 
     * @return the mantissa.
     */
    public LargeInteger getMantissa() {
        return _mantissa;
    }

    /**
     * Returns the maximum error (positive) on this real mantissa.
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
     * Indicates if this real approximates the one specified. 
     * This method takes into account possible errors (e.g. numeric
     * errors) to make this determination.
     *  
     * <p>Note: This method returns <code>false</code> if <code>this</code> or 
     *          <code>that</code> {@link #isNaN}.</p>
     *
     * @param  that the real to compare with.
     * @return <code>this &asymp; that</code>
     */
    public boolean approximates(Real that) {
        Real diff = this.minus(that);
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
        if (_error == null) 
            throw new ArithmeticException("Cannot convert NaN to integer value");
        return _mantissa.E(_exponent);
    }

    /**
     * Returns the negation of this real number.
     * 
     * @return <code>-this</code>.
     */
    public Real opposite() {
        Real real = FACTORY.object();
        real._mantissa = _mantissa.opposite();
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
    public Real plus(Real that) {
        if ((this._error == null) || (that._error == null)) {
            return NaN;
        } else if (this._exponent == that._exponent) {
            Real real = (Real) FACTORY.object();
            real._exponent = _exponent;
            real._mantissa = this._mantissa.plus(that._mantissa);
            real._error = this._error.plus(that._error).plus(LargeInteger.ONE);
            return real.scale();
        } else if (this._exponent > that._exponent) {
            return this.plus(that.scale(this._exponent));
        } else {
            return that.plus(this.scale(that._exponent));
        }
    }

    /**
     * Returns the difference between this real number and the one
     * specified.
     * 
     * @param that the real to be subtracted.
     * @return <code>this - that</code>.
     */
    public Real minus(Real that) {
        if ((this._error == null) || (that._error == null)) {
            return NaN;
        } else if (this._exponent == that._exponent) {
            Real real = (Real) FACTORY.object();
            real._exponent = _exponent;
            real._mantissa = this._mantissa.minus(that._mantissa);
            real._error = this._error.plus(that._error).plus(LargeInteger.ONE);
            return real.scale();
        } else if (this._exponent > that._exponent) {
            return this.minus(that.scale(this._exponent));
        } else {
            return this.scale(that._exponent).minus(that);
        }
    }


    /**
     * Returns the product of this real number with the specified 
     * <code>long</code> multiplier.
     * 
     * @param multiplier the <code>long</code> multiplier.
     * @return <code>this * multiplier</code>.
     */
    public Real times(long multiplier) {
        if (this._error == null) 
            return NaN;
        Real real = FACTORY.object();
        real._exponent = this._exponent;
        real._mantissa = this._mantissa.times(multiplier);
        real._error = this._error.times(multiplier);
        return real.scale();
    }

    /**
     * Returns the product of this real number with the one specified.
     * 
     * @param that the real multiplier.
     * @return <code>this * that</code>.
     */
    public Real times(Real that) {
        long exp = ((long) this._exponent) + that._exponent;
        if ((this._error == null) || (that._error == null)
                || (exp > Integer.MAX_VALUE || (exp < Integer.MIN_VALUE))) {
            return NaN;
        }
        LargeInteger thisMin = this._mantissa.minus(this._error);
        LargeInteger thisMax = this._mantissa.plus(this._error);
        LargeInteger thatMin = that._mantissa.minus(that._error);
        LargeInteger thatMax = that._mantissa.plus(that._error);
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
        Real real = FACTORY.object();
        real._exponent = (int) exp;
        real._mantissa = min.plus(max).shiftRight(1);
        real._error = max.minus(min).plus(LargeInteger.ONE);
        return real.scale();
    }
    
    /**
     * Returns this real number divided by the specified <code>int</code>
     * divisor.  
     * 
     * @param divisor the <code>int</code> divisor.
     * @return <code>this / divisor</code>
     */
    public Real divide(int divisor) {
        if ((_error == null) || (divisor == 0)) return NaN;
        Real real = FACTORY.object();
        real._exponent = this._exponent - 18;
        real._mantissa = this._mantissa.times(E18asLong).divide(divisor);
        real._error = this._error.times(E18asLong).divide(divisor).plus(LargeInteger.ONE);
        return real.scale();
    }
    private static long E18asLong = 1000000000000000000L;
    
    /**
     * Returns this real number divided by the one specified.
     * 
     * @param that the real divisor.
     * @return <code>this / that</code>.
     * @throws ArithmeticException if <code>that.equals(ZERO)</code>
     */
    public Real divide(Real that) {
        return this.times(that.inverse());
    }

    /**
     * Returns the reciprocal (or inverse) of this real number.
     *
     * @return <code>1 / this</code>.
     */
    public Real inverse() {
        if (this._error == null) {
            return NaN;
        }
        LargeInteger thisMin = this._mantissa.minus(this._error);
        LargeInteger thisMax = this._mantissa.plus(this._error);
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
        real._mantissa = min.plus(max).shiftRight(1);
        real._error = max.minus(min).plus(LargeInteger.ONE);
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
    private static final double DIGITS_TO_BITS = MathLib.LOG10 / MathLib.LOG2;

    /**
     * Returns the absolute value of this real number.
     * 
     * @return <code>|this|</code>.
     */
    public Real abs() {
        return _mantissa.isNegative() ? this.opposite() : this;
    }

    /**
     * Compares the absolute value of two real numbers.
     *
     * @param that the real number to be compared with.
     * @return <code>|this| > |that|</code>
     */
    public boolean isLargerThan(Real that) {
        if (this._exponent == that._exponent)
            return this._mantissa.isLargerThan(that._mantissa);
        if (this._exponent > that._exponent) {
            return this._mantissa.isLargerThan(that._mantissa.E(this._exponent - that._exponent));
        } else {
            return this._mantissa.E(that._exponent - this._exponent).isLargerThan(that._mantissa);
        }
    }

    /**
     * Returns the square root of this real number, the more accurate is this 
     * real number, the more accurate the square root. 
     * 
     * @return the positive square root of this real number.
     */
    public Real sqrt() {
        if (this._error == null) return NaN;
        LargeInteger thisMin = this._mantissa.minus(this._error);
        LargeInteger thisMax = this._mantissa.plus(this._error);
        if (thisMin.isNegative()) return NaN;
        int exponent = _exponent >> 1;
        if ((_exponent & 1) == 1) { // Odd exponent.
            thisMin = thisMin.E(1);
            thisMax = thisMax.E(1);
        }
        LargeInteger minSqrt = thisMin.sqrt();
        LargeInteger maxSqrt = thisMax.sqrt().plus(LargeInteger.ONE);
        LargeInteger sqrt = minSqrt.plus(maxSqrt).shiftRight(1);
        Real z = FACTORY.object();
        z._mantissa = sqrt;
        z._error = maxSqrt.minus(sqrt);
        z._exponent = exponent;
        return z;
    }
    
    /**
     * Returns the decimal text representation of this number.
     *
     * @return the text representation of this number.
     */
    public Text toText() {
        if (this.isNaN()) return Text.valueOf("NaN");
        int errorDigits = (int) (_error.bitLength() * BITS_TO_DIGITS + 1);
        LargeInteger m = (_mantissa.isPositive()) ? _mantissa.plus(FIVE
                .E(errorDigits - 1)) : _mantissa.plus(MINUS_FIVE
                .E(errorDigits - 1));
        m = m.E(-errorDigits);
        int exp = _exponent + errorDigits;
        Text txt = m.toText();
        int digits = (m.isNegative()) ? txt.length() - 1 : txt.length();
        if (digits > 1) {
            if ((exp < 0) && (-exp < digits)) {
                txt = txt.insert(txt.length() + exp, Text.valueOf('.'));
            } else { // Scientific notation.
                txt = txt.insert(txt.length() - digits + 1, Text.valueOf('.'));
                txt = txt.concat(Text.valueOf('E')).concat(Text.valueOf(exp + digits - 1));
            }
        } else {
            txt = txt.concat(Text.valueOf('E')).concat(Text.valueOf(exp));
        }
        return txt;
    }
    private static final LargeInteger FIVE = LargeInteger.valueOf(5).moveHeap();
    private static final LargeInteger MINUS_FIVE = LargeInteger.valueOf(-5).moveHeap();

    /**
     * Compares this real number against the specified object.
     * 
     * @param that the object to compare with.
     * @return <code>true</code> if the objects are two reals with same 
     *        mantissa and exponent;<code>false</code> otherwise.
     */
    public boolean equals(Object that) {
        if (!(that instanceof Real)) 
            return false;
            Real thatReal = (Real) that;
            return this._mantissa.equals(thatReal._mantissa)
                    && (this._exponent == thatReal._exponent);
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
     * Returns the value of this real number as a <code>long</code>.
     * 
     * @return the numeric value represented by this real after conversion
     *         to type <code>long</code>.
     */
    public long longValue() {
        return (long) doubleValue();
    }

    /**
     * Returns the value of this real number as a <code>double</code>.
     * 
     * @return the numeric value represented by this real after conversion
     *         to type <code>double</code>.
     */
    public double doubleValue() {
        // Shift the mantissa to a 18+ digits integer (long compatible).
        int nbrDigits = (int)(_mantissa.bitLength() * BITS_TO_DIGITS);
        int digitShift = nbrDigits - 18;
        long reducedMantissa = _mantissa.E(-digitShift).longValue();
        int exponent = _exponent + digitShift;
        return exponent < 0 ? reducedMantissa / Math.pow(10, -exponent)
                : reducedMantissa * Math.pow(10, exponent);
        // TODO Javolution should provide utilities in MathLib
        //      to perform sudh conversion efficiently (no Math.pow) 
    }

    /**
     * Compares two real numbers numerically.
     * 
     * @param that the real to compare with.
     * @return -1, 0 or 1 as this real is numerically less than, equal to,
     *         or greater than <code>that</code>.
     * @throws ClassCastException <code>that</code> is not a {@link Real}.
     */
    public int compareTo(Real that) {
        Real diff = this.minus(that);
        if (diff.isPositive()) {
            return 1;
        } else if (diff.isNegative()) {
            return -1;
        } else {
            return 0;
        }
    }

    // Moves additional real-time members.
    public boolean move(ObjectSpace os) {
        if (super.move(os)) {
            _mantissa.move(os);
            if (_error != null) {
                _error.move(os);
            }
            return true;
        }
        return false;
    }

    /**
     * Returns this real equivalent to this real but having the specified
     * exponent.
     *
     * @param  exponent the exponent.
     */
    private Real scale(int exponent) {
        int e = this._exponent - exponent;
        Real real = FACTORY.object();
        real._mantissa = this._mantissa.E(e);
        real._error = this._error.E(e).plus(LargeInteger.ONE);
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
    private static final LargeInteger MAX_ERROR = LargeInteger.ONE.E(16).moveHeap();

    /**
     * Holds the factory constructing real instances.
     */
    private static final Factory<Real> FACTORY = new Factory<Real>() {

        public Real create() {
            return new Real();
        }
    };

    private static final long serialVersionUID = 1L;

}