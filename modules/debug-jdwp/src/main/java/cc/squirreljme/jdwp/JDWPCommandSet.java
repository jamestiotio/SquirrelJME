// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jdwp;

/**
 * Command set for JDWP Packets.
 *
 * @since 2021/03/12
 */
public enum JDWPCommandSet
	implements __IdNumbered__
{
	/** Unknown command set. */
	UNKNOWN(-1),
	
	/** Virtual machine command set. */
	VIRTUAL_MACHINE(1, CommandSetVirtualMachine.values()),
	
	/** Reference type. */
	REFERENCE_TYPE(2, CommandSetReferenceType.values()),
	
	/** Class types. */
	CLASS_TYPE(3, CommandSetClassType.values()),
	
	/** Methods. */
	METHODS(6, CommandSetMethod.values()),
	
	/** Object references. */
	OBJECT_REFERENCE(9, CommandSetObjectReference.values()),
	
	/** String reference. */
	STRING_REFERENCE(10, CommandSetStringReference.values()),
	
	/** Thread Reference. */
	THREAD_REFERENCE(11, CommandSetThreadReference.values()),
	
	/** Thread group reference. */
	THREAD_GROUP_REFERENCE(12, CommandSetThreadGroupReference.values()),
	
	/** Array references. */
	ARRAY_REFERENCE(13, CommandSetArrayReference.values()),
	
	/** Class loader. */
	CLASS_LOADER(14, CommandSetClassLoader.values()),
	
	/** Event requests. */
	EVENT_REQUEST(15, CommandSetEventRequest.values()),
	
	/** Stack frames. */
	STACK_FRAMES(16, CommandSetStackFrame.values()),
	
	/** Class object references. */
	CLASS_OBJECT_REFERENCE(17, CommandSetClassObjectReference.values()),
	
	/* End. */
	;
	
	/** Quick lookup for command sets. */
	private static final __QuickTable__<JDWPCommandSet> _QUICK =
		new __QuickTable__<>(JDWPCommandSet.values());
	
	/** The Id of the set. */
	public final int id;
	
	/** Command set commands. */
	private final __QuickTable__<JDWPCommand> _commands; 
	
	/**
	 * Initializes the command set enum.
	 * 
	 * @param __id The ID number.
	 * @param __cmds Available commands.
	 * @since 2021/03/12
	 */
	JDWPCommandSet(int __id, JDWPCommand... __cmds)
	{
		this.id = __id;
		this._commands = new __QuickTable__<>(__cmds);
	}
	
	/**
	 * Returns the command.
	 * 
	 * @param __id The ID of the command.
	 * @return The command used, {@code null} if unknown.
	 * @since 2021/03/12
	 */
	public final JDWPCommand command(int __id)
	{
		return this._commands.get(__id);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/03/13
	 */
	@Override
	public final int debuggerId()
	{
		return this.id;
	}
	
	/**
	 * Returns the command set of the given ID.
	 * 
	 * @param __id The id of the command set.
	 * @return The command set used or {@link #UNKNOWN} if not known.
	 * @since 2021/03/12
	 */
	public static JDWPCommandSet of(int __id)
	{
		JDWPCommandSet rv = JDWPCommandSet._QUICK.get(__id);
		
		// Is always not null
		if (rv == null)
			return JDWPCommandSet.UNKNOWN;
		return rv;
	}
}
