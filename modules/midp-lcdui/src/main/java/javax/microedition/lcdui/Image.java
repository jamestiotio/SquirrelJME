// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package javax.microedition.lcdui;

import cc.squirreljme.jvm.mle.constants.UIPixelFormat;
import cc.squirreljme.runtime.cldc.annotation.Api;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import cc.squirreljme.runtime.lcdui.image.AccessibleImage;
import cc.squirreljme.runtime.lcdui.image.ImageReaderDispatcher;
import cc.squirreljme.runtime.lcdui.mle.PencilGraphics;
import cc.squirreljme.runtime.midlet.ActiveMidlet;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

@Api
public class Image
	extends AccessibleImage
{
	/** The RGB image data. */
	private final int[] _data;
	
	/** Image width. */
	private final int _width;
	
	/** Image height. */
	private final int _height;
	
	/** Is this image mutable? */
	private final boolean _mutable;
	
	/** Does this have an alpha channel? */
	private final boolean _alpha;
	
	Image()
	{
		throw Debugging.todo();
	}
	
	/**
	 * Initializes the image with the given settings.
	 *
	 * @param __data The image data, this is used directly.
	 * @param __w The image width.
	 * @param __h The image height.
	 * @param __mut If this image is mutable
	 * @param __alpha If this image has an alpha channel.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/02/10
	 */
	Image(int[] __data, int __w, int __h, boolean __mut, boolean __alpha)
	{
		// Check
		if (__data == null)
			throw new NullPointerException("NARG");
		
		// Set
		this._data = __data;
		this._width = __w;
		this._height = __h;
		this._mutable = __mut && !this.isAnimated() && !this.isScalable();
		this._alpha = __alpha;
		
		// If no alpha, set upper channel to full opaqueness
		if (!__alpha)
			for (int i = 0, n = __data.length; i < n; i++)
				__data[i] |= 0xFF_000000;
	}
	
	@Api
	public final void getARGB16(short[] __data, int __off, int __scanlen,
		int __x, int __y, int __w, int __h)
	{
		throw Debugging.todo();
	}
	
	/**
	 * This obtains the graphics interface which is used to draw on top of
	 * a mutable image.
	 *
	 * It defaults to:
	 * The clipping region covers the entire image.
	 * The color is a fully opaque black.
	 * The blending mode is {@link Graphics#SRC_OVER}.
	 * The stroke is {@link Graphics#SOLID}.
	 * The font is the default font.
	 * The coordinate origin is the top-left corner.
	 *
	 * The blending mode may only be changed to {@link Graphics#SRC} if the
	 * image has an alpha channel.
	 *
	 * @return A new graphics drawer.
	 * @throws IllegalStateException If the image is not mutable.
	 * @since 2017/02/10
	 */
	@Api
	public final Graphics getGraphics()
		throws IllegalStateException
	{
		/* {@squirreljme.error EB28 Cannot get mutable graphic operations for
		an immutable image.} */
		if (!this.isMutable())
			throw new IllegalStateException("EB28");
		
		// Create hardware accelerated graphics where possible
		return PencilGraphics.hardwareGraphics(
			(this._alpha ? UIPixelFormat.INT_RGBA8888 :
				UIPixelFormat.INT_RGB888),
			this._width, this._height,
			this._data, 0, null,
			0, 0, this._width, this._height);
	}
	
	/**
	 * Returns the image height.
	 *
	 * @return The height of the image.
	 * @since 2017/02/10
	 */
	@Api
	public final int getHeight()
	{
		return this._height;
	}
	
	/**
	 * Copies RGB image data from the source image.
	 *
	 * The source image data must be within the bounds of the image.
	 *
	 * All written pixels will have an alpha value regardless if the image has
	 * an alpha channel or not. In the case the image has no alpha channel then
	 * all read pixels will have a value of {@code 0xFF} as their alpha
	 * channel.
	 *
	 * @param __b The destination array.
	 * @param __o The offset into the array.
	 * @param __sl The scanline length of the destination array, this value may
	 * be negative to indicate that pixels are placed in reverse order.
	 * @param __x The source X position.
	 * @param __y The source Y position.
	 * @param __w The width to copy, if this is zero nothing is copied.
	 * @param __h The height to copy, if this is zero nothing is copied.
	 * @throws ArrayIndexOutOfBoundsException If writing to the destination
	 * buffer would result in a write that exceeds the bounds of the array.
	 * @throws IllegalArgumentException If the source X or Y position is
	 * negative; If the source region exceeds the image bounds; If the absolute
	 * value of the scanline length is lower than the width.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/02/11
	 */
	@Api
	public final void getRGB(int[] __b, int __o, int __sl, int __x, int __y,
		int __w, int __h)
		throws ArrayIndexOutOfBoundsException, IllegalArgumentException,
			NullPointerException
	{
		// Do nothing
		if (__w <= 0 || __h <= 0)
			return;
		
		// Scalable images must be rasterized first
		if (this.isScalable())
			throw Debugging.todo();
			
		// Check
		if (__b == null)
			throw new NullPointerException("NARG");
		
		/* {@squirreljme.error EB29 The source coordinates are negative.} */
		if (__x < 0 || __y < 0)
			throw new IllegalArgumentException("EB29");
	
		/* {@squirreljme.error EB2a The absolute value of the scanline length
		exceeds the read width.} */
		int scanLen = Math.abs(__sl);
		if (scanLen < __w)
			throw new IllegalArgumentException("EB2a");
		
		/* {@squirreljme.error EB2b Reading of RGB data would exceed the bounds
		out the output array.} */
		int areaPix = __w * __h;
		int areaScan = __sl * __h;
		if (__o < 0 || (__o + areaScan) > __b.length || (__o + areaScan) < 0)
			throw new ArrayIndexOutOfBoundsException("EB2b");
		
		/* {@squirreljme.error EB2c The area to read exceeds the bounds of the
		image.} */
		int ex = __x + __w,
			ey = __y + __h;
		int iw = this._width,
			ih = this._height;
		if (ex > iw || ey > ih)
			throw new IllegalArgumentException("EB2c");
		
		// If the alpha channel is not used then all RGB data is forced to
		// be fully opaque
		boolean alpha = this._alpha;
		int opaqueMask = (this._alpha ? 0 : 0xFF_000000);
		
		// Read image data
		int[] data = this._data;
		for (int sy = __y, wy = 0; sy < ey; sy++, wy++)
		{
			// Calculate offsets
			int srcoff = (iw * sy) + __x;
			int dstoff = __o + (wy * __sl);
			
			// Copy data, arraycopy is much faster of an operation!
			/*for (int sx = __x; sx < ex; sx++)
				__b[dstoff++] = data[srcoff++] | opqmask;*/
			System.arraycopy(data, srcoff,
				__b, dstoff, ex - __x);
			
			// If not using alpha, then force all pixels to have the given
			// alpha mask
			if (!alpha)
				for (int sx = __x; sx < ex; sx++)
					__b[dstoff++] |= opaqueMask;
		}
	}
	
	@Api
	public final void getRGB16(short[] __data, int __off, int __scanlen,
		int __x, int __y, int __w, int __h)
	{
		throw Debugging.todo();
	}
	
	/**
	 * Returns the image width.
	 *
	 * @return The width of the image.
	 * @since 2017/02/10
	 */
	@Api
	public final int getWidth()
	{
		return this._width;
	}
	
	/**
	 * Returns {@code true} if this image has an alpha channel.
	 *
	 * @return {@code true} if this image has an alpha channel.
	 * @since 2017/02/10
	 */
	@Api
	public final boolean hasAlpha()
	{
		return this._alpha;
	}
	
	/**
	 * Returns {@code true} if this image is animated.
	 *
	 * @return {@code true} if this image is animated.
	 * @since 2017/02/10
	 */
	@Api
	public final boolean isAnimated()
	{
		return (this instanceof AnimatedImage);
	}
	
	/**
	 * Returns {@code true} if this image is mutable.
	 *
	 * @return {@code true} if this image is mutable.
	 * @since 2017/02/10
	 */
	@Api
	public final boolean isMutable()
	{
		return this._mutable && !this.isAnimated() && !this.isScalable();
	}
	
	/**
	 * Returns {@code true} if this image is scalable.
	 *
	 * @return {@code true} if this image is scalable.
	 * @since 2017/02/10
	 */
	@Api
	public final boolean isScalable()
	{
		return (this instanceof ScalableImage);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/01/26
	 */
	@Override
	public final int squirreljmeDirectOffset()
	{
		if (this.squirreljmeIsDirect())
			return 0;
		return Integer.MIN_VALUE;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/01/26
	 */
	@Override
	public final int[] squirreljmeDirectRGBInt()
	{
		if (this.squirreljmeIsDirect())
			return this._data;
		return null;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/01/26
	 */
	@Override
	public final int squirreljmeDirectScanLen()
	{
		if (this.squirreljmeIsDirect())
			return this._width;
		return Integer.MIN_VALUE;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/01/26
	 */
	@Override
	public final boolean squirreljmeIsDirect()
	{
		return !(this.isScalable() || this.isAnimated());
	}
	
	/**
	 * Loads the specified image from the specified byte array.
	 *
	 * @param __b The array to read from.
	 * @param __o The offset into the array.
	 * @param __l The length of the array.
	 * @return The loaded image.
	 * @throws ArrayIndexOutOfBoundsException If the offset and/or length are
	 * negative or exceed the array bounds.
	 * @throws IllegalArgumentException If the image could not be read.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/02/28
	 */
	@Api
	public static Image createImage(byte[] __b, int __o, int __l)
		throws ArrayIndexOutOfBoundsException, IllegalArgumentException,
			NullPointerException
	{
		// Check
		if (__b == null)
			throw new NullPointerException("NARG");
		
		// Could fail
		try
		{
			return Image.createImage(new ByteArrayInputStream(__b, __o, __l));
		}
		
		/* {@squirreljme.error EB2d Could not load the image data.} */
		catch (IOException e)
		{
			throw new IllegalArgumentException("EB2d", e);
		}
	}
	
	/**
	 * Same as {@code createImage(__w, __h, false, 0x00FFFFFF)}.
	 *
	 * @param __w The width of the image.
	 * @param __h The height of the image.
	 * @return The created image.
	 * @throws IllegalArgumentException If the requested image size has a zero
	 * or negative dimension.
	 * @since 2017/02/11
	 */
	@Api
	public static Image createImage(int __w, int __h)
		throws IllegalArgumentException
	{
		return Image.createImage(__w, __h, false, 0x00FFFFFF);
	}
	
	/**
	 * Creates a mutable image that may or may not have an alpha channel.
	 *
	 * @param __w The width of the image.
	 * @param __h The height of the image.
	 * @param __alpha Whether it has an alpha channel.
	 * @param __c The initial color to fill.
	 * @return The created image.
	 * @throws IllegalArgumentException If the requested image size has a zero
	 * or negative dimension.
	 * @since 2017/02/11
	 */
	@Api
	public static Image createImage(int __w, int __h, boolean __alpha, int __c)
		throws IllegalArgumentException
	{
		/* {@squirreljme.error EB2e Zero or negative image size requested.} */
		if (__w <= 0 || __h <= 0)
			throw new IllegalArgumentException("EB2e");
		
		// Setup buffer
		int area = __w * __h;
		int[] data = new int[area];
		
		// Fill with color, unless we asked for zero then nothing needs to
		// actually be done
		if (__c != 0)
			for (int i = 0; i < area; i++)
				data[i] = __c;
		
		// Create
		return new Image(data, __w, __h, true, __alpha);
	}
	
	/**
	 * Loads the image from the specified input stream.
	 *
	 * @param __is The stream to read image data from.
	 * @return The parsed image.
	 * @throws IOException If the image could not be read.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/02/28
	 */
	@Api
	public static Image createImage(InputStream __is)
		throws IOException, NullPointerException
	{
		// Check
		if (__is == null)
			throw new NullPointerException("NARG");
		
		// Parse the image
		return ImageReaderDispatcher.parse(__is);
	}
	
	/**
	 * This loads the specified resource as an image.
	 *
	 * @param __s The string to load the resource for.
	 * @return The created image.
	 * @throws IOException If the resource does not exist or the image data
	 * could not be decoded.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/02/28
	 */
	@Api
	public static Image createImage(String __s)
		throws IOException, NullPointerException
	{
		// Check
		if (__s == null)
			throw new NullPointerException("NARG");
		
		// Try loading it
		try (InputStream is = ActiveMidlet.get().getClass().
			getResourceAsStream(__s))
		{
			/* {@squirreljme.error EB2f The specified resource does not
			exist. (The resource name)} */
			if (is == null)
				throw new IOException(String.format("EB2f %s", __s));
			
			return Image.createImage(is);
		}
	}
	
	/**
	 * Creates an immutable image which is an exact copy of the other image. If
	 * the other image is scalable it will be rasterized with whatever
	 * dimensions the other image has. If the specified image is immutable then
	 * it will be returned.
	 *
	 * @param __i The image to copy.
	 * @return The resulting copy.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/06/24
	 */
	@Api
	public static Image createImage(Image __i)
		throws NullPointerException
	{
		if (__i == null)
			throw new NullPointerException("NARG");
		
		// Needs to be rendered
		if (__i instanceof ScalableImage)
			throw Debugging.todo();
		
		// Same otherwise
		else if (!__i._mutable)
			return __i;
		
		// Copy and make this immutable
		return new Image(__i._data.clone(), __i._width, __i._height,
			false, __i._alpha);
	}
	
	@Api
	public static Image createImage(Image __i, int __x, int __y,
		int __w, int __h, int __trans)
	{
		return Image.createImage(__i, __x, __y, __w, __h, __trans,
			__i.getWidth(), __i.getHeight());
	}
	
	@Api
	public static Image createImage(Image __i, int __x, int __y, int __w,
		int __h, int __trans, int __iw, int __ih)
	{
		throw Debugging.todo();
	}
	
	/**
	 * Creates an immutable image from the specified ARGB pixel array.
	 *
	 * @param __rgb The ARGB or RGB image data to use as the image data.
	 * @param __w The width of the image.
	 * @param __h The height of the image.
	 * @param __alpha If {@code true} then the alpha is processed, otherwise
	 * all pixels are treated as fully opaque.
	 * @return The created image.
	 * @throws IllegalArgumentException If the width or height is negative.
	 * @throws IndexOutOfBoundsException If the input array is too small.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/02/10
	 */
	@Api
	public static Image createRGBImage(int[] __rgb, int __w, int __h, boolean 
		__alpha)
		throws IllegalArgumentException, IndexOutOfBoundsException,
			NullPointerException
	{
		// Check
		if (__rgb == null)
			throw new NullPointerException("NARG");
		
		/* {@squirreljme.error EB2g Invalid image size. (The width;
		The height)} */
		int area = __w * __h;
		if (__w <= 0 || __h <= 0 || area <= 0)
			throw new IllegalArgumentException(String.format("EB2g %d %d",
				__w, __h));
		
		/* {@squirreljme.error EB2h The input integer buffer is shorter than
		the specified image area.} */
		int rgbLen = __rgb.length;
		if (rgbLen < area)
			throw new IndexOutOfBoundsException(String.format(
				"EB2h %d < %d", rgbLen, area));
		
		// Use a cloned copy of the pixel data?
		if (rgbLen == area)
			__rgb = __rgb.clone();
		
		// Otherwise initialize a new one
		else
		{
			int[] copy = new int[area];
			
			System.arraycopy(__rgb, 0,
				copy, 0, area);
			
			__rgb = copy;
		}
		
		// If there is no alpha channel, force all of it to be opaque
		if (!__alpha)
			for (int i = 0; i < area; i++)
				__rgb[i] |= 0xFF000000;
		
		// Setup image
		return new Image(__rgb, __w, __h, false, __alpha);
	}
}

