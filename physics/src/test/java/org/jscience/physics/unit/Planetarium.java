/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2010 - JScience (http://jscience.org/)
 * All rights reserved.
 *
 * Permission to use, copy, modify, and distribute this software is
 * freely granted, provided that this notice is preserved.
 */
package org.jscience.physics.unit;

import static org.jscience.physics.util.TestUtil.print;

import org.junit.Test;


/**
 * @author Werner Keil
 *
 */
public class Planetarium {

	@Test
	public void testPlanets() {
		Planet[] solarSystem = Planet.values();
		
		for (Planet planet : solarSystem) {
			print(planet);
		}
	}
}
