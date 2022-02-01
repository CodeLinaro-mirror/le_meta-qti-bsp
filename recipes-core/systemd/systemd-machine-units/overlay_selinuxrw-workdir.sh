#!/bin/sh
# Copyright (c) 2021, The Linux Foundation. All rights reserved.
#
# Redistribution and use in source and binary forms, with or without
# modification, are permitted provided that the following conditions are
# met:
#   * Redistributions of source code must retain the above copyright
#     notice, this list of conditions and the following disclaimer.
#   * Redistributions in binary form must reproduce the above
#     copyright notice, this list of conditions and the following
#     disclaimer in the documentation and/or other materials provided
#     with the distribution.
#   * Neither the name of The Linux Foundation nor the names of its
#     contributors may be used to endorse or promote products derived
#     from this software without specific prior written permission.
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

CreateSelinuxOverlayDirectories () {
   mount_operation=$1
   if [ $mount_operation == "start" ]; then
     mkdir -p /data/etc_selinux
     chmod 0755 /data/etc_selinux
     chcon system_u:object_r:selinux_config_t:s0 /data/etc_selinux
     mkdir -p /data/etc_selinux_wk
     chmod 0755 /data/etc_selinux_wk
     chcon system_u:object_r:selinux_config_t:s0 /data/etc_selinux_wk

     mkdir -p /data/var_selinux
     chmod 0755 /data/var_selinux
     chcon system_u:object_r:semanage_store_t:s0 /data/var_selinux
     mkdir -p /data/var_selinux_wk
     chmod 0755 /data/var_selinux_wk
     chcon system_u:object_r:semanage_store_t:s0 /data/var_selinux_wk

     mkdir -p /data/etc_c2c
     chmod 0755 /data/etc_c2c
     chcon system_u:object_r:ota_data_conf_t:s0 /data/etc_c2c
     mkdir -p /data/etc_c2c_wk
     chmod 0755 /data/etc_c2c_wk
     chcon system_u:object_r:ota_data_conf_t:s0 /data/etc_c2c_wk

     mount -t overlay overlay -o context=system_u:object_r:ota_data_conf_t:s0,upperdir=/data/etc_c2c,lowerdir=/etc/c2c,workdir=/data/etc_c2c_wk /etc/c2c
     mount -t overlay overlay -o context=system_u:object_r:selinux_config_t:s0,upperdir=/data/etc_selinux,lowerdir=/etc/selinux,workdir=/data/etc_selinux_wk,redirect_dir=on /etc/selinux
     mount -t overlay overlay -o context=system_u:object_r:semanage_store_t:s0,upperdir=/data/var_selinux,lowerdir=/var/lib/selinux,workdir=/data/var_selinux_wk,redirect_dir=on /var/lib/selinux
   elif [ $mount_operation == "stop" ]; then
     umount /etc/selinux
     umount /var/lib/selinux
     umount /etc/c2c
   fi
}

eval CreateSelinuxOverlayDirectories $1
exit 0
