/*
* Copyright (c) Qualcomm Technologies, Inc. and/or its subsidiaries.
* SPDX-License-Identifier: BSD-3-Clause-Clear
*/

SECTIONS
{
	. = ALIGN(16);
	__tasklet_start = .;
	rodata.data.tasklet : {
		KEEP(*(.data.tasklet))
	}
	__tasklet_end = .;
}
INSERT AFTER .rodata
