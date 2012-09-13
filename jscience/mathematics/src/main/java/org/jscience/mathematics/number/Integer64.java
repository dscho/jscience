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
import javolution.context.ObjectFactory;
import javolution.lang.MathLib;
import javolution.text.Cursor;
import javolution.text.TextFormat;
import javolution.text.TypeFormat;

/**
 * <p> This class represents a 64 bits integer number.</p>
 * 
 * @author <a href="mailto:jean-marie@dautelle.com">Jean-Marie Dautelle</a>
 * @version 5.0, November 20, 2009
 * @see <a href="http://en.wikipedia.org/wiki/Integer">
 *      Wikipedia: Integer</a>
 */
public final class Integer64 extends Number<Integer64> {

    /**
     * Holds the default text format for 64 bits integer numbers.
     *
     * @see TextFormat#getDefault
     */
    protected static final TextFormat<Integer64> TEXT_FORMAT = new TextFormat<Integer64>(Integer64.class) {

        @Override
        public Appendable format(Integer64 integer64, Appendable out) throws IOException {
            return TypeFormat.format(integer64._value, out);
        }

        @Override
        public Integer64 parse(CharSequence csq, Cursor cursor) throws IllegalArgumentException {
            return Integer64.valueOf(TypeFormat.parseLong(csq, 10, cursor));
        }
    };

    /**
     * Holds the factory used to produce 64 bits integer instances.
     */
    private static final ObjectFactory<Integer64> FACTORY = new ObjectFactory<Integer64>() {

        protected Integer64 create() {
            return new Integer64();
        }
    };

    /**
     * The 64 bits integer representing zero.
     */
    public static final Integer64 ZERO = new Integer64(0L);

    /**
     * The 64 bits integer representing one.
     */
    public static final Integer64 ONE = new Integer64(1L);

    /**
     * The associated long value.
     */
    private long _value;

    /**
     * Default constructor.
     */
    private Integer64() {
    }

    /**
     * Creates a 64 bits integer number always on the heap independently from the
     * current {@link javolution.context.AllocatorContext allocator context}.
     * To allow for custom object allocation policies, static factory methods
     * <code>valueOf(...)</code> are recommended.
     *
     * @param  longValue the <code>long</code> value for this number.
     * @see    #longValue()
     */
    public Integer64(long longValue) {
        _value = longValue;
    }

    /**
     * Returns the 64 bits integer from the specified <code>long</code> value.
     *
     * @param  longValue the <code>long</code> value for this number.
     * @return the corresponding number.
     * @see    #longValue()
     */
    public static Integer64 valueOf(long longValue) {
        Integer64 r = FACTORY.object();
        r._value = longValue;
        return r;
    }

    /**
     * Returns the 64 bits integer for the specified character sequence.
     *
     * @param  csq the character sequence.
     * @return <code>TEXT_FORMAT.parse(csq)</code>.
     * @throws IllegalArgumentException if the character sequence does not
     *         contain a parsable number.
     * @see #TEXT_FORMAT
     */
    public static Integer64 valueOf(CharSequence csq) {
        return TEXT_FORMAT.parse(csq);
    }

    /**
     * Returns the sum of this number with the specifice value.
     *
     * @param  value the value to be added.
     * @return <code>this + value</code>.
     */
    public Integer64 plus(long value) {
        Integer64 r = FACTORY.object();
        r._value = this._value + value;
        return r;
    }

    /**
     * Returns the difference between this number and the specified value
     *
     * @param  value the value to be subtracted.
     * @return <code>this - value</code>.
     */
    public Integer64 minus(long value) {
        Integer64 r = FACTORY.object();
        r._value = this._value - value;
        return r;
    }

    /**
     * Returns this number divided by the one specified.
     *
     * @param  that the number divisor.
     * @return <code>this / that</code>.
     */
    public Integer64 divide(Integer64 that) {
        Integer64 r = FACTORY.object();
        r._value = this._value / that._value;
        return r;
    }

    /**
     * Returns this number divided by the specified value.
     *
     * @param  value the value divisor.
     * @return <code>this / value</code>.
     */
    public Integer64 divide(long value) {
        Integer64 r = FACTORY.object();
        r._value = this._value / value;
        return r;
    }

    /**
     * Compares this number against the specified value.
     *
     * @param  value the value to compare with.
     * @return <code>this.longValue() == value</code>
     */
    public boolean equals(long value) {
        return this._value == value;
    }

    /**
     * Compares this number with the specified value for order.
     *
     * @param value the value to be compared with.
     * @return a negative integer, zero, or a positive integer as this number
     *        is less than, equal to, or greater than the specified value.
     */
    public int compareTo(long value) {
        if (this._value < value) {
            return -1;
        } else if (this._value > value) {
            return 1;
        } else {
            return 0;
        }
    }

    // Implements GroupAdditive.
    public Integer64 opposite() {
        Integer64 r = FACTORY.object();
        r._value = -this._value;
        return r;
    }

    // Implements GroupAdditive.
    public Integer64 plus(Integer64 that) {
        Integer64 r = FACTORY.object();
        r._value = this._value + that._value;
        return r;
    }

    @Override
    public Integer64 minus(Integer64 that) {
        Integer64 r = FACTORY.object();
        r._value = this._value - that._value;
        return r;
    }

    @Override
    public Integer64 times(long multiplier) {
        Integer64 r = FACTORY.object();
        r._value = this._value * multiplier;
        return r;
    }
    // Implements Ring

    public Integer64 times(Integer64 that) {
        Integer64 r = FACTORY.object();
        r._value = this._value * that._value;
        return r;
    }

    // Implements abstract class Number.
    public Integer64 abs() {
        Integer64 r = FACTORY.object();
        r._value = MathLib.abs(this._value);
        return r;
    }

    @Override
    public long longValue() {
        return _value;
    }

    @Override
    public double doubleValue() {
        return _value;
    }

    @Override
    public BigDecimal decimalValue() {
        return BigDecimal.valueOf(_value);
    }

    @Override
    public int compareTo(Integer64 that) {
        return compareTo(that._value);
    }

    @Override
    public Integer64 copy() {
        return Integer64.valueOf(_value);
    }
    private static final long serialVersionUID = 1L;

}
