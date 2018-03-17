// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.lcdui.server;

import cc.squirreljme.runtime.cldc.service.ServiceServer;
import cc.squirreljme.runtime.cldc.system.type.EnumType;
import cc.squirreljme.runtime.cldc.system.type.IntegerArray;
import cc.squirreljme.runtime.cldc.system.type.LocalIntegerArray;
import cc.squirreljme.runtime.cldc.system.type.VoidType;
import cc.squirreljme.runtime.cldc.task.SystemTask;
import cc.squirreljme.runtime.lcdui.DisplayableType;
import cc.squirreljme.runtime.lcdui.LcdFunction;

/**
 * This class implements the base for the LCDUI interface used for the
 * server end which resides in the kernel.
 *
 * @since 2018/03/16
 */
public final class LcdServer
	implements ServiceServer
{
	/** The task this provides a service for. */
	protected final SystemTask task;
	
	/** Request handler for events, everything goes through this. */
	protected final LcdRequestHandler requesthandler;
	
	/**
	 * Initializes the LCDUI server.
	 *
	 * @param __task The task this provides a service for.
	 * @param __rh The request handler for events.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/03/17
	 */
	public LcdServer(SystemTask __task, LcdRequestHandler __rh)
		throws NullPointerException
	{
		if (__task == null || __rh == null)
			throw new NullPointerException("NARG");
		
		this.task = __task;
		this.requesthandler = __rh;
	}
	
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/16
	 */
	@Override
	public final Object serviceCall(EnumType __func, Object... __args)
		throws NullPointerException
	{
		if (__func == null)
			throw new NullPointerException("NARG");
		
		if (__args == null)
			__args = new Object[0];
		
		// Depends on the function
		LcdFunction func;
		switch ((func = __func.<LcdFunction>asEnum(LcdFunction.class)))
		{
			case DISPLAY_QUERY:
				return this.__displayQuery();
			
			case CREATE_DISPLAYABLE:
				return this.__createDisplayable(((EnumType)__args[0]).
					<DisplayableType>asEnum(DisplayableType.class));
		
			case DISPLAYABLE_SET_TITLE:
				this.__displayableSetTitle((Integer)__args[0],
					(String)__args[1]);
				return VoidType.INSTANCE;
			
				// {@squirreljme.error EB1u Unknown or unimplemented LCDUI
				// function. (The LCD function)}
			default:
				throw new RuntimeException(String.format("EB1u %s", func));
		}
	}
	
	/**
	 * Creates a new displayable of the given type.
	 *
	 * @param __t The type of displayable to create.
	 * @return The handle to the displayable.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/03/17
	 */
	private final int __createDisplayable(DisplayableType __t)
		throws NullPointerException
	{
		if (__t == null)
			throw new NullPointerException("NARG");
		
		throw new todo.TODO();
	}
	
	/**
	 * Sets the title of the given displayable.
	 *
	 * @param __handle The handle of the displayable.
	 * @param __title The title to use, {@code null} clears it.
	 * @since 2018/03/17
	 */
	private final void __displayableSetTitle(int __handle, String __title)
	{
		throw new todo.TODO();
	}
	
	/**
	 * Queries the displays which are currently available.
	 *
	 * @return The available displays.
	 * @since 2018/03/17
	 */
	private final IntegerArray __displayQuery()
	{
		throw new todo.TODO();
	}
}

