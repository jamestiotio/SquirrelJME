// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.c;

import java.io.IOException;

/**
 * C function.
 *
 * @since 2023/05/31
 */
public class CFunctionBlock
	extends CBlock
{
	/**
	 * Initializes the C function block.
	 *
	 * @param __ref The reference to use.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/05/31
	 */
	CFunctionBlock(CSourceWriter __ref)
		throws NullPointerException
	{
		super(__ref, "}");
	}
	
	/**
	 * Initializes the switch case.
	 * 
	 * @param __condition The condition.
	 * @return The switch case writer.
	 * @throws IOException On write errors.
	 * @since 2023/05/31
	 */
	public CSwitchBlock switchCase(Object... __condition)
		throws IOException
	{
		// Write up tokens for the switch
		this.tokens("switch", "(", __condition, ")", "{");
		
		// Push
		CSwitchBlock rv = new CSwitchBlock(this);
		return this.__file().__pushBlock(rv, true);
	}
}
