/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include <string.h>

#include "elevator.h"
#include "proto.h"
#include "sjme/nvmFunc.h"
#include "test.h"
#include "unit.h"

sjme_jboolean configNvmLocalPopInteger(
	sjme_attrInNotNull sjme_elevatorState* inState,
	sjme_attrInNotNull sjme_elevatorRunCurrent* inCurrent)
{
	/* Check. */
	if (inState == NULL || inCurrent == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Configure. */
	switch (inCurrent->type)
	{
		case SJME_ELEVATOR_DO_TYPE_MAKE_FRAME:
			inCurrent->data.frame.maxLocals = 1;
			inCurrent->data.frame.maxStack = 1;
			inCurrent->data.frame.treads[SJME_JAVA_TYPE_ID_INTEGER]
				.max = 2;
			inCurrent->data.frame.treads[SJME_JAVA_TYPE_ID_INTEGER]
				.stackBaseIndex = 1;
			break;
	}
	
	return SJME_JNI_TRUE;
}

/** Elevator set for test. */
static const sjme_elevatorSet elevatorNvmLocalPopInteger =
{
	configNvmLocalPopInteger,
	0,

	/* Elevator calls. */
	{
		sjme_elevatorDoInit,
		sjme_elevatorDoMakeThread,
		sjme_elevatorDoMakeFrame,
		NULL
	}
};

sjme_attrUnused SJME_TEST_DECLARE(testNvmLocalPopInteger)
{
	sjme_elevatorState state;
	sjme_nvm_frame* frame;
	sjme_jint oldNumStack;
	sjme_nvm_frameTread* intsTread;
	sjme_nvm_frameStack* stack;
	
	/* Perform the elevator. */
	memset(&state, 0, sizeof(state));
	if (!sjme_elevatorAct(&state,
		&elevatorNvmLocalPopInteger, 0))
		sjme_die("Invalid elevator");
		
	/* Get initialize frame size. */
	frame = state.threads[0].nvmThread->top;
	
	/* Setup integer values. */
	intsTread = frame->treads[SJME_JAVA_TYPE_ID_INTEGER];
	stack = frame->stack;
	intsTread->values.jints[1] = 0x12345678;
	intsTread->count = intsTread->stackBaseIndex + 1;
	stack->count = 1;
	stack->order[0] = SJME_JAVA_TYPE_ID_INTEGER;
	
	/* Pop integer from the stack to the first local. */
	oldNumStack = stack->count;
	if (!sjme_nvm_localPopInteger(frame, 0))
		return sjme_unitFail(test, "Failed to pop local integer.");
	
	/* New stack should be lower. */
	sjme_unitEqualI(test, stack->count, oldNumStack - 1,
		"Items in stack not lower?");
	
	/* Check that the value was moved over. */
	sjme_unitEqualI(test, 0x12345678, intsTread->values.jints[0],
		"Popped stack into local was not the correct value.");
		
	/* And the stack value was cleared. */
	sjme_unitEqualI(test, 0, intsTread->values.jints[1],
		"Stack value did not get cleared.");
	
	/* Success! */
	return SJME_TEST_RESULT_PASS;
}
