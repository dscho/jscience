/*
 * jScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2004 - The jScience Consortium (http://jscience.org/)
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation (http://www.gnu.org/copyleft/lesser.html); either version
 * 2.1 of the License, or any later version.
 */
package org.jscience.mathematics.functions;

/**
 * <p> Thrown when a function operation cannot be performed.</p>
 *
 * @author  <a href="mailto:jean-marie@dautelle.com">Jean-Marie Dautelle</a>
 * @version 1.0, October 24, 2004
 */
public class FunctionException extends RuntimeException {

    /**
     * Constructs a {@link FunctionException} with no detail message.
     */
    public FunctionException() {
        super();
    }

    /**
     * Constructs a {@link FunctionException} with the specified message.
     *
     * @param  message the message.
     */
    public FunctionException(String message) {
        super(message);
    }

    private static final long serialVersionUID = -3146391701681108513L;
}