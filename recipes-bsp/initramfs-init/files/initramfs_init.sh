#!/bin/sh

# Copyright (c) 2022 Qualcomm Innovation Center, Inc. All rights reserved.
#
# Redistribution and use in source and binary forms, with or without
# modification, are permitted (subject to the limitations in the
# disclaimer below) provided that the following conditions are met:
#
#     * Redistributions of source code must retain the above copyright
#       notice, this list of conditions and the following disclaimer.
#
#     * Redistributions in binary form must reproduce the above
#       copyright notice, this list of conditions and the following
#       disclaimer in the documentation and/or other materials provided
#       with the distribution.
#
#     * Neither the name of Qualcomm Innovation Center, Inc. nor the names of its
#       contributors may be used to endorse or promote products derived
#       from this software without specific prior written permission.
#
# NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE
# GRANTED BY THIS LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT
# HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED
# WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
# MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
# IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR
# ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
# DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE
# GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
# INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER
# IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR
# OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN
# IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.


PATH=/sbin:/bin:/usr/sbin:/usr/bin

#------------------------------------------------------------
# Below macro can be set by build scripts, if not,
# a default volume will be set in function SetArgs()

# System image partition name not including slot suffix
SYS_PART_NAME="SET_BY_SED"

# UBI partition name (This is used when CONFIG_MTD_UBI_GLUEBI=y)
UBI_PART_NAME="SET_BY_SED"

# Set UBI bad block percentage for current partition
MTD_UBI_BEB_LIMIT_PER1024="SET_BY_SED"

# UBI device number for system image
SYS_UBI_DEV_NUM="SET_BY_SED"

# root certificate key path
CERT_CA_PATH="SET_BY_SED"

#------------------------------------------------------------

# Temporary rootfs mount node
ROOT_MOUNT="/rootfs"

# Function return status
STATUS_OK=0
STATUS_ERR=1

WaitDevReady()
{
    local maxTrials=500

    while [ ! "$1" "$2" ]; do
        usleep 10000
        maxTrials=$( echo $(( ${maxTrials} - 1 )) )
        if [ ${maxTrials} -eq 0 ]; then
            return ${STATUS_ERR}
        fi
    done
    return ${STATUS_OK}
}

EarlySetup() {
    mkdir -p /proc
    mkdir -p /sys
    mount -t proc proc /proc
    mount -t sysfs sysfs /sys
    mount -t devtmpfs none /dev

    mkdir -p /run
    mkdir -p /var/run

    mkdir -p $ROOT_MOUNT
    return ${STATUS_OK}
}

SetArgs() {
    if [ "x${SYS_PART_NAME}" == x"SET_BY_SED" ]; then
        SYS_PART_NAME="system"
    fi
    if [ "x${UBI_PART_NAME}" == x"SET_BY_SED" ]; then
        UBI_PART_NAME="nad_ubi"
    fi
    if [ "x${MTD_UBI_BEB_LIMIT_PER1024}" == x"SET_BY_SED" ]; then
        MTD_UBI_BEB_LIMIT_PER1024="30"
    fi

    if [ "x${SYS_UBI_DEV_NUM}" == x"SET_BY_SED" ]; then
        SYS_UBI_DEV_NUM="0"
    fi

    if [ "x${CERT_CA_PATH}" == x"SET_BY_SED" ]; then
        CERT_CA_PATH="/etc/keys/x509_root.der"
    fi

    # Root image name
    DM_SYST_NAME="${SYS_PART_NAME}"

    return ${STATUS_OK}
}

MoveMountToSystem() {
    mount -n --move /proc ${ROOT_MOUNT}/proc
    mount -n --move /sys ${ROOT_MOUNT}/sys
    mount -n --move /dev ${ROOT_MOUNT}/dev
    return ${STATUS_OK}
}

SwitchToSystem() {
    exec switch_root ${ROOT_MOUNT} /sbin/init
    echo "switch_root failure: We are not expected to reach here..."
}

#
# Get device number with partition name
# $1 -- partition name
#
GetStorageDev() {
    local partition_name=$1

    DEV_NUM=`cat /proc/mtd | grep "\"${partition_name}\"" | cut -d ":" -f 1 | cut -b 4-`
    if [ -z "${DEV_NUM}" ]; then
        echo Error: GetStorageDev: Get device of ${partition_name} failed.
        return ${STATUS_ERR}
    fi
    return ${STATUS_OK}
}

