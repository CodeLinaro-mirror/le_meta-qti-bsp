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

usb_mode_file="/var/usb/usb_mode.txt"
usb_dev_path="/sys/devices/platform/soc"

case $1/$2 in
  pre/*)
    echo "Entering into $2..."

    systemctl stop loc_launcher.service
    systemctl stop location_hal_daemon.service
    systemctl stop audiod.service
    if [ $2 == "hibernate" ]; then
        echo 0 > /sys/kernel/boot_adsp/boot
    fi

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

    systemctl stop init_qti_wlan_auto.service

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
    echo "Exiting from $2..."

    if [ ! -f "$usb_mode_file" ]; then
        echo "USB mode recover failed for $usb_mode_file dose not exist."
    else
        for line in `cat $usb_mode_file`
        do
            dev=`echo $line | awk -F '[=]' '{print $1}'`
            usb_mode=`echo $line | awk -F '[=]' '{print $2}'`
            echo $usb_mode > $usb_dev_path/$dev/mode
        done
    fi
    systemctl restart synergy.service

    if [ $2 == "hibernate" ]; then
        echo 1 > /sys/kernel/boot_adsp/boot
    fi
    systemctl restart loc_launcher.service
    systemctl restart location_hal_daemon.service
    systemctl restart audiod.service

    #load WLAN
    sleep 3
    systemctl restart init_qti_wlan_auto.service
    ;;
esac
