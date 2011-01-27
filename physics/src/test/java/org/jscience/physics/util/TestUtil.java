/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2010 - JScience (http://jscience.org/)
 * All rights reserved.
 *
 * Permission to use, copy, modify, and distribute this software is
 * freely granted, provided that this notice is preserved.
 */
package org.jscience.physics.util;

/**
 * A static helper class, checking e.g. if some tests require optional console output
 * XXX this could be done using a logging framework eventually
 */
public abstract class TestUtil {
    private static final String TEST_CONSOLE_OUTPUT = "testConsoleOutput";

    public static final boolean isTestOutput() {
       return ("true".equals(System.getProperty(TEST_CONSOLE_OUTPUT)));
    }
    public static final void print(String message) {
        if (isTestOutput()) {
            System.out.println(message);
        }
    }
    public static final void print(Object object) {
    	print(String.valueOf(object));
    }
}
