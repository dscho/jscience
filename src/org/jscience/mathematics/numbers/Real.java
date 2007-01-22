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
import javolution.text.Text;
import javolution.text.TypeFormat;
import javolution.xml.XMLFormat;
import javolution.xml.stream.XMLStreamException;

/**
 * <p> This class represents a real number of arbitrary precision with 
 *     known/guaranteed uncertainty. A real number consists of a 
 *     {@link #getMantissa mantissa}, a maximum {@link #getError error} 
 *     (on the mantissa) and a decimal {@link #getExponent exponent}: 
 *     (<code>(mantissa ± error) · 10<sup>exponent</sup></code>).</p>
 *     
 * <p> Reals number can be exact. The minimum precision 
 *     for an exact number during calculations is given by 
 *     getExactMinimumPrecision() 
 *     ({@link javolution.context.LocalContext context local}).<p>
 * 
 * <p> The actual {@link #getPrecision precision} and {@link #getAccuracy 
 *     accuracy} of any real number is available and <b>guaranteed</b> 
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
 * @version 3.3, January 8, 2006
 * @see <a href="http://en.wikipedia.org/wiki/Real_number">
 *      Wikipedia: Real number</a>
 */
public final class Real extends Number<Real> implements Field<Real> {

    /**
     * Holds the default XML representation for real numbers.
     * This representation consists of a simple <code>value</code> attribute
     * holding the {@link #toText() textual} representation.
     */
    protected static final XMLFormat<Real> XML = new XMLFormat<Real>(Real.class) {
        
        @Override
        public Real newInstance(Class<Real> cls, InputElement xml) throws XMLStreamException {
            return Real.valueOf(xml.getAttribute("value"));
        }
        
        public void write(Real real, OutputElement xml) throws XMLStreamException {
             xml.setAttribute("value", real.toText());
         }

         public void read(InputElement xml, Real real) {
             // Nothing to do, immutable.
         }
     };

    /** 
     * Holds a Not-a-Number instance (infinite error). 
     */
    public static final Real NaN = new Real(); // Unique (0 ± 1E2147483647)
    static {
        NaN._mantissa = LargeInteger.ZERO;
        NaN._error = LargeInteger.ONE;
        NaN._exponent = Integer.MAX_VALUE;
    }

    /** 
     * Holds the exact ZERO instance. It is the only real instance
     * being exact (represented "0"). 
     */
    public static final Real ZERO = new Real(); // Unique (0E-2147483648)
    static {
        ZERO._mantissa = LargeInteger.ZERO;
        ZERO._error = LargeInteger.ZERO;
        ZERO._exponent = Integer.MIN_VALUE;
    }

    /**
     * The mantissa value.
     */
    private LargeInteger _mantissa;

    /**
     * The mantissa  error.
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
     * If the error is <code>0</code>, the real is assumed exact. 
     * For example:[code]
     * 
     *     // x = 0.0 ± 0.01 
     *     Real x = Real.valueOf(LargeInteger.ZERO, 1, -2);
     *                           
     *      // y = -12.3 exact 
     *     Real y = Real.valueOf(LargeInteger.valueOf("-123"), 0, -1);
     * 
     *     // The floating-point 1.000..00 with an accuracy of 100 digits. 
     *     Real one = Real.valueOf(LargeInteger.ONE.E(100), 1, -100);
     * [/code]
     * 
     * @param mantissa this real mantissa.
     * @param error the maximum error on the mantissa.
     * @param exponent the decimal exponent.
     * @return <code>(mantissa ± error)·10<sup>exponent</sup>)</code>
     * @throws IllegalArgumentException if <code>error < 0</code>
     */
    public static Real valueOf(LargeInteger mantissa, int error,
            int exponent) {
        if (error < 0) 
            throw new IllegalArgumentException("Error cannot be negative");
        Real real = FACTORY.object();
        real._mantissa = mantissa;
        real._error = LargeInteger.valueOf(error);
        real._exponent = exponent;
        return real;
    }

    /**
     * Returns the real number (inexact except for <code>0.0</code>) 
     * corresponding to the specified <code>double</code> value. 
     * The error is derived from the inexact representation of 
     * <code>double</code> values intrinsic to the 64 bits IEEE 754 format.
     * 
     * @param doubleValue the <code>double</code> value to convert.
     * @return the corresponding real number.
     */
    public static Real valueOf(double doubleValue) {
        if (doubleValue == 0.0) return ZERO;
        // Find the exponent e such as: value == x.xxx * 10^e
        int e = MathLib.floorLog10(doubleValue) - 18 + 1; // 18 digits mantissa.
        long mantissa = MathLib.toLongPow10(doubleValue, -e); 
        int error = (int) MathLib.toLongPow10(Math.ulp(doubleValue), -e) + 1;
        return 
           Real.valueOf(LargeInteger.valueOf(mantissa), error, e);
    }

    /**
     * Returns the real number (exact) corresponding to the specified 
     * <code>long</code> value (convenience method).  
     * 
     * @param longValue the exact long value.
     * @return <code>Real.valueOf(LargeInteger.valueOf(longValue), 0, 0)</code>
     */
    public static Real valueOf(long longValue) {
        return Real.valueOf(LargeInteger.valueOf(longValue), 0, 0);
    }

    /**
     * Returns the real for the specified character sequence.
     * If the precision is not specified (using the <code>±</code> symbol), 
     * the real is supposed exact. Example of valid character sequences:
     * <li>"1.2E3" (1200 exact)</li>
     * <li>"1.2E3±1E-2" (1200 ± 0.01)</li></ul>
     * 
     * @param  chars the character sequence.
     * @return the corresponding real number.
     * @throws NumberFormatException if the character sequence does not contain
     *         a parsable real.
     */
    public static Real valueOf(CharSequence chars) throws NumberFormatException {
        
        
        
        Text txt = Text.valueOf(chars); // TODO Use TextFormat...
        if ((txt.length() == 3) && (txt.indexOf("NaN", 0) == 0)) 
            return NaN;
        if (txt.equals("0")) return ZERO;
        int exponentIndex = txt.indexOf("E",  0);
        if (exponentIndex >= 0) {
            int exponent = TypeFormat.parseInt(txt.subtext(
                    exponentIndex + 1, txt.length()));
            Real r = valueOf(txt.subtext(0, exponentIndex));
            if (r == ZERO) 
                return valueOf(LargeInteger.ZERO, 1, exponent);
            r._exponent += exponent;
            return r.scale();
        }
        Real real = FACTORY.object();
        int errorIndex = txt.indexOf("±", 0);
        if (errorIndex >= 0) {
            real._mantissa = LargeInteger.valueOf(txt.subtext(0,
                    errorIndex));
            real._error = LargeInteger.valueOf(txt.subtext(
                    errorIndex + 1, txt.length()));
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
            real._mantissa = integer.isNegative() ?
                    integer.times10pow(fractionDigits).minus(fraction) :
                    integer.times10pow(fractionDigits).plus(fraction);
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
     * Returns this real mantissa.
     * 
     * @return the mantissa.
     */
    public LargeInteger getMantissa() {
        return _mantissa;
    }

    /**
     * Returns the maximum error (positive) on this real mantissa.
     * 
     * @return the maximum error on the mantissa.
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
        if (this == NaN) return Integer.MIN_VALUE;
        if (this == ZERO) return Integer.MAX_VALUE;
        return -_exponent - maxDigitLength(_error);
    }

    /**
     * Returns the total number of decimal digits guaranteed exact
     * (relative error).
     *
     * @return a measure of the relative error of this real number.
     */
    public final int getPrecision() {
        if (this == NaN) return Integer.MIN_VALUE;
        if (this == ZERO) return Integer.MAX_VALUE;
        return maxDigitLength(_mantissa) - maxDigitLength(_error);
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
        if (diff == NaN) return false;
        return diff._error.isLargerThan(diff._mantissa);
    }

    /**
     * Converts this real to a {@link LargeInteger} instance. 
     * Any fractional part of this real is discarded.
     * 
     * @return the integer part of this real.
     * @throws ArithmeticException if <code>this.isNaN()</code>
     */
    public LargeInteger toLargeInteger() {
        if (this == NaN) 
            throw new ArithmeticException("Cannot convert NaN to integer value");
        return _mantissa.times10pow(_exponent);
    }

    /**
     * Returns the negation of this real number.
     * 
     * @return <code>-this</code>.
     */
    public Real opposite() {
        if (this == NaN) return NaN;
        if (this == ZERO) return ZERO;
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
        if ((this == NaN) || (that == NaN)) return NaN;
        if (this == ZERO) return that;
        if (that == ZERO) return this;
        if (this._exponent == that._exponent) {
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
        if ((this == NaN) || (that == NaN)) return NaN;
        if (this == ZERO) return that.opposite();
        if (that == ZERO) return this;
        if (this._exponent == that._exponent) {
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
        if (this == NaN) return NaN;
        if ((this == ZERO) || (multiplier == 0)) 
            return ZERO;
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
        if ((this == NaN) || (that == NaN)) return NaN;
        if ((this == ZERO) || (that == ZERO)) return ZERO;                
        long exp = ((long) this._exponent) + that._exponent;
        if (exp > Integer.MAX_VALUE || (exp < Integer.MIN_VALUE)) 
            return NaN; // Exponent overflow.
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
        if ((this == NaN) || (divisor == 0)) return NaN;
        if (this == ZERO) return ZERO;
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
        if ((this == NaN) || (this == ZERO))
            return NaN;
        LargeInteger thisMin = this._mantissa.minus(this._error);
        LargeInteger thisMax = this._mantissa.plus(this._error);
        if (thisMin.isNegative() && thisMax.isPositive())  // Encompasses 0
            return NaN;
        int digits = MathLib.max(maxDigitLength(thisMin), maxDigitLength(thisMax));
        long exp = ((long) -this._exponent) - digits - digits;
        if ((exp > Integer.MAX_VALUE || (exp < Integer.MIN_VALUE))) 
            return NaN; // Exponent overflow.
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
        LargeInteger result = reciprocal.times10pow(exp);
        return result.shiftRight(expBitLength + 1);
    }
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
        return this.abs().compareTo(that.abs()) > 0;
    }

    /**
     * Returns the square root of this real number, the more accurate is this 
     * real number, the more accurate the square root. 
     * 
     * @return the positive square root of this real number.
     */
    public Real sqrt() {
        if (this == NaN) return NaN;
        if (this == ZERO) return ZERO;
        LargeInteger thisMin = this._mantissa.minus(this._error);
        LargeInteger thisMax = this._mantissa.plus(this._error);
        if (thisMin.isNegative()) return NaN;
        int exponent = _exponent >> 1;
        if ((_exponent & 1) == 1) { // Odd exponent.
            thisMin = thisMin.times10pow(1);
            thisMax = thisMax.times10pow(1);
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
        if (this == NaN) return Text.valueOf("NaN");
        if (this == ZERO) return Text.valueOf("0");
        int errorDigits = maxDigitLength(_error);
        LargeInteger m = (_mantissa.isPositive()) ? _mantissa.plus(FIVE
                .times10pow(errorDigits - 1)) : _mantissa.plus(MINUS_FIVE
                .times10pow(errorDigits - 1));
        m = m.times10pow(-errorDigits);
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
     * <p>Note: This method returns <code>true</code> if <code>this</code> or 
     *          <code>that</code> {@link #isNaN is Not-A-Number}, even though
     *          <code>Double.NaN == Double.NaN</code> has the value
     *          <code>false</code>.</p>
     *
     * @param that the object to compare with.
     * @return <code>true</code> if the objects are two reals with same 
     *        mantissa, error and exponent;<code>false</code> otherwise.
     */
    public boolean equals(Object that) {
        if (this == that) return true;
        if (!(that instanceof Real)) 
            return false;
        Real thatReal = (Real) that;
        return this._mantissa.equals(thatReal._mantissa) && this._error.equals(thatReal._error)
              && (this._exponent == thatReal._exponent);
    }

    /**
     * Returns the hash code for this real number.
     * 
     * @return the hash code value.
     */
    public int hashCode() {
        return _mantissa.hashCode() + _error.hashCode() + _exponent * 31;
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
        if (this == NaN) return Double.NaN;
        if (this == ZERO) return 0.0;
        // Shift the mantissa to a >18 digits integer (long compatible).
        int nbrDigits = maxDigitLength(_mantissa);
        int digitShift = nbrDigits - 18;
        long reducedMantissa = _mantissa.times10pow(-digitShift).longValue();
        int exponent = _exponent + digitShift;
        return MathLib.toDoublePow10(reducedMantissa, exponent);
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
            _error.move(os);
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
    private Real scale(long exponent) {
        long e = this._exponent - exponent;
        if (e < Integer.MIN_VALUE) return ZERO;
        if (e > Integer.MAX_VALUE) return NaN;
        Real real = FACTORY.object();
        real._mantissa = this._mantissa.times10pow((int)e);
        real._error = this._error.times10pow((int)e).plus(LargeInteger.ONE);
        real._exponent = (int) exponent;
        return real;
    }

    /**
     * Returns this real at the closest normalized scale.
     */
    private Real scale() {
        int residual = this._exponent & 0x7;
        if (residual != 0) { // Ensures that exponent is a multiplier of 8.
            if (this._exponent < Integer.MIN_VALUE + residual)
                return ZERO; // Underflow.
            return this.scale(this._exponent - residual).scale();
        } else if (_error.isLargerThan(MAX_ERROR)) {
            if (this._exponent > Integer.MAX_VALUE - 8)
                return NaN; // Overflow
            return this.scale(this._exponent + 8).scale();
        } else {
            return this;
        }
    }
    private static final LargeInteger MAX_ERROR = LargeInteger.ONE.times10pow(16);


    /**
     * Returns the maximum number of decimal digits necessary to represent 
     * this large integer (sign excluded). The actual number of digits 
     * to represent this large integer is up to one less digit than the number
     * of digits returned.
     * 
     * @return the maximum number of digits.
     */
    private static int maxDigitLength(LargeInteger li) {
        return (int)(li.bitLength() * BITS_TO_DIGITS) + 1;
    }

    private static final double BITS_TO_DIGITS = MathLib.LOG2 / MathLib.LOG10;
    
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