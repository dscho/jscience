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

import javolution.util.Text;
import javolution.util.TextBuilder;
import javolution.util.TypeFormat;
import javolution.xml.XmlElement;
import javolution.xml.XmlFormat;

import org.jscience.mathematics.matrices.Matrix;
import org.jscience.mathematics.matrices.Operable;

/**
 * <p> This class represents the ratio of two {@link LargeInteger} numbers.</p>
 * <p> Instances of this class are immutable and can be used to find exact 
 *     solutions to linear equations with the {@link Matrix} class.</p>
 * 
 * @author <a href="mailto:jean-marie@dautelle.com">Jean-Marie Dautelle</a>
 * @version 1.0, October 24, 2004
 */
public final class Rational extends RealtimeNumber implements Comparable {

    /**
     * Holds the default XML representation for rational numbers.
     * This representation consists of a simple <code>value</code> attribute
     * holding its textual representation.
     * 
     * @see #valueOf(CharSequence)
     * @see #toText
     */
    protected static final XmlFormat RATIONAL_XML = new XmlFormat(Rational.class) {
        public void format(Object obj, XmlElement xml) {
            xml.setAttribute("value", ((Rational) obj).toText());
        }

        public Object parse(XmlElement xml) {
            return Rational.valueOf(xml.getAttribute("value"));
        }
    };

    /**
     * Holds the factory constructing rational instances.
     */
    private static final Factory FACTORY = new Factory() {

        public Object create() {
            return new Rational();
        }
    };

    /**
     * The {@link Rational} representing the additive identity.
     */
    public static final Rational ZERO = (Rational) valueOf(LargeInteger.ZERO,
            LargeInteger.ONE).moveHeap();

