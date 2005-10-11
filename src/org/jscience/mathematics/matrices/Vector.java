/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2005 - JScience (http://jscience.org/)
 * All rights reserved.
 * 
 * Permission to use, copy, modify, and distribute this software is
 * freely granted, provided that this notice is preserved.
 */
package org.jscience.mathematics.matrices;

import java.util.Collection;
import java.util.Iterator;

import org.jscience.mathematics.numbers.Float64;

import javolution.lang.Text;
import javolution.lang.TextBuilder;
import javolution.util.FastTable;
import javolution.xml.XmlElement;
import javolution.xml.XmlFormat;

/**
 * <p> This class represents an immutable element of a vector space arranged as 
 *     single column {@link Matrix}. For example:<pre>
 *         Vector<Float64> xyz = Vector.valueOf(2.0, -3.0, 0.0);
 *    </pre></p>
 * 
 * @author  <a href="mailto:jean-marie@dautelle.com">Jean-Marie Dautelle</a>
 * @version 2.0, June 6, 2004
 * @see <a href="http://mathworld.wolfram.com/Vector.html">
 *      Vector -- from MathWorld</a> 
 */
public class Vector<O extends Operable<O>> extends Matrix<O> {

    /**
     * Overrides {@link Matrix#XML} representation for {@link Vector}
     * and its sub-classes.  This representation consists of the vector's 
     * elements as nested xml elements. For example: <pre>
     *    &lt;math:Vector&gt;
     *      &lt;math:Complex real="1.0" imaginary="0.0"/&gt;
     *      &lt;math:Complex real="0.0" imaginary="1.0"/&gt;
     *    &lt;/math:Vector&gt;</pre>
     */
    protected static final XmlFormat<Vector> XML = new XmlFormat<Vector>(
            Vector.class) {
        public void format(Vector vector, XmlElement xml) {
            for (int i = 0; i < vector.m; i++) {
                xml.add(vector.o[i]);
            }
        }

        public Vector parse(XmlElement xml) {
        	FastTable coordinates = FastTable.newInstance();
        	while (xml.hasNext()) {
        		coordinates.add(xml.getNext());
        	}
            return Vector.valueOf(coordinates);
        }
    };

    /**
     * Base constructor.
     * 
     * @param size the maximum number of elements.
     */
    Vector(int size) {
        super(size);
    }

    /**
     * Creates a vector having the specified coordinates.
     * 
     * @param coordinates the columns elements of this vector matrix.
     */
    public Vector(O... coordinates) {
        super(coordinates.length);
        this.n = 1;
        this.m = coordinates.length;
        for (int i = 0; i < m;) {
            this.o[i] = coordinates[i++];
        }
    }

    /**
     * Returns the vector corresponding to the specified coordinates.
     *
     * @param coordinates the columns elements of this vector matrix.
     * @return the vector having the specified coordinates elements.
     */
    public static <O extends Operable<O>> Vector<O> valueOf(O... coordinates) {
        Vector<O> V = newInstance(coordinates.length);
        for (int i = 0; i < V.m;) {
            V.o[i] = coordinates[i++];
        }
        return V;
    }

    /**
     * Returns the vector corresponding to the specified <code>double</code>
     * values (convenience method).
     *
     * @param  coordinates the array of <code>double</code> values.
     * @return the corresponding vector of {@link Float64} elements.
     */
    public static Vector<Float64> valueOf(double... coordinates) {
        Vector<Float64> V = newInstance(coordinates.length);
        for (int i = 0; i < V.m;) {
            V.set(i, Float64.valueOf(coordinates[i++]));
        }
        return V;
    }

    /**
     * Returns a m-dimensional vector populated from the specified collection 
     * of {@link Operable} objects.
     *
     * @param  coordinates the collection of {@link Operable} objects.
     * @return the vector having the specified elements.
     * @throws ClassCastException if any of the element is not {@link Operable}.
     */
    public static <O extends Operable<O>> Vector<O> valueOf(
            Collection<O> coordinates) {
        Vector<O> V = newInstance(coordinates.size());
        Iterator<O> iterator = coordinates.iterator();
        for (int i = 0; i < V.m;) {
            V.o[i++] = iterator.next();
        }
        return V;
    }

    /**
     * Returns the number of coordinates held by this vector.
     *
     * @return this vector dimension (number of rows).
     */
    public final int getDimension() {
        return m;
    }

    /**
     * Returns a single coordinate from this vector.
     *
     * @param  i the coordinate index (range [0..n[).
     * @return the coordinates at <code>i</code>.
     * @throws IndexOutOfBoundsException <code>(i < 0) || (i >= size())</code>
     */
    public final O get(int i) {
        if (i >= m)
            throw new IndexOutOfBoundsException("i: " + i + " (Vector[" + m
                    + "])");
        return o[i];
    }

    /**
     * Sets a element from this vector.
     *
     * @param  i the coordinate index (range [0..n[).
     * @param  coordinate the element at i.
     */
    final void set(int i, O coordinate) {
    	o[i] = coordinate;
    }
    
    /**
     * Returns the dot product of this vector with the one specified.
     *
     * @param  that the vector multiplier.
     * @return <code>this · that</code>
     * @throws MatrixException if <code>this.size() != that.size()</code> 
     * @see <a href="http://mathworld.wolfram.com/DotProduct.html">
     *      Dot Product -- from MathWorld</a>
     */
    public final O dot(Vector<O> that) {
        if (this.m != that.m)
            throw new MatrixException(this.m + " different from " + that.m);
        O sum = this.o[0].times(that.o[0]);
        for (int i = 1; i < m; i++) {
            sum = sum.plus(this.o[i].times(that.o[i]));
        }
        return sum;
    }

    /**
     * Returns the cross product of two 3-dimensional vectors.
     *
     * @param  that the vector multiplier.
     * @return <code>this x that</code>
     * @throws MatrixException if 
     *         <code>(this.size() != 3) && (that.size() != 3)</code> 
     * @see <a href="http://mathworld.wolfram.com/CrossProduct.html">
     *      Cross Product -- from MathWorld</a>
     */
    public final Vector<O> cross(Vector<O> that) {
        if ((this.m != 3) || (that.m != 3))
            throw new MatrixException(
                    "The cross product of two vectors requires "
                            + "3-dimensional vectors");
        Vector<O> V = newInstance(3);
        V.o[0] = this.o[1].times(that.o[2]).plus(
                this.o[2].times(that.o[1]).opposite());
        V.o[1] = this.o[2].times(that.o[0]).plus(
                this.o[0].times(that.o[2]).opposite());
        V.o[2] = this.o[0].times(that.o[1]).plus(
                this.o[1].times(that.o[0]).opposite());
        return V;
    }

    // Overrides.
    public Vector<O> opposite() {
        return (Vector<O>) super.opposite();
    }

    // Overrides.
    public Vector<O> plus(Vector<O> that) {
        return (Vector<O>) super.plus(that);
    }

    // Overrides.
    public Vector<O> minus(Vector<O> that) {
        return (Vector<O>) super.minus(that);
    }

    // Overrides.
    public Vector<O> times(O k) {
        return (Vector<O>) super.times(k);
    }

    /**
     * Returns the text representation of this vector.
     *
     * @return the text representation of this vector.
     */
    public Text toText() {
        TextBuilder cb = TextBuilder.newInstance();
        cb.append("{");
        for (int i = 0; i < m; i++) {
            if (i != 0) {
                cb.append(", ");
            }
            cb.append(o[i]);
        }
        cb.append("}");
        return cb.toText();
    }

    /**
     * Returns a m-dimensional vector (non-initialized).
     *
     * @param  m the dimension of this vector.
     * @return a m-by-1 matrix non-initialized.
     */
    static <O extends Operable<O>> Vector<O> newInstance(int n) {
        Vector V;
        if (n <= 1 << 3) {
            V = FACTORY_3.object();
        } else if (n <= 1 << 6) {
            V = FACTORY_6.object();
        } else if (n <= 1 << 9) {
            V = FACTORY_9.object();
        } else if (n <= 1 << 12) {
            V = FACTORY_12.object();
        } else if (n <= 1 << 15) {
            V = FACTORY_15.object();
        } else if (n <= 1 << 18) {
            V = FACTORY_18.object();
        } else if (n <= 1 << 21) {
            V = FACTORY_21.object();
        } else if (n <= 1 << 24) {
            V = FACTORY_24.object();
        } else if (n <= 1 << 27) {
            V = FACTORY_27.object();
        } else if (n <= 1 << 30) {
            V = FACTORY_30.object();
        } else {
            throw new UnsupportedOperationException("Vector too large");
        }
        V.m = n;
        V.n = 1;
        return V;
    }

    // TBD: Use recursive structures (see Matrix).
    //

    private static final Factory<Vector> FACTORY_3 = new Factory<Vector>() {
        protected Vector create() {
            return new Vector(1 << 3);
        }
    };

    private static final Factory<Vector> FACTORY_6 = new Factory<Vector>() {
        protected Vector create() {
            return new Vector(1 << 6);
        }
    };

    private static final Factory<Vector> FACTORY_9 = new Factory<Vector>() {
        protected Vector create() {
            return new Vector(1 << 9);
        }
    };

    private static final Factory<Vector> FACTORY_12 = new Factory<Vector>() {
        protected Vector create() {
            return new Vector(1 << 12);
        }
    };

    private static final Factory<Vector> FACTORY_15 = new Factory<Vector>() {
        protected Vector create() {
            return new Vector(1 << 15);
        }
    };

    private static final Factory<Vector> FACTORY_18 = new Factory<Vector>() {
        protected Vector create() {
            return new Vector(1 << 18);
        }
    };

    private static final Factory<Vector> FACTORY_21 = new Factory<Vector>() {
        protected Vector create() {
            return new Vector(1 << 21);
        }
    };

    private static final Factory<Vector> FACTORY_24 = new Factory<Vector>() {
        protected Vector create() {
            return new Vector(1 << 24);
        }
    };

    private static final Factory<Vector> FACTORY_27 = new Factory<Vector>() {
        protected Vector create() {
            return new Vector(1 << 27);
        }
    };

    private static final Factory<Vector> FACTORY_30 = new Factory<Vector>() {
        protected Vector create() {
            return new Vector(1 << 30);
        }
    };

    private static final long serialVersionUID = 1L;
}