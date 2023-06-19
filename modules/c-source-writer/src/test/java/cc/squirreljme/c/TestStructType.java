// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.c;

import net.multiphasicapps.tac.TestRunnable;

/**
 * Tests structure types.
 *
 * @since 2023/06/12
 */
public class TestStructType
	extends TestRunnable
{
	/**
	 * {@inheritDoc}
	 * @since 2023/06/12
	 */
	@Override
	public void test()
		throws Throwable
	{
		// Build a basic struct
		CStructType struct = new CStructTypeBuilder("foo")
			.member(CPrimitiveType.INT32_T, "xint")
			.member(CPrimitiveType.VOID.pointerType(), "xvoidptr")
			.build();
		
		// Declare struct
		try (__Spool__ spool = new __Spool__();
			CFile file = spool.file())
		{
			file.declare(CVariable.of(struct, "foo"));
			
			this.secondary("declare", spool.tokens());
		}
		
		// Define struct
		try (__Spool__ spool = new __Spool__();
			 CFile file = spool.file())
		{
			file.define(CVariable.of(struct, "foo"));
			
			this.secondary("define", spool.tokens());
		}
	}
}
