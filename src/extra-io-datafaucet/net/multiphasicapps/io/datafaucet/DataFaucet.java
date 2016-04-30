// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.io.datafaucet;

import java.io.Flushable;
import java.util.NoSuchElementException;
import net.multiphasicapps.util.circlebufs.CircularByteBuffer;

/**
 * This is a data faucet which generates bytes.
 *
 * {@squirreljme.error AB01 Cannot add bytes for draining when the output is
 * complete.}
 *
 * @since 2016/04/30
 */
public abstract class DataFaucet
	implements Flushable
{
	/** The internal lock. */
	protected final Object lock;
	
	/** The output temporary buffer. */
	private final CircularByteBuffer _output;
	
	/** Is the faucet complete? */
	private volatile boolean _complete;
	
	/**
	 * Initializes the data faucet.
	 *
	 * @since 2016/04/30
	 */
	public DataFaucet()
	{
		this(new Object());
	}
	
	/**
	 * Initializes the data faucet with the given lock.
	 *
	 * @param __lk The data lock to use.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/04/30
	 */
	public DataFaucet(Object __lk)
		throws NullPointerException
	{
		// Check
		if (__lk == null)
			throw new NullPointerException("NARG");
		
		// Set
		lock = __lk;
		
		// Setup output buffer
		_output = new CircularByteBuffer(__lk);
	}
	
	/**
	 * Adds data to be output via the drain.
	 *
	 * @param __b The single byte to add.
	 * @return {@code this}.
	 * @throws FaucetProcessException If the faucet is complete.
	 * @since 2016/04/30
	 */
	protected final DataFaucet fill(byte __b)
		throws FaucetProcessException
	{
		// Lock
		synchronized (lock)
		{
			// Cannot fill when already complete
			if (_complete)
				throw new FaucetProcessException("AB01");
			
			throw new Error("TODO");
		}
	}
	
	/**
	 * Adds data to be output via the drain.
	 *
	 * @param __b The bytes to add.
	 * @return {@code this}.
	 * @throws FaucetProcessException If the faucet is complete.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/04/30
	 */
	protected final DataFaucet fill(byte[] __b)
		throws FaucetProcessException, NullPointerException
	{
		return fill(__b, 0, __b.length);
	}
	
	/**
	 * Adds data to be output via the drain.
	 *
	 * @param __b The bytes to add.
	 * @return {@code this}.
	 * @throws FaucetProcessException If the faucet is complete.
	 * @throws IndexOutOfBoundsException If the offset or length are negative
	 * or exceed the array size.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/04/30
	 */
	protected final DataFaucet fill(byte[] __b, int __o, int __l)
		throws FaucetProcessException, IndexOutOfBoundsException,
			NullPointerException
	{
		// Check
		if (__b == null)
			throw new NullPointerException("NARG");
		if (__o < 0 || __l < 0 || (__o + __l) > __b.length)
			throw new IndexOutOfBoundsException("BAOB");
		
		// Lock
		synchronized (lock)
		{
			// Cannot fill when already complete
			if (_complete)
				throw new FaucetProcessException("AB01");
			
			throw new Error("TODO");
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/04/30
	 */
	@Override
	public final void flush()
	{
		// Lock
		synchronized (lock)
		{
			throw new Error("TODO");
		}
	}
	
	/**
	 * Returns {@code true} if there is no more output to be generated from
	 * this faucet.
	 *
	 * @return {@code true} if the faucet has no more data to generate.
	 * @since 2016/04/30
	 */
	public final boolean isComplete()
	{
		// Lock
		synchronized (lock)
		{
			return _complete;
		}
	}
}

