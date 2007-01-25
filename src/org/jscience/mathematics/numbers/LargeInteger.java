/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2006 - JScience (http://jscience.org/)
 * All rights reserved.
 * 
 * Permission to use, copy, modify, and distribute this software is
 * freely granted, provided that this notice is preserved.
 */
package org.jscience.mathematics.numbers;

import java.io.IOException;

import javolution.context.ConcurrentContext;
import javolution.context.PoolContext;
import javolution.context.PoolContext.Reference;
import javolution.context.ConcurrentContext.Logic;
import javolution.context.LocalContext;
import javolution.lang.MathLib;
import javolution.text.Text;
import javolution.text.TextBuilder;
import javolution.text.TextFormat;
import javolution.text.TypeFormat;
import javolution.xml.XMLFormat;
import javolution.xml.stream.XMLStreamException;
import static org.jscience.mathematics.numbers.Calculus.*;

/**
 * <p> This class represents an immutable integer number of arbitrary size.</p>
 * 
 * <p> It has the following advantages over the 
 *     <code>java.math.BigInteger</code> class:
 * <ul>
 *     <li> Optimized for 64 bits architectures. But still runs significantly 
 *          faster on 32 bits processors.</li>
 *     <li> Real-time compliant for improved performance and predictability
 *          (no garbage generated when executing in 
 *          {@link javolution.context.PoolContext PoolContext}).</li>
 *     <li> Improved algorithms (e.g. Concurrent Karabutsa multiplication in 
 *          O(n<sup>Log3</sup>) instead of O(n<sup>2</sup>).</li>
 * </ul></p>
 * 
 * <p> <b>Note:</b> This class uses {@link ConcurrentContext concurrent 
 *     contexts} to accelerate calculations on multi-processors
 *     systems. Numbers up to 64 KBits are allocated on the stack when 
 *     executing in a {@link javolution.context.PoolContext PoolContext},
 *     larger numbers are heap allocated.</p>
 *     
 * @author <a href="mailto:jean-marie@dautelle.com">Jean-Marie Dautelle</a>
 * @version 3.3, January 14, 2007
 * @see <a href="http://en.wikipedia.org/wiki/Arbitrary-precision_arithmetic">
 *      Wikipedia: Arbitrary-precision Arithmetic</a>
 */
public final class LargeInteger extends Number<LargeInteger> {

    /**
     * Holds the {@link javolution.context.LocalContext context local} format 
     * for large integers (decimal representation by default).
     */
    public static final LocalContext.Reference<TextFormat<LargeInteger>> FORMAT = new LocalContext.Reference<TextFormat<LargeInteger>>(
            Format.INSTANCE);

    /**
     * Holds the default XML representation for large integers.
     * This representation consists of a simple <code>value</code> attribute
     * holding the {@link #toText() textual} representation.
     */
    public static final XMLFormat<LargeInteger> XML = new XMLFormat<LargeInteger>(
            LargeInteger.class) {

        @Override
        public LargeInteger newInstance(Class<LargeInteger> cls,
                InputElement xml) throws XMLStreamException {
            return LargeInteger.valueOf(xml.getAttribute("value"));
        }

        public void write(LargeInteger li, OutputElement xml)
                throws XMLStreamException {
            xml.setAttribute("value", li.toText());
        }

        public void read(InputElement xml, LargeInteger li) {
            // Nothing to do, immutable.
        }
    };

    /**
     * The large integer representing the additive identity.
     */
    public static final LargeInteger ZERO = new LargeInteger(1);
    static {
        ZERO._size = 0;
        ZERO._words[0] = 0;
        // Note:  _words[0] == 0 for all zero values.
    }

    /**
     * The large integer representing the multiplicative identity.
     */
    public static final LargeInteger ONE = new LargeInteger(1);
    static {
        ONE._size = 1;
        ONE._words[0] = 1;
    }

    /**
     * Holds the remainder after a {@link #divide} operation.
     */
    private LargeInteger _remainder;

    /**
     * Indicates if this large integer is negative.
     */
    private boolean _isNegative;

    /**
     * The size of this large integer in words. 
     * The most significand word different from 0 is at index: _size-1
     */
    private int _size;

    /**
     * This large integer positive words (63 bits). 
     * Least significant word first (index 0).
     */
    private final long[] _words;

    /**
     * Creates a large integer with the specified 63 bits word capacity.
     * 
     * @link wordLength the internal positive <code>long</code> array length.
     */
    private LargeInteger(int wordLength) {
        _words = new long[wordLength];
    }

    /**
     * Returns the large integer of specified <code>long</code> value.
     * 
     * @param  value the <code>long</code> value.
     * @return the corresponding large integernumber.
     */
    public static LargeInteger valueOf(long value) {
        if (value == 0)
            return LargeInteger.ZERO;
        if (value == 1)
            return LargeInteger.ONE;
        if (value == Long.MIN_VALUE)
            return LargeInteger.LONG_MIN_VALUE;
        LargeInteger li = FACTORY_4.object();
        boolean negative = li._isNegative = value < 0;
        li._words[0] = negative ? -value : value;
        li._size = 1;
        return li;
    }

    /**
     * Returns the large integer of specified two's-complement binary
     * representation. The input array is assumed to be in <i>big-endian</i>
     * byte-order: the most significant byte is at the offset position.
     * 
     * <p>Note: This representation is consitent with <code>java.lang.BigInteger
     *          </code> byte array representation and can be used for conversion 
     *          between the two classes.</p>
     * 
     * @param  bytes the binary representation (two's-complement).
     * @param  offset the offset at which to start reading the bytes.
     * @param  length the maximum number of bytes to read.
     * @return the corresponding large integer number.
     * @throws IndexOutOfBoundsException 
     *         if <code>offset + length > bytes.length</code>  
     * @see    #toByteArray
     */
    public static LargeInteger valueOf(byte[] bytes, int offset, int length) {
        // Ensures result is large enough (takes into account potential
        // extra bits during negative to positive conversion).
        LargeInteger z = LargeInteger.newInstance(((length * 8 + 1) / 63) + 1);
        final boolean isNegative = bytes[offset] < 0;
        int wordIndex = 0;
        int bitIndex = 0;
        z._words[0] = 0;
        for (int i = offset + length; i > offset; bitIndex += 8) {
            long bits = (isNegative ? ~bytes[--i] : bytes[--i]) & MASK_8;
            if (bitIndex < 63 - 8) {
                z._words[wordIndex] |= bits << bitIndex;
            } else { // End of word reached.
                z._words[wordIndex] |= (bits << bitIndex) & MASK_63;
                bitIndex -= 63; // In range [-8..-1]
                z._words[++wordIndex] = bits >> -bitIndex;
            }
        }
        // Calculates size.
        while (z._words[wordIndex] == 0) {
            if (--wordIndex < 0) {
                break;
            }
        }
        z._size = wordIndex + 1;
        z._isNegative = isNegative;

        // Converts one's-complement to two's-complement if negative.
        if (isNegative) { // Adds ONE.
            if (z._size > 0) {
                z._size = add(z._words, z._size, ONE._words, 1, z._words);
            } else {
                z._size = add(ONE._words, 1, z._words, 0, z._words);
            }
        }
        return z;
    }

    /**
     * Returns the large integer for the specified character sequence in
     * decimal number.
     * 
     * @param chars the character sequence.
     * @return {@link #valueOf(CharSequence, int) valueOf(chars, 10)}
     */
    public static LargeInteger valueOf(CharSequence chars) {
        return FORMAT.get().parse(chars);
    }

    /**
     * Returns the large integer for the specified character sequence stated
     * in the specified radix. The characters must all be digits of the
     * specified radix, except the first character which may be a plus sign
     * <code>'+'</code> or a minus sign <code>'-'</code>.
     * 
     * @param chars the character sequence to parse.
     * @param radix the radix to be used while parsing.
     * @return the corresponding large integer.
     * @throws NumberFormatException if the specified character sequence does
     *         not contain a parsable large integer.
     */
    public static LargeInteger valueOf(CharSequence chars, int radix) {
        TextFormat.Cursor cursor = TextFormat.Cursor.newInstance(0, chars
                .length());
        LargeInteger li = Format.INSTANCE.parse(chars, radix, cursor);
        TextFormat.Cursor.recycle(cursor);
        return li;
    }

    /**
     * Returns the large integer corresponding to the specified 
     * <code>java.math.BigInteger</code> instance.
     * 
     * @param  bigInteger the big integer instance.
     * @return the large integer having the same value.
     */
    public static LargeInteger valueOf(java.math.BigInteger bigInteger) {
        byte[] bytes = bigInteger.toByteArray();
        return LargeInteger.valueOf(bytes, 0, bytes.length);
    }

    /**
     * Indicates if this large integer is greater than {@link #ZERO}
     * ({@link #ZERO}is not included).
     * 
     * @return <code>this > ZERO</code>
     */
    public boolean isPositive() {
        return !_isNegative && (_size != 0);
    }

    /**
     * Indicates if this large integer is less than {@link #ZERO}.
     * 
     * @return <code>this < ZERO</code>
     */
    public boolean isNegative() {
        return _isNegative;
    }

    /**
     * Indicates if this large integer is equal to {@link #ZERO}.
     * 
     * @return <code>this == ZERO</code>
     */
    public boolean isZero() {
        return _size == 0;
    }

    /**
     * Returns the signum function of this large integer.
     *
     * @return -1, 0 or 1 as the value of this integer is negative, zero or
     *         positive.
     */
    public int signum() {
        return _isNegative ? -1 : (_size == 0) ? 0 : 1;
    }

    /**
     * Indicates if this large integer is an even number.
     * 
     * @return <code>(this & 1) == ZERO</code>
     */
    public boolean isEven() {
        return (_words[0] & 1) == 0;
    }

    /**
     * Indicates if this large integer is an odd number.
     * 
     * @return <code>(this & 1) != ZERO</code>
     */
    public boolean isOdd() {
        return (_words[0] & 1) != 0;
    }

    /**
     * Returns the minimal number of bits to represent this large integer
     * in the minimal two's-complement (sign excluded).
     * 
     * @return the length of this integer in bits (sign excluded).
     */
    public int bitLength() {
        if (_size == 0)
            return 0;
        final int n = _size - 1;
        final int bitLength = MathLib.bitLength(_words[n]) + (n << 6) - n;
        return (this.isNegative() && this.isPowerOfTwo()) ? bitLength - 1
                : bitLength;
    }

    /**
     * Indicates if this number is a power of two (equals to 2<sup>
     * ({@link #bitLength bitLength()} - 1)</sup>).
     * 
     * @return <code>true</code> if this number is a power of two; 
     *         <code>false</code> otherwise.
     */
    public boolean isPowerOfTwo() {
        if (_size == 0)
            return false;
        final int n = _size - 1;
        for (int j = 0; j < n; j++) {
            if (_words[j] != 0)
                return false;
        }
        final int bitLength = MathLib.bitLength(_words[n]);
        return _words[n] == (1L << (bitLength - 1));
    }

    /**
     * Returns the index of the lowest-order one bit in this large integer
     * or <code>-1</code> if <code>this.equals(ZERO)</code>.
     *
     * @return the index of the rightmost bit set or <code>-1</code>
     */
    public int getLowestSetBit() {
        if (_size == 0)
            return -1;
        for (int i = 0;; i++) {
            long w = _words[i];
            if (w == 0)
                continue;
            for (int j = 0;; j++) {
                if (((1L << j) & w) != 0)
                    return i * 63 + j;
            }
        }
    }

    /**
     * Returns the final undivided part after division that is less or of 
     * lower degree than the divisor. This value is only set by the 
     * {@link #divide} operation and is not considered as part of 
     * this large integer (ignored by all methods).
     * 
     * @return the remainder of the division for which this large integer
     *         is the quotient.
     */
    public LargeInteger getRemainder() {
        return _remainder;
    }

    /**
     * Indicates if this large integer is larger than the one
     * specified in absolute value.
     * 
     * @param that the integer to be compared with.
     * @return <code>this.abs().compareTo(that.abs()) > 0</code>.
     */
    public boolean isLargerThan(LargeInteger that) {
        return (this._size > that._size)
                || ((this._size == that._size) && Calculus.compare(this._words,
                        that._words, this._size) > 0);
    }

    /**
     * Returns the two's-complement binary representation of this 
     * large integer. The output array is in <i>big-endian</i>
     * byte-order: the most significant byte is at the offset position.
     * 
     * <p>Note: This representation is consitent with <code>java.lang.BigInteger
     *          </code> byte array representation and can be used for conversion 
     *          between the two classes.</p>
     * 
     * @param  bytes the bytes to hold the binary representation 
     *         (two's-complement) of this large integer.
     * @param  offset the offset at which to start writing the bytes.
     * @return the number of bytes written.
     * @throws IndexOutOfBoundsException 
     *         if <code>bytes.length < (bitLength() >> 3) + 1</code>  
     * @see    #valueOf(byte[], int, int)
     * @see    #bitLength
     */
    public int toByteArray(byte[] bytes, int offset) {
        int bytesLength = (bitLength() >> 3) + 1;
        int wordIndex = 0;
        int bitIndex = 0;
        if (_isNegative) {
            long word = _words[0] - 1;
            long borrow = word >> 63; // -1 if borrow
            word = ~word & MASK_63;
            for (int i = bytesLength + offset; i > offset; bitIndex += 8) {
                if (bitIndex < 63 - 8) {
                    bytes[--i] = (byte) word;
                    word >>= 8;
                } else { // End of word reached.
                    byte bits = (byte) word;
                    word = (++wordIndex < _size) ? _words[wordIndex] + borrow
                            : borrow;
                    borrow = word >> 63; // -1 if borrow
                    word = ~word & MASK_63;
                    bitIndex -= 63; // In range [-8..-1]
                    bytes[--i] = (byte) ((word << -bitIndex) | bits);
                    word >>= (8 + bitIndex);
                }
            }
        } else {
            if (_size != 0) {
                long word = _words[0];
                for (int i = bytesLength + offset; i > offset; bitIndex += 8) {
                    if (bitIndex < 63 - 8) {
                        bytes[--i] = (byte) word;
                        word >>= 8;
                    } else { // End of word reached.
                        byte bits = (byte) word;
                        word = (++wordIndex < _size) ? _words[wordIndex] : 0;
                        bitIndex -= 63; // In range [-8..-1]
                        bytes[--i] = (byte) ((word << -bitIndex) | bits);
                        word >>= (8 + bitIndex);
                    }
                }
            } else { // ZERO
                bytes[offset] = 0;
            }
        }
        return bytesLength;
    }

    /**
     * Returns the opposite of this large integer.
     * 
     * @return <code>-this</code>.
     */
    public LargeInteger opposite() {
        if (_size == 0)
            return LargeInteger.ZERO;
        LargeInteger li = LargeInteger.newInstance(_size);
        System.arraycopy(_words, 0, li._words, 0, _size);
        li._size = _size;
        li._isNegative = !_isNegative;
        return li;
    }

    /**
     * Returns the sum of this large integer with the specified 
     * <code>long</code> integer.
     * 
     * @param value the <code>long</code> integer being added.
     * @return <code>this + value</code>.
     */
    public LargeInteger plus(long value) {
        return this.plus(LargeInteger.valueOf(value));
    }

    /**
     * Returns the sum of this large integer with the one specified.
     * 
     * @param that the integer to be added.
     * @return <code>this + that</code>.
     */
    public LargeInteger plus(LargeInteger that) {
        if (this._size < that._size) // Adds smallest in size to largest. 
            return that.plus(this);
        if ((this._isNegative != that._isNegative) && (that._size != 0))
            return this.minus(that.opposite()); // Switches that sign.
        LargeInteger li = LargeInteger.newInstance(_size + 1);
        li._size = Calculus.add(_words, _size, that._words, that._size,
                li._words);
        li._isNegative = _isNegative;
        return li;
    }

    /**
     * Returns the difference between this large integer and the one
     * specified.
     * 
     * @param that the integer to be subtracted.
     * @return <code>this - that</code>.
     */
    public LargeInteger minus(LargeInteger that) {
        if ((this._isNegative != that._isNegative) && (that._size != 0))
            return this.plus(that.opposite()); // Switches that sign.
        if (that.isLargerThan(this)) // Always subtract the smallest to the largest. 
            return that.minus(this).opposite();
        LargeInteger li = LargeInteger.newInstance(this._size);
        li._size = Calculus.subtract(_words, _size, that._words, that._size,
                li._words);
        li._isNegative = this._isNegative && (li._size != 0);
        return li;
    }

    /** 
     * Returns the product of this large integer with the specified 
     * <code>long</code> multiplier.
     * 
     * @param multiplier the <code>long</code> multiplier.
     * @return <code>this · multiplier</code>.
     */
    public LargeInteger times(long multiplier) {
        if ((this._size == 0) || (multiplier == 0))
            return LargeInteger.ZERO;
        if (multiplier < 0)
            return ((multiplier == Long.MIN_VALUE) ? this.shiftLeft(63) : this
                    .times(-multiplier)).opposite();
        LargeInteger li = LargeInteger.newInstance(_size + 1);
        li._size = Calculus.multiply(_words, _size, multiplier, li._words);
        li._isNegative = _isNegative;
        return li;
    }

    /**
     * Returns the product of this large integer with the one specified.
     * 
     * @param that the large integer multiplier.
     * @return <code>this · that</code>.
     */
    public LargeInteger times(LargeInteger that) {
        if (that._size > this._size) // Always multiply the smallest to the largest.
            return that.times(this);
        if (that._size <= 1) // Direct times(long) multiplication.
            return this.times(that.longValue());
        if (that._size < 10) { // Conventional multiplication.
            LargeInteger li = LargeInteger.newInstance(this._size + that._size);
            li._size = Calculus.multiply(this._words, this._size, that._words,
                    that._size, li._words);
            li._isNegative = (this._isNegative != that._isNegative);
            return li;
        } else if (that._size < 20) { // Karatsuba (sequential).
            int n = (that._size >> 1) + (that._size & 1);
            // this = a + 2^(n*63) b, that = c + 2^(n*63) d
            LargeInteger b = this.high(n);
            LargeInteger a = this.low(n);
            // Optimization for square (a == c, b == d).
            LargeInteger d = (this == that) ? b : that.high(n);
            LargeInteger c = (this == that) ? a : that.low(n);
            LargeInteger ab = a.plus(b);
            LargeInteger cd = (this == that) ? ab : c.plus(d);
            LargeInteger abcd = ab.times(cd);
            LargeInteger ac = a.times(c);
            LargeInteger bd = b.times(d);
            // li = a*c + ((a+b)*(c+d)-(a*c+b*d)) 2^n + b*d 2^2n 
            return ac.plus(abcd.minus(ac.plus(bd)).shiftWordLeft(n)).plus(
                    bd.shiftWordLeft(n << 1));
        } else { // Karatsuba (concurrent).
            PoolContext.enter();
            try {
                int n = (that._size >> 1) + (that._size & 1);
                // this = a + 2^(63*n) b, that = c + 2^(63*n) d
                LargeInteger b = this.high(n);
                LargeInteger a = this.low(n);
                // Optimization for square (a == c, b == d).
                LargeInteger d = (this == that) ? b : that.high(n);
                LargeInteger c = (this == that) ? a : that.low(n);
                LargeInteger ab = a.plus(b);
                LargeInteger cd = (this == that) ? ab : c.plus(d);
                Reference<LargeInteger> ac = Reference.newInstance();
                Reference<LargeInteger> bd = Reference.newInstance();
                Reference<LargeInteger> abcd = Reference.newInstance();
                ConcurrentContext.enter();
                try { // this = a + 2^n b,   that = c + 2^n d
                    ConcurrentContext.execute(MULTIPLY, ab, cd, abcd);
                    ConcurrentContext.execute(MULTIPLY, a, c, ac);
                    ConcurrentContext.execute(MULTIPLY, b, d, bd);
                } finally {
                    ConcurrentContext.exit();
                }
                // li = a*c + ((a+b)*(c+d)-(a*c+b*d)) 2^n + b*d 2^2n 
                LargeInteger li = ac.get().plus(
                        abcd.get().minus(ac.get().plus(bd.get()))
                                .shiftWordLeft(n)).plus(
                        bd.get().shiftWordLeft(n << 1));
                return li.export();
            } finally {
                PoolContext.exit();
            }
        }
    }

    private LargeInteger high(int w) { // this.shiftRight(w * 63)
        LargeInteger li = LargeInteger.newInstance(_size - w);
        li._isNegative = _isNegative;
        li._size = _size - w;
        System.arraycopy(_words, w, li._words, 0, _size - w);
        return li;
    }

    private LargeInteger low(int w) { // this.minus(high(w).shiftLeft(w * 63));
        LargeInteger li = LargeInteger.newInstance(w);
        li._isNegative = _isNegative;
        System.arraycopy(_words, 0, li._words, 0, w);
        for (int i = w; i > 0; i--) {
            if (_words[i - 1] != 0) {
                li._size = i;
                return li;
            }
        } // Else zero.
        return LargeInteger.ZERO;
    }

    private LargeInteger shiftWordLeft(int w) { // this.minus(high(w).shiftLeft(w * 63));
        LargeInteger li = LargeInteger.newInstance(w + _size);
        li._isNegative = _isNegative;
        li._size = w + _size;
        for (int i = 0; i < w;) {
            li._words[i++] = 0;
        }
        System.arraycopy(_words, 0, li._words, w, _size);
        return li;
    }

    private static final Logic MULTIPLY = new Logic() {
        public void run() {
            LargeInteger left = getArgument(0);
            LargeInteger right = getArgument(1);
            PoolContext.Reference<LargeInteger> result = getArgument(2);
            result.set(left.times(right));// Recursive.
        }
    };

    /**
     * Returns this large integer divided by the specified <code>int</code>
     * divisor. The remainder of this division is accessible using 
     * {@link #getRemainder}. 
     * 
     * @param divisor the <code>int</code> divisor.
     * @return <code>this / divisor</code> and <code>this % divisor</code>
     *        ({@link #getRemainder})
     * @throws ArithmeticException if <code>divisor == 0</code>
     */
    public LargeInteger divide(int divisor) {
        if (divisor == 0)
            throw new ArithmeticException("Division by zero");
        if (divisor == Integer.MIN_VALUE) { // abs(divisor) would overflow.
            LargeInteger li = this.times2pow(-31).copy();
            li._isNegative = !_isNegative && (li._size != 0);
            li._remainder = _isNegative ? LargeInteger
                    .valueOf(-(_words[0] & MASK_31)) : LargeInteger
                    .valueOf(_words[0] & MASK_31);
            return li;
        }
        LargeInteger li = LargeInteger.newInstance(_size);
        long rem = Calculus.divide(_words, _size, MathLib.abs(divisor),
                li._words);
        li._size = (_size > 0) && (li._words[_size - 1] == 0L) ? _size - 1
                : _size;
        li._isNegative = (_isNegative != (divisor < 0)) && (li._size != 0);
        li._remainder = LargeInteger.valueOf(_isNegative ? -rem : rem);
        return li;
    }

    /**
     * Returns this large integer divided by the one specified (integer
     * division). The remainder of this division is accessible using 
     * {@link #getRemainder}. 
     * 
     * @param that the integer divisor.
     * @return <code>this / that</code> and <code>this % that</code> 
     *        ({@link #getRemainder})
     * @throws ArithmeticException if <code>that.equals(ZERO)</code>
     */
    public LargeInteger divide(LargeInteger that) {
        if ((that._size <= 1) && ((that._words[0] >> 32) == 0))
            return divide(that.intValue());
        LargeInteger result;
        LargeInteger remainder;
        PoolContext.enter();
        try {
            LargeInteger thisAbs = this.abs();
            LargeInteger thatAbs = that.abs();
            int precision = thisAbs.bitLength() - thatAbs.bitLength() + 1;
            if (precision <= 0) {
                result = LargeInteger.ZERO;
                remainder = this;
            } else {
                LargeInteger thatReciprocal = thatAbs.inverseScaled(precision);
                result = thisAbs.times(thatReciprocal);
                result = result.shiftRight(thisAbs.bitLength() + 1);

                // Calculates remainder, corrects for result +/- 1 error. 
                remainder = thisAbs.minus(thatAbs.times(result));
                if (remainder.compareTo(thatAbs) >= 0) {
                    remainder = remainder.minus(thatAbs);
                    result = result.plus(LargeInteger.ONE);
                    if (remainder.compareTo(thatAbs) >= 0)
                        throw new Error("Verification error for " + this + "/"
                                + that + ", please submit a bug report.");
                } else if (remainder.isNegative()) {
                    remainder = remainder.plus(thatAbs);
                    result = result.minus(ONE);
                    if (remainder.isNegative())
                        throw new Error("Verification error for " + this + "/"
                                + that + ", please submit a bug report.");
                }
            }
        } finally {
            PoolContext.exit();
        }
        // Setups result and remainder.
        LargeInteger li = result.copy();
        li._isNegative = (this._isNegative != that._isNegative)
                && (result._size != 0);
        li._remainder = remainder.copy();
        li._remainder._isNegative = this._isNegative && (remainder._size != 0);
        return li;
    }

    /**
     * Returns the remainder of the division of this large integer with 
     * the one specified (convenience method equivalent to 
     * <code>this.divide(that).getRemainder()</code>).
     *
     * @param that the value by which this integer is to be divided, and the
     *        remainder returned.
     * @return <code>this % that</code>
     * @throws ArithmeticException if <code>that.equals(ZERO)</code>
     * @see #divide(LargeInteger)
     */
    public LargeInteger remainder(LargeInteger that) {
        return this.divide(that).getRemainder();
    }

    /**
     * Returns a scaled approximation of <code>1 / this</code>.
     * 
     * @param precision the requested precision (reciprocal error being ± 1).
     * @return <code>2<sup>(precision + this.bitLength())</sup> / this</code>
     * @throws ArithmeticException if <code>this.isZero()</code>
     */
    public LargeInteger inverseScaled(int precision) {
        if (precision <= 30) { // Straight calculation.
            long divisor = this.shiftRight(this.bitLength() - precision - 1)._words[0];
            long dividend = 1L << (precision * 2 + 1);
            return (this.isNegative()) ? LargeInteger.valueOf(-dividend
                    / divisor) : LargeInteger.valueOf(dividend / divisor);
        } else { // Newton iteration (x = 2 * x - x^2 * this).
            LargeInteger x = inverseScaled(precision / 2 + 1); // Estimate.
            LargeInteger thisTrunc = shiftRight(bitLength() - (precision + 2));
            LargeInteger prod = thisTrunc.times(x).times(x);
            int diff = 2 * (precision / 2 + 2);
            LargeInteger prodTrunc = prod.shiftRight(diff);
            LargeInteger xPad = x.shiftLeft(precision - precision / 2 - 1);
            LargeInteger tmp = xPad.minus(prodTrunc);
            return xPad.plus(tmp);
        }
    }

    /**
     * Returns this large integer modulo the specified large integer. 
     * 
     * <p> Note: The result as the same sign as the divisor unlike the Java 
     *     remainder (%) operator (which as the same sign as the dividend).</p> 
     * 
     * @param m the modulus.
     * @return <code>this mod m</code>
     * @see #getRemainder()
     */
    public LargeInteger mod(LargeInteger m) {
        final LargeInteger li = m.isLargerThan(this) ? this : this.divide(m)
                .getRemainder();
        return (this._isNegative == m._isNegative) ? li : li.plus(m);
    }

    /**
     * Returns the large integer whose value is <code>(this<sup>-1</sup> mod m)
     * </code>.
     *
     * @param  m the modulus.
     * @return <code>this<sup>-1</sup> mod m</code>.
     * @throws ArithmeticException <code> m &lt;= 0</code>, or this integer
     *         has no multiplicative inverse mod m (that is, this integer
     *         is not <i>relatively prime</i> to m).
     */
    public LargeInteger modInverse(LargeInteger m) {
        if (!m.isPositive())
            throw new ArithmeticException("Modulus is not a positive number");
        PoolContext.enter();
        try {
            // Extended Euclidian Algorithm
            LargeInteger a = this;
            LargeInteger b = m;
            LargeInteger p = ONE;
            LargeInteger q = ZERO;
            LargeInteger r = ZERO;
            LargeInteger s = ONE;
            while (!b.isZero()) {
                LargeInteger quot = a.divide(b);
                LargeInteger c = quot.getRemainder();
                a = b;
                b = c;
                LargeInteger new_r = p.minus(quot.times(r));
                LargeInteger new_s = q.minus(quot.times(s));
                p = r;
                q = s;
                r = new_r;
                s = new_s;
            }
            if (!a.abs().equals(ONE)) // (a != 1) || (a != -1)
                throw new ArithmeticException("GCD(" + this + ", " + m + ") = "
                        + a);
            if (a.isNegative())
                return p.opposite().mod(m).export();
            return p.mod(m).export();
        } finally {
            PoolContext.exit();
        }
    }

    /**
     * Returns this large integer raised at the specified exponent modulo 
     * the specified modulus.
     *
     * @param  exp the exponent.
     * @param  m the modulus.
     * @return <code>this<sup>exp</sup> mod m</code>
     * @throws ArithmeticException <code>m &lt;= 0</code>
     * @see    #modInverse
     */
    public LargeInteger modPow(LargeInteger exp, LargeInteger m) {
        if (!m.isPositive())
            throw new ArithmeticException("Modulus is not a positive number");
        if (exp.isPositive()) {
            PoolContext.enter();
            try {
                LargeInteger pow2 = this.mod(m);
                LargeInteger result = null;
                while (exp.compareTo(ONE) >= 0) { // Iteration.
                    if (exp.isOdd()) {
                        result = (result == null) ? pow2 : result.times(pow2)
                                .mod(m);
                    }
                    pow2 = pow2.times(pow2).mod(m);
                    exp = exp.shiftRight(1);
                }
                return result.export();
            } finally {
                PoolContext.exit();
            }
        } else if (exp.isNegative()) {
            return this.modPow(exp.opposite(), m).modInverse(m);
        } else { // exp == 0
            return LargeInteger.ONE;
        }
    }

    /**
     * Returns the greatest common divisor of this large integer and 
     * the one specified.
     * 
     * @param  that the other number to compute the GCD with.
     * @return a positive number or {@link #ZERO} if
     *         <code>(this.isZero() && that.isZero())</code>.
     */
    public LargeInteger gcd(LargeInteger that) {
        if (this.isZero())
            return that;
        if (that.isZero())
            return this;
        // Works with local (modifiable) copies of the inputs.
        LargeInteger u = this.copy();
        u._isNegative = false; // abs()
        LargeInteger v = that.copy();
        v._isNegative = false; // abs()

        // Euclidian algorithm until u, v about the same size.
        while (MathLib.abs(u._size - v._size) > 1) {
            LargeInteger tmp = u.divide(v);
            LargeInteger rem = tmp.getRemainder();
            u = v;
            v = rem;
            if (v.isZero())
                return u;
        }

        // Binary GCD Implementation adapted from
        // http://en.wikipedia.org/wiki/Binary_GCD_algorithm
        final int uShift = u.getLowestSetBit();
        u.shiftRightSelf(uShift);
        final int vShift = v.getLowestSetBit();
        v.shiftRightSelf(vShift);

        // From here on, u is always odd.
        while (true) {
            // Now u and v are both odd, so diff(u, v) is even.
            // Let u = min(u, v), v = diff(u, v)/2.
            if (u.compareTo(v) < 0) {
                v.subtract(u);
            } else {
                u.subtract(v);
                LargeInteger tmp = u;
                u = v;
                v = tmp; // Swaps. 
            }
            v.shiftRightSelf();
            if (v.isZero())
                break;
            v.shiftRightSelf(v.getLowestSetBit());
        }
        return u.shiftLeft(MathLib.min(uShift, vShift));
    }

    private void shiftRightSelf() {
        if (_size == 0)
            return;
        _size = Calculus.shiftRight(0, 1, _words, _size, _words);
    }

    private void shiftRightSelf(int n) {
        if ((n == 0) || (_size == 0))
            return;
        int wordShift = n < 63 ? 0 : n / 63;
        int bitShift = n - ((wordShift << 6) - wordShift); // n - wordShift * 63
        _size = Calculus.shiftRight(wordShift, bitShift, _words, _size, _words);
    }

    private void subtract(LargeInteger that) { // this >= that
        _size = Calculus.subtract(_words, _size, that._words, that._size,
                _words);
    }

    /**
     * Returns the absolute value of this large integer.
     * 
     * @return <code>|this|</code>.
     */
    public LargeInteger abs() {
        if (_isNegative)
            return this.opposite();
        return this;
    }

    /**
     * Returns the value of this large integer after performing a binary
     * shift to left. The shift distance, <code>n</code>, may be negative,
     * in which case this method performs a right shift.
     * 
     * @param n the shift distance, in bits.
     * @return <code>this &lt;&lt; n</code>.
     * @see #shiftRight
     */
    public LargeInteger shiftLeft(int n) {
        if (n < 0)
            return shiftRight(-n);
        if (_size == 0)
            return LargeInteger.ZERO;
        final int wordShift = n < 63 ? 0 : n / 63;
        final int bitShift = n - wordShift * 63;
        LargeInteger li = newInstance(_size + wordShift + 1);
        li._isNegative = _isNegative;
        li._size = Calculus.shiftLeft(wordShift, bitShift, _words, _size,
                li._words);
        return li;
    }

    /**
     * Returns the value of this large integer after performing a binary
     * shift to right with sign extension <code>(-1 >> 1 == -1)</code>.
     * The shift distance, <code>n</code>, may be negative, in which case 
     * this method performs a {@link #shiftLeft(int)}.
     * 
     * @param n the shift distance, in bits.
     * @return <code>this &gt;&gt; n</code>.
     */
    public LargeInteger shiftRight(int n) {
        LargeInteger li = this.times2pow(-n);
        if ((_isNegative) && (n > 0)) { // Adjust, two's-complement is being shifted.
            int wordShift = n < 63 ? 0 : n / 63;
            int bitShift = n - ((wordShift << 6) - wordShift); // n - wordShift * 63
            int i = wordShift;
            boolean bitsLost = (bitShift != 0)
                    && (_words[i] << (64 - bitShift)) != 0;
            while ((!bitsLost) && --i >= 0) {
                bitsLost = _words[i--] != 0;
            }
            if (bitsLost)
                return li.minus(ONE);
        }
        return li;
    }

    /**
     * Returns the value of this large integer after multiplication by 
     * a power of two. This method is equivalent to {@link #shiftLeft(int)}
     * for positive n; but it is different from {@link #shiftRight(int)} for 
     * negative value as no sign extension is performed 
     * <code>(-1 >>> 1 == 0)</code>.
     * 
     * @param n the power of 2 exponent.
     * @return <code>this · 2<sup>n</sup></code>.
     */
    public LargeInteger times2pow(int n) {
        if (n >= 0)
            return shiftLeft(n);
        n = -n; // Works with positive n.
        int wordShift = n < 63 ? 0 : n / 63;
        int bitShift = n - ((wordShift << 6) - wordShift); // n - wordShift * 63
        if (_size <= wordShift) // All bits have been shifted.
            return LargeInteger.ZERO;
        LargeInteger li = newInstance(_size - wordShift);
        li._size = Calculus.shiftRight(wordShift, bitShift, _words, _size,
                li._words);
        li._isNegative = _isNegative && (li._size != 0);
        return li;
    }

    /**
     * Returns the value of this large integer after multiplication by 
     * a power of ten. For example:[code]
     *     LargeInteger billion = LargeInteger.ONE.times10pow(9); // 1E9
     *     LargeInteger million = billion.times10pow(-3);[/code]
     *
     * @param n the decimal exponent.
     * @return <code>this · 10<sup>n</sup></code>
     */
    public LargeInteger times10pow(int n) {
        if (this._size == 0)
            return LargeInteger.ZERO;
        if (n >= 0) {
            int eBitLength = (int) (n * DIGITS_TO_BITS);
            LargeInteger li = newInstance(_size + (eBitLength / 63) + 1);
            li._isNegative = _isNegative;
            final int wordShift = n < 63 ? 0 : n / 63;
            final int bitShift = n - wordShift * 63;
            while (n != 0) { // Multiplies by 5^n
                int i = (n >= LONG_POW_5.length) ? LONG_POW_5.length - 1 : n;
                li._size = Calculus.multiply(li._words, li._size,
                        LONG_POW_5[i], li._words);
                n -= i;
            }
            // Multiplies by 2^n
            li._size = Calculus.shiftLeft(wordShift, bitShift, li._words,
                    li._size, li._words);
            return li;
        } // Else n < 0
        n = -n;
        // Divides by 2^n
        final int wordShift = n < 63 ? 0 : n / 63;
        final int bitShift = n - wordShift * 63;
        if (_size <= wordShift) // All bits would be shifted. 
            return LargeInteger.ZERO;
        LargeInteger li = newInstance(_size - wordShift);
        int size = Calculus.shiftRight(wordShift, bitShift, _words, _size,
                li._words);
        while (n != 0) { // Divides by 5^n
            int i = (n >= INT_POW_5.length) ? INT_POW_5.length - 1 : n;
            Calculus.divide(li._words, size, INT_POW_5[i], li._words);
            size = ((size > 0) && (li._words[size - 1] == 0L)) ? size - 1
                    : size;
            n -= i;
        }
        li._isNegative = _isNegative && (size != 0);
        li._size = size;
        return li;
    }

    private static double DIGITS_TO_BITS = MathLib.LOG10 / MathLib.LOG2;

    private static int[] INT_POW_5 = new int[] { 1, 5, 25, 125, 625, 3125,
            15625, 78125, 390625, 1953125, 9765625, 48828125, 244140625,
            1220703125 };

    private static long[] LONG_POW_5 = new long[] { 1L, 5L, 25L, 125L, 625L,
            3125L, 15625L, 78125L, 390625L, 1953125L, 9765625L, 48828125L,
            244140625L, 1220703125L, 6103515625L, 30517578125L, 152587890625L,
            762939453125L, 3814697265625L, 19073486328125L, 95367431640625L,
            476837158203125L, 2384185791015625L, 11920928955078125L,
            59604644775390625L, 298023223876953125L, 1490116119384765625L,
            7450580596923828125L };

    /**
     * Returns the decimal text representation of this number.
     *
     * @return the text representation of this number.
     */
    public Text toText() {
        return FORMAT.get().format(this);
    }

    /**
     * Returns the text representation of this number in the specified radix.
     *
     * @param radix the radix of the representation.
     * @return the text representation of this number.
     */
    public Text toText(int radix) {
        TextBuilder tmp = TextBuilder.newInstance();
        Format.INSTANCE.format(this, radix, tmp);
        Text txt = tmp.toText();
        TextBuilder.recycle(tmp);
        return txt;
    }

    /**
     * Compares this large integer against the specified object.
     * 
     * @param that the object to compare with.
     * @return <code>true</code> if the objects are the same; <code>false</code>
     *         otherwise.
     */
    public boolean equals(Object that) {
        if (!(that instanceof LargeInteger))
            return false;
        LargeInteger li = (LargeInteger) that;
        return (_size == li._size) && (_isNegative == li._isNegative)
                && (compare(_words, li._words, _size) == 0);
    }

    /**
     * Returns the hash code for this large integer number.
     * 
     * @return the hash code value.
     */
    public int hashCode() {
        long code = 0;
        for (int i = _size - 1; i >= 0; i--) {
            code = code * 31 + _words[i];
        }
        return _isNegative ? -(int) code : (int) code;
    }

    /**
     * Converts this large integer to <code>int</code>; unlike 
     * {@link #intValue} this method raises {@link ArithmeticException} if this 
     * integer cannot be represented by an <code>int</code> type.
     * 
     * @return the numeric value represented by this integer after conversion
     *         to type <code>int</code>.
     * @throws ArithmeticException if conversion to <code>int</code> is not 
     *         possible.
     */
    public int toInt() {
        if (((_size > 1) || (_words[0] > Integer.MAX_VALUE))
                && !this.equals(INT_MIN_VALUE))
            throw new ArithmeticException(this
                    + " cannot be represented as int");
        return (int) longValue();
    }

    /**
     * Converts this large integer to <code>long</code>; unlike 
     * {@link #longValue} this method raises {@link ArithmeticException} if this 
     * integer cannot be represented by a <code>long</code> type.
     * 
     * @return the numeric value represented by this integer after conversion
     *         to type <code>long</code>.
     * @throws ArithmeticException if conversion to <code>long</code> is not 
     *         possible.
     */
    public long toLong() {
        if ((_size > 1) && !this.equals(LONG_MIN_VALUE))
            throw new ArithmeticException(this
                    + " cannot be represented as long");
        return longValue();
    }

    /**
     * Returns the low order bits of this large integer as 
     * a <code>long</code>.
     * 
     * <p>Note: This conversion can lose information about the overall magnitude
     *          of the integer value and may return a result with the opposite 
     *          sign.</p>
     * 
     * @return the numeric value represented by this integer after conversion
     *         to type <code>long</code>.
     */
    public long longValue() {
        if (_size == 0)
            return 0;
        if (_size == 1)
            return _isNegative ? -_words[0] : _words[0];
        // bitLength > 63 bits.
        return _isNegative ? -((_words[1] << 63) | _words[0])
                : (_words[1] << 63) | _words[0];
    }

    /**
     * Returns the value of this large integeras a <code>double</code>.
     * 
     * @return the numeric value represented by this integer after conversion
     *         to type <code>double</code>.
     */
    public double doubleValue() {
        if (_size == 0)
            return 0.0;
        if (_size == 1)
            return _isNegative ? -_words[0] : _words[0];
        // bitLength > 63
        int bitLength = this.bitLength();
        int pow2 = bitLength - 63;
        LargeInteger int63bits = this.shiftRight(pow2);
        double d = MathLib.toDoublePow2(int63bits._words[0], pow2);
        return _isNegative ? -d : d;

    }

    /**
     * Compares two large integers numerically.
     * 
     * @param  that the integer to compare with.
     * @return -1, 0 or 1 as this integer is numerically less than, equal to,
     *         or greater than <code>that</code>.
     * @throws ClassCastException <code>that</code> is not a 
     *         large integer.
     */
    public int compareTo(LargeInteger that) {
        // Compares sign.
        if (_isNegative && !that._isNegative)
            return -1;
        if (!_isNegative && that._isNegative)
            return 1;
        // Same sign, compares size.
        if (_size > that._size)
            return _isNegative ? -1 : 1;
        if (that._size > _size)
            return _isNegative ? 1 : -1;
        // Same size.
        return _isNegative ? compare(that._words, _words, _size) : compare(
                _words, that._words, _size);
    }

    /**
     * Returns an exact copy of this large integer allocated on the 
     * stack when executing in a {@link PoolContext}).
     *
     * @return an exact copy of this large integer. 
     */
    public LargeInteger copy() {
        LargeInteger li = newInstance(_size);
        li._isNegative = _isNegative;
        li._size = _size;
        if (_size > 1) {
            System.arraycopy(_words, 0, li._words, 0, _size);
        } else {
            li._words[0] = _words[0]; // Ensures zero is correctly copied.
        }
        return li;
    }

    /**
     * Returns the integer square root of this integer.
     * 
     * @return <code>k<code> such as <code>k^2 <= this < (k + 1)^2</code>
     * @throws ArithmeticException if this integer is negative.
     */
    public LargeInteger sqrt() {
        if (this.isNegative())
            throw new ArithmeticException("Square root of negative integer");
        int bitLength = this.bitLength();
        PoolContext.enter();
        try {
            // First approximation.
            LargeInteger k = this
                    .times2pow(-((bitLength >> 1) + (bitLength & 1)));
            while (true) {
                LargeInteger newK = (k.plus(this.divide(k))).times2pow(-1);
                if (newK.equals(k))
                    return k.export();
                k = newK;
            }
        } finally {
            PoolContext.exit();
        }
    }

    ///////////////////////
    // Factory creation. //
    ///////////////////////

    private static LargeInteger newInstance(int nbrWords) {
        return (nbrWords <= 4) ? FACTORY_4.object()
                : newLargeInstance(nbrWords);
    }

    private static LargeInteger newLargeInstance(int nbrWords) {
        if (nbrWords <= 6)
            return FACTORY_6.object();
        if (nbrWords <= 10)
            return FACTORY_10.object();
        if (nbrWords <= 18)
            return FACTORY_18.object();
        if (nbrWords <= 34)
            return FACTORY_34.object();
        if (nbrWords <= 67)
            return FACTORY_67.object();
        if (nbrWords <= 132)
            return FACTORY_132.object();
        if (nbrWords <= 262)
            return FACTORY_262.object();
        if (nbrWords <= 522)
            return FACTORY_522.object();
        if (nbrWords <= 1042)
            return FACTORY_1042.object();
        return new LargeInteger(nbrWords); // Heap allocated.
    }

    private static final Factory<LargeInteger> FACTORY_4 = new Factory<LargeInteger>() {
        @Override
        protected LargeInteger create() {
            return new LargeInteger(4); // Ok for 128 bits additions.
        }
    };

    private static final Factory<LargeInteger> FACTORY_6 = new Factory<LargeInteger>() {
        @Override
        protected LargeInteger create() {
            return new LargeInteger(6); // Ok for 256 bits additions.
        }
    };

    private static final Factory<LargeInteger> FACTORY_10 = new Factory<LargeInteger>() {
        @Override
        protected LargeInteger create() {
            return new LargeInteger(10); // Ok for 512 bits additions.
        }
    };

    private static final Factory<LargeInteger> FACTORY_18 = new Factory<LargeInteger>() {
        @Override
        protected LargeInteger create() {
            return new LargeInteger(18); // Ok for 1024 bits additions.
        }
    };

    private static final Factory<LargeInteger> FACTORY_34 = new Factory<LargeInteger>() {
        @Override
        protected LargeInteger create() {
            return new LargeInteger(34); // Ok for 2048 bits additions.
        }
    };

    private static final Factory<LargeInteger> FACTORY_67 = new Factory<LargeInteger>() {
        @Override
        protected LargeInteger create() {
            return new LargeInteger(67); // Ok for 4096 bits additions.
        }
    };

    private static final Factory<LargeInteger> FACTORY_132 = new Factory<LargeInteger>() {
        @Override
        protected LargeInteger create() {
            return new LargeInteger(132); // Ok for 8192 bits additions.
        }
    };

    private static final Factory<LargeInteger> FACTORY_262 = new Factory<LargeInteger>() {
        @Override
        protected LargeInteger create() {
            return new LargeInteger(262); // Ok for 16384 bits additions.
        }
    };

    private static final Factory<LargeInteger> FACTORY_522 = new Factory<LargeInteger>() {
        @Override
        protected LargeInteger create() {
            return new LargeInteger(522); // Ok for 32768 bits additions.
        }
    };

    private static final Factory<LargeInteger> FACTORY_1042 = new Factory<LargeInteger>() {
        @Override
        protected LargeInteger create() {
            return new LargeInteger(1042); // Ok for 65536 bits additions.
        }
    };

    private static final long serialVersionUID = 1L;

    /**
     * This class represents a multi-radix format for {@link LargeInteger}.
     */
    static final class Format extends TextFormat<LargeInteger> {

        private static Format INSTANCE = new Format();

        Appendable format(LargeInteger li, int radix, Appendable out) {
            return null;
        }

        LargeInteger parse(CharSequence csq, int radix, Cursor cursor) {
            return null;
        }

        @Override
        public Appendable format(LargeInteger li, Appendable out)
                throws IOException {
            if (li.isNegative()) {
                out.append('-');
            }
            LargeInteger tmp = li.copy();
            write(tmp, out);
            return out;
        }

        // Recursive, writes 9 digits at a time.
        private void write(LargeInteger li, Appendable out) throws IOException {
            if ((li._size <= 1)) { // Direct long formatting.
                TypeFormat.format(li._words[0], out);
                return;
            }
            int rem = (int) Calculus.divide(li._words, li._size, 1000000000,
                    li._words);
            li._size = (li._words[li._size - 1] == 0L) ? li._size - 1
                    : li._size;
            write(li, out);
            for (int i = MathLib.digitLength(rem); i < 9; i++) {
                out.append('0');
            }
            TypeFormat.format(rem, out);
        }

        @Override
        public LargeInteger parse(CharSequence csq, Cursor cursor) {
            final int end = cursor.getEndIndex();
            boolean isNegative = cursor.at('-', csq);
            cursor.increment(isNegative || cursor.at('+', csq) ? 1 : 0);
            LargeInteger li = ZERO.copy();
            while (true) { // Reads up to 18 digits at a time.
                int start = cursor.getIndex();
                cursor.setEndIndex(MathLib.min(start + 18, end));
                long l = TypeFormat.parseLong(csq, 10, cursor);
                LargeInteger tmp = li.times10pow(cursor.getIndex() - start);
                li = tmp.plus(l);
                if (cursor.getIndex() == end)
                    break; // Reached end.
                char c = csq.charAt(cursor.getIndex());
                if ((c < '0') || (c > '9'))
                    break; // No more digit.
            }
            cursor.setEndIndex(end); // Restore end index.
            li._isNegative = isNegative && (li._size != 0);
            return li;
        }
    }

    /**
     * The large integer representing the minimum value representable as int.
     */
    private static final LargeInteger INT_MIN_VALUE = LargeInteger.ONE
            .shiftLeft(31).opposite().moveHeap();

    /**
     * The large integer representing the minimum value representable as long.
     */
    private static final LargeInteger LONG_MIN_VALUE = LargeInteger.ONE
            .shiftLeft(63).opposite().moveHeap();

}