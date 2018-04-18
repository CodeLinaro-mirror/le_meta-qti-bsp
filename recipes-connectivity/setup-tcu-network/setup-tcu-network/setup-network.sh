#!/bin/sh

# Copyright (c) 2018, The Linux Foundation. All rights reserved.
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

CMDLINE_PATH=/proc/cmdline
AGL1_IFACE_ARRAY=(eth0 eth1 eth2 eth3 eth4)
AGL2_IFACE_ARRAY=(eth0 eth1)

function get_gvm_version()
{
    local cmdline_value
    local system_name_value

    cmdline_value=$(cat $CMDLINE_PATH)
    system_name_value=${cmdline_value#*system_name=}
    system_name_value=${system_name_value%% *}
    echo "system_name_value=${system_name_value}!"
    case $system_name_value in
        agl_1) return 1
          ;;
        agl_2) return 2
          ;;
        *) return 0
          ;;
    esac
}

function check_all_interfaces_up()
{
    local iface_name
    local iface_cnt
    local iface_arr

    iface_arr=$1

    for iface_name in ${iface_arr[*]}
    do
        iface_cnt=$(ifconfig -a | grep $iface_name | wc -l)
        if [[ ${iface_cnt} -ne 1 ]]
        then
            echo " WARN : $iface_name is not Ready"
            return 0;
        fi
    done

    return 1;
}

function setup_network_agl_vm_1()
{
    echo 0 > /proc/sys/net/ipv4/ip_forward

    echo "Setup Bridge Network"
    brctl addbr tether0

    brctl addif tether0 eth1
    brctl addif tether0 eth2
    brctl addif tether0 eth3
    brctl addif tether0 eth4
    sleep 1

    echo "Assign IP address to eth0"
    ifconfig eth0 192.168.0.2 up
    ifconfig eth1 up
    ifconfig eth2 up
    ifconfig eth3 up
    ifconfig eth4 up
    sleep 1

    echo "Assign IP address to Bridge"
    ifconfig tether0 192.168.1.2 up

    local RETRY_CNT=0
    for i in {1..5}
    do
        sleep 1
        if [[ $(ip address show tether0 up | grep 'state UP' | wc -l) -ne 1 ]];
        then
            echo "tether0 is not up, try to bring up again !!"
            ifconfig tether0 192.168.1.2 up
            RETRY_CNT=$((RETRY_CNT+1))
            echo "retry ${RETRY_CNT} times ..."
        else
            echo "tether0 is up now"
            break
        fi
    done

    echo "Setup NAT rules"
    iptables -t nat -A POSTROUTING -s 192.168.1.0/24 -o rmnet_data0 -j MASQUERADE
    iptables -t nat -A POSTROUTING -s 192.168.1.0/24 -o wlan0 -j MASQUERADE

    echo 1 > /proc/sys/net/ipv4/ip_forward
}

function setup_network_agl_vm_2()
{
    echo "Assign IP address"
    ifconfig eth0 192.168.0.6 up
    ifconfig eth1 192.168.1.6 up

    echo "Setup route"
    ip route add default dev eth1 via 192.168.1.2
}

get_gvm_version
gvm_version=$?

if [[ ${gvm_version} -eq 0 ]]
then
    echo " ERR : this gvm is not expected"
    exit
fi
echo "current system is agl gvm $gvm_version"

# try 10 times
for i in {1..10}
do
    if [[ ${gvm_version} -eq 1 ]]
    then
        check_all_interfaces_up "${AGL1_IFACE_ARRAY[*]}"
        iface_is_up=$?
        echo "current interface status is ${iface_is_up}"
        if [[ "${iface_is_up}" -eq 1 ]]
        then
            setup_network_agl_vm_1
            break
        else
            echo " ERR : Ethernet Interfaces are not Ready !!!"
        fi
    else
        check_all_interfaces_up "${AGL2_IFACE_ARRAY[*]}"
        iface_is_up=$?
        echo "current interface status is ${iface_is_up}"
        if [[ "${iface_is_up}" -eq 1 ]]
        then
            setup_network_agl_vm_2
            break
        else
            echo " ERR : Ethernet Interfaces are not Ready !!!"
        fi
    fi

    sleep 2
done

