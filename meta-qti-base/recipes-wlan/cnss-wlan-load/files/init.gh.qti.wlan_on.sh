#!/bin/sh
# Copyright (c) Qualcomm Technologies, Inc. and/or its subsidiaries.
# SPDX-License-Identifier: BSD-3-Clause-Clear
#

install_module() {
	modprobe $1 || modprobe -d /vendor $1;
	echo 1 > /sys/kernel/cnss_0/fs_ready
}

echo "##########Trying to load wlan platform driver ##########"
modprobe cnss2
modprobe pcie-qcom-ecam

echo "##########Trying to load wlan host driver ##########"

n=0
while [ $n -le 5 ]
	do
	if (lspci -k|grep cnss_pci);then
		if [ "$(lspci -k | grep 1102)" ] || [ "$(lspci -n | grep 1102)" ]; then
			echo "##########load qca6595#############"
			if [ -f /firmware/image/qcn7605/amss.bin ];then
				install_module qca6595
			else
				echo "##########Error! QCA6595 FW is not available!#####"
			fi
		elif [ "$(lspci -k | grep 003e)" ] || [ "$(lspci -n | grep 003e)" ] || [ "$(lspci -k|grep QCA6174)" ];then
			echo "##########load qca6574#############"
			if [ -f /firmware/image/qca6174/qwlan30.bin ];then
				install_module qca6574
			else
				echo "##########Error! QCA6574 FW is not available!#####"
			fi
		elif [ "$(lspci -k | grep 1101)" ] || [ "$(lspci -n | grep 1101)" ] || [ "$(lspci -k|grep QCA6390)" ];then
			echo "##########load qca6696#############"
			if [ -f /firmware/image/qca6390/amss20.bin ];then
				install_module qca6696
			else
				echo "##########Error! QCA6696 FW is not available!#####"
			fi
		elif [ "$(lspci -k | grep 1103)" ] || [ "$(lspci -n | grep 1103)" ];then
			echo "##########load qca6698#############"
			if [ -f /firmware/image/qca6490/amss20.bin ];then
				install_module qca6698
			else
				echo "##########Error! QCA6698 FW is not available!#####"
			fi
		elif [ "$(lspci -k | grep 1107)" ] || [ "$(lspci -n | grep 1107)" ];then
			echo "##########load qca6797#############"
			if [ -f /firmware/image/kiwi/amss20.bin ];then
				install_module qca6797
			else
				echo "##########Error! QCA6797 FW is not available!#####"
			fi
		else
			echo "##########load default wlan########"
			install_module wlan
		fi
		break
	fi
	n=$((n + 1))
	echo "Retry loading wlan @$n"
	sleep 1
done
echo "##########Load wlanhost driver done################"

sleep 1
n=0
while [ $n -le 5 ]
	do
	if (ls -la /sys/class/net/ | grep wlan0);then
		ifconfig wlan0 up
		sleep 1
		if (ifconfig wlan0 | grep UP);then
			echo "wlan0 up ready"
			break
		fi
	fi
	n=$((n + 1))
	echo "Retry loading wlan @$n"
	sleep 1
done
