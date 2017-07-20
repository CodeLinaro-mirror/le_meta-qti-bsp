#!/bin/sh
# Copyright (c) 2017, The Linux Foundation. All rights reserved.
#
# Redistribution and use in source and binary forms, with or without
# modification, are permitted provided that the following conditions are
# met:
#     * Redistributions of source code must retain the above copyright
#	notice, this list of conditions and the following disclaimer.
#     * Redistributions in binary form must reproduce the above
#	copyright notice, this list of conditions and the following
#	disclaimer in the documentation and/or other materials provided
#	with the distribution.
#     * Neither the name of The Linux Foundation nor the names of its
#	contributors may be used to endorse or promote products derived
#	from this software without specific prior written permission.
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

#constants
INTERFACE_PATH="/sys/class/net/"
TIMEOUT=300 #Timeout in seconds for interface to come up

#MHI Interface for SWIP netdev
NETDEV0_ifname="mhi_swip0"
NETDEV1_ifname="mhi_swip1"
NETDEV2_ifname="mhi_swip2"

#IFALIAS for net device
NETDEV0_ifalias=""
NETDEV0_ifalias=""
NETDEV0_ifalias=""

#pcie bus# associated with netdevice
NETDEV0_bus=""
NETDEV1_bus=""
NETDEV2_bus=""

#host IP# for each NAD interface
NAD0_hostIP="192.168.1.1"
NAD1_hostIP="192.168.1.2"
NAD2_hostIP="192.168.1.3"

#NAD IP# for swip interface
NAD0_mdmIP="192.168.1.4"
NAD1_mdmIP="192.168.1.5"
NAD2_mdmIP="192.168.1.6"

#NADs associated bus
NAD0_bus=3
NAD1_bus=4
NAD2_bus=5

#NADS associated interface
NAD0_iface=""
NAD1_iface=""
NAD2_iface=""

#check if all three interfaces are up
function isUP() {
    local path
    local -i count=0

    #1st interface see if path exists
    path="$INTERFACE_PATH$NETDEV0_ifname"
    if [ -d $path ] ; then
	((count++))
    fi

    #2nd interface see if path exists
    path="$INTERFACE_PATH$NETDEV1_ifname"
    if [ -d "$path" ]; then
	((count++))
    fi;

    #3rd interface see if path exists
    path="$INTERFACE_PATH$NETDEV2_ifname"
    if [ -d "$path" ]; then
	((count++))
    fi;

    echo "Number of interface found " $count
    [ $count == 3 ]
}

#get ifalias and bus information for interface
function ifalias() {
    local -n interface=$1
    local -n ref_ifalias=$2
    local -n ref_bus=$3
    local ifname path
    local ifalias bus

    path="$INTERFACE_PATH$interface/ifalias"
    echo "interface name" $path
    ifalias="$(cat $path)"

    #get ifalias
    if [ $? -ne 0 ] ; then
	echo 'Could not capture ifalias @ ' $path
	return 1
    fi;

    eval ref_ifalias=$ifalias

    #parse the bus info
    if [[ $ifalias == *"02.03.00"* ]] ; then
	eval ref_bus=3
    fi;
    if [[ "$ifalias" == *"02.04.00"* ]] ; then
	eval ref_bus=4
    fi;
    if [[ "$ifalias" == *"02.05.00"* ]] ; then
	eval ref_bus=5
    fi;
}

#bind interface with NAD
function iface() {
    local -n bus=$1
    local -n ref_iface=$2

    if [ $bus -eq $NETDEV0_bus ]; then
	eval ref_iface=$NETDEV0_ifname
	return 0;
    fi;
    if [ $bus -eq $NETDEV1_bus ]; then
	eval ref_iface=$NETDEV1_ifname
	return 0;
    fi;
    if [ $bus -eq $NETDEV2_bus ]; then
	eval ref_iface=$NETDEV2_ifname
	return 0;
    fi;

    echo 'Did not match any of the interfaces'
    return 1;
}

#set android property for interface (not required)
function setproperty() {
    local NAD=$1
    local hostIP=$2
    local mdmIP=$3
    local bus=$4
    local iface=$5
    local property

    property="$NAD""_SWIP_iface"
    setprop $property $iface
    property="$NAD""_SWIP_hostIP"
    setprop $property $hostIP
    property="$NAD""_SWIP_mdmIP"
    setprop $property $mdmIP
    property="$NAD""_SWIP_bus"
    setprop $property $bus
}

#configure the interface
function config() {
    local hostIP=$1
    local mdmIP=$2
    local iface=$3

    ifconfig $iface $hostIP  "netmask" "255.255.128.0" "up"
    ip route add $mdmIP "via" $hostIP dev $iface
}

#Check if the interface is up, if not sleep 1sec and recheck
#start run as init daemon

/etc/init.d/config_swip_net.sh > /dev/null 2>&1 &

timeout=0
while [ $timeout -lt $TIMEOUT ]; do
    if isUP; then
	break
    fi;
    ((timeout++))
    sleep 1
done
if [ $timeout -ge $TIMEOUT ]; then
    echo "Not all interfaces up"
    exit 1
fi;

#Capture the ifalias and bus
ifalias NETDEV0_ifname NETDEV0_ifalias NETDEV0_bus
if [ $? -ne 0 ] ; then
    echo 'Could not capture ifalias for NETDEV0'
    exit 1
fi;

ifalias NETDEV1_ifname NETDEV1_ifalias NETDEV1_bus
if [ $? -ne 0 ] ; then
    echo 'Could not capture ifalias for NETDEV1'
    exit 1
fi;

ifalias NETDEV2_ifname NETDEV2_ifalias NETDEV2_bus
if [ $? -ne 0 ] ; then
    echo 'Could not capture ifalias for NETDEV2'
    exit 1
fi;

#Bind NAD to NETDEV
iface NAD0_bus NAD0_iface
if [ $? -ne 0 ] ; then
    echo 'Could not match NAD0 with a NETDEV'
    exit 1
fi;
iface NAD1_bus NAD1_iface
if [ $? -ne 0 ] ; then
    echo 'Could not match NAD1 with a NETDEV'
    exit 1
fi;
iface NAD2_bus NAD2_iface
if [ $? -ne 0 ] ; then
    echo 'Could not match NAD2 with a NETDEV'
    exit 1
fi;

#Set and configure the NAD interface
setproperty "NAD0" $NAD0_hostIP $NAD0_mdmIP $NAD0_bus $NAD0_iface
setproperty "NAD1" $NAD1_hostIP $NAD1_mdmIP $NAD1_bus $NAD1_iface
setproperty "NAD2" $NAD2_hostIP $NAD2_mdmIP $NAD2_bus $NAD2_iface
config $NAD0_hostIP $NAD0_mdmIP $NAD0_iface
config $NAD1_hostIP $NAD1_mdmIP $NAD1_iface
config $NAD2_hostIP $NAD2_mdmIP $NAD2_iface
