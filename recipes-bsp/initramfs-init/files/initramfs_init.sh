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
SYS_PART_NAME="system"

# UBI partition name (This is used when CONFIG_MTD_UBI_GLUEBI=y)
UBI_PART_NAME="nad_ubi"

# Set UBI bad block percentage for current partition
MTD_UBI_BEB_LIMIT_PER1024="30"

# UBI device number for system image
SYS_UBI_DEV_NUM="0"

# root certificate key path
CERT_CA_PATH="/etc/keys/x509_root.der"

# FDE Encryption path
FDE_ENCRYPTION_PATH="/tmp"

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

    mkdir -p /tmp
    mount -t tmpfs tmpfs /tmp

    mkdir -p /run
    mkdir -p /var/run

    mkdir -p ${ROOT_MOUNT}
    return ${STATUS_OK}
}

SetArgs() {

    # Root image name
    DM_SYST_NAME="${SYS_PART_NAME}"

    return ${STATUS_OK}
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

GetUbiVolumeID () {
    local ubi_dev_number=$1
    local ubi_vol_name=$2
    GetUbiVolumeID_RESULT=""

    act_slot=`cat /proc/cmdline | sed 's/.*SLOT_SUFFIX=//' | awk '{print $1}'`
    fs_ab_name=${ubi_vol_name}${act_slot}
    volcount=`cat /sys/class/ubi/ubi${ubi_dev_number}/volumes_count`

    for vid in `seq 0 ${volcount}`; do
        WaitDevReady "-c" "/dev/ubi${ubi_dev_number}_${vid}"
        if [ $? -ne ${STATUS_OK} ]; then
            echo Error: wait UBI volume: /dev/ubi${ubi_dev_number}_${vid} timeout
            return ${STATUS_ERR}
        fi

        name=`cat /sys/class/ubi/ubi${ubi_dev_number}_${vid}/name`
        if [ "${name}" == "${ubi_vol_name}" ] || [ "${name}" == "${fs_ab_name}" ]; then
            GetUbiVolumeID_RESULT=${vid}
            break
        fi
    done
}

CheckTmpDirectorySizeForFde () {
    local ubi_dev_num=${1}
    local ubi_vol_num=${2}

    local tmp_directory_size=`df -m | grep "/tmp" | awk '{print $4}'`
    local ubi_vol_size=`cat /sys/class/ubi/ubi${ubi_dev_num}_${ubi_vol_num}/data_bytes`
    let ubi_vol_size=ubi_vol_size/1024/1024+10

    if [ ${ubi_vol_size} -gt ${tmp_directory_size} ]; then
        let ubi_vol_size=ubi_vol_size-5
        if [ ${ubi_vol_size} -gt ${tmp_directory_size} ]; then
            echo "Err: Not enough RAM size for FDE."
            return ${STATUS_ERR}
        else
            echo "Warning: The RAM size is insufficient for FDE."
        fi
    fi
    return ${STATUS_OK}
}

# nad-fde-app -t ${device_name} -n ${char_device} -d ${block_device} -x ${encryption_path} -k ${key_index} -B ${BS}
# nad-fde-app return:
# 0 -- Run encryption and/or decryption successful
# 1 -- Fuse not blown, continue as normal boot
# 2 -- Failed
NadFdeApp () {
    local device_name=$1
    local char_device=$2
    local block_device=$3
    local encryption_path=$4
    local key_index=$5
    local bs_size=$6

    FDE_DEV_PATH="/dev/mapper/${device_name}"

    if [ ! -e "${key_index}" ]; then
        echo NadFde App: fuse is NOT blown.
        echo Can not find key file: ${key_index}
        nad_fde_result=1
        return 1
    fi

    # Check if the image type is squashfs in UBI volume
    if dd if=${char_device}\
                  count=1 bs=4 2>/dev/null | grep 'hsqs' > /dev/null; then
       echo "NOT encrypted ${device_name}"

       if [ "${encryption_path}" == "" ]; then
           encryption_path="/tmp"
           echo "Using /tmp for ${device_name} encryption"
       fi
       mkdir -p ${encryption_path}

       echo "Preparing ${device_name} image"
       dd if=/dev/zero of=${encryption_path}/test.img bs=${bs_size} count=4096
       if [ $? -ne ${STATUS_OK} ]; then
           echo Error: NadFde App create test.img failed.
           nad_fde_result=2
           return 2
       fi
       sync

       losetup -f ${encryption_path}/test.img

       echo "Creating encrypted partition"
       cryptsetup --cipher=aes-cbc-essiv:sha256 --offset=0 --key-file=${key_index} --key-size=256 open --type plain ${encryption_path}/test.img ${device_name}
       if [ $? -ne ${STATUS_OK} ]; then
           echo Error: NadFde App create FDE interface failed.
           nad_fde_result=2
           return 2
       fi

       echo "Encrypting ${device_name}"
       dd if=${char_device} of=${FDE_DEV_PATH} bs=${bs_size} count=4096
       if [ $? -ne ${STATUS_OK} ]; then
           echo Error: NadFde App encryption failed.
           nad_fde_result=2
           return 2
       fi

       cryptsetup close ${device_name}
       if [ $? -ne ${STATUS_OK} ]; then
           echo Error: NadFde App close FDE interface ${device_name} failed.
           nad_fde_result=2
           return 2
       fi
       sync

       echo "Release loop device"
       LOOP_DEVICE=`losetup -a | grep test.img | awk -F ":" '{print $1}'`
       losetup -d ${LOOP_DEVICE}

       echo "Update ${char_device}"
       ubiupdatevol ${char_device} ${encryption_path}/test.img
       if [ $? -ne ${STATUS_OK} ]; then
           echo Error: NadFde App update encrypted image to ${char_device} failed.
           nad_fde_result=2
           return 2
       fi

       rm -rf ${encryption_path}/test.img
    fi

    echo "Encrypted ${device_name}"
    cryptsetup --cipher=aes-cbc-essiv:sha256 --offset=0 --key-file=${key_index} --key-size=256 open --type plain ${block_device} ${device_name}
    if [ $? -ne ${STATUS_OK} ]; then
        echo Error: NadFde App enable FDE failed.
        nad_fde_result=2
        return 2
    fi
    nad_fde_result=0
    return 0
}

# device_name - The name of the partition
# ubi_dev_num - UBI device number
# ubi_vol_num - UBI volume number
# key_index - The key use for partition encryption
EncryptUbiPartition () {
    local device_name=${1}
    local ubi_dev_num=${2}
    local ubi_vol_num=${3}
    local key_index=${4}
    local bs_size=${5}

    local char_device=""
    local block_device=""
    local encryption_path=${FDE_ENCRYPTION_PATH}

    if [ "${ubi_vol_num}" == "null" ]; then
        GetUbiVolumeID ${ubi_dev_num} ${device_name}
        if [ "${GetUbiVolumeID_RESULT}" == "" ]; then
            echo "Cannot get ${device_name} volume."
            return ${STATUS_ERR}
        fi
        ubi_vol_num=${GetUbiVolumeID_RESULT}
    fi

    char_device=/dev/ubi${ubi_dev_num}_${ubi_vol_num}
    block_device=/dev/ubiblock${ubi_dev_num}_${ubi_vol_num}

    CheckTmpDirectorySizeForFde ${ubi_dev_num} ${ubi_vol_num}
    if [ $? -ne ${STATUS_OK} ]; then
        echo Error: Not enough RAM size for ${device_name} encryption.
        return ${STATUS_ERR}
    fi

    if [ ! -e "${block_device}" ]; then
        ubiblock --create "${char_device}"
        WaitDevReady "-b" "${block_device}"
        if [ $? -ne ${STATUS_OK} ]; then
            echo Error: EncryptUbiPartition no device: ${block_device} found
            return ${STATUS_ERR}
        fi
    fi

    # nad-fde-app return:
    # 0 -- Run encryption and/or decryption successful
    # 1 -- Fuse not blown, continue as normal boot
    # 2 -- Failed
    #nad-fde-app -n ${char_device} -d ${block_device} -m ${FDE_DEV_PATH} -t ${device_name} -b ${BS} -c ${KCOUNT}
    NadFdeApp ${device_name} ${char_device} ${block_device} ${encryption_path} ${key_index} ${bs_size}
    #nad_fde_result=$?
    echo nad_fde_result=${nad_fde_result}
    case "${nad_fde_result}" in
        0)
            echo FDE enabled for ${device_name}.
            FDE_MOUNT_NODE="/dev/mapper/${device_name}"
            return ${STATUS_OK}
        ;;
        1)
            echo FDE fuse bit not blown.
            FDE_MOUNT_NODE=${block_device}
            return ${STATUS_OK}
        ;;
        *)
            echo FDE encryption failed.
            FDE_MOUNT_NODE=""
            return ${STATUS_ERR}
        ;;
    esac
}

