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