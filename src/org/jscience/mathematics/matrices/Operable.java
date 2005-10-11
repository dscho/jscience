/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2005 - JScience (http://jscience.org/)
 * All rights reserved.
 * 
 * Permission to use, copy, modify, and distribute this software is
 * freely granted, provided that this notice is preserved.
 */
package org.jscience.mathematics.matrices;

import javolution.realtime.Realtime;

/**
 * <p> This interface abstracts the fundamental arithmetic operations:
 *     plus (+), times (*), opposite (-) and reciprocal (1/).</p>
 *     
 * <p> If the set of objects implementing this interface is commutative under
 *     addition and associative under multiplication and the two operations are
 *     related by distributive laws, then it forms a mathematical ring (linear
 *     algebra). System of linear equations involving these objects can be
 *     resolved using the {@link Matrix} class.</p>
 *
 * @author  <a href="mailto:jean-marie@dautelle.com">Jean-Marie Dautelle</a>
 * @version 2.0, June 6, 2004
 */
public interface Operable<T extends Operable<T>> extends Realtime {

    /**
     * Returns the sum of this object with the one specified.
     *
     * @param  that the object to be added.
     * @return <code>this + that</code>.
     */
     T plus(T that);

    /**
     * Returns the additive inverse of this object. It is the object such as
     * <code>this.plus(this.opposite()) == ZERO</code>,
     * with <code>ZERO</code> being the additive identity.
     *
     * @return <code>-this</code>.
     */
     T opposite();

    /**
     * Returns the product of this object with the one specified.
     *
     * @param  that the object multiplier.
     * @return <code>this * that</code>.
     */
     T times(T that);

    /**
     * Returns the multiplicative inverse of this object. It it the object
     * such as <code>this.times(this.reciprocal()) == ONE </code>,
     * with <code>ONE</code> being the multiplicative identity.
     *
     * @return <code>ONE / this</code>.
     */
     T reciprocal();

}