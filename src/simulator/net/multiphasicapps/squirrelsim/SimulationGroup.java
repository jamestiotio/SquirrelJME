// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirrelsim;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ServiceLoader;
import net.multiphasicapps.squirreljme.jit.JITCPUEndian;
import net.multiphasicapps.squirreljme.jit.JITCPUVariant;
import net.multiphasicapps.squirreljme.jit.JITFactory;
import net.multiphasicapps.util.unmodifiable.UnmodifiableList;

/**
 * This is a simulation group which contains multiple simulations which are
 * shared under the same group. Simulations that are within the same group
 * may communicate with each other.
 *
 * Simulations part of the same group have the same "home" file system visible
 * to them although they have differing root filesystems (which are read-only
 * in most cases).
 *
 * @since 2016/06/14
 */
public class SimulationGroup
{
	/** Services which are available. */
	private static final ServiceLoader<SimulationProvider> _SERVICES =
		ServiceLoader.<SimulationProvider>load(SimulationProvider.class);
	
	/** The set of simulations which are available for running. */
	private final List<Simulation> _simulations =
		new ArrayList<>(2);
	
	/**
	 * Initializes the simulation group.
	 *
	 * @since 2016/06/14
	 */
	public SimulationGroup()
	{
	}
	
	/**
	 * Creates a new simulation with the given triplet, program, and arguments
	 * to the program. The created simulation is added to the simulation list.
	 *
	 * @param __triplet The triplet to use.
	 * @param __program The program to use.
	 * @param __args The program arguments.
	 * @return The simulation.
	 * @throws IllegalArgumentException If the triplet is unknown.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/07/04
	 */
	public final Simulation create(String __triplet, String __program,
		String... __args)
		throws IllegalArgumentException, NullPointerException
	{
		// Check
		if (__triplet == null || __program == null || __args == null)
			throw new NullPointerException("NARG");
		
		// {@squirreljme.error BV02 Triplet is not in the form of
		// {@code arch+variant,endian.os.variant}.}
		int dota = __triplet.indexOf('.');
		if (dota < 0)
			throw new IllegalArgumentException(String.format("BV02 %s",
				__triplet));
		int dotb = __triplet.indexOf('.', dota + 1);
		if (dotb < 0)
			throw new IllegalArgumentException(String.format("BV02 %s",
				__triplet));
		
		// Extract bits
		String fullarch = __triplet.substring(0, dota),
			os = __triplet.substring(dota + 1, dotb),
			osvar = __triplet.substring(dotb + 1);
		
		// Determine the chosen architecture
		int aplu = fullarch.indexOf('+'),
			acom = fullarch.indexOf(',');
		if (aplu < 0 || acom < 0 || acom < aplu)
			throw new IllegalArgumentException(String.format("BV02 %s",
				__triplet));
		
		// Extract architecture target
		String arch = fullarch.substring(0, aplu);
		JITCPUEndian archend = JITCPUEndian.of(fullarch.substring(acom + 1));
		
		// Find the 
		JITCPUVariant archvar = null;
		String rawarchvar = fullarch.substring(aplu + 1, acom);
		for (JITFactory jf : ServiceLoader.<JITFactory>load(JITFactory.class))
			if (arch.equals(jf.architectureName()))
				if (jf.supportsEndianess(archend))
					if (null != (archvar = jf.getVariant(rawarchvar)))
						break;
		
		// {@squirreljme.error BV03 No architecture variant is known by the
		// JIT for the specified architecture, or the endianess is not
		// supported by a JIT provider. (The triplet; The architecture
		// variant)}
		if (archvar == null)
			throw new IllegalArgumentException(String.format("BV03 %s %s",
				__triplet, rawarchvar));
		
		// The program is the path
		Path progpath = Paths.get(__program);
		
		// Setup start configuration
		SimulationStartConfig ssc = new SimulationStartConfig(this, arch,
			archvar, archend, os, osvar, progpath, __args);
		
		// Find the provider for this simulation and create it
		ServiceLoader<SimulationProvider> services = _SERVICES;
		SimulationStartException defer = null;
		synchronized (services)
		{
			for (SimulationProvider sp : services)
				try
				{
					// Create
					Simulation sim = sp.create(ssc);
					
					// Replace an existing null or add to the end of the
					// simulation
					List<Simulation> simulations = _simulations;
					synchronized (simulations)
					{
						int n = simulations.size();
						for (int i = 0; i <= n; i++)
							if (i == n)
								simulations.add(sim);
							else if (simulations.get(i) == null)
							{
								simulations.set(i, sim);
								break;
							}
					}
					
					// Return
					return sim;
				}
				catch (SimulationStartException e)
				{
					defer = e;
				}
		}
		
		// {@squirreljme.error BV04 No system simulates the given triplet.
		// (The triplet)}
		if (defer != null)
			throw new IllegalArgumentException(String.format("BV04 %s",
				__triplet), defer);
		else
			throw new IllegalArgumentException(String.format("BV04 %s",
				__triplet));
	}
	
	/**
	 * Goes through all simulations and runs a single cycle.
	 *
	 * @return {@code true} if there are simulations still running.
	 * @since 2016/06/15
	 */
	public final boolean runCycle()
	{
		// Infinite loop mostly
		int count = 0;
		List<Simulation> sims = this._simulations;
		synchronized (sims)
		{
			// Stop if no simulations remain
			int n = sims.size();
			if (n <= 0)
				return false;
			
			// Go through all simulations
			for (int i = 0; i < n; i++)
			{
				// Get
				Simulation sim = sims.get(i);
				
				// Run cycle, remove if it has no threads left
				if (sim != null)
				{
					if (!sim.runCycle())
						sims.set(i, null);
					else
						count++;
				}
			}
			
			// Keep going
			return (count != 0);
		}
	}
}

