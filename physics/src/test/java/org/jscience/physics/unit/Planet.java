/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2010 - JScience (http://jscience.org/)
 * All rights reserved.
 *
 * Permission to use, copy, modify, and distribute this software is
 * freely granted, provided that this notice is preserved.
 */
package org.jscience.physics.unit;

import static org.jscience.physics.unit.SI.*;

/**
 * @author  <a href="mailto:jsr275@catmedia.us">Werner Keil</a>
 * @author Josh Bloch
 * @version 1.0.1, $Date$
 * 
 * This <type>enum</type> is based on Josh Bloch's example in <a href="http://java.sun.com/docs/books/effective/">Effective Java Second Edition</a>
 * 
 * <p>
 * Suppose you want to add data and behavior to an enum. 
 * For example consider the planets of the solar system. 
 * Each planet knows its mass and radius, and can calculate its surface gravity and the weight of an object on the planet. 
 * Here is how it looks:
 * </p>
 */
public enum Planet {

    MERCURY(newMass(3.303e+23, KILOGRAM), newLength(2.4397e6, METRE)),
    VENUS(newMass(4.869e+24, KILOGRAM), newLength(6.0518e6, METRE)),
    EARTH(newMass(5.976e+24, KILOGRAM), newLength(6.37814e6, METRE)),
    MARS(newMass(6.421e+23, KILOGRAM), newLength(3.3972e6, METRE)),
    JUPITER(newMass(1.9e+27, KILOGRAM), newLength(7.1492e7, METRE)),
    SATURN(newMass(5.688e+26, KILOGRAM), newLength(6.0268e7, METRE)),
    URANUS(newMass(8.686e+25, KILOGRAM), newLength(2.5559e7, METRE)),
    NEPTUNE(newMass(1.024e+26, KILOGRAM), newLength(2.4746e7, METRE)),
    PLUTO(newMass(1.27e+22, KILOGRAM), newLength(1.137e6, METRE));

    private final Mass mass;   // in kilograms

    private final Length radius; // in meters

    Planet(Mass mass, Length radius) {
        this.mass = mass;
        this.radius = radius;
    }

    public Mass getMass() {
        return mass;
    }

    public Length getRadius() {
        return radius;
    }

    // universal gravitational constant  (m3 kg-1 s-2)
    private static double G = 6.67300E-11;

    public Acceleration surfaceGravity() {
        double m = mass.doubleValue(KILOGRAM);
        double r = radius.doubleValue(METRE);
        return QuantityFactory.getInstance(Acceleration.class).create(
                G * m / (r * r), METRES_PER_SQUARE_SECOND);
    }

    private static Mass newMass(double value, PhysicsUnit<Mass> unit) {
        return QuantityFactory.getInstance(Mass.class).create(value, unit);
    }

    private static Length newLength(double value, PhysicsUnit<Length> unit) {
        return QuantityFactory.getInstance(Length.class).create(value, unit);
    }

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(super.toString());
		sb.append("; ");
		sb.append(Mass.class.getSimpleName());
		sb.append(": ");
		sb.append(getMass());
		sb.append("; ");
		sb.append("Radius: ");
		sb.append(getRadius());
		sb.append("; ");
		sb.append("Surface Gravity: ");
		sb.append(surfaceGravity());
		return sb.toString();
	}
}