// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.kernel.packets;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.ArrayDeque;
import java.util.Deque;

/**
 * This allows the creation for .
 *
 * @since 2018/01/17
 */
public final class LoopbackDatagramDuplex
{
	/** A double deque is used from one side to another. */
	private final Deque<__Datagram__>[] _queues;
	
	/** Side A reference. */
	private volatile Reference<Side> _a;
	
	/** Side B reference. */
	private volatile Reference<Side> _b;
	
	/**
	 * Initialize the datagram duplex.
	 *
	 * @since 2018/01/17
	 */
	public LoopbackDatagramDuplex()
	{
		throw new todo.TODO();
	}
	
	/**
	 * Returns side A.
	 *
	 * @return Side A.
	 * @since 2018/01/04
	 */
	public final Side sideA()
	{
		Reference<Side> ref = this._a;
		Side rv;
		
		if (ref == null || null == (rv = ref.get()))
		{
			Deque<__Datagram__>[] queues = this._queues;
			this._a = new WeakReference<>(
				(rv = new Side(queues[0], queues[1])));
		}
		
		return rv;
	}
	
	/**
	 * Returns side B.
	 *
	 * @return Side B.
	 * @since 2018/01/04
	 */
	public final Side sideB()
	{
		Reference<Side> ref = this._b;
		Side rv;
		
		if (ref == null || null == (rv = ref.get()))
		{
			Deque<__Datagram__>[] queues = this._queues;
			this._b = new WeakReference<>(
				(rv = new Side(queues[1], queues[0])));
		}
		
		return rv;
	}
	
	/**
	 * This represents a single side within the loopback.
	 *
	 * @since 2018/01/17
	 */
	public final class Side
	{
		/** The input queue. */
		protected final Deque<__Datagram__> in;
		
		/** The output queue. */
		protected final Deque<__Datagram__> out;
		
		/** Input datagrams. */
		private Reference<DatagramIn> _datagramin;
		
		/** Output datagrams. */
		private Reference<DatagramOut> _datagramout;
		
		/**
		 * Initializes the side.
		 *
		 * @param __in The input queue.
		 * @param __out The output queue.
		 * @throws NullPointerException On null arguments.
		 * @since 2018/01/04
		 */
		private Side(Deque<__Datagram__> __in, Deque<__Datagram__> __out)
			throws NullPointerException
		{
			if (__in == null || __out == null)
				throw new NullPointerException("NARG");
			
			this.in = __in;
			this.out = __out;
		}
		
		/**
		 * Returns the input for this side.
		 *
		 * @return The side input.
		 * @since 2018/01/04
		 */
		public final DatagramIn input()
		{
			Reference<DatagramIn> ref = this._datagramin;
			DatagramIn rv;
			
			if (ref == null || null == (rv = ref.get()))
				this._datagramin = new WeakReference<>(
					(rv = new __Input__(this.in)));
			
			return rv;
		}
		
		/**
		 * Returns the output for this side.
		 *
		 * @return The side output.
		 * @since 2018/01/04
		 */
		public final DatagramOut output()
		{
			Reference<DatagramOut> ref = this._datagramout;
			DatagramOut rv;
			
			if (ref == null || null == (rv = ref.get()))
				this._datagramout = new WeakReference<>(
					(rv = new __Output__(this.out)));
			
			return rv;
		}
	}
	
	/**
	 * Represents a datagram.
	 *
	 * @since 2018/01/17
	 */
	private static final class __Datagram__
	{
		/** The key for the datagram. */
		final int _key;
		
		/** The packet for the datagram. */
		final Packet _packet;
		
		/**
		 * Initializes the datagram.
		 *
		 * @param __key The datagram key.
		 * @param __packet The datagram packet.
		 * @throws NullPointerException On null arguments.
		 * @since 2018/01/17
		 */
		__Datagram__(int __key, Packet __packet)
			throws NullPointerException
		{
			if (__packet == null)
				throw new NullPointerException("NARG");
			
			this._key = __key;
			this._packet = __packet;
		}
	}
	
	/**
	 * Reads datagrams from a queue.
	 *
	 * @since 2018/01/17
	 */
	private static final class __Input__
		implements DatagramIn
	{
		/** The queue to read from. */
		protected final Deque<__Datagram__> in;
		
		/**
		 * Initializes the datagram input.
		 *
		 * @param __in The datagram input.
		 * @throws NullPointerException On null arguments.
		 * @since 2018/01/17
		 */
		__Input__(Deque<__Datagram__> __in)
			throws NullPointerException
		{
			if (__in == null)
				throw new NullPointerException("NARG");
			
			this.in = __in;
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2018/01/17
		 */
		@Override
		public final void close()
			throws DatagramIOException
		{
			// Does nothing
		}
	
		/**
		 * {@inheritDoc}
		 * @since 2018/01/17
		 */
		@Override
		public final Packet read(int[] __key)
			throws ArrayIndexOutOfBoundsException, DatagramIOException,
				NullPointerException
		{
			if (__key == null)
				throw new NullPointerException("NARG");
			if (__key.length < 1)
				throw new ArrayIndexOutOfBoundsException("IOOB");
			
			throw new todo.TODO();
		}
	}
	
	/**
	 * Writes datagrams to a queue.
	 *
	 * @since 2018/01/17
	 */
	private static final class __Output__
		implements DatagramOut
	{
		/** The queue to write to. */
		protected final Deque<__Datagram__> out;
		
		/**
		 * Initializes the datagram output.
		 *
		 * @param __out The datagram output.
		 * @throws NullPointerException On null arguments.
		 * @since 2018/01/17
		 */
		__Output__(Deque<__Datagram__> __out)
			throws NullPointerException
		{
			if (__out == null)
				throw new NullPointerException("NARG");
			
			this.out = __out;
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2018/01/17
		 */
		@Override
		public final void close()
			throws DatagramIOException
		{
			// Does nothing
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2018/01/17
		 */
		@Override
		public final void write(int __key, Packet __p)
			throws DatagramIOException, NullPointerException
		{
			if (__p == null)
				throw new NullPointerException("NARG");
			
			throw new todo.TODO();
		}
	}
}

