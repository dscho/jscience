/*
 * jScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2004 - The jScience Consortium (http://jscience.org/)
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation (http://www.gnu.org/copyleft/lesser.html); either version
 * 2.1 of the License, or any later version.
 */
package org.jscience.mathematics.matrices;

import javolution.realtime.Realtime;

/**
 * <p> This interface abstracts the fundamental arithmetic operations:
 *     plus (+), times (*), opposite (-) and reciprocal (1/).</p>
 * <p> If the set of objects implementing this interface is commutative under
 *     addition and associative under multiplication and the two operations are
 *     related by distributive laws, then it forms a mathematical ring (linear
 *     algebra). System of linear equations involving these objects can be
 *     resolved using the {@link Matrix} class.</p>
 * <p> Typically, {@link Operable} classes provide also non-generic
 *     operations as well. Such as: <code>add, subtract, negate, multiply,
 *     divide</code> and <code>inverse</code>.
 *
 * @author  <a href="mailto:jean-marie@dautelle.com">Jean-Marie Dautelle</a>
 * @version 1.0, October 24, 2004
 */
public interface Operable extends Realtime {

    /**
     * Returns the sum of this object with the one specified.
     *
     * @param  that the object to be added.
     * @return <code>this + that</code>.
     */
    Operable plus(Operable that);

    /**
     * Returns the additive inverse of this object. It is the object such as
     * <code>this.plus(this.opposite()) == ZERO</code>,
     * with <code>ZERO</code> being the additive identity.
     *
     * @return <code>-this</code>.
     */
    Operable opposite();

    /**
     * Returns the product of this object with the one specified.
     *
     * @param  that the object multiplier.
     * @return <code>this * that</code>.
     */
    Operable times(Operable that);

    /**
     * Returns the multiplicative inverse of this object. It it the object
     * such as <code>this.times(this.reciprocal()) == ONE </code>,
     * with <code>ONE</code> being the multiplicative identity.
     *
     * @return <code>ONE / this</code>.
     */
    Operable reciprocal();

}