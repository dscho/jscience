/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2010 - JScience (http://jscience.org/)
 * All rights reserved.
 *
 * Permission to use, copy, modify, and distribute this software is
 * freely granted, provided that this notice is preserved.
 */
package org.jscience.physics.util;

import static org.jscience.physics.unit.SI.KILOGRAM;
import static org.jscience.physics.unit.SI.METRE;

import org.jscience.physics.quantity.Length;
import org.jscience.physics.quantity.Mass;
import org.jscience.physics.quantity.QuantityFactory;

/**
 * @author <a href="mailto:desruisseaux@users.sourceforge.net">Martin Desruisseaux</a>
 * @author  <a href="mailto:jsr275@catmedia.us">Werner Keil</a>
 */
public class Demo {

    private static Length getSomeLength() {
        return QuantityFactory.getInstance(Length.class).create(20, METRE);
    }

    private static Mass getSomeMass() {
        return QuantityFactory.getInstance(Mass.class).create(30, KILOGRAM);
    }

    public static void main(String[] args) {
        Length someLength = getSomeLength();
        System.out.println("toString = " + someLength);
        System.out.println();

        Mass someMass = getSomeMass();
        System.out.println("toString = " + someMass);
    }
}
