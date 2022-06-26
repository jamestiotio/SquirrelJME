// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package jdk.dio.gpio;

import cc.squirreljme.runtime.cldc.debug.Debugging;
import jdk.dio.DevicePermission;

public class GPIOPortPermission
	extends DevicePermission
{
	public static final String SET_DIRECTION =
		"setdirection";
	
	public GPIOPortPermission(String __a)
	{
		super((String)null);
		throw Debugging.todo();
	}
	
	public GPIOPortPermission(String __a, String __b)
	{
		super((String)null);
		throw Debugging.todo();
	}
	
	@Override
	public String getActions()
	{
		throw Debugging.todo();
	}
}


