// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package jdk.dio.adc;

import cc.squirreljme.runtime.cldc.annotation.Api;
import cc.squirreljme.runtime.cldc.annotation.ApiDefinedDeprecated;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import jdk.dio.DeviceConfig;

@Api
public final class ADCChannelConfig
	implements DeviceConfig<ADCChannel>, DeviceConfig.HardwareAddressing
{
	@ApiDefinedDeprecated
	@Api
	public ADCChannelConfig(int __a, int __b, int __c, int __d, int __e)
	{
		throw Debugging.todo();
	}
	
	@ApiDefinedDeprecated
	@Api
	public ADCChannelConfig(String __a, int __b, int __c, int __d, int __e)
	{
		throw Debugging.todo();
	}
	
	@Override
	public boolean equals(Object __a)
	{
		throw Debugging.todo();
	}
	
	@Api
	public int getChannelNumber()
	{
		throw Debugging.todo();
	}
	
	@Override
	public String getControllerName()
	{
		throw Debugging.todo();
	}
	
	@Override
	public int getControllerNumber()
	{
		throw Debugging.todo();
	}
	
	@Api
	public int getInputBufferSize()
	{
		throw Debugging.todo();
	}
	
	@Api
	public int getResolution()
	{
		throw Debugging.todo();
	}
	
	@Api
	public int getSamplingInterval()
	{
		throw Debugging.todo();
	}
	
	@Api
	public int getSamplingTime()
	{
		throw Debugging.todo();
	}
	
	@Api
	public double getScaleFactor()
	{
		throw Debugging.todo();
	}
	
	@Override
	public int hashCode()
	{
		throw Debugging.todo();
	}
	
	@Override
	public int serialize(OutputStream __a)
		throws IOException
	{
		if (false)
			throw new IOException();
		throw Debugging.todo();
	}
	
	@Api
	public static ADCChannelConfig deserialize(InputStream __a)
		throws IOException
	{
		if (false)
			throw new IOException();
		throw Debugging.todo();
	}
	
	@Api
	public static final class Builder
	{
		@Api
		public Builder()
		{
			throw Debugging.todo();
		}
		
		@Api
		public ADCChannelConfig build()
		{
			throw Debugging.todo();
		}
		
		@Api
		public Builder setChannelNumber(int __a)
		{
			throw Debugging.todo();
		}
		
		@Api
		public Builder setControllerName(String __a)
		{
			throw Debugging.todo();
		}
		
		@Api
		public Builder setControllerNumber(int __a)
		{
			throw Debugging.todo();
		}
		
		@Api
		public Builder setInputBufferSize(int __a)
		{
			throw Debugging.todo();
		}
		
		@Api
		public Builder setResolution(int __a)
		{
			throw Debugging.todo();
		}
		
		@Api
		public Builder setSamplingInterval(int __a)
		{
			throw Debugging.todo();
		}
		
		@Api
		public Builder setSamplingTime(int __a)
		{
			throw Debugging.todo();
		}
		
		@Api
		public Builder setScaleFactor(double __a)
		{
			throw Debugging.todo();
		}
	}
}


