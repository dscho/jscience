/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2005 - JScience (http://jscience.org/)
 * All rights reserved.
 * 
 * Permission to use, copy, modify, and distribute this software is
 * freely granted, provided that this notice is preserved.
 */
package org.jscience.mathematics.matrices;

/**
 * <p> Thrown when an operation is performed upon two matrices whose dimensions
 *     disagree.</p>
 * 
 * @author <a href="mailto:jean-marie@dautelle.com">Jean-Marie Dautelle </a>
 * @version 1.0, October 24, 2004
 */
public class MatrixException extends RuntimeException {

    /**
     * Constructs a {@link MatrixException}with no detail message.
     */
    public MatrixException() {
        super();
    }

    /**
     * Constructs a {@link MatrixException}with the specified message.
     * 
     * @param message
     *            the message.
     */
    public MatrixException(String message) {
        super(message);
    }

    private static final long serialVersionUID = -5898534450456737296L;
}