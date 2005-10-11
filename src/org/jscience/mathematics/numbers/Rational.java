/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2005 - JScience (http://jscience.org/)
 * All rights reserved.
 * 
 * Permission to use, copy, modify, and distribute this software is
 * freely granted, provided that this notice is preserved.
 */
package org.jscience.mathematics.numbers;

import javolution.lang.Text;
import javolution.lang.TypeFormat;
import javolution.xml.XmlElement;
import javolution.xml.XmlFormat;

/**
 * <p> This class represents the ratio of two {@link LargeInteger} numbers.</p>
 * 
 * <p> Instances of this class are immutable and can be used to find exact 
 *     solutions to linear equations with the {@link 
 *     org.jscience.mathematics.matrices.Matrix Matrix} class.</p>
 * 
 * @author <a href="mailto:jean-marie@dautelle.com">Jean-Marie Dautelle</a>
 * @version 2.0, June 6, 2005
 */
public final class Rational extends Number<Rational> {

    /**
     * Holds the default XML representation for rational numbers.
     * This representation consists of a simple <code>value</code> attribute
     * holding the {@link #toText() textual} representation.
     */
    protected static final XmlFormat<Rational> XML = new XmlFormat<Rational>(
            Rational.class) {
        public void format(Rational r, XmlElement xml) {
            xml.setAttribute("value", r.toText());
        }

        public Rational parse(XmlElement xml) {
            return Rational.valueOf(xml.getAttribute("value"));
        }
    };

    /**
     * Holds the factory constructing rational instances.
     */
    private static final Factory<Rational> FACTORY = new Factory<Rational>() {

        protected Rational create() {
            return new Rational();
        }
    };

    /**
     * The {@link Rational} representing the additive identity.
     */
    public static final Rational ZERO = Rational.valueOf(LargeInteger.ZERO,
            LargeInteger.ONE).moveHeap();

    /**
     * The {@link Rational} representing the multiplicative identity.
     */
    public static final Rational ONE = Rational.valueOf(LargeInteger.ONE,
            LargeInteger.ONE).moveHeap();

    /**
     * Holds the dividend.
     */
    private LargeInteger _dividend;

    /**
     * Holds the divisor.
     */
    private LargeInteger _divisor;

    /**
     * Default constructor. 
     */
    private Rational() {
    }

    /**
     * Returns the rational number for the specified integer dividend and 
     * divisor. 
     * 
     * @param dividend the dividend value.
     * @param divisor the divisor value.
     * @return <code>dividend / divisor</code>
     * @throws ArithmeticException if <code>divisor == 0</code>
     */
    public static Rational valueOf(long dividend, long divisor) {
        Rational r = FACTORY.object();
        r._dividend = LargeInteger.valueOf(dividend);
        r._divisor = LargeInteger.valueOf(divisor);
        return r.normalize();
    }

    /**
     * Returns the rational number for the specified large integer 
     * dividend and divisor. 
     * 
     * @param dividend the dividend value.
     * @param divisor the divisor value.
     * @return <code>dividend / divisor</code>
     * @throws ArithmeticException if <code>divisor.isZero()</code>
     */
    public static Rational valueOf(LargeInteger dividend, LargeInteger divisor) {
        Rational r = FACTORY.object();
        r._dividend = dividend;
        r._divisor = divisor;
        return r.normalize();
    }

    /**
     * Returns the rational number for the specified character sequence.
     * 
     * @param  chars the character sequence.
     * @return the corresponding rational number.
     */
    public static Rational valueOf(CharSequence chars) {
        int sep = TypeFormat.indexOf("/", chars, 0);
        if (sep >= 0) {
            LargeInteger dividend = LargeInteger.valueOf(chars.subSequence(0,
                    sep));
            LargeInteger divisor = LargeInteger.valueOf(chars.subSequence(
                    sep + 1, chars.length()));
            return valueOf(dividend, divisor);
        } else { // No divisor.
            return valueOf(LargeInteger.valueOf(chars.subSequence(0, sep)),
                    LargeInteger.ONE);
        }
    }

    /**
     * Returns the smallest dividend of the fraction representing this
     * rational number.
     * 
     * @return this rational dividend.
     */
    public LargeInteger getDividend() {
        return _dividend;
    }

    /**
     * Returns the smallest divisor of the fraction representing this 
     * rational (always positive).
     * 
     * @return this rational divisor.
     */
    public LargeInteger getDivisor() {
        return _divisor;
    }

    /**
     * Returns the closest integer to this rational number.
     * 
     * @return this rational rounded to the nearest integer.
     */
    public LargeInteger round() {
        return _dividend.divide(_divisor);
    }

    /**
     * Returns the opposite of this rational number.
     * 
     * @return <code>-this</code>.
     */
    public Rational opposite() {
        return Rational.valueOf(_dividend.oppositeBasic(), _divisor);
    }

    /**
     * Returns the sum of this rational number with the one specified.
     * 
     * @param that the rational number to be added.
     * @return <code>this + that</code>.
     */
    public Rational plus(Rational that) {
        return Rational.valueOf(
                this._dividend.timesBasic(that._divisor).plusBasic(
                        this._divisor.timesBasic(that._dividend)),
                this._divisor.timesBasic(that._divisor)).normalize();
    }

    /**
     * Returns the difference between this rational number and the one
     * specified.
     * 
     * @param that the rational number to be subtracted.
     * @return <code>this - that</code>.
     */
    public Rational minus(Rational that) {
        return Rational.valueOf(
                this._dividend.timesBasic(that._divisor).minusBasic(
                        this._divisor.timesBasic(that._dividend)),
                this._divisor.timesBasic(that._divisor)).normalize();
    }

    /**
     * Returns the product of this rational number with the one specified.
     * 
     * @param that the rational number multiplier.
     * @return <code>this * that</code>.
     */
    public Rational times(Rational that) {
        return Rational.valueOf(this._dividend.timesBasic(that._dividend),
                this._divisor.timesBasic(that._divisor)).normalize();
    }

    /**
     * Returns the reciprocal (or inverse) of this rational number.
     * 
     * @return <code>1 / this</code>.
     * @throws ArithmeticException if <code>dividend.isZero()</code>
     */
    public Rational reciprocal() {
        if (_dividend.isZero())
            throw new ArithmeticException("Dividend is zero");
        return _dividend.isNegative() ? Rational.valueOf(_divisor.oppositeBasic(),
                _dividend.oppositeBasic()) : Rational.valueOf(_divisor, _dividend);
    }

    /**
     * Returns this rational number divided by the one specified.
     * 
     * @param that the rational number divisor.
     * @return <code>this / that</code>.
     * @throws ArithmeticException if <code>that.equals(ZERO)</code>
     */
    public Rational divide(Rational that) {
        return Rational.valueOf(this._dividend.timesBasic(that._divisor),
                this._divisor.timesBasic(that._dividend)).normalize();
    }

    /**
     * Returns the norm (absolute value) of this rational number.
     * 
     * @return <code>|this|</code>.
     */
    public Rational norm() {
        return Rational.valueOf(_dividend.norm(), _divisor);
    }

    /**
     * Compares the {@link #norm norm} of two rational numbers.
     *
     * @param that the rational number to be compared with.
     * @return <code>|this| > |that|</code>
     */
    public boolean isLargerThan(Rational that) {
        return this._dividend.timesBasic(that._divisor).isLargerThan(
                that._dividend.timesBasic(this._divisor));
    }

    /**
     * Throws {@link ArithmeticException}, the square root is not a rational 
     * number in the general case.
     * 
     * @throws ArithmeticException 
     */
    public Rational sqrt() {
        throw new ArithmeticException(
                "Square Root of a rational number is not a rational number"
                        + " in the general case");
    }

    /**
     * Returns the decimal text representation of this number.
     *
     * @return the text representation of this number.
     */
    public Text toText() {
        return _dividend.toText().concat(Text.valueOf('/')).concat(
                _divisor.toText());
    }

    /**
     * Returns the text representation of this number in the specified radix.
     *
     * @param radix the radix of the representation.
     * @return the text representation of this number.
     */
    public Text toText(int radix) {
        return _dividend.toText(radix).concat(Text.valueOf('/')).concat(
                _divisor.toText(radix));
    }

    /**
     * Compares this rational number against the specified object.
     * 
     * @param that the object to compare with.
     * @return <code>true</code> if the objects are the same;
     *         <code>false</code> otherwise.
     */
    public boolean equals(Object that) {
        if (that instanceof Rational) {
            return this._dividend.equals(((Rational) that)._dividend)
                    && this._divisor.equals(((Rational) that)._divisor);
        } else {
            return false;
        }
    }

    /**
     * Returns the hash code for this rational number.
     * 
     * @return the hash code value.
     */
    public int hashCode() {
        return _dividend.hashCode() - _divisor.hashCode();
    }

    /**
     * Returns the value of this rational number as a <code>long</code>.
     * 
     * @return the numeric value represented by this rational after conversion
     *         to type <code>long</code>.
     */
    public long longValue() {
        return (long) doubleValue();
    }

    /**
     * Returns the value of this rational number as a <code>double</code>.
     * 
     * @return the numeric value represented by this rational after conversion
     *         to type <code>double</code>.
     */
    public double doubleValue() {
        return _dividend.doubleValue() / _divisor.doubleValue();
    }

    /**
     * Compares two rational number numerically.
     * 
     * @param that the rational number to compare with.
     * @return -1, 0 or 1 as this rational number is numerically less than, 
     *         equal to, or greater than <code>that</code>.
     */
    public int compareTo(Rational that) {
        return this._dividend.timesBasic(that._divisor).compareTo(
                that._dividend.timesBasic(this._divisor));
    }

    /**
     * Returns the normalized form of this rational.
     * 
     * @return this rational after normalization.
     * @throws ArithmeticException if <code>divisor.isZero()</code>
     */
    private Rational normalize() {
        if (!_divisor.isZero()) {
            if (_divisor.isPositive()) {
                LargeInteger gcd = _dividend.gcd(_divisor);
                if (!gcd.equals(LargeInteger.ONE)) {
                    _dividend = _dividend.divide(gcd);
                    _divisor = _divisor.divide(gcd);
                }
                return this;
            } else {
                _dividend = _dividend.oppositeBasic();
                _divisor = _divisor.oppositeBasic();
                return normalize();
            }
        } else {
            throw new ArithmeticException("Zero divisor");
        }
    }

    // Moves additional real-time members.
    public boolean move(ObjectSpace os) {
        if (super.move(os)) {
            _dividend.move(os);
            _divisor.move(os);
            return true;
        }
        return false;
    }

    private static final long serialVersionUID = 1L;

}