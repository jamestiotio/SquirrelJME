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

struct sjme_elevatorRunData
{
	/** The index type counts. */
	jint indexTypeCount[SJME_NUM_ELEVATOR_DO_TYPES];
	
	/** The current run. */
	sjme_elevatorRunCurrent current;
};

/**
 * Elevator function to do type.
 * 
 * @since 2023/11/11
 */
struct
{
	sjme_elevatorDoFunc func;
	sjme_elevatorDoType type;
} sjme_elevatorFuncToType[SJME_NUM_ELEVATOR_DO_TYPES] =
{
	{sjme_elevatorDoInit, SJME_ELEVATOR_DO_TYPE_INIT},
	{NULL, SJME_ELEVATOR_DO_TYPE_UNKNOWN}
};

jboolean sjme_elevatorAct(
	sjme_attrInNotNull sjme_elevatorState* inState,
	sjme_attrInNotNull const sjme_elevatorSet* inSet)
{
	jint dx, i;
	sjme_elevatorRunData data;
	sjme_elevatorDoType doType;
	
	/* Check. */
	if (inState == NULL || inSet == NULL)
		return JNI_FALSE;
	
	/* Confirm that the set is valid. */
	if (inSet->config == NULL)
		return sjme_die("Invalid configuration.");
		
	/* Initialize base data. */
	memset(&data, 0, sizeof(data));
	
	/* Go through each entry, stop at NULl. */
	for (dx = 0; inSet->order[dx] != NULL; dx++)
	{
		/* Always wipe the current data so it is fresh. */
		memset(&data.current, 0, sizeof(data.current));
		
		/* This index is just a straight through. */
		data.current.indexAll = dx;
		
		/* Find the type for this function. */
		doType = SJME_ELEVATOR_DO_TYPE_UNKNOWN;
		for (i = 0; i < SJME_NUM_ELEVATOR_DO_TYPES; i++)
			if (inSet->order[dx] == sjme_elevatorFuncToType[i].func)
			{
				doType = sjme_elevatorFuncToType[i].type;
				break;
			}
		
		/* Not found? */
		if (doType == SJME_ELEVATOR_DO_TYPE_UNKNOWN)
			return sjme_die("Could not find the type for do function.");
		
		/* Increment up the index for this. */
		data.current.type = doType;
		data.current.indexType = data.indexTypeCount[doType]++;
		
		/* Run configuration function to initialize the data set. */
		if (!inSet->config(inState, &data.current))
			return sjme_die("Configuration step failed at %d.", dx);
		
		/* Call do function to perform whatever test initialization. */
		if (!inSet->order[dx](inState, &data.current))
			return sjme_die("Do failed at %d.", dx);
	}
	
	/* Successful. */
	return JNI_TRUE;
}

jboolean sjme_elevatorDoInit(
	sjme_attrInNotNull sjme_elevatorState* inState,
	sjme_attrInNotNull sjme_elevatorRunData* inData)
{
	if (inState == NULL || inData == NULL)
		return JNI_FALSE;

	sjme_todo("Implement this?");
}
