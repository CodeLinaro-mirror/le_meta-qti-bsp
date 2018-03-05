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

IFACE_ARRAY=(eth0 eth1 eth2 eth3 eth4)

function check_all_interfaces_up()
{
    local iface_name
    local iface_cnt

    for iface_name in ${IFACE_ARRAY[@]}
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


# try 10 times
for i in {1..10}
do
    check_all_interfaces_up
    iface_is_up=$?
    echo "current interface is ${iface_is_up}"
    if [[ "${iface_is_up}" -eq 1 ]]
    then
        echo "Configure Ethernet Interfaces via Connman Tool"
        connmanctl config ethernet_aaaaaaaaaa0a_cable --ipv4 manual 192.168.0.2 255.255.255.0 192.168.0.3
        connmanctl config ethernet_aaaaaaaaaa1a_cable --ipv4 off
        connmanctl config ethernet_aaaaaaaaaa2a_cable --ipv4 off
        connmanctl config ethernet_aaaaaaaaaa3a_cable --ipv4 off
        connmanctl config ethernet_aaaaaaaaaa4a_cable --ipv4 off
        sleep 1

        echo "Setup Bridge Network"
        brctl addbr tether0

        brctl addif tether0 eth1
        brctl addif tether0 eth2
        brctl addif tether0 eth3
        brctl addif tether0 eth4
        sleep 1

        echo "Assign IP address and setup NAT rules"
        ifconfig tether0 192.168.1.2 up

        iptables -t nat -A POSTROUTING -s 192.168.1.0/24 -o rmnet_data0 -j MASQUERADE

        break
    else
        echo " ERR : Ethernet Interfaces are not Ready !!!"
    fi
    sleep 2
done


