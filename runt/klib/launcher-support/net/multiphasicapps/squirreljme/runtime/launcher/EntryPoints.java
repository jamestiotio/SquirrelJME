// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.runtime.launcher;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import net.multiphasicapps.tool.manifest.JavaManifest;
import net.multiphasicapps.tool.manifest.JavaManifestAttributes;
import net.multiphasicapps.tool.manifest.JavaManifestKey;

/**
 * This parses the entry points which are available for usage.
 *
 * @since 2017/08/20
 */
public class EntryPoints
	extends AbstractList<EntryPoint>
{
	/** Available entry points. */
	private final EntryPoint[] _entrypoints;
	
	/**
	 * Initializes the entry points the hosted launcher can find.
	 *
	 * @param __attr The attributes to parse.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/08/20
	 */
	public EntryPoints(JavaManifestAttributes __attr)
		throws NullPointerException
	{
		// Check
		if (__attr == null)
			throw new NullPointerException("NARG");
		
		// Target list
		List<EntryPoint> target = new ArrayList<>();
		
		// Parse main class first
		String oldclass = __attr.get(new JavaManifestKey("Main-Class"));
		if (oldclass != null)
			target.add(new EntryPoint("Command Line", oldclass, false));
		
		// Parse MIDlet identifiers next
		for (int i = 1; i >= 1; i++)
		{
			// These are always in sequence
			String midletval = __attr.get(new JavaManifestKey(
				String.format("MIDlet-%d", i)));
			if (midletval == null)
				break;
			
			// The MIDlet field is in 3 fields: name, icon, class
			// {@squirreljme.error CM01 Expected two commas in the MIDlet
			// field.}
			int pc = midletval.indexOf(','),
				sc = midletval.indexOf(',', Math.max(pc + 1, 0));
			if (pc < 0 || sc < 0)
				throw new RuntimeException("CM01");
		
			// Split fields
			target.add(new EntryPoint(midletval.substring(0, pc).trim(),
				midletval.substring(sc + 1).trim(), true));
		}
		
		// Finalize
		EntryPoint[] entrypoints = target.<EntryPoint>toArray(
			new EntryPoint[target.size()]);
		Arrays.sort(entrypoints);
		this._entrypoints = entrypoints;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/08/20
	 */
	@Override
	public EntryPoint get(int __i)
	{
		return this._entrypoints[__i];
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/08/20
	 */
	@Override
	public int size()
	{
		return this._entrypoints.length;
	}
}