#
# Mount the root file system
#
MountSystem () {
    local parti_name=${UBI_PART_NAME}

    if [ ! -e /bin/dd ]; then
        echo Error: cmd: /bin/dd not found
        return ${STATUS_ERR}
    fi

    GetStorageDev "${parti_name}"
    if [ $? -ne ${STATUS_OK} ]; then
        echo Error: GetStorageDev failed
        return ${STATUS_ERR}
    fi

    # Check if it is UBI partition
    if dd if=/dev/mtd${DEV_NUM} count=1 bs=4 2>/dev/null | grep 'UBI#' > /dev/null; then

        ubiattach -m ${DEV_NUM} -d ${SYS_UBI_DEV_NUM} -b ${MTD_UBI_BEB_LIMIT_PER1024}
        WaitDevReady "-e" "/sys/class/ubi/ubi${SYS_UBI_DEV_NUM}/volumes_count"
        if [ $? -ne ${STATUS_OK} ]; then
            echo Error: "/sys/class/ubi/ubi${SYS_UBI_DEV_NUM}/volumes_count" not found
            return ${STATUS_ERR}
        fi

        if grep 'recovery=' /proc/cmdline > /dev/null; then
            SYS_IMAGE_VOL=`cat /proc/cmdline | awk -F 'recovery=' '{print $2}' | awk '{print $1}'`
        else
            volcount=`cat /sys/class/ubi/ubi${SYS_UBI_DEV_NUM}/volumes_count`
            for vid in `seq 0 $volcount`; do
                WaitDevReady "-c" "/dev/ubi${SYS_UBI_DEV_NUM}_${vid}"
                if [ $? -ne ${STATUS_OK} ]; then
                    echo Error: wait UBI volume: /dev/ubi${SYS_UBI_DEV_NUM}_${vid} timeout
                    return ${STATUS_ERR}
                fi

                act_slot=`cat /proc/cmdline | sed 's/.*SLOT_SUFFIX=//' | awk '{print $1}'`
                fs_ab_name=${DM_SYST_NAME}${act_slot}
                name=`cat /sys/class/ubi/ubi${SYS_UBI_DEV_NUM}_${vid}/name`
                if [ "${name}" == "${DM_SYST_NAME}" ] || [ "${name}" == "${fs_ab_name}" ]; then
                    SYS_IMAGE_VOL=${vid}
                    break
                fi
            done
        fi
        if [ "${SYS_IMAGE_VOL}" == "" ]; then
            echo "Cannot get ${DM_SYST_NAME} volume."
            return ${STATUS_ERR}
        fi

        char_device=/dev/ubi${SYS_UBI_DEV_NUM}_${SYS_IMAGE_VOL}
        block_device=/dev/ubiblock${SYS_UBI_DEV_NUM}_${SYS_IMAGE_VOL}


        # Check if the image type is squashfs in UBI volume
        if dd if=${char_device}\
              count=1 bs=4 2>/dev/null | grep 'hsqs' > /dev/null; then

            if [ ! -e "${block_device}" ]; then
                ubiblock --create "${char_device}"
                WaitDevReady "-b" "${block_device}"
                if [ $? -ne ${STATUS_OK} ]; then
                    echo Error: MountSystem no device: ${block_device} found
                    return ${STATUS_ERR}
                fi
            fi

            if grep 'nad_avb=1' /proc/cmdline > /dev/null; then
                dm_verity_device=/dev/mapper/${DM_SYST_NAME}
                verified-boot -n ${DM_SYST_NAME} -d $block_device -k ${CERT_CA_PATH}
                if [ $? -ne 0 ] ; then
                    echo "Created dm-verity device ${dm_verity_device} failed."
                    return ${STATUS_ERR}
                fi
                WaitDevReady "-b" "${dm_verity_device}"
                if [ $? -ne 0 ]; then
                   echo "Failed to wait on ${dm_verity_device}, exiting."
                   return ${STATUS_ERR}
                else
                    block_device=${dm_verity_device}
                fi
            fi

            mount -t squashfs ${block_device} ${ROOT_MOUNT} -oro
            if [ $? -ne ${STATUS_OK} ]; then
                echo Error: mount 'squashfs ${block_device}' failed
                return ${STATUS_ERR}
            fi
        else
            # If not squashfs in UBI then take it as ubifs, for new image type, you need to add it
            mount -t ubifs "${char_device}" ${ROOT_MOUNT} -orw
            if [ $? -ne ${STATUS_OK} ]; then
                echo Error: mount 'ubifs ${char_device}' failed
                return ${STATUS_ERR}
            fi
        fi
    else
        echo This is not a ubi partition, not support yet
        return ${STATUS_ERR}
    fi
    return ${STATUS_OK}
}

MainBoot() {

    local tasks_list="
                    EarlySetup
                    SetArgs
                    MountSystem
                    MoveMountToSystem
                    SwitchToSystem
                    "
    for task in ${tasks_list}; do
        ${task}
        if [ $? -ne ${STATUS_OK} ]; then
            echo Error: ${task} failed
            return ${STATUS_ERR}
        else
            echo Init: ${task}
        fi
    done
}

MainBoot
echo "MainBoot Error: InitRamFS boot failed"

