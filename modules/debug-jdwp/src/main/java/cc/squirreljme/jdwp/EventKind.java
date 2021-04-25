// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jdwp;

import cc.squirreljme.jdwp.event.EventModContext;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.util.Arrays;
import java.util.List;
import net.multiphasicapps.collections.EmptyList;
import net.multiphasicapps.collections.UnmodifiableIterable;
import net.multiphasicapps.collections.UnmodifiableList;

/**
 * The kind of event that is generated.
 *
 * @since 2021/03/13
 */
public enum EventKind
	implements __IdNumbered__
{
	/** Single Step. */
	SINGLE_STEP(1, null, null, EventModKind.THREAD_ONLY,
		EventModKind.CLASS_ONLY,
		EventModKind.CLASS_MATCH_PATTERN, EventModKind.CLASS_EXCLUDE_PATTERN,
		EventModKind.LOCATION_ONLY, EventModKind.CALL_STACK_STEPPING,
		EventModKind.THIS_INSTANCE_ONLY, EventModKind.LIMIT_OCCURRENCES)
	{
		/**
		 * {@inheritDoc}
		 * @since 2021/03/16
		 */
		@Override
		public void write(JDWPController __controller, Object __thread,
			JDWPPacket __packet, Object... __args)
			throws JDWPException
		{
			throw Debugging.todo();
		}
	},
	
	/** Breakpoint. */
	BREAKPOINT(2, Arrays.asList(EventModContext.CURRENT_THREAD),
		null, EventModKind.THREAD_ONLY,
		EventModKind.CLASS_ONLY,
		EventModKind.CLASS_MATCH_PATTERN, EventModKind.CLASS_EXCLUDE_PATTERN,
		EventModKind.LOCATION_ONLY, EventModKind.THIS_INSTANCE_ONLY,
		EventModKind.LIMIT_OCCURRENCES)
	{
		/**
		 * {@inheritDoc}
		 * @since 2021/03/16
		 */
		@Override
		public void write(JDWPController __controller, Object __thread,
			JDWPPacket __packet, Object... __args)
			throws JDWPException
		{
			throw Debugging.todo();
		}
	},
	
	/** Frame pop. */
	FRAME_POP(3, null, null,
		EventModKind.THREAD_ONLY, EventModKind.CLASS_ONLY,
		EventModKind.CLASS_MATCH_PATTERN, EventModKind.CLASS_EXCLUDE_PATTERN,
		EventModKind.THIS_INSTANCE_ONLY, EventModKind.LIMIT_OCCURRENCES)
	{
		/**
		 * {@inheritDoc}
		 * @since 2021/03/16
		 */
		@Override
		public void write(JDWPController __controller, Object __thread,
			JDWPPacket __packet, Object... __args)
			throws JDWPException
		{
			throw Debugging.todo();
		}
	},
	
	/** Exception. */
	EXCEPTION(4, null, null,
		EventModKind.THREAD_ONLY, EventModKind.CLASS_ONLY,
		EventModKind.CLASS_MATCH_PATTERN, EventModKind.CLASS_EXCLUDE_PATTERN,
		EventModKind.LOCATION_ONLY, EventModKind.EXCEPTION_ONLY,
		EventModKind.THIS_INSTANCE_ONLY, EventModKind.LIMIT_OCCURRENCES)
	{
		/**
		 * {@inheritDoc}
		 * @since 2021/03/16
		 */
		@Override
		public void write(JDWPController __controller, Object __thread,
			JDWPPacket __packet, Object... __args)
			throws JDWPException
		{
			throw Debugging.todo();
		}
	},
	
	/** User defined. */
	USER_DEFINED(5, null, null,
		EventModKind.THREAD_ONLY, EventModKind.CLASS_ONLY,
		EventModKind.CLASS_MATCH_PATTERN, EventModKind.CLASS_EXCLUDE_PATTERN,
		EventModKind.THIS_INSTANCE_ONLY, EventModKind.LIMIT_OCCURRENCES)
	{
		/**
		 * {@inheritDoc}
		 * @since 2021/03/16
		 */
		@Override
		public void write(JDWPController __controller, Object __thread,
			JDWPPacket __packet, Object... __args)
			throws JDWPException
		{
			throw Debugging.todo();
		}
	},
	
	/** Start of thread. */
	THREAD_START(6, null, null, EventModKind.THREAD_ONLY,
		EventModKind.LIMIT_OCCURRENCES)
	{
		/**
		 * {@inheritDoc}
		 * @since 2021/03/16
		 */
		@Override
		public void write(JDWPController __controller, Object __thread,
			JDWPPacket __packet, Object... __args)
			throws JDWPException
		{
			__packet.writeId(System.identityHashCode(__args[0]));
		}
	},
	
	/** End of thread. */
	THREAD_DEATH(7, null, null, EventModKind.THREAD_ONLY,
		EventModKind.LIMIT_OCCURRENCES)
	{
		/**
		 * {@inheritDoc}
		 * @since 2021/03/16
		 */
		@Override
		public void write(JDWPController __controller, Object __thread,
			JDWPPacket __packet, Object... __args)
			throws JDWPException
		{
			__packet.writeId(System.identityHashCode(__args[0]));
		}
	},
	
	/** Class being prepared. */
	CLASS_PREPARE(8, null,
		Arrays.asList(EventModContext.PARAMETER_TYPE),
		EventModKind.THREAD_ONLY, EventModKind.CLASS_ONLY,
		EventModKind.CLASS_MATCH_PATTERN, EventModKind.CLASS_EXCLUDE_PATTERN,
		EventModKind.LIMIT_OCCURRENCES)
	{
		/**
		 * {@inheritDoc}
		 * @since 2021/03/16
		 */
		@Override
		public void write(JDWPController __controller, Object __thread,
			JDWPPacket __packet, Object... __args)
			throws JDWPException
		{
			// Extract arguments
			Object cl = __args[0];
			JDWPClassStatus status = (JDWPClassStatus)__args[1];
			
			// Calling thread
			__packet.writeId(System.identityHashCode(__thread));
			
			// The Class ID
			__packet.writeByte(JDWPUtils.classType(__controller, cl).id);
			__packet.writeId(System.identityHashCode(cl));
			
			// The signature of the class
			__packet.writeString(__controller.viewType().signature(cl));
			
			// The state of this class
			__packet.writeInt(status.bits);
		}
	},
	
	/** Class unloading. */
	CLASS_UNLOAD(9, null, null,
		EventModKind.CLASS_MATCH_PATTERN,
		EventModKind.CLASS_EXCLUDE_PATTERN, EventModKind.LIMIT_OCCURRENCES)
	{
		/**
		 * {@inheritDoc}
		 * @since 2021/03/16
		 */
		@Override
		public void write(JDWPController __controller, Object __thread,
			JDWPPacket __packet, Object... __args)
			throws JDWPException
		{
			throw Debugging.todo();
		}
	},
	
	/** Class loading. */
	CLASS_LOAD(10, null, null,
		EventModKind.THREAD_ONLY, EventModKind.CLASS_ONLY,
		EventModKind.CLASS_MATCH_PATTERN, EventModKind.CLASS_EXCLUDE_PATTERN,
		EventModKind.THIS_INSTANCE_ONLY, EventModKind.LIMIT_OCCURRENCES)
	{
		/**
		 * {@inheritDoc}
		 * @since 2021/03/16
		 */
		@Override
		public void write(JDWPController __controller, Object __thread,
			JDWPPacket __packet, Object... __args)
			throws JDWPException
		{
			throw Debugging.todo();
		}
	},
	
	/** Field access. */
	FIELD_ACCESS(20, null, null,
		EventModKind.THREAD_ONLY, EventModKind.CLASS_ONLY,
		EventModKind.CLASS_MATCH_PATTERN, EventModKind.CLASS_EXCLUDE_PATTERN,
		EventModKind.LOCATION_ONLY, EventModKind.FIELD_ONLY,
		EventModKind.THIS_INSTANCE_ONLY, EventModKind.LIMIT_OCCURRENCES)
	{
		/**
		 * {@inheritDoc}
		 * @since 2021/03/16
		 */
		@Override
		public void write(JDWPController __controller, Object __thread,
			JDWPPacket __packet, Object... __args)
			throws JDWPException
		{
			throw Debugging.todo();
		}
	},
	
	/** Field modification. */
	FIELD_MODIFICATION(21, null, null,
		EventModKind.THREAD_ONLY,
		EventModKind.CLASS_ONLY, EventModKind.CLASS_MATCH_PATTERN,
		EventModKind.CLASS_EXCLUDE_PATTERN, EventModKind.LOCATION_ONLY,
		EventModKind.FIELD_ONLY, EventModKind.THIS_INSTANCE_ONLY,
		EventModKind.LIMIT_OCCURRENCES)
	{
		/**
		 * {@inheritDoc}
		 * @since 2021/03/16
		 */
		@Override
		public void write(JDWPController __controller, Object __thread,
			JDWPPacket __packet, Object... __args)
			throws JDWPException
		{
			throw Debugging.todo();
		}
	},
	
	/** Exception catch. */
	EXCEPTION_CATCH(30, null, null,
		EventModKind.THREAD_ONLY, EventModKind.CLASS_ONLY,
		EventModKind.CLASS_MATCH_PATTERN, EventModKind.CLASS_EXCLUDE_PATTERN,
		EventModKind.LOCATION_ONLY, EventModKind.EXCEPTION_ONLY,
		EventModKind.THIS_INSTANCE_ONLY, EventModKind.LIMIT_OCCURRENCES)
	{
		/**
		 * {@inheritDoc}
		 * @since 2021/03/16
		 */
		@Override
		public void write(JDWPController __controller, Object __thread,
			JDWPPacket __packet, Object... __args)
			throws JDWPException
		{
			throw Debugging.todo();
		}
	},
	
	/** Method entry. */
	METHOD_ENTRY(40, null, null,
		EventModKind.THREAD_ONLY, EventModKind.CLASS_ONLY,
		EventModKind.CLASS_MATCH_PATTERN, EventModKind.CLASS_EXCLUDE_PATTERN,
		EventModKind.THIS_INSTANCE_ONLY, EventModKind.LIMIT_OCCURRENCES)
	{
		/**
		 * {@inheritDoc}
		 * @since 2021/03/16
		 */
		@Override
		public void write(JDWPController __controller, Object __thread,
			JDWPPacket __packet, Object... __args)
			throws JDWPException
		{
			throw Debugging.todo();
		}
	},
	
	/** Method exit. */
	METHOD_EXIT(41, null, null, EventModKind.THREAD_ONLY,
		EventModKind.CLASS_ONLY,
		EventModKind.CLASS_MATCH_PATTERN, EventModKind.CLASS_EXCLUDE_PATTERN,
		EventModKind.THIS_INSTANCE_ONLY, EventModKind.LIMIT_OCCURRENCES)
	{
		/**
		 * {@inheritDoc}
		 * @since 2021/03/16
		 */
		@Override
		public void write(JDWPController __controller, Object __thread,
			JDWPPacket __packet, Object... __args)
			throws JDWPException
		{
			throw Debugging.todo();
		}
	},
	
	/** Method exit with return value. */
	METHOD_EXIT_WITH_RETURN_VALUE(42, null, null,
		EventModKind.THREAD_ONLY,
		EventModKind.CLASS_ONLY, EventModKind.CLASS_MATCH_PATTERN,
		EventModKind.CLASS_EXCLUDE_PATTERN, EventModKind.THIS_INSTANCE_ONLY,
		EventModKind.LIMIT_OCCURRENCES)
	{
		/**
		 * {@inheritDoc}
		 * @since 2021/03/16
		 */
		@Override
		public void write(JDWPController __controller, Object __thread,
			JDWPPacket __packet, Object... __args)
			throws JDWPException
		{
			throw Debugging.todo();
		}
	},
	
	/** Contended monitor enter. */
	MONITOR_CONTENDED_ENTER(43, null, null,
		EventModKind.THREAD_ONLY,
		EventModKind.CLASS_ONLY, EventModKind.CLASS_MATCH_PATTERN,
		EventModKind.CLASS_EXCLUDE_PATTERN, EventModKind.THIS_INSTANCE_ONLY,
		EventModKind.LIMIT_OCCURRENCES)
	{
		/**
		 * {@inheritDoc}
		 * @since 2021/03/16
		 */
		@Override
		public void write(JDWPController __controller, Object __thread,
			JDWPPacket __packet, Object... __args)
			throws JDWPException
		{
			throw Debugging.todo();
		}
	},
	
	/** Contended monitor exit. */
	MONITOR_CONTENDED_EXIT(44, null, null,
		EventModKind.THREAD_ONLY,
		EventModKind.CLASS_ONLY, EventModKind.CLASS_MATCH_PATTERN,
		EventModKind.CLASS_EXCLUDE_PATTERN, EventModKind.THIS_INSTANCE_ONLY,
		EventModKind.LIMIT_OCCURRENCES)
	{
		/**
		 * {@inheritDoc}
		 * @since 2021/03/16
		 */
		@Override
		public void write(JDWPController __controller, Object __thread,
			JDWPPacket __packet, Object... __args)
			throws JDWPException
		{
			throw Debugging.todo();
		}
	},
	
	/** Monitor wait. */
	MONITOR_WAIT(45, null, null, EventModKind.THREAD_ONLY,
		EventModKind.CLASS_ONLY,
		EventModKind.CLASS_MATCH_PATTERN, EventModKind.CLASS_EXCLUDE_PATTERN,
		EventModKind.THIS_INSTANCE_ONLY, EventModKind.LIMIT_OCCURRENCES)
	{
		/**
		 * {@inheritDoc}
		 * @since 2021/03/16
		 */
		@Override
		public void write(JDWPController __controller, Object __thread,
			JDWPPacket __packet, Object... __args)
			throws JDWPException
		{
			throw Debugging.todo();
		}
	},
	
	/** Monitor waited. */
	MONITOR_WAITED(46, null, null, EventModKind.THREAD_ONLY,
		EventModKind.CLASS_ONLY,
		EventModKind.CLASS_MATCH_PATTERN, EventModKind.CLASS_EXCLUDE_PATTERN,
		EventModKind.THIS_INSTANCE_ONLY, EventModKind.LIMIT_OCCURRENCES)
	{
		/**
		 * {@inheritDoc}
		 * @since 2021/03/16
		 */
		@Override
		public void write(JDWPController __controller, Object __thread,
			JDWPPacket __packet, Object... __args)
			throws JDWPException
		{
			throw Debugging.todo();
		}
	},
	
	/** Virtual machine start. */
	VM_START(90, null, null, EventModKind.THREAD_ONLY,
		EventModKind.LIMIT_OCCURRENCES)
	{
		/**
		 * {@inheritDoc}
		 * @since 2021/03/16
		 */
		@Override
		public void write(JDWPController __controller, Object __thread,
			JDWPPacket __packet, Object... __args)
			throws JDWPException
		{
			throw Debugging.todo();
		}
	},
	
	/** Virtual machine death. */
	VM_DEATH(99, null, null, EventModKind.LIMIT_OCCURRENCES)
	{
		/**
		 * {@inheritDoc}
		 * @since 2021/03/16
		 */
		@Override
		public void write(JDWPController __controller, Object __thread,
			JDWPPacket __packet, Object... __args)
			throws JDWPException
		{
			throw Debugging.todo();
		}
	},
	
	/* End. */
	;
	
	/** Quick table. */
	private static final __QuickTable__<EventKind> _QUICK =
		new __QuickTable__<>(EventKind.values());
	
	/** The event ID. */
	public final int id;
	
	/** The modifier ordinal bits, for quicker lookup. */
	private final int _modifierBits;
	
	/** Non-argument context. */
	private final Iterable<EventModContext> _nonArg;
	
	/** Argument context. */
	private final List<EventModContext> _arg;
	
	/**
	 * Initializes the event kind.
	 * 
	 * @param __id The identifier.
	 * @param __nonArg Non-argument context.
	 * @param __arg Argument context.
	 * @param __modifiers The possible supported modifiers for this event.
	 * @since 2021/03/13
	 */
	EventKind(int __id, Iterable<EventModContext> __nonArg,
		List<EventModContext> __arg, EventModKind... __modifiers)
	{
		this.id = __id;
		
		// Contexts for modifiers
		this._nonArg = (__nonArg == null ? EmptyList.<EventModContext>empty() :
			UnmodifiableIterable.<EventModContext>of(__nonArg));
		this._arg = (__arg == null ? EmptyList.<EventModContext>empty() :
			UnmodifiableList.<EventModContext>of(__arg));
		
		// Determine the modifier bits to quickly get the items
		int modifierBits = 0;
		for (EventModKind mod : __modifiers)
			modifierBits |= (1 << mod.ordinal());
		this._modifierBits = modifierBits;
	}
	
	/**
	 * Writes the packet event data.
	 * 
	 * @param __controller The controller used.
	 * @param __thread The current thread of execution.
	 * @param __packet The packet to write to.
	 * @param __args The arguments to the packet, the first value should
	 * always be {@code thread}.
	 * @throws JDWPException If it could not be written.
	 * @since 2021/03/16
	 */
	public abstract void write(JDWPController __controller, Object __thread,
		JDWPPacket __packet, Object... __args)
		throws JDWPException;
	
	/**
	 * Returns the context for the given argument.
	 * 
	 * @param __i The argument index, the first value should always be
	 * {@code thread}.
	 * @return The context for the given argument, will be {@code null} if
	 * not valid for negative values.
	 * @since 2021/04/17
	 */
	public final EventModContext contextArgument(int __i)
	{
		List<EventModContext> arg = this._arg;
		if (__i < 0 || __i >= arg.size())
			return null;
		
		return arg.get(__i);
	}
	
	/**
	 * Returns the general context.
	 * 
	 * @return The general context.
	 * @since 2021/04/25
	 */
	public final Iterable<EventModContext> contextGeneral()
	{
		return this._nonArg;
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
	 * Checks if the given modifier is valid for this.
	 * 
	 * @param __mod The modifier to check.
	 * @return Is this a valid modifier?
	 * @since 2021/04/17
	 */
	public final boolean isValidModifier(EventModKind __mod)
	{
		// Is the ordinal set for this modifier?
		return (0 != (this._modifierBits & (1 << __mod.ordinal())));
	}
	
	/**
	 * Looks up the constant by the given Id.
	 * 
	 * @param __id The Id.
	 * @return The found constant.
	 * @since 2021/03/13
	 */
	public static EventKind of(int __id)
	{
		return EventKind._QUICK.get(__id);
	}
}
