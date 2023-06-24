// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.c;

import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.io.IOException;

/**
 * Array data block.
 *
 * @since 2023/05/31
 */
public class CArrayBlock
	extends CBlock
{
	/** The current array index. */
	private volatile int _index;
	
	/**
	 * Initializes the C array Block.
	 *
	 * @param __ref The reference to use.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/05/31
	 */
	CArrayBlock(CSourceWriter __ref)
		throws NullPointerException
	{
		super(__ref, "}");
	}
	
	/**
	 * Writes a struct to the array.
	 * 
	 * @return The struct block.
	 * @throws IOException On write errors.
	 * @since 2023/05/31
	 */
	public CStructVariableBlock struct()
		throws IOException
	{
		// Prefix element with comma?
		int index = this._index;
		if (index > 0)
			this.token(",");
		
		throw Debugging.todo();
		/*
		// Open block
		CStructVariableBlock rv = new CStructVariableBlock(
			this, struct, "}");
		this.token("{");
		
		// Push it to the writer
		return this.__file().__pushBlock(rv, false);
		
		 */
	}
}
