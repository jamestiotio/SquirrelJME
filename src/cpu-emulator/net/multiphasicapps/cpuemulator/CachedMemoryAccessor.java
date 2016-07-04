// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.cpuemulator;

/**
 * This is a memory accessor which simulates being behind an instruction and
 * data cache.
 *
 * @since 2016/07/04
 */
public class CachedMemoryAccessor
	extends MemoryAccessor
{
	/** The memory to access. */
	protected final CPUMemory memory;
	
	/**
	 * Initializes the cached memory accessor.
	 *
	 * @param __mem The memory to access.
	 * @throws NullPointerException On null arguments
	 * @since 2016/07/04
	 */
	public CachedMemoryAccessor(CPUMemory __mem)
		throws NullPointerException
	{
		// Check
		if (__mem == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.memory = __mem;
	}
}

