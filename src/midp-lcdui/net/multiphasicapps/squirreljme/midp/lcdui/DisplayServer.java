// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.midp.lcdui;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;
import javax.microedition.io.StreamConnectionNotifier;

/**
 * This is a class which implements the display server used by the LCDUI
 * interface. The client implementation communicates with this server.
 *
 * @since 2016/10/11
 */
public abstract class DisplayServer
	implements Runnable
{
	/** The URI the client uses to connect. */
	public static final String CLIENT_URI =
		"imc://*:net.multiphasicapps.squirreljme.midp.lcdui.DisplayServer:" +
		"0.0.2;authmode=false";
	
	/** The URI the server uses to host. */
	private static final String _SERVER_URI =
		"imc://:net.multiphasicapps.squirreljme.midp.lcdui.DisplayServer:" +
		"0.0.2;authmode=false";
	
	/** The display server thread. */
	protected final Thread thread;
	
	/** The server socket. */
	private final StreamConnectionNotifier _serversock;
	
	/** A connection count, just for threading. */
	private volatile int _conncount;
	
	/**
	 * Initializes the base display server using the default server.
	 *
	 * @since 2016/10/11
	 */
	public DisplayServer()
		throws IOException
	{
		this((StreamConnectionNotifier)Connector.open(_SERVER_URI));
	}
	
	/**
	 * Initializes the base display server using the given connection.
	 *
	 * @param __sc The connection to use.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/10/14
	 */
	public DisplayServer(StreamConnectionNotifier __sc)
		throws NullPointerException
	{
		// Check
		if (__sc == null)
			throw new NullPointerException("NARG");
		
		// Setup display server thread
		Thread thread;
		this.thread = (thread = new Thread(this, "SquirrelJMEDisplayServer"));
		
		// Server specific thread modification?
		modifyThread(thread);
		
		// Set server socket
		this._serversock = __sc;
		
		// Start it
		thread.start();
	}
	
	/**
	 * This may be used by an implementation of the display server to modify
	 * the thread behavior.
	 *
	 * The default implementation does nothing.
	 *
	 * @param __t The thread to modify.
	 * @since 2016/10/11
	 */
	protected void modifyThread(Thread __t)
	{
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/10/11
	 */
	@Override
	public final void run()
	{
		// Infinite loop
		try (StreamConnectionNotifier svsock = this._serversock)
		{
			for (;;)
				try (StreamConnection sock = svsock.acceptAndOpen())
				{
					// Create thread for the given socket
					Thread t = new Thread(new __Socket__(sock),
						"SquirrelJMEDisplayConnection-" + (++_conncount));
				
					// Start it
					t.start();
				}
			
				// Failed read, ignore
				catch (IOException e)
				{
				}
		}
		
		// {@squirreljme.error EB05 Fail to close the display server socket.}
		catch (IOException e)
		{
			throw new RuntimeException("EB05", e);
		}
	}
	
	/**
	 * Outputs the number of displays which are currently available to this
	 * display server.
	 *
	 * @param __out The output stream.
	 * @throws IOException On write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/10/14
	 */
	private void __commandNumDisplays(DataOutputStream __out)
		throws IOException, NullPointerException
	{
		// Check
		if (__out == null)
			throw new NullPointerException("NARG");
		
		throw new Error("TODO");
	}
	
	/**
	 * A display socket that has been initialized.
	 *
	 * @since 2016/10/13
	 */
	private final class __Socket__
		implements Runnable
	{
		/** The input for the socket. */
		protected final DataInputStream in;
		
		/** The output to the remote end. */
		protected final DataOutputStream out;
		
		/**
		 * Initializes the socket connection to a display.
		 *
		 * @param __sock The socket connection.
		 * @throws IOException On read/write errors.
		 * @throws NullPointerException On null arguments.
		 * @since 2016/10/13
		 */
		private __Socket__(StreamConnection __sock)
			throws IOException, NullPointerException
		{
			// Check
			if (__sock == null)
				throw new NullPointerException("NARG");
			
			// Open input and output
			this.in = __sock.openDataInputStream();
			this.out = __sock.openDataOutputStream();
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2016/10/13
		 */
		@Override
		public void run()
		{
			// Handle input
			try (DataInputStream in = this.in;
				DataOutputStream out = this.out)
			{
				// Handle every command possible
				for (;;)
				{
					// Read a command and handle it
					int command = in.readUnsignedByte();
					switch (command)
					{
							// Request the displays that are available and
							// are attached
						case DisplayProtocol.COMMAND_REQUEST_NUMDISPLAYS:
							__commandNumDisplays(out);
							break;
						
							// {@squirreljme.error EB07 Unknown display server
							// command. (The command ID)}
						default:
							throw new RuntimeException(String.format("EB07 %s",
								command));
					}
				
					// Flush the output so the commands are written
					out.flush();
				}
			}
			
			// {@squirreljme.error EB06 Read/write error handling display
			// command.}
			catch (IOException e)
			{
				throw new RuntimeException("EB06", e);
			}
			
			// Make sure cleanup happens
			finally
			{
			}
		}
	}
}

