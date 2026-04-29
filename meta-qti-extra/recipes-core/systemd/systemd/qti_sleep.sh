#!/bin/sh
# Copyright (c) 2020, The Linux Foundation. All rights reserved.

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
#
# Changes from Qualcomm Innovation Center are provided under the following license:
#
# Copyright (c) 2022 Qualcomm Innovation Center, Inc. All rights reserved.
#
# Redistribution and use in source and binary forms, with or without
# modification, are permitted (subject to the limitations in the
# disclaimer below) provided that the following conditions are met:
#
#    * Redistributions of source code must retain the above copyright
#      notice, this list of conditions and the following disclaimer.
#
#    * Redistributions in binary form must reproduce the above
#      copyright notice, this list of conditions and the following
#      disclaimer in the documentation and/or other materials provided
#      with the distribution.
#
#    * Neither the name of Qualcomm Innovation Center, Inc. nor the names of its
#      contributors may be used to endorse or promote products derived
#      from this software without specific prior written permission.
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
#

usb_mode_file="/var/usb/usb_mode.txt"
usb_mounts_file="/persist/usb/usb_mounts.txt"
usb_dev_path="/sys/devices/platform/soc"

# /proc/self/mounts escapes space as \040 etc.
unescape_mnt() {
    echo "$1" | sed -e 's/\\040/ /g' -e 's/\\011/\t/g' -e 's/\\012/\n/g' -e 's/\\134/\\/g'
}

# Resolve symlink to real device node if needed
realdev() {
    d="$1"
    readlink -f "$d" 2>/dev/null || echo "$d"
}

# Check if this device is USB storage
is_usb_blockdev() {
    [ -b "$1" ] || return 1

    udevadm info --query=property --name="$1" 2>/dev/null | grep -q '^ID_BUS=usb' && return 0
    return 1
}

# Get filesystem UUID for a partition (sdh1 etc.)
get_uuid() {
    udevadm info --query=property --name="$1" 2>/dev/null \
        | sed -n 's/^ID_FS_UUID=//p' | head -n 1
}

# Mount with retries to survive USB enumeration delay after resume
mount_with_retry() {
    what="$1"  # UUID
    mp="$2"
    fs="$3"
    opts="$4"

    [ -d "$mp" ] || mkdir -p "$mp"

    n=0
    while [ $n -lt 5 ]; do
        if mount -t "$fs" -o "$opts" "$what" "$mp" 2>/dev/null; then
            return 0
        fi
        n=$((n + 1))
        sleep 1
    done
    return 1
}

case $1/$2 in
  pre/*)
    echo "Entering into $1/$2 ..."

    systemctl stop loc_launcher.service
    systemctl stop location_hal_daemon.service
    systemctl stop audiod.service
    if [ $2 == "hibernate" ]; then
        echo 0 > /sys/kernel/boot_adsp/boot
    fi

    # unload WLAN driver
    systemctl stop init_qti_wlan_auto.service

    # disable BT as hsuart could block suspend
    systemctl stop synergy.service

    # disable WLAN related app
    killall wpa_supplicant &
    PID_KW=$!
    killall hostapd &
    PID_KH=$!
    echo "wait killing... $PID_KW and $PID_KH"
    wait $PID_KW
    wait $PID_KH

    # save USB mounts (UUID) and unmount them
    : > "$usb_mounts_file"

    echo "Scanning /proc/self/mounts for USB mounts..."
    while read -r src mnt fs opts rest; do
        case "$src" in
            /dev/*)
                src_real="$(realdev "$src")"
                if is_usb_blockdev "$src_real"; then
                    mnt_real="$(unescape_mnt "$mnt")"
                    uuid="$(get_uuid "$src_real")"

                    if [ -n "$uuid" ]; then
                        echo "UUID=$uuid|$mnt_real|$fs|$opts" >> "$usb_mounts_file"
                        echo "SAVE: UUID=$uuid -> $mnt_real ($fs) opts=$opts"
                    fi
                fi
                ;;
        esac
    done < /proc/self/mounts

    if [ -s "$usb_mounts_file" ]; then
        echo "Unmounting saved USB mountpoints in reverse order..."
        tac "$usb_mounts_file" | while IFS="|" read -r key mnt_real fs opts; do
            echo "UMOUNT: $mnt_real"
            umount "$mnt_real"
        done
    else
        echo "No USB mounts detected; usb_mounts_file remains empty."
    fi

    # save all usb mode to file
    if [ ! -f "$usb_mode_file" ]; then
        touch $usb_mode_file
    else
        sed -i '1,$d' $usb_mode_file
    fi
    for dev in `ls $usb_dev_path | grep 'ssusb$'`
    do
        usb_mode=`cat $usb_dev_path/$dev/mode`
        echo $dev=$usb_mode >> $usb_mode_file
        echo none > $usb_dev_path/$dev/mode
    done
    ;;
  post/*)
    echo "Exiting from $1/$2 ..."

    # restore USB controller modes
    if [ ! -f "$usb_mode_file" ]; then
        echo "USB mode recover failed for $usb_mode_file dose not exist."
    else
        for line in `cat $usb_mode_file`
        do
            dev=`echo $line | awk -F '[=]' '{print $1}'`
            usb_mode=`echo $line | awk -F '[=]' '{print $2}'`
            echo $usb_mode > $usb_dev_path/$dev/mode
        done
        rm "$usb_mode_file"
    fi

    # restore USB mounts by UUID
    if [ -s "$usb_mounts_file" ]; then
        echo "Restoring USB mounts from $usb_mounts_file ..."
        while IFS="|" read -r key mnt_real fs opts; do
            [ -n "$key" ] || continue
            echo "MOUNT: $key -> $mnt_real ($fs) opts=$opts"

            if mount_with_retry "$key" "$mnt_real" "$fs" "$opts"; then
                echo "MOUNT OK: $key -> $mnt_real"
                continue
            fi
        done < "$usb_mounts_file"
        rm "$usb_mounts_file"
    else
        echo "USB mount restore skipped: $usb_mounts_file missing/empty"
    fi

    if [ $2 == "hibernate" ]; then
        echo 1 > /sys/kernel/boot_adsp/boot
    fi
    systemctl restart loc_launcher.service
    systemctl restart location_hal_daemon.service
    systemctl restart audiod.service

    # load WLAN
    systemctl restart init_qti_wlan_auto.service
    n=0
    while [ $n -le 5 ]
    do
        if [ $(ifconfig -a | grep wlan0 | wc -l) -ne 1 ];then
            echo "wlan0 is not ready!"
            let n++
            sleep 1
        else
            echo "wlan0 is ready!"
            break
        fi
    done

    # restart BT service
    systemctl restart synergy.service || true

    ;;
esac
