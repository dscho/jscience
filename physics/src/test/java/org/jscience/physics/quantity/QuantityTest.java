/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2010 - JScience (http://jscience.org/)
 * All rights reserved.
 *
 * Permission to use, copy, modify, and distribute this software is
 * freely granted, provided that this notice is preserved.
 */
package org.jscience.physics.quantity;

import static org.jscience.physics.unit.SI.*;

import java.math.BigInteger;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests the {@link Quantity} implementations.
 *
 * @author Martin Desruisseaux
 */
public class QuantityTest {

    /**
     * Tests the creation quantities backed by the {@code double} primitive type.
     */
    @Test
    public void testDouble() {
        final QuantityFactory<Length> factory = QuantityFactory.getInstance(Length.class);
        final Length length = factory.create(4.0, METRE);

        assertSame  ("Wrong tuple element.", METRE, length.getUnit());
        assertEquals("Wrong tuple element.", Double.valueOf(4.0), length.getValue());
        assertEquals("Wrong conversion.", 4.0,   length.doubleValue(METRE), 0.0);
        assertEquals("Wrong conversion.", 400.0, length.doubleValue(CENTI(METRE)), 0.0);

        final Length other = factory.create(8.0, METRE);
        assertSame ("Expected same implementation class.", length.getClass(), other.getClass());
        assertFalse("Quantities shall not be equal.", length.equals(other));
        assertFalse("Quantities shall not be equal.", length.hashCode() == other.hashCode());

        final Length equivalent = factory.create(4.0, METRE);
        assertSame  ("Expected same implementation class.", length.getClass(), equivalent.getClass());
        assertFalse ("Quantities shall not be equal.", equivalent.equals(other));
        assertTrue  ("Quantities shall be equal.", equivalent.equals(length));
        assertTrue  ("'equals' shall be symmetric.", length.equals(equivalent));
        assertEquals("Expected same hash code.", length.hashCode(), equivalent.hashCode());
    }

    /**
     * Tests the creation quantities backed by the {@link Number} class.
     */
    @Test
    public void testNumber() {
        final QuantityFactory<Length> factory = QuantityFactory.getInstance(Length.class);
        final BigInteger value  = BigInteger.valueOf(4);
        final Length length = factory.create(value, METRE);

        assertSame  ("Wrong tuple element.", METRE, length.getUnit());
        assertEquals ("Wrong tuple element.", BigInteger.valueOf(4), length.getValue());
        assertEquals("Wrong conversion.", 4.0,   length.doubleValue(METRE), 0.0);
        assertEquals("Wrong conversion.", 400.0, length.doubleValue(CENTI(METRE)), 0.0);

        // Quantity equivalent to 'length', but backed by a double.
        // This is not the same class, but should nevertheless by considered equivalent.
        final Length equivalent = factory.create(4, METRE);
        assertTrue   ("Quantities shall be equal.", equivalent.equals(length));
        assertTrue   ("'equals' shall be symmetric.", length.equals(equivalent));
        assertEquals ("Expected same hash code.", length.hashCode(), equivalent.hashCode());
    }
}