    /**
     * The {@link Rational} representing the multiplicative identity.
     */
    public static final Rational ONE = (Rational) valueOf(LargeInteger.ONE,
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
     * Returns the {@link Rational} from the specified dividend and divisor. 
     * 
     * @param dividend the dividend value.
     * @param divisor the divisor value.
     * @return <code>dividend / divisor</code>
     * @throws ArithmeticException if <code>divisor.isZero()</code>
     */
    public static Rational valueOf(LargeInteger dividend, LargeInteger divisor) {
        Rational r = (Rational) FACTORY.object();
        r._dividend = dividend;
        r._divisor = divisor;
        return r.normalize();
    }

    /**
     * Returns the {@link Rational} for the specified character sequence.
     * 
     * @param  chars the character sequence.
     * @return the corresponding Rational number.
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
     * {@link Rational}.
     * 
     * @return this rational's dividend.
     */
    public LargeInteger getDividend() {
        return _dividend;
    }

    /**
     * Returns the smallest divisor of the fraction representing this 
     * {@link Rational} (always positive).
     * 
     * @return this rational's divisor.
     */
    public LargeInteger getDivisor() {
        return _divisor;
    }

    /**
     * Returns the closest {@link Integer} to this {@link Rational}.
     * 
     * @return this rational rounded to the nearest integer.
     */
    public LargeInteger round() {
        return _dividend.divide(_divisor);
    }

    /**
     * Returns the negation of this {@link Rational}.
     * 
     * @return <code>-this</code>.
     */
    public Rational negate() {
        return valueOf(_dividend.negate(), _divisor);
    }

    /**
     * Returns the sum of this {@link Rational} with the one specified.
     * 
     * @param that the Rational to be added.
     * @return <code>this + that</code>.
     */
    public Rational add(Rational that) {
        return valueOf(
                this._dividend.multiply(that._divisor).add(
                        this._divisor.multiply(that._dividend)),
                this._divisor.multiply(that._divisor)).normalize();
    }

    /**
     * Returns the difference between this {@link Rational} and the one
     * specified.
     * 
     * @param that the Rational to be subtracted.
     * @return <code>this - that</code>.
     */
    public Rational subtract(Rational that) {
        return valueOf(
                this._dividend.multiply(that._divisor).subtract(
                        this._divisor.multiply(that._dividend)),
                this._divisor.multiply(that._divisor)).normalize();
    }

    /**
     * Returns the product of this {@link Rational} with the one specified.
     * 
     * @param that the Rational multiplier.
     * @return <code>this * that</code>.
     */
    public Rational multiply(Rational that) {
        return valueOf(this._dividend.multiply(that._dividend),
                this._divisor.multiply(that._divisor)).normalize();
    }

    /**
     * Returns this {@link Rational} divided by the one specified.
     * 
     * @param that the Rational divisor.
     * @return <code>this / that</code>.
     * @throws ArithmeticException if <code>that.equals(ZERO)</code>
     */
    public Rational divide(Rational that) {
        return valueOf(this._dividend.multiply(that._divisor),
                this._divisor.multiply(that._dividend)).normalize();
    }

    /**
     * Returns the absolute value of this {@link Rational}.
     * 
     * @return <code>abs(this)</code>.
     */
    public Rational abs() {
        return valueOf(_dividend.abs(), _divisor);
    }

    /**
     * Returns the decimal text representation of this number.
     *
     * @return the text representation of this number.
     */
    public Text toText() {
        try {
            TextBuilder tb = TextBuilder.newInstance();
            appendTo(tb, 10);
            return tb.toText();
        } catch (IOException ioError) {
            throw new InternalError(); // Should never get there.
        }
    }

    /**
     * Returns the text representation of this number in the specified radix.
     *
     * @param radix the radix of the representation.
     * @return the text representation of this number.
     */
    public Text toText(int radix) {
        try {
            TextBuilder tb = TextBuilder.newInstance();
            appendTo(tb, radix);
            return tb.toText();
        } catch (IOException ioError) {
            throw new InternalError(); // Should never get there.
        }
    }

    /**
     * Appends the text representation of this {@link Rational} to the
     * <code>Appendable</code> argument in the specified radix.
     *
     * @param a the <code>Appendable</code> to append.
     * @param radix the radix of the representation.
     * @return the specified <code>Appendable</code>.
     * @throws IOException if an I/O exception occurs.
     */
    Appendable appendTo(Appendable a, int radix) throws IOException {
        _dividend.appendTo(a, radix);
        a.append('/');
        return _divisor.appendTo(a, radix);
    }

    /**
     * Compares this {@link Rational} against the specified object.
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
     * Returns the hash code for this {@link Rational} number.
     * 
     * @return the hash code value.
     */
    public int hashCode() {
        return _dividend.hashCode() - _divisor.hashCode();
    }

    /**
     * Returns the value of this {@link Rational} as an <code>int</code>.
     * 
     * @return the numeric value represented by this rational after conversion
     *         to type <code>int</code>.
     */
    public int intValue() {
        return (int) doubleValue();
    }

    /**
     * Returns the value of this {@link Rational} as a <code>long</code>.
     * 
     * @return the numeric value represented by this rational after conversion
     *         to type <code>long</code>.
     */
    public long longValue() {
        return (long) doubleValue();
    }

    /**
     * Returns the value of this {@link Rational} as a <code>float</code>.
     * 
     * @return the numeric value represented by this rational after conversion
     *         to type <code>float</code>.
     */
    public float floatValue() {
        return (float) doubleValue();
    }

    /**
     * Returns the value of this {@link Rational} as a <code>double</code>.
     * 
     * @return the numeric value represented by this rational after conversion
     *         to type <code>double</code>.
     */
    public double doubleValue() {
        return _dividend.doubleValue() / _divisor.doubleValue();
    }

    /**
     * Compares two {@link Rational} numerically.
     * 
     * @param that the Rational to compare with.
     * @return -1, 0 or 1 as this Rational is numerically less than, equal to,
     *         or greater than <code>that</code>.
     * @throws ClassCastException <code>that</code> is not an {@link Rational}.
     */
    public int compareTo(Object that) {
        return this._dividend.multiply(((Rational) that)._divisor).compareTo(
                ((Rational) that)._dividend.multiply(this._divisor));
    }

    /**
     * Normalizes this rational.
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
                _dividend = _dividend.negate();
                _divisor = _divisor.negate();
                return normalize();
            }
        } else {
            throw new ArithmeticException("Zero divisor");
        }
    }

    // Implements Operable.
    public Operable plus(Operable that) {
        return this.add((Rational) that);
    }

    // Implements Operable.
    public Operable opposite() {
        return this.negate();
    }

    // Implements Operable.
    public Operable times(Operable that) {
        return this.multiply((Rational) that);
    }

    // Implements Operable.
    public Operable reciprocal() {
        return _dividend.divide(_divisor);
    }

    // Moves additional real-time members.
    public void move(ContextSpace cs) {
        super.move(cs);
        _dividend.move(cs);
        _divisor.move(cs);
    }

}