/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2006 - JScience (http://jscience.org/)
 * All rights reserved.
 * 
 * Permission to use, copy, modify, and distribute this software is
 * freely granted, provided that this notice is preserved.
 */
package org.jscience.mathematics.vectors;

import java.util.Iterator;
import java.util.List;

import javax.realtime.MemoryArea;

import javolution.lang.MathLib;
import javolution.xml.XMLFormat;
import javolution.xml.stream.XMLStreamException;

import org.jscience.mathematics.numbers.Float64;
import org.jscience.mathematics.structures.VectorSpaceNormed;

/**
 * <p> This class represents an optimized {@link Vector vector} implementation
 *     for 64 bits floating point elements.</p>
 * 
 * @author <a href="mailto:jean-marie@dautelle.com">Jean-Marie Dautelle</a>
 * @version 3.3, January 2, 2007
 */
public final class Float64Vector extends Vector<Float64> implements
        VectorSpaceNormed<Vector<Float64>, Float64> {

    /**
     * Holds the default XML representation. For example:
     * [code]
     *    <Float64Vector dimension="2">
     *        <Float64 value="1.0" />
     *        <Float64 value="0.0" />
     *    </Float64Vector>[/code]
     */
    protected static final XMLFormat<Float64Vector> XML = new XMLFormat<Float64Vector>(
            Float64Vector.class) {

        @Override
        public Float64Vector newInstance(Class<Float64Vector> cls, InputElement xml)
                throws XMLStreamException {
            int dimension = xml.getAttribute("dimension", 0);
            Float64Vector V = Float64Vector.newInstance(dimension);
            V._dimension = dimension;
            return V;
        }

        @SuppressWarnings("unchecked")
        @Override
        public void read(InputElement xml, Float64Vector V)
                throws XMLStreamException {
            for (int i=0, n=V._dimension; i < n;) {
                V._values[i++] = ((Float64)xml.getNext()).doubleValue();
            }
            if (xml.hasNext()) 
                throw new XMLStreamException("Too many elements");
        }

        @Override
        public void write(Float64Vector V, OutputElement xml)
                throws XMLStreamException {
            xml.setAttribute("dimension", V._dimension);
            for (int i = 0, n=V._dimension; i < n;) {
                xml.add(V.get(i++));
            }
        }
    };

    /**
     * Holds the dimension.
     */
    int _dimension;
    
    /**
     * Holds the values.
     */
    double[] _values;

    /**
     * Returns a new vector holding the specified <code>double</code> values.
     *
     * @param values the vector values.
     * @return the vector having the specified values.
     */
    public static Float64Vector valueOf(double... values) {
        int n = values.length;
        Float64Vector V = Float64Vector.newInstance(n);
        System.arraycopy(values, 0, V._values, 0, n);
        return V;
    }

    /**
     * Returns a new vector holding the elements from the specified 
     * collection.
     *
     * @param elements the collection of floating-points numbers.
     * @return the vector having the specified elements.
     */
    public static Float64Vector valueOf(List<Float64> elements) {
        int n = elements.size();
        Float64Vector V = Float64Vector.newInstance(n);
        Iterator<Float64> iterator = elements.iterator();
        for (int i = 0; i < n; i++) {
            V._values[i] = iterator.next().doubleValue();
        }
        return V;
    }

    /**
     * Returns a {@link Float64Vector} instance equivalent to the 
     * specified vector.
     *
     * @param that the vector to convert. 
     * @return <code>that</code> or new equivalent Float64Vector.
     */
    public static Float64Vector valueOf(Vector<Float64> that) {
        if (that instanceof Float64Vector)
            return (Float64Vector) that;        
        int n = that.getDimension();
        Float64Vector V = Float64Vector.newInstance(n);
        for (int i = 0; i < n; i++) {
            V._values[i] = that.get(i).doubleValue();
        }
        return V;
    }

    /**
     * Returns the value of a floating point number from this vector (fast).
     *
     * @param  i the floating point number index.
     * @return the value of the floating point number at <code>i</code>.
     * @throws IndexOutOfBoundsException <code>(i < 0) || (i >= dimension())</code>
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
        return Float64.valueOf(normValue());
    }

    /**
     * Returns the {@link #norm()} value of this vector.
     *
     * @return <code>this.norm().doubleValue()</code>.
     */
    public double normValue() {
        double normSquared = 0;
        for (int i = _dimension; --i >= 0;) {
            double values = _values[i];
            normSquared += values * values;
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
            throw new IndexOutOfBoundsException();
        return Float64.valueOf(_values[i]);
    }

    @Override
    public Float64Vector opposite() {
        Float64Vector V = Float64Vector.newInstance(_dimension);
        for (int i = 0; i < _dimension; i++) {
            V._values[i] = - _values[i];
        }
        return V;
    }

    @Override
    public Float64Vector plus(Vector<Float64> that) {
        Float64Vector T = Float64Vector.valueOf(that);
        if (T._dimension != _dimension) throw new DimensionException();
        Float64Vector V = Float64Vector.newInstance(_dimension);
        for (int i = 0; i < _dimension; i++) {
            V._values[i] = _values[i] + T._values[i];
        }
        return V;
    }

    @Override
    public Float64Vector minus(Vector<Float64> that) {
        Float64Vector T = Float64Vector.valueOf(that);
        if (T._dimension != _dimension) throw new DimensionException();
        Float64Vector V = Float64Vector.newInstance(_dimension);
        for (int i = 0; i < _dimension; i++) {
            V._values[i] = _values[i] - T._values[i];
        }
        return V;
    }

    @Override
    public Float64Vector times(Float64 k) {
        Float64Vector V = Float64Vector.newInstance(_dimension);
        double d = k.doubleValue();
        for (int i = 0; i < _dimension; i++) {
            V._values[i] = _values[i] * d;
        }
        return V;
    }
    
    /**
     * Equivalent to <code>this.times(Float64.valueOf(k))</code>
     *
     * @param k the coefficient. 
     * @return <code>this * k</code>
     */
    public Float64Vector times(double k) {
        Float64Vector V = Float64Vector.newInstance(_dimension);
        for (int i = 0; i < _dimension; i++) {
            V._values[i] = _values[i] * k;
        }
        return V;
    }    
    
    @Override
    public Float64 times(Vector<Float64> that) {
        Float64Vector T = Float64Vector.valueOf(that);
        if (T._dimension != _dimension)
            throw new DimensionException();
        double[] T_values = T._values;
        double sum = _values[0] * T_values[0];
        for (int i = 1; i < _dimension; i++) {
            sum += _values[i] * T_values[i];
        }
        return Float64.valueOf(sum);
    }

    ///////////////////////////////
    // Package Private Utilities //
    ///////////////////////////////
    
    void set(int i, Float64 f) {
         _values[i] = f.doubleValue();
    }    

    ///////////////////////
    // Factory creation. //
    ///////////////////////

    static Float64Vector newInstance(final int dimension) {
        final Float64Vector V = FACTORY.object();
        V._dimension = dimension;
        if ((V._values == null) || (V._values.length < dimension)) {
            MemoryArea.getMemoryArea(V).executeInArea(new Runnable() {
                public void run() {
                    V._values = new double[dimension];
                }
            });
        }
        return V;
    }

    private static Factory<Float64Vector> FACTORY = new Factory<Float64Vector>() {
        protected Float64Vector create() {
            return new Float64Vector();
        }
    };

    private Float64Vector() {
    }

    private static final long serialVersionUID = 1L;
}