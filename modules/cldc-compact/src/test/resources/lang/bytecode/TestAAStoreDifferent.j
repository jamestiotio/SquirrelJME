; -*- Mode: Jasmin; indent-tabs-mode: t; tab-width: 4 -*-
; ---------------------------------------------------------------------------
; SquirrelJME
;     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
; ---------------------------------------------------------------------------
; SquirrelJME is under the Mozilla Public License Version 2.0.
; See license.mkd for licensing and copyright information.
; ---------------------------------------------------------------------------

.class public lang/bytecode/TestAAStoreDifferent
.super net/multiphasicapps/tac/TestSupplier

.method public <init>()V
	aload 0
	invokenonvirtual net/multiphasicapps/tac/TestSupplier/<init>()V
	return
.end method

.method public test()Ljava/lang/Object;
.limit stack 6

; Obtain array
	invokestatic lang/bytecode/ByteCodeUtil/makeStringArray()[Ljava/lang/String;
	dup
	
; Store to array
	bipush 3
	new java/lang/Integer
	dup 
	bipush 1
	invokenonvirtual java/lang/Integer/<init>(I)V
	aastore
	
; Return value
	areturn
.end method
