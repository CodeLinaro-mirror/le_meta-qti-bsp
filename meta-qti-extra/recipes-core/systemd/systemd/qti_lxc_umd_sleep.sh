#!/bin/sh
# Copyright (c) 2024 Qualcomm Innovation Center, Inc. All rights reserved.
# SPDX-License-Identifier: BSD-3-Clause-Clear
case $1/$2 in
  pre/*)
    echo "Entering into lxc umd $2..."

    systemctl stop video-driver.service
    systemctl stop weston
    sleep 2
    systemctl stop openwfd_server_@0.service

    ;;
  post/*)
    echo "Exiting from lxc umd $2..."

    systemctl restart openwfd_server_@0.service
    sleep 20
    systemctl restart weston
    systemctl restart video-driver.service
    ulimit -q unlimited
    ;;
esac
