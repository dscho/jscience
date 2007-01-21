/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2007 - JScience (http://jscience.org/)
 * All rights reserved.
 * 
 * Permission to use, copy, modify, and distribute this software is
 * freely granted, provided that this notice is preserved.
 */
package org.jscience.mathematics.numbers;

/**
 * <p> This class holds utilities on array of positive <code>long</code> 
 *     arrays.</p>
 *     
 * @author <a href="mailto:jean-marie@dautelle.com">Jean-Marie Dautelle</a>
 * @version 3.3, January 14, 2006
 */
final class Calculus {

    /**
     * Default constructor (private for utilities).
     */
    private Calculus() {
    }

    static final long MASK_63 = 0x7FFFFFFFFFFFFFFFL;

    static final long MASK_32 = 0xFFFFFFFFL;

    static final long MASK_31 = 0x7FFFFFFFL;

    static final long MASK_8 = 0xFFL;
        
    /**
     * Preconditions: xSize != 0, y >= 0
     * @return z size
     */
    static int add(long[] x, int xSize, long y, long[] z) {
        long sum = x[0] + y;
        z[0] = sum & MASK_63;
        sum >>>= 63;
        int i = 1;
        while (true) {
            if (sum == 0) {
                if (z == x) return xSize; // No need to copy.
                while (i < xSize) {
                    z[i] = x[i++];
                }
                return xSize;
            }
            if (i == xSize) {
                z[xSize] = sum;
                return xSize + 1;
            }
            sum += x[i];
            z[i++] = sum & MASK_63;
            sum >>>= 63;
        }
    }

    /**
     * Preconditions: xSize >= ySize
     * @return z size
     */
    static int add(long[] x, int xSize, long[] y, int ySize, long[] z) {
        long sum = 0;
        int i = 0;
        while (i < ySize) {
            sum += x[i] + y[i];
            z[i++] = sum & MASK_63;
            sum >>>= 63;
        }
        while (true) {
            if (sum == 0) {
                if (z == x) return xSize; // No need to copy.
                while (i < xSize) {
                       z[i] = x[i++];
                }
                return xSize;
            }
            if (i == xSize) {
                z[xSize] = sum;
                return xSize + 1;
            }
            sum += x[i];
            z[i++] = sum & MASK_63;
            sum >>>= 63;
        }
    }

    /**
     * Preconditions: xSize !=0, x >= y
     * @return z size
     */
    static int subtract(long[] x, int xSize, long y, long[] z) {
        long diff = x[0] - y;
        z[0] = diff & MASK_63;
        diff >>= 63; // Equals to -1 if borrow.
        int i = 1;
        while (diff != 0) {
            diff += x[i];
            z[i++] = diff & MASK_63;
            diff >>= 63; // Equals to -1 if borrow.
        }
        if (x != z) { // Copies rest of x to z.
            while (i < xSize) {
                z[i] = x[i++];
            }
        }
        for (int j=xSize; j > 0;) { // Calculates size.
            if (z[--j] != 0) return j + 1;
        }
        return 0;
    }

    /**
     * Preconditions: x >= y
     * @return z size
     */
    static int subtract(long[] x, int xSize, long[] y, int ySize,
            long[] z) {
        long diff = 0;
        int i = 0;
        while (i < ySize) {
            diff += x[i] - y[i];
            z[i++] = diff & MASK_63;
            diff >>= 63; // Equals to -1 if borrow.
        }
        while (diff != 0) {
            diff += x[i];
            z[i++] = diff & MASK_63;
            diff >>= 63; // Equals to -1 if borrow.
        }
        if (x != z) { // Copies rest of x to z.
            while (i < xSize) {
                z[i] = x[i++];
            }
        }
        for (int j=xSize; j > 0;) { // Calculates size.
            if (z[--j] != 0) return j + 1;
        }
        return 0;
    }

    /**
     * Preconditions: N/A
     * @return 1, -1, 0 
     */
    static int compare(long[] left, long[] right, int size) {
        for (int i = size; --i >= 0;) {
            if (left[i] > right[i]) {
                return 1;
            } else if (left[i] < right[i]) {
                return -1;
            }
        }
        return 0;
    }

    /**
     * Preconditions: xSize != 0
     * @return z size 
     */
    static int shiftLeft(int wordShift, int bitShift, long[] x,
            int xSize, long[] z) {
        final int shiftRight = 63 - bitShift;
        int i = xSize;
        int j = xSize + wordShift;
        long tmp = x[--i];
        long high = tmp >>> shiftRight;
        if (high != 0) {
            z[j] = high;
        }
        while (i > 0) {
            z[--j] = ((tmp << bitShift) & MASK_63)
                    | ((tmp = x[--i]) >>> shiftRight);
        }
        z[--j] = (tmp << bitShift) & MASK_63;
        while (j > 0) {
            z[--j] = 0;
        }
        return (high != 0) ? xSize + wordShift + 1 : xSize
                + wordShift;
    }

    /**
     * Preconditions: xSize > wordShift
     * @return z size 
     */
    static int shiftRight(int wordShift, int bitShift, long[] x,
            int xSize, long[] z) {
        final int shiftLeft = 63 - bitShift;
        int i = wordShift;
        int j = 0;
        long tmp = x[i];
        while (i < xSize - 1) {
            z[j++] = (tmp >>> bitShift) | ((tmp = x[++i]) << shiftLeft)
                    & MASK_63;
        }
        tmp >>>= bitShift;
        z[j] = tmp;
        return (tmp != 0) ? j + 1 : j;
    }

    /**
     * Preconditions: y != 0, x != 0
     * @return z size 
     */
    static int multiply(long[] x, int xSize, long y, long[] z) {
        return multiply(x, xSize, y, z, 0);
    }

    /**
     * Preconditions: y != 0, xSize >= ySize
     * @return z size 
     */
    static int multiply(long[] x, int xSize, long[] y, int ySize, long[] z) {
        int zSize = 0;
        for (int i = 0; i < ySize;) {
            zSize = multiply(x, xSize, y[i], z, i++);
        }
        return zSize;
    }

    // Multiplies by k, add to z if shift != 0
    private static int multiply(long[] x, int xSize, long k, long[] z,
            int shift) {
        long carry = 0; // 63 bits
        long kl = k & MASK_32; // 32 bits.
        long kh = k >> 32; // 31 bits
        for (int i = 0, j = shift; i < xSize;) {
            long w = x[i++];
            long wl = w & MASK_32; // 32 bits
            long wh = w >> 32; // 31 bits

            // Lower product.
            long tmp = wl * kl; // 64 bits
            long low = (tmp & MASK_63) + carry;
            carry = (tmp >>> 63) + (low >>> 63);

            // Cross product.
            tmp = wl * kh + wh * kl; // 64 bits
            low = (low & MASK_63) + ((tmp << 32) & MASK_63);

            // Calculates new carry  
            carry += ((wh * kh) << 1 | (low >>> 63)) + (tmp >>> 31);
            
            if (shift != 0) { // Adds to z.
                low = z[j] + (low & MASK_63);
                z[j++] = low & MASK_63;
                carry += low >>> 63;
            } else { // Sets z.
                z[j++] = low & MASK_63;
            }
        }
        z[shift + xSize] = carry;
        return (carry != 0) ? shift + xSize + 1 : shift + xSize;
    }

    /**
     * Preconditions: y is 31 bits or less
     * @return remainder 
     */
    static long divide(long[] x, int xSize, long y, long[] z) {
        long r = 0;
        for (int i = xSize; i > 0;) {
            long w = x[--i];

            long wh = (r << 31) | (w >>> 32);
            long qh = wh / y;
            r = wh - qh * y;

            long wl = (r << 32) | (w & MASK_32);
            long ql = wl / y;
            r = wl - ql * y;

            z[i] = (qh << 32) | ql;
        }
        return r;
    }
    
}