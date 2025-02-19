// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package lang;

import net.multiphasicapps.tac.TestSupplier;

/**
 * This tests that {@link Integer#parseInt(int, int)} when the input value
 * overflows it throws an exception.
 *
 * @since 2018/10/13
 */
public class TestIntegerParseIntOverflow
	extends TestSupplier<String>
{
	/**
	 * {@inheritDoc}
	 * @since 2018/10/13
	 */
	@Override
	public String test()
	{
		StringBuilder sb = new StringBuilder();
		
		// Check overflow for all radixes
		int rv = 0;
		for (int r = Character.MIN_RADIX; r <= Character.MAX_RADIX; r++)
			try
			{
				// This is a really long value which is valid for all radixes
				// it is either a 33-bit integer or log(33)??
				Integer.parseInt("-111111111111111111111111111111111", r);
				
				sb.append(Character.toUpperCase(
					Character.forDigit(r, Character.MAX_RADIX)));
			}
			catch (NumberFormatException e)
			{
				sb.append(Character.forDigit(r, Character.MAX_RADIX));
			}
		
		return sb.toString();
	}
}

