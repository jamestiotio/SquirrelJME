// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit.generic;

import net.multiphasicapps.squirreljme.java.symbols.IdentifierSymbol;
import net.multiphasicapps.squirreljme.java.symbols.MemberTypeSymbol;
import net.multiphasicapps.squirreljme.jit.base.JITMemberFlags;

/**
 * This is the base class storage for fields and methods.
 *
 * @since 2016/08/18
 */
abstract class __Member__
{
	/**
	 * Initializes the base member.
	 *
	 * @param __gcw The owning class writer.
	 * @param __f The flags for the member.
	 * @param __name The name of the member.
	 * @param __type The type of the member.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/08/18
	 */
	__Member__(GenericClassWriter __gcw, JITMemberFlags __f,
		IdentifierSymbol __name, MemberTypeSymbol __type)
		throws NullPointerException
	{
		// Check
		if (__f == null || __name == null || __type == null)
			throw new NullPointerException("NARG");
		
		throw new Error("TODO"); 
	}
}

