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

    # set all usb mode to none
    echo none > /sys/devices/platform/soc/a600000.ssusb/mode
    echo none > /sys/devices/platform/soc/a800000.ssusb/mode
    echo none > /sys/devices/platform/soc/a400000.ssusb/mode
    ;;
  post/*)
    echo "Exiting from $2..."

    echo peripheral > /sys/devices/platform/soc/a600000.ssusb/mode
    echo host > /sys/devices/platform/soc/a800000.ssusb/mode
    echo host > /sys/devices/platform/soc/a400000.ssusb/mode

    systemctl restart synergy.service

    if [ $2 == "hibernate" ]; then
        echo 1 > /sys/kernel/boot_adsp/boot
    fi
    systemctl restart loc_launcher.service
    systemctl restart location_hal_daemon.service
    systemctl restart audiod.service
    ;;
esac
