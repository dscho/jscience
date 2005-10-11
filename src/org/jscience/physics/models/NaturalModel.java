/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2005 - JScience (http://jscience.org/)
 * All rights reserved.
 * 
 * Permission to use, copy, modify, and distribute this software is
 * freely granted, provided that this notice is preserved.
 */
package org.jscience.physics.models;

import static org.jscience.physics.units.Dimension.*;
import javolution.lang.MathLib;

import org.jscience.physics.units.Converter;
import org.jscience.physics.units.MultiplyConverter;
import org.jscience.physics.units.SI;

/**
 * This class represents the natural model.
 *
 * @author  <a href="mailto:jean-marie@dautelle.com">Jean-Marie Dautelle</a>
 * @version 1.0, October 24, 2004
 */
public final class NaturalModel extends PhysicalModel {

	/**
	 * Holds the single instance of this class.
	 */
	private final static NaturalModel INSTANCE = new NaturalModel();

	/**
	 * Selects the natural model as the current model.
	 */
	public static void select() {
		INSTANCE.setPhysicalDimensions();
		PhysicalModel.setCurrent(INSTANCE);
	}

	/**
	 * Sets the dimensional units of the seven base quantities as follow:
	 * <ul>
	 * <li>{@link org.jscience.physics.quantities.Length} : <code>"1"</code></li>
	 * <li>{@link org.jscience.physics.quantities.Mass} : <code>"1"</code></li>
	 * <li>{@link org.jscience.physics.quantities.Duration Duration} : <code>"1"</code></li>
	 * <li>{@link org.jscience.physics.quantities.ElectricCurrent ElectricCurrent} : <code>"1"</code></li>
	 * <li>{@link org.jscience.physics.quantities.Temperature Temperature} : <code>"1"</code></li>
	 * <li>{@link org.jscience.physics.quantities.AmountOfSubstance AmountOfSubstance} : <code>"mol"</code></li>
	 * <li>{@link org.jscience.physics.quantities.LuminousIntensity LuminousIntensity} : <code>"cd"</code></li>
	 * </ul>
	 *
	 * @see     org.jscience.physics.units.BaseUnit#setDimension
	 */
	protected final void setPhysicalDimensions() {

		// H_BAR (SECOND * JOULE = SECOND * (KILOGRAM / C^2 )) = 1
		// SPEED_OF_LIGHT (METER / SECOND) = 1
		// BOLTZMANN (JOULE / KELVIN = (KILOGRAM / C^2 ) / KELVIN) = 1
		// MAGNETIC CONSTANT (NEWTON / AMPERE^2) = 1
		// GRAVITATIONAL CONSTANT (METER^3 / KILOGRAM / SECOND^2) = 1
		SI.SECOND.setDimension(NONE, new MultiplyConverter((c * c)
				* MathLib.sqrt(c / (hBar * G))));
		SI.METER.setDimension(NONE, new MultiplyConverter(c
				* MathLib.sqrt(c / (hBar * G))));
		SI.KILOGRAM.setDimension(NONE, new MultiplyConverter(MathLib.sqrt(G
				/ (hBar * c))));
		SI.KELVIN.setDimension(NONE, new MultiplyConverter(k
				* MathLib.sqrt(G / (hBar * c)) / (c * c)));
		SI.AMPERE.setDimension(NONE, new MultiplyConverter(MathLib.sqrt(µ0 * G)
				/ (c * c)));
		SI.MOLE.setDimension(AMOUNT_OF_SUBSTANCE, Converter.IDENTITY);
		SI.CANDELA.setDimension(LUMINOUS_INTENSITY, Converter.IDENTITY);
	}
}
