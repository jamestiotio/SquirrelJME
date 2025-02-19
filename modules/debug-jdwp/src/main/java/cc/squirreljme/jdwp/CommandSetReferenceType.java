// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jdwp;

import cc.squirreljme.jdwp.views.JDWPViewType;

/**
 * Reference type command set.
 *
 * @since 2021/03/13
 */
public enum CommandSetReferenceType
	implements JDWPCommand
{
	/** Non-generic signature of a given type. */
	SIGNATURE(1)
	{
		/**
		 * {@inheritDoc}
		 * @since 2021/03/14
		 */
		@Override
		public JDWPPacket execute(JDWPController __controller,
			JDWPPacket __packet)
			throws JDWPException
		{
			return this.__signature(false, __controller, __packet);
		}
	},
	
	/** The class loader used on a class. */
	CLASS_LOADER(2)
	{
		/**
		 * {@inheritDoc}
		 * @since 2021/04/20
		 */
		@Override
		public JDWPPacket execute(JDWPController __controller,
			JDWPPacket __packet)
			throws JDWPException
		{
			Object type = __packet.readType(__controller, false);
			
			JDWPPacket rv = __controller.__reply(
				__packet.id(), ErrorType.NO_ERROR);
			
			// Make sure the class loader is loaded if used
			Object loader = __controller.viewType().classLoader(type);
			if (loader != null)
				__controller.state.items.put(loader);
			
			// Write the class loader identifier
			rv.writeId(System.identityHashCode(loader));
			
			return rv;
		}
	},
	
	/** Modifiers for class. */
	MODIFIERS(3)
	{
		/**
		 * {@inheritDoc}
		 * @since 2021/04/20
		 */
		@Override
		public JDWPPacket execute(JDWPController __controller,
			JDWPPacket __packet)
			throws JDWPException
		{
			Object type = __packet.readType(__controller, false);
			
			JDWPPacket rv = __controller.__reply(
				__packet.id(), ErrorType.NO_ERROR);
			
			rv.writeInt(__controller.viewType().flags(type));
			
			return rv;
		}
	},
	
	/** Fields without generic. */
	FIELDS(4)
	{
		/**
		 * {@inheritDoc}
		 * @since 2021/04/30
		 */
		@Override
		public JDWPPacket execute(JDWPController __controller,
			JDWPPacket __packet)
			throws JDWPException
		{
			return this.__fields(false, __controller, __packet);
		}
	},
	
	/** Methods without generic. */
	METHODS(5)
	{
		/**
		 * {@inheritDoc}
		 * @since 2021/04/30
		 */
		@Override
		public JDWPPacket execute(JDWPController __controller,
			JDWPPacket __packet)
			throws JDWPException
		{
			return this.__methods(false, __controller, __packet);
		}
	},
	
	/** Static field values. */
	STATIC_FIELD_VALUE(6)
	{
		/**
		 * {@inheritDoc}
		 * @since 2021/03/15
		 */
		@Override
		public JDWPPacket execute(JDWPController __controller,
			JDWPPacket __packet)
			throws JDWPException
		{
			// Which class does this refer to?
			JDWPViewType viewType = __controller.viewType();
			Object type = __packet.readType(__controller, false);
			
			// Read in all field indexes and check for their validity
			int numFields = __packet.readInt();
			int[] fields = new int[numFields];
			for (int i = 0; i < numFields; i++)
			{
				int fieldDx = __packet.readId();
				if (!viewType.isValidField(type, fieldDx))
					throw ErrorType.INVALID_FIELD_ID.toss(type, fieldDx);
				
				fields[i] = fieldDx;
			}
			
			JDWPPacket rv = __controller.__reply(
				__packet.id(), ErrorType.NO_ERROR);
			
			// Write field mappings
			rv.writeInt(numFields);
			for (int i = 0; i < numFields; i++)
				try (JDWPValue value = __controller.value())
				{
					// Determine the field type and its tag
					String fieldSig = viewType.fieldSignature(type, fields[i]);
					JDWPValueTag tag = JDWPValueTag.fromSignature(fieldSig);
					
					// Read the field value, fallback if not valid
					if (!viewType.readValue(type, fields[i], value))
						value.set(tag.defaultValue);
					
					// Always write as tagged value
					rv.writeValue(__controller, value, tag, false);
					
					// Store object for later use
					if (value.get() != null && tag.isObject)
						__controller.state.items.put(value.get());
				}
			
			return rv;
		}
	},
	
	/** Source file. */
	SOURCE_FILE(7)
	{
		/**
		 * {@inheritDoc}
		 * @since 2021/03/14
		 */
		@Override
		public JDWPPacket execute(JDWPController __controller,
			JDWPPacket __packet)
			throws JDWPException
		{
			Object type = __packet.readType(__controller, false);
			
			// Does this have a source file?
			String sourceFile = __controller.viewType().sourceFile(type);
			if (sourceFile == null)
				throw ErrorType.ABSENT_INFORMATION.toss(type,
					System.identityHashCode(type));
			
			JDWPPacket rv = __controller.__reply(
				__packet.id(), ErrorType.NO_ERROR);
			
			rv.writeString(sourceFile);
			
			return rv;
		}
	},
	
	/** Interfaces. */
	INTERFACES(10)
	{
		/**
		 * {@inheritDoc}
		 * @since 2021/03/14
		 */
		@Override
		public JDWPPacket execute(JDWPController __controller,
			JDWPPacket __packet)
			throws JDWPException
		{
			Object type = __packet.readType(__controller, false);
			
			// Get every interface
			Object[] interfaces = __controller.viewType().interfaceTypes(type);
			
			JDWPPacket rv = __controller.__reply(
				__packet.id(), ErrorType.NO_ERROR);
			
			// Write all the interfaces
			rv.writeInt(interfaces.length);
			for (Object impl : interfaces)
			{
				rv.writeObject(__controller, impl);
				
				// Record interface so it is known
				__controller.state.items.put(impl);
			}
			
			return rv;
		}
	},
	
	/** Class object of type. */
	CLASS_OBJECT(11)
	{
		/**
		 * {@inheritDoc}
		 * @since 2021/04/20
		 */
		@Override
		public JDWPPacket execute(JDWPController __controller,
			JDWPPacket __packet)
			throws JDWPException
		{
			Object type = __packet.readType(__controller, false);
			
			JDWPPacket rv = __controller.__reply(
				__packet.id(), ErrorType.NO_ERROR);
			
			Object instance = __controller.viewType().instance(type);
			__controller.state.items.put(instance);
			
			rv.writeObject(__controller, instance);
			
			return rv;
		}
	},
	
	/** Signature with generic (class). */
	SIGNATURE_WITH_GENERIC(13)
	{
		/**
		 * {@inheritDoc}
		 * @since 2021/03/13
		 */
		@Override
		public JDWPPacket execute(JDWPController __controller,
			JDWPPacket __packet)
			throws JDWPException
		{
			return this.__signature(true, __controller, __packet);
		}
	},
	
	/** Fields with generic types. */
	FIELDS_WITH_GENERIC(14)
	{
		/**
		 * {@inheritDoc}
		 * @since 2021/03/14
		 */
		@Override
		public JDWPPacket execute(JDWPController __controller,
			JDWPPacket __packet)
			throws JDWPException
		{
			return this.__fields(true, __controller, __packet);
		}
	},
	
	/** Methods with generic types. */
	METHODS_WITH_GENERIC(15)
	{
		/**
		 * {@inheritDoc}
		 * @since 2021/03/13
		 */
		@Override
		public JDWPPacket execute(JDWPController __controller,
			JDWPPacket __packet)
			throws JDWPException
		{
			return this.__methods(true, __controller, __packet);
		}
	},
	
	/** Class file version. */
	CLASS_FILE_VERSION(17)
	{
		/**
		 * {@inheritDoc}
		 * @since 2021/04/20
		 */
		@Override
		public JDWPPacket execute(JDWPController __controller,
			JDWPPacket __packet)
			throws JDWPException
		{
			Object type = __packet.readType(__controller, false);
			
			JDWPPacket rv = __controller.__reply(
				__packet.id(), ErrorType.NO_ERROR);
			
			// Always Java 1.7
			rv.writeInt(51);
			rv.writeInt(0);
			
			return rv;
		}
	},
	
	/* End. */
	;
	
	/** The ID of the packet. */
	public final int id;
	
	/**
	 * Returns the ID used.
	 * 
	 * @param __id The ID used.
	 * @since 2021/03/13
	 */
	CommandSetReferenceType(int __id)
	{
		this.id = __id;
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
	 * Handles standard or generic field information.
	 * 
	 * @param __generic Write generic fields?
	 * @param __controller The controller used.
	 * @param __packet The packet to read from.
	 * @return The resultant packet.
	 * @throws JDWPException If this is not valid.
	 * @since 2021/04/30
	 */
	JDWPPacket __fields(boolean __generic, JDWPController __controller,
		JDWPPacket __packet)
		throws JDWPException
	{
		// Which class does this refer to?
		JDWPViewType viewType = __controller.viewType();
		Object type = __packet.readType(__controller, false);
		
		JDWPPacket rv = __controller.__reply(
			__packet.id(), ErrorType.NO_ERROR);
		
		// Do not allow reading the fields of class of weird things can happen
		if ("Ljava/lang/Class;".equals(viewType.signature(type)))
		{
			rv.writeInt(0);
			return rv;
		}
		
		// Write number of fields
		int[] fields = viewType.fields(type);
		rv.writeInt(fields.length);
		
		// Write information on each method
		for (int fieldDx : fields)
		{
			// Information about the method
			rv.writeId(fieldDx);
			rv.writeString(viewType.fieldName(type, fieldDx));
			rv.writeString(viewType.fieldSignature(type, fieldDx));
			
			// Generics are not used in SquirrelJME, ignore
			if (__generic)
				rv.writeString("");
			
			// Modifier flags
			rv.writeInt(viewType.fieldFlags(type, fieldDx));
		}
		
		return rv;
	}
	
	/**
	 * Handles standard or generic method information.
	 * 
	 * @param __generic Write generic methods?
	 * @param __controller The controller used.
	 * @param __packet The packet to read from.
	 * @return The resultant packet.
	 * @throws JDWPException If this is not valid.
	 * @since 2021/04/30
	 */
	JDWPPacket __methods(boolean __generic,
		JDWPController __controller, JDWPPacket __packet)
		throws JDWPException
	{
		// Which class does this refer to?
		JDWPViewType viewType = __controller.viewType();
		Object type = __packet.readType(__controller, false);
		
		JDWPPacket rv = __controller.__reply(
			__packet.id(), ErrorType.NO_ERROR);
		
		// Write number of methods
		int[] methods = viewType.methods(type);
		rv.writeInt(methods.length);
		
		// Write information on each method
		for (int methodDx : methods)
		{
			// Information about the method
			rv.writeId(methodDx);
			rv.writeString(viewType.methodName(type, methodDx));
			rv.writeString(viewType.methodSignature(type, methodDx));
			
			// Generics are not used in SquirrelJME, ignore
			if (__generic)
				rv.writeString("");
			
			// Modifier flags
			rv.writeInt(viewType.methodFlags(type, methodDx));
		}
		
		return rv;
	}
	
	/**
	 * Returns the signature of a given type.
	 * 
	 * @param __generic Is this a generic signature request?
	 * @param __controller The controller used.
	 * @param __packet The packet to read from.
	 * @return The resultant packet.
	 * @throws JDWPException If this could not be handled.
	 * @since 2021/04/14
	 */
	JDWPPacket __signature(boolean __generic, JDWPController __controller,
		JDWPPacket __packet)
		throws JDWPException
	{
		Object type = __packet.readType(__controller, false);
		
		// Write the normal class signature
		JDWPPacket rv = __controller.__reply(
			__packet.id(), ErrorType.NO_ERROR);
		
		rv.writeString(__controller.viewType().signature(type));
		if (__generic)
			rv.writeString("");
		
		return rv;
	}
}
