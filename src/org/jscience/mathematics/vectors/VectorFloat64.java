/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2006 - JScience (http://jscience.org/)
 * All rights reserved.
 * 
 * Permission to use, copy, modify, and distribute this software is
 * freely granted, provided that this notice is preserved.
 */
package org.jscience.mathematics.vectors;

import javolution.lang.MathLib;

import org.jscience.mathematics.numbers.Float64;
import org.jscience.mathematics.structures.VectorSpaceNormed;

/**
 * <p> This class represents an optimized {@link Vector vector} implementation
 *     for 64 bits floating point elements.</p>
 * 
 * @author <a href="mailto:jean-marie@dautelle.com">Jean-Marie Dautelle</a>
 * @version 3.0, February 13, 2006
 * @see <a href="http://en.wikipedia.org/wiki/Vector_space">
 *      Wikipedia: Vector Space</a>
 */
public final class VectorFloat64 extends Vector<Float64>
     implements VectorSpaceNormed<Vector<Float64>, Float64> {

    /**
     * Holds the values.
     */
    private double[] _values;

    /**
     * Holds the dimension.
     */
    private int _dimension;

    /**
     * Returns a vector holding the specified elements.
     *
     * @param elements the vector elements.
     * @return the vector having the specified elements.
     */
    static VectorFloat64 newInstance(Float64... elements) {
        VectorFloat64 v = newInstance(elements.length);
        for (int i = elements.length; --i >= 0;) {
            v._values[i] = elements[i].doubleValue();
        }
        return v;
    }

    /**
     * Returns an instance of this class equivalent to the specified
     * vector.
     *
     * @param vector the vector to copy into a {@link VectorFloat64} instance.
     * @return the vector having the values of the specified vector.
     */
    static VectorFloat64 newInstance(Vector<Float64> vector) {
        int dimension = vector.getDimension();
        VectorFloat64 v = VectorFloat64.newInstance(dimension);
        for (int i = dimension; --i >= 0;) {
            v._values[i] = vector.get(i).doubleValue();
        }
        return v; 
    }

    /**
     * Returns a vector holding the specified element values.
     *
     * @param elements the vector element values.
     * @return the vector having the specified element values.
     */
    static VectorFloat64 newInstance(double... values) {
        VectorFloat64 v = newInstance(values.length);
        System.arraycopy(values, 0, v._values, 0, values.length);
        return v;
    }

    /**
     * Returns the value of a single element from this vector (fast).
     *
     * @param  i the element index (range [0..n[).
     * @return the value of element at <code>i</code>.
     * @throws IndexOutOfBoundsException <code>(i < 0) || (i >= size())</code>
     */
    public double getValue(int i) {
        if (i >= _dimension)
            throw new ArrayIndexOutOfBoundsException();
        return _values[i];
    }

    /**
     * Returns the Euclidian norm of this vector (square root of the 
     * dot product of this vector and itself).
     *
     * @return <code>sqrt(this · this)</code>.
     */
    public Float64 norm() {
        double normSquared = 0;
        for (int i = _dimension; --i >= 0;) {
            normSquared += _values[i] * _values[i];
        }
        return Float64.valueOf(MathLib.sqrt(normSquared));
    }

    /**
     * Returns the {@link #norm()} value of this vector.
     *
     * @return <code>this.norm().doubleValue()</code>.
     */
    public double normValue() {
        double normSquared = 0;
        for (int i = _dimension; --i >= 0;) {
            normSquared += _values[i] * _values[i];
        }
        return MathLib.sqrt(normSquared);
    }

    @Override
    public int getDimension() {
        return _dimension;
    }

    @Override
    public Float64 get(int i) {
        if (i >= _dimension)
            throw new ArrayIndexOutOfBoundsException();
        return Float64.valueOf(_values[i]);
    }

    @Override
    public Float64 times(Vector<Float64> that) {
        VectorFloat64 v = (that instanceof VectorFloat64) ?
                (VectorFloat64) that : VectorFloat64.newInstance(that);
        if (v._dimension != _dimension)
            throw new DimensionException();
        double sum = _values[0] * v._values[0];
        for (int i = 1; i < _dimension; i++) {
            sum += _values[i] * v._values[i];
        }
        return Float64.valueOf(sum);
    }

    @Override
    public VectorFloat64 times(Float64 a) {
        VectorFloat64 r = newInstance(_dimension);
        for (int i = _dimension; --i >= 0;) {
            r._values[i] = _values[i] * a.doubleValue();
        }
        return r;
    }

    @Override
    public VectorFloat64 plus(Vector<Float64> that) {
        VectorFloat64 v = (that instanceof VectorFloat64) ?
                (VectorFloat64) that : VectorFloat64.newInstance(that);
        if (v._dimension != _dimension)
            throw new DimensionException();
        VectorFloat64 r = newInstance(_dimension);
        for (int i = _dimension; --i >= 0;) {
            r._values[i] = _values[i] + v._values[i];
        }
        return r;
    }

    @Override
    public VectorFloat64 minus(Vector<Float64> that) {
        VectorFloat64 v = (that instanceof VectorFloat64) ?
                (VectorFloat64) that : VectorFloat64.newInstance(that);
        if (v._dimension != _dimension)
            throw new DimensionException();
        VectorFloat64 r = newInstance(_dimension);
        for (int i = _dimension; --i >= 0;) {
            r._values[i] = _values[i] - v._values[i];
        }
        return r;
    }

    @Override
    public VectorFloat64 opposite() {
        VectorFloat64 v = newInstance(_dimension);
        for (int i = _dimension; --i >= 0;) {
            v._values[i] = -_values[i];
        }
        return v;
    }

    // Sets the specified element.
    final void set_(int i, double value) {
        _values[i] = value;
    }

    // Gets the specified element.
    final double get_(int i) {
        return _values[i];
    }
    
    ///////////////////////
    // Factory creation. //
    ///////////////////////
    
    static VectorFloat64 newInstance(int dimension) {
        VectorFloat64 vector = FACTORY.object();
        if ((vector._values == null) || (vector._values.length < dimension)) {
            vector._values = new double[dimension];
        }
        vector._dimension = dimension;
        return vector;
    }

    // TODO Use different factories for large matrices.
    private static Factory<VectorFloat64> FACTORY = new Factory<VectorFloat64>() {
        protected VectorFloat64 create() {
            return new VectorFloat64();
        }
    };

    private VectorFloat64() {
    }

}