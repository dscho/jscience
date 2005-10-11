/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2005 - JScience (http://jscience.org/)
 * All rights reserved.
 * 
 * Permission to use, copy, modify, and distribute this software is
 * freely granted, provided that this notice is preserved.
 */
package org.jscience.physics.units;

/**
 * Signals that a problem of some sort has occurred either when creating a
 * converter between two units or during the conversion itself.
 *
 * @author  <a href="mailto:jean-marie@dautelle.com">Jean-Marie Dautelle</a>
 * @version 1.0, October 24, 2004
 */
public class ConversionException extends RuntimeException {

    /**
     * Constructs a <code>ConversionException</code> with no detail message.
     */
    public ConversionException() {
        super();
    }

    /**
     * Constructs a <code>ConversionException</code> with the specified detail
     * message.
     *
     * @param  message the detail message.
     */
    public ConversionException(String message) {
        super(message);
    }

    private static final long serialVersionUID = 1L;
}