/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2006 - JScience (http://jscience.org/)
 * All rights reserved.
 * 
 * Permission to use, copy, modify, and distribute this software is
 * freely granted, provided that this notice is preserved.
 */
package org.jscience.physics.measures;

import javax.quantities.Quantity;
import javax.units.Unit;
import org.jscience.mathematics.structures.VectorSpaceNormed;
import org.jscience.mathematics.vectors.DimensionException;
import org.jscience.mathematics.vectors.Vector;

/**
 * <p> This class represents a measure vector.</p>
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
     * Holds the measurements.
     */
    private Measure<Q>[] _measures;

    /**
     * Holds the vector dimension..
     */
    private int _dimension;

    /**
     * Returns a measurement vector holding the specified measures.
     *
     * @param measures the vector elements.
     * @return the vector having the specified elements.
     */
    public static <Q extends Quantity> MeasureVector<Q> valueOf(
            Measure<Q>... measures) {
        final int d = measures.length;
        MeasureVector<Q> v = MeasureVector.newInstance(d);
        for (int i = 0; i < d; i++) {
            v._measures[i] = measures[i];
        }
        return v;
    }

    /**
     * Returns the exact measurement vector holding the specified values stated 
     * in the specified unit.
     *
     * @param unit the unit in which the values are stated.
     * @param values the exact values stated in the specified unit.
     * @return the corresponding measurement vector.
     */
    public static <Q extends Quantity> MeasureVector<Q> valueOf(Unit<Q> unit,
            long... values) {
        final int d = values.length;
        MeasureVector<Q> v = MeasureVector.newInstance(d);
        for (int i = 0; i < d; i++) {
            v._measures[i] = Measure.valueOf(values[i], unit);
        }
        return v;
    }

    /**
     * Returns the approximate measurement vector holding the specified values
     * stated in the specified unit.
     *
     * @param unit the unit in which the values are stated.
     * @param values the approximate values stated in the specified unit.
     * @return the corresponding measurement vector.
     */
    public static <Q extends Quantity> MeasureVector<Q> valueOf(Unit<Q> unit,
            double... values) {
        final int d = values.length;
        MeasureVector<Q> v = MeasureVector.newInstance(d);
        for (int i = 0; i < d; i++) {
            v._measures[i] = Measure.valueOf(values[i], unit);
        }
        return v;
    }

    @Override
    public final int getDimension() {
        return _dimension;
    }

    @Override
    public final Measure<Q> get(int i) {
        if (i >= _dimension)
            throw new ArrayIndexOutOfBoundsException();
        return _measures[i];
    }

    @Override
    public MeasureVector<Q> plus(Vector<Measure<?>> that) {
        if (that.getDimension() != _dimension)
            throw new DimensionException();
        MeasureVector<Q> v = MeasureVector.newInstance(_dimension);
        for (int i = 0; i < _dimension; i++) {
            v._measures[i] = _measures[i].plus(that.get(i));
        }
        return v;
    }

    @Override
    public MeasureVector<Q> opposite() {
        MeasureVector<Q> v = MeasureVector.newInstance(_dimension);
        for (int i = 0; i < _dimension; i++) {
            v._measures[i] = _measures[i].opposite();
        }
        return v;
    }

    @Override
    public MeasureVector<Q> minus(Vector<Measure<?>> that) {
        if (that.getDimension() != _dimension)
            throw new DimensionException();
        MeasureVector<Q> v = MeasureVector.newInstance(_dimension);
        for (int i = 0; i < _dimension; i++) {
            v._measures[i] = _measures[i].minus(that.get(i));
        }
        return v;
    }

    /**
     * Returns the Euclidian norm of this vector (square root of the 
     * dot product of this vector and itself).
     *
     * @return <code>sqrt(this · this)</code>.
     */
    @SuppressWarnings("unchecked")
    public Measure<Q> norm() {
        Measure<?> normSquared = _measures[0].times(_measures[0]);
        for (int i = 1; i < _dimension; i++) {
            normSquared = normSquared.plus(_measures[i].times(_measures[i]));
        }
        return (Measure<Q>) normSquared.sqrt();
    }

    ///////////////////////
    // Factory creation. //
    ///////////////////////

    @SuppressWarnings("unchecked")
    static <Q extends Quantity> MeasureVector<Q> newInstance(int dimension) {
        MeasureVector<Q> v = FACTORY.object();
        if ((v._measures == null) || (v._measures.length < dimension)) {
            v._measures = (Measure<Q>[]) new Measure[dimension];
        }
        v._dimension = dimension;
        return v;
    }

    // TODO Use different factories for large vectors.
    private static Factory<MeasureVector> FACTORY = new Factory<MeasureVector>() {
        protected MeasureVector create() {
            return new MeasureVector();
        }
    };

    private MeasureVector() {
    }

    private static final long serialVersionUID = 1L;

}