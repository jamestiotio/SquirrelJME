// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package javax.microedition.lcdui;

import cc.squirreljme.runtime.cldc.debug.Debugging;

public class Gauge
	extends Item
{
	public static final int CONTINUOUS_IDLE =
		0;
	
	public static final int CONTINUOUS_RUNNING =
		2;
	
	public static final int INCREMENTAL_IDLE =
		1;
	
	public static final int INCREMENTAL_UPDATING =
		3;
	
	public static final int INDEFINITE =
		-1;
	
	/** Is this interactive? */
	final boolean _interactive;
	
	/** Maximum value. */
	volatile int _maxvalue;
	
	/** Current value. */
	volatile int _value;
	
	/**
	 * Initializes the gauge.
	 *
	 * @param __l The label.
	 * @param __int Can the user change the value?
	 * @param __max The maximum value.
	 * @param __iv The initial value.
	 * @throws IllegalArgumentException If the max value is not positive for
	 * interactive ranges, if the max value is not positive or
	 * {@link #INDEFINITE} for non-interactive ranges, or the initial value
	 * is not one of the special values if it is {@link #INDEFINITE}.
	 * @since 2019/05/17
	 */
	public Gauge(String __l, boolean __int, int __max, int __iv)
		throws IllegalArgumentException
	{
		super(__l);
		
		// {@squirreljme.error EB24 An interactive gauge cannot have a negative
		// maximum value.}
		if (__int && __max < 0)
			throw new IllegalArgumentException("EB24");
		
		// {@squirreljme.error EB25 A non-interactive gauge cannot have a
		// negative value that is not indefinite.}
		if (!__int && !(__max >= 0 || __max == Gauge.INDEFINITE))
			throw new IllegalArgumentException("EB25");
		
		// {@squirreljme.error EB26 Invalid symbolism for indefinite range.}
		if (__max == Gauge.INDEFINITE && __iv != Gauge.CONTINUOUS_IDLE &&
			__iv != Gauge.CONTINUOUS_RUNNING && __iv != Gauge.INCREMENTAL_IDLE &&
			__iv != Gauge.INCREMENTAL_UPDATING)
			throw new IllegalArgumentException("EB26");
		
		this._interactive = __int;
		this._value = __iv;
		this._maxvalue = __max;
	}
	
	public int getIncrementValue()
	{
		throw Debugging.todo();
	}
	
	public int getMaxValue()
	{
		throw Debugging.todo();
	}
	
	public int getMinValue()
	{
		throw Debugging.todo();
	}
	
	public int getValue()
	{
		throw Debugging.todo();
	}
	
	public boolean isInteractive()
	{
		throw Debugging.todo();
	}
	
	public void setIncrementValue(int __v)
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @throws IllegalArgumentException If this gauge is associated with an
	 * alert.
	 * @since 2019/05/17
	 */
	@Override
	public void setLabel(String __l)
		throws IllegalArgumentException
	{
		// {@squirreljme.error EB27 Cannot set the label of a gauge associated
		// with an alert.}
		if (this._displayable instanceof Alert)
			throw new IllegalArgumentException("EB27");
		
		// Use super logic
		super.setLabel(__l);
	}
	
	public void setMaxValue(int __a)
	{
		throw Debugging.todo();
	}
	
	public void setMinValue(int __v)
	{
		throw Debugging.todo();
	}
	
	public void setValue(int __a)
	{
		throw Debugging.todo();
	}
}


