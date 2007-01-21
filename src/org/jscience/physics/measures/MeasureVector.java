/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2006 - JScience (http://jscience.org/)
 * All rights reserved.
 * 
 * Permission to use, copy, modify, and distribute this software is
 * freely granted, provided that this notice is preserved.
 */
package org.jscience.physics.measures;

import javax.measure.converters.ConversionException;
import javax.measure.quantities.Quantity;
import javax.measure.units.Unit;
import javax.realtime.MemoryArea;

import javolution.context.PoolContext;
import org.jscience.mathematics.structures.VectorSpaceNormed;
import org.jscience.mathematics.vectors.DimensionException;
import org.jscience.mathematics.vectors.Vector;

/**
 * <p> This class represents a measurement vector for which all components
 *     are of the same type.</p>
 *     
 * <p> Measure vectors are typically 2 or 3 dimensional. For example:[code]
 *     class Velocity2D extends MeasureVector<Velocity> { ... }
 *     MeasureVector<Length> xyz = MeasureVector.valueOf(METER, 2.0, -4.0, 3.0);
 *     [/code]</p>
 *     
 * @author  <a href="mailto:jean-marie@dautelle.com">Jean-Marie Dautelle</a>
 * @version 3.1, April 2, 2006
 */
public class MeasureVector<Q extends Quantity> extends Vector<Measure<?>>
        implements VectorSpaceNormed<Vector<Measure<?>>, Measure<?>> {

    /**
     * Holds the vector's elements.
     */
    Measure<Q>[] _elements;

    /**
     * Holds the vector's dimension.
     */
    int _dimension;

    /**
     * Creates a measurement vector holding the specified elements.
     *
     * @param elements the vector elements.
     */
    @SuppressWarnings("unchecked")
    public MeasureVector(Measure<Q> ... elements) {
        _dimension = elements.length;
        _elements = new Measure[_dimension];
        for (int i = 0; i < _dimension; i++) {
            _elements[i] = elements[i];
        }
    }

    /**
     * Returns a new measurement vector (potentially {@link #recycle recycled}) 
     * holding the specified elements.
     *
     * @param elements the vector elements.
     * @return the vector having the specified elements.
     * @throws DimensionException if the rows do not have the same length.
     * @see javolution.context.ObjectFactory#object()
     */
    public static <Q extends Quantity> MeasureVector<Q> valueOf(Measure<Q> ... elements) {
        int n = elements.length;
        MeasureVector<Q> V = MeasureVector.newInstance(n);
        for (int i = 0; i < n; i++) {
            V._elements[i] = elements[i];
        }
        return V;
    }

    /**
     * Returns a {@link MeasureVector} stated in the specified unit equivalent
     * to the specified vector
     *
     * @param unit the unit in which the measurements are stated.
     * @param that the vector to convert. 
     * @return <code>that</code> or new equivalent dense vector.
     */
    public static <Q extends Quantity> MeasureVector<Q> valueOf(Unit<Q> unit, Vector<Measure<?>> that) {
        int n = that.getDimension();
        MeasureVector<Q> V = MeasureVector.newInstance(n);
        for (int i = 0; i < n; i++) {
            V._elements[i] = that.get(i).to(unit);
        }
        return V;
    }
    
    /**
     * Returns the exact measurement vector holding the specified values stated 
     * in the specified unit.
     *
     * @param unit the unit in which the measurements are stated.
     * @param values the exact values stated in the specified unit.
     * @return the corresponding measurement vector.
     */
    public static <Q extends Quantity> MeasureVector<Q> valueOf(Unit<Q> unit,
            long... values) {
        final int d = values.length;
        MeasureVector<Q> V = MeasureVector.newInstance(d);
        for (int i = 0; i < d; i++) {
            V._elements[i] = Measure.valueOf(values[i], unit);
        }
        return V;
    }

    /**
     * Returns the approximate measurement vector holding the specified values
     * stated in the specified unit.
     *
     * @param unit the unit in which the measurements are stated.
     * @param values the approximate values stated in the specified unit.
     * @return the corresponding measurement vector.
     */
    public static <Q extends Quantity> MeasureVector<Q> valueOf(Unit<Q> unit,
            double... values) {
        final int d = values.length;
        MeasureVector<Q> V = MeasureVector.newInstance(d);
        for (int i = 0; i < d; i++) {
            V._elements[i] = Measure.valueOf(values[i], unit);
        }
        return V;
    }

    /**
     * Recycles the specified vector immediately.
     *
     * @param vector the vector being recycled.
     * @see javolution.context.ObjectFactory#recycle(Object)
     */
    public static void recycle(MeasureVector vector) {
        FACTORY.recycle(vector);
    }

    /**
     * Returns the Euclidian norm of this vector (square root of the 
     * dot product of this vector and itself).
     *
     * @return <code>sqrt(this · this)</code>.
     */
    @SuppressWarnings("unchecked")
    public Measure<Q> norm() {
        Measure<?> normSquared = _elements[0].times(_elements[0]);
        for (int i = 1; i < _dimension; i++) {
            normSquared = normSquared.plus(_elements[i].times(_elements[i]));
        }
        return (Measure<Q>) normSquared.sqrt();
    }

    /**
     * Returns the measurement vector equivalent to this vector but stated
     * in the specified unit.
     * 
     * @param  unit the unit of the measurements to be returned.
     * @return a measurement vector equivalent to this vector but whose 
     *         elements are stated in the specified unit.
     * @throws ConversionException if the current model does not allows for
     *         conversion to the specified unit.
     */
    @SuppressWarnings("unchecked")
    public <R extends Quantity> MeasureVector<R> to(Unit<R> unit) {
        MeasureVector<R> V = MeasureVector.newInstance(_dimension);
        for (int i = 0; i < _dimension; i++) {
            V._elements[i] = _elements[i].to(unit);
        }
        return V;
    }

    @Override
    public final int getDimension() {
        return _dimension;
    }

    @Override
    public final Measure<Q> get(int i) {
        if (i >= _dimension)
            throw new IndexOutOfBoundsException();
        return _elements[i];
    }

    @Override
    public MeasureVector<Q> opposite() {
        MeasureVector<Q> V = MeasureVector.newInstance(_dimension);
        for (int i = 0; i < _dimension; i++) {
            V._elements[i] = _elements[i].opposite();
        }
        return V;
    }

    @Override
    public MeasureVector<Q> plus(Vector<Measure<?>> that) {
        MeasureVector<Q> T = MeasureVector.valueOf(_elements[0].getUnit(), that);
        if (T._dimension != _dimension) throw new DimensionException();
        MeasureVector<Q> V = MeasureVector.newInstance(_dimension);
        for (int i = 0; i < _dimension; i++) {
            V._elements[i] = _elements[i].plus(T._elements[i]);
        }
        return V;
    }

    @SuppressWarnings("unchecked")
    @Override
    public MeasureVector<? extends Quantity> times(Measure k) {
        MeasureVector V = MeasureVector.newInstance(_dimension);
        for (int i = 0; i < _dimension; i++) {
            V._elements[i] = _elements[i].times(k);
        }
        return V;
    }

    @Override
    public Measure<?> times(Vector<Measure<?>> that) {
        MeasureVector<? extends Quantity> T = MeasureVector.valueOf(that.get(0).getUnit(), that);
        if (T._dimension != _dimension) throw new DimensionException();
        PoolContext.enter();
        try {
           Measure<?> sum = (Measure<?>) _elements[0].times(T._elements[0]);
           for (int i = 1; i < _dimension; i++) {
               sum = sum.plus(_elements[i].times(T._elements[i]));
           }
           sum.move(ObjectSpace.OUTER);
           return sum;
       } finally {
           PoolContext.exit();
       }
    }
    
    ///////////////////////
    // Factory creation. //
    ///////////////////////
    
    @SuppressWarnings("unchecked")
    static <Q extends Quantity> MeasureVector<Q> newInstance(final int dimension) {
        final MeasureVector<Q> V = FACTORY.object();
        V._dimension = dimension;
        if ((V._elements == null) || (V._elements.length < dimension)) {
            MemoryArea.getMemoryArea(V).executeInArea(new Runnable() {
                public void run() {
                    V._elements = new Measure[dimension];
                }
            });
        }
        return V;
    }

    private static Factory<MeasureVector> FACTORY = new Factory<MeasureVector>() {
        protected MeasureVector create() {
            return new MeasureVector();
        }
    };

    private MeasureVector() {
    }

    private static final long serialVersionUID = 1L;
}