// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.io.dynhistin;

import java.io.InputStream;
import java.io.IOException;
import net.multiphasicapps.util.dynbuffer.DynamicByteBuffer;

/**
 * This is an input stream which allows any future data in the stream to be
 * cached for later actual reading. This class should be used in situations
 * where it is needed to read future bytes in the stream and react to those
 * bytes.
 *
 * @since 2016/07/19
 */
public class DynamicHistoryInputStream
	extends InputStream
{
	/** Internal lock. */
	protected final Object lock =
		new Object();
	
	/** The backing buffer. */
	protected final DynamicByteBuffer buffer;
	
	/** The source input stream. */
	protected final InputStream input;
	
	/**
	 * Initializes a dynamic history stream which sources data from the given
	 * input stream.
	 *
	 * @param __is The stream to read data from.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/07/19
	 */
	public DynamicHistoryInputStream(InputStream __is)
		throws NullPointerException
	{
		this(__is, DynamicByteBuffer.DEFAULT_CHUNK_SIZE);
	}
	
	/**
	 * Initializes a dynamic history stream which sources data from the given
	 * input stream. An alternative chunk size for the backing
	 * {@link DynamicByteBuffer} may also be specified.
	 *
	 * @param __is The stream to read data from.
	 * @param __cs The chunk size to be used in the {@link DynamicByteBuffer}.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/07/19
	 */
	public DynamicHistoryInputStream(InputStream __is, int __cs)
		throws NullPointerException
	{
		// Check
		if (__is == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.input = __is;
		this.buffer = new DynamicByteBuffer(__cs);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/07/19
	 */
	@Override
	public void close()
		throws IOException
	{
		// Lock
		synchronized (this.lock)
		{
			throw new Error("TODO");
		}
	}
	
	/**
	 * Grabs the specified number of bytes and loads them into an internal
	 * queue where they may then be obtained using another method. 
	 *
	 * @param __i The number of bytes to read ahead and buffer.
	 * @return The number of bytes which are available for input, this may
	 * be less than or greater than the input parameter.
	 * @throws IndexOutOfBoundsException If the count is negative.
	 * @throws IOException On read errors.
	 * @since 2016/07/19
	 */
	public int grab(int __i)
		throws IndexOutOfBoundsException, IOException
	{
		// {@squirreljme.error BI03 A negative number of bytes cannot be
		// grabbed. (The number of bytes to grab)}
		if (__i < 0)
			throw new IndexOutOfBoundsException(String.format("BI03 %s", __i));
		
		// Lock
		DynamicByteBuffer buffer = this.buffer;
		synchronized (this.lock)
		{
			// Already have this number of bytes grabbed
			int cursize = buffer.size();
			if (__i <= cursize)
				return cursize;
			
			throw new Error("TODO");
		}
	}
	
	/**
	 * Reads a single byte that is ahead of the current read position.
	 *
	 * @param __a The position of the byte ahead of the current read position
	 * to read.
	 * @return The read value or a negative value if the byte to be read
	 * exceeds the end of the stream.
	 * @throws IndexOutOfBoundsException If the requested read ahead position
	 * is negative.
	 * @throws IOException On read errors.
	 * @since 2016/07/19
	 */
	public int peek(int __a)
		throws IndexOutOfBoundsException, IOException
	{
		// {@squirreljme.error BI01 Cannot a peek byte which have already been
		// read. (The requested index)}
		if (__a < 0)
			throw new IndexOutOfBoundsException(String.format("BI01 %d", __a));
		
		// Lock
		synchronized (this.lock)
		{
			// Grab bytes, stop if none are available
			int avail = grab(__a + 1);
			if (avail < __a)
				return -1;
			
			throw new Error("TODO");
		}
	}
	
	/**
	 * Reads multiple bytes which are ahead of the current read position.
	 *
	 * @param __a The start position of the bytes ahead of the current read
	 * position.
	 * @param __b The array which receives the bytes being read.
	 * @return The number of bytes read or a negative value if there are no
	 * bytes to be read because they exceed the end of the stream.
	 * @throws IndexOutOfBoundsException If the requested read ahead position
	 * is negative.
	 * @throws IOException On read errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/07/19
	 */
	public int peek(int __a, byte[] __b)
		throws IndexOutOfBoundsException, IOException, NullPointerException
	{
		return this.peek(__a, __b, 0, __b.length);
	}
	
	/**
	 * Reads multiple bytes which are ahead of the current read position.
	 *
	 * @param __a The start position of the bytes ahead of the current read
	 * position.
	 * @param __b The array which receives the bytes being read.
	 * @param __o The starting offset into the array to write into.
	 * @param __l The number of bytes to read.
	 * @return The number of bytes read or a negative value if there are no
	 * bytes to be read because they exceed the end of the stream.
	 * @throws IndexOutOfBoundsException If the requested read ahead position
	 * is negative; or the offset and or length exceed the array size.
	 * @throws IOException On read errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/07/19
	 */
	public int peek(int __a, byte[] __b, int __o, int __l)
		throws IndexOutOfBoundsException, IOException, NullPointerException
	{
		// {@squirreljme.error BI02 Cannot peek bytes which have already been
		// read. (The requested index)}
		if (__a < 0)
			throw new IndexOutOfBoundsException(String.format("BI02 %d", __a));
		
		// Check
		if (__b == null)
			throw new NullPointerException("NARG");
		int n = __b.length;
		if (__o < 0 || __l < 0 || (__o + __l) > n)
			throw new IndexOutOfBoundsException("IOOB");
		
		// Lock
		synchronized (this.lock)
		{
			// Grab bytes, stop if none are available
			int avail = grab(__a + __l);
			if (avail < __a)
				return -1;
			
			// Not reading anything?
			if (__l < 0)
				return 0;
			
			throw new Error("TODO");
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/07/19
	 */
	@Override
	public int read()
		throws IOException
	{
		// Lock
		synchronized (this.lock)
		{
			// Grab a single byte
			int gc = grab(1);
			
			// Nothing left
			if (gc <= 0)
				return -1;
			
			throw new Error("TODO");
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/07/19
	 */
	@Override
	public int read(byte[] __b, int __o, int __l)
		throws IndexOutOfBoundsException, IOException, NullPointerException
	{
		// Check
		if (__b == null)
			throw new NullPointerException("NARG");
		int n = __b.length;
		if (__o < 0 || __l < 0 || (__o + __l) >= n)
			throw new IndexOutOfBoundsException("IOOB");
		
		// Lock
		synchronized (this.lock)
		{
			// Grab multiple bytes
			int gc = grab(__l);
			
			// Nothing left?
			if (gc <= 0)
				return -1;
			
			// No bytes to read?
			int dc = Math.min(gc, __l);
			if (dc <= 0)
				return 0;
			
			throw new Error("TODO");
		}
	}
}