EncryptNotSysPartition () {
    local fde_parti_list=`cat /proc/cmdline | sed 's/ /\n/g' | grep -E "fde\."`

    for parti in ${fde_parti_list}; do
        parti_name=`echo ${parti} | awk -F "." '{print $2}'`
        key_index=`echo ${parti} | awk -F "." '{print $3}'`
        echo parti_name=${parti_name} key_index=${key_index}

        if [ "${DM_SYST_NAME}" == "${parti_name}" ]; then
            continue
        fi
        key_index="/etc/keys/keyfile"
        EncryptUbiPartition ${parti_name} \
                            ${SYS_UBI_DEV_NUM} \
                            "null" \
                            ${key_index} \
                            "498"
        if [ $? -ne ${STATUS_OK} ] ; then
            echo "Encrypt partition ${parti_name} failed."
            return ${STATUS_ERR}
        fi
    done
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
# Mount the root file system
#
MountSystem () {
    local parti_name=${UBI_PART_NAME}
    local image_type="ubifs"
    local nad_fde_status="disabled"
    local sys_key_index="0"

    if [ ! -e /bin/dd ]; then
        echo Error: cmd: /bin/dd not found
        return ${STATUS_ERR}
    fi

    GetStorageDev "${parti_name}"
    if [ $? -ne ${STATUS_OK} ]; then
        echo Error: GetStorageDev failed "DEV_NUM=${DEV_NUM}"
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
            GetUbiVolumeID ${SYS_UBI_DEV_NUM} ${DM_SYST_NAME}
            if [ "${GetUbiVolumeID_RESULT}" == "" ]; then
                echo "Cannot get ${DM_SYST_NAME} volume."
                return ${STATUS_ERR}
            fi
            SYS_IMAGE_VOL=${GetUbiVolumeID_RESULT}
        fi
        if [ "${SYS_IMAGE_VOL}" == "" ]; then
            echo "Cannot get ${DM_SYST_NAME} volume."
            return ${STATUS_ERR}
        fi

        char_device=/dev/ubi${SYS_UBI_DEV_NUM}_${SYS_IMAGE_VOL}
        block_device=/dev/ubiblock${SYS_UBI_DEV_NUM}_${SYS_IMAGE_VOL}
        echo char_device: ${char_device} block_device: ${block_device}

        if dd if=${char_device}\
            count=1 bs=4 2>/dev/null | grep 'hsqs' > /dev/null; then
            image_type="squashfs"
        fi

        if grep 'nad_fde=1' /proc/cmdline > /dev/null; then

            # 4+4 device has not enough ram size for FDE encryption
            # So, skip encryption if the memory is smaller than 512
            supported_mem_size=`free -m | grep Mem | awk '{print $4}'`
            echo supported_mem_size=${supported_mem_size}
            if [ ${supported_mem_size} -gt 512 ]; then

                #InitialEnvForFde

                # Encrypt other images first, and later will encrypt the root file system.
                EncryptNotSysPartition
                if [ $? -ne ${STATUS_OK} ]; then
                    echo Error: MountSystem no device: ${block_device} found
                    return ${STATUS_ERR}
                fi

                nad_fde_status="enabled"

                # Currently, only squashfs image is supported for FDE. If the image magic
                # isn't squashfs type "hsqs", then suppose this partition was encrypted.
                image_type="squashfs"
            else
                echo "Warning: Memory size is too small, skip FDE."
            fi
        fi

        # Check if the image type is squashfs in UBI volume
        if [ "${image_type}" == "squashfs" ] || [ "${nad_fde_status}" == "enabled" ; then

            if [ ! -e "${block_device}" ]; then
                ubiblock --create "${char_device}"
                WaitDevReady "-b" "${block_device}"
                if [ $? -ne ${STATUS_OK} ]; then
                    echo Error: MountSystem no device: ${block_device} found
                    return ${STATUS_ERR}
                fi
            fi

            # For root file system FDE enabling
            if [ "${nad_fde_status}" == "enabled" ]; then

                #sys_key_index=`cat /proc/cmdline | sed 's/ /\n/g' |\
                #                    grep -E 'fde\.system' | awk -F '.' '{print $3}'`
                sys_key_index="/etc/keys/keyfile"
                #sys_key_index="/cache/keyfile"

                EncryptUbiPartition ${DM_SYST_NAME} \
                                    ${SYS_UBI_DEV_NUM} \
                                    ${SYS_IMAGE_VOL} \
                                    ${sys_key_index} \
                                    "16080"
                if [ $? -ne ${STATUS_OK} ] ; then
                    echo "Encrypt partition ${DM_SYST_NAME} failed."
                    return ${STATUS_ERR}
                fi
                block_device=${FDE_MOUNT_NODE}

                echo fde: block_device=${block_device}
            # For root file system Verified boot enabling
            elif grep 'nad_avb=1' /proc/cmdline > /dev/null; then
                dm_verity_device=/dev/mapper/${DM_SYST_NAME}
                verified-boot -n ${DM_SYST_NAME} -d ${block_device} -k ${CERT_CA_PATH}
                if [ $? -ne ${STATUS_OK} ] ; then
                    echo "Created dm-verity device ${dm_verity_device} failed."
                    return ${STATUS_ERR}
                fi
                WaitDevReady "-b" "${dm_verity_device}"
                if [ $? -ne ${STATUS_OK} ]; then
                   echo "Failed to wait on ${dm_verity_device}, exiting."
                   return ${STATUS_ERR}
                else
                    block_device=${dm_verity_device}
                fi
            fi

            mount -t ${image_type} ${block_device} ${ROOT_MOUNT} -oro
            if [ $? -ne ${STATUS_OK} ]; then
                echo Error: mount 'squashfs ${block_device}' failed
                return ${STATUS_ERR}
            fi
        else
            # If not squashfs in UBI then take it as ubifs, for new image type, you need to add it
            mount -t ${image_type} "${char_device}" ${ROOT_MOUNT} -oro
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
