#!/bin/sh

# Copyright (c) 2021 The Linux Foundation. All rights reserved.
#
# Redistribution and use in source and binary forms, with or without
# modification, are permitted provided that the following conditions are
# met:
#     * Redistributions of source code must retain the above copyright
#       notice, this list of conditions and the following disclaimer.
#     * Redistributions in binary form must reproduce the above
#       copyright notice, this list of conditions and the following
#       disclaimer in the documentation and/or other materials provided
#       with the distribution.
#     * Neither the name of The Linux Foundation nor the names of its
#       contributors may be used to endorse or promote products derived
#       from this software without specific prior written permission.
#
# THIS SOFTWARE IS PROVIDED "AS IS" AND ANY EXPRESS OR IMPLIED
# WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
# MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NON-INFRINGEMENT
# ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS
# BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
# CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
# SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR
# BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
# WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE
# OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN
# IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

PATH=/sbin:/bin:/usr/sbin:/usr/bin

KDUMP_VMCORE_PATH="/data/crash/`date +"%Y-%m-%d"`"
MAKEDUMPFILE=/usr/bin/makedumpfile
MAKEDUMPFILE_ARGS="-d 31 -c"

mkdir /proc
mount -t proc proc /proc

do_mount_fs() {
	grep -q "$1" /proc/filesystems || return
	test -d "$2" || mkdir -p "$2"
	mount -t "$1" "$1" "$2"
}

do_save_dump()
{
	mkdir -p ${KDUMP_VMCORE_PATH}

	${MAKEDUMPFILE} ${MAKEDUMPFILE_ARGS} /proc/vmcore ${KDUMP_VMCORE_PATH}/vmcore-"`date +"%H:%M:%S"`"
	return $?
}

do_mount_fs sysfs /sys
do_mount_fs devtmpfs /dev
do_mount_fs tmpfs /tmp

mkdir -p /data
mount /dev/sda18 /data

if [ -s /proc/vmcore ]; then
	echo 1 > /proc/sys/kernel/kptr_restrict
	do_save_dump
	sync && echo c > /proc/sysrq-trigger
fi

exec sh

