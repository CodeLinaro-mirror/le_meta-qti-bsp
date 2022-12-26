#!/bin/sh
# Copyright (c) 2020-2021, The Linux Foundation. All rights reserved.

# Redistribution and use in source and binary forms, with or without
# modification, are permitted provided that the following conditions are
# met:
#    * Redistributions of source code must retain the above copyright
#      notice, this list of conditions and the following disclaimer.
#    * Redistributions in binary form must reproduce the above
#      copyright notice, this list of conditions and the following
#      disclaimer in the documentation and/or other materials provided
#      with the distribution.
#    * Neither the name of The Linux Foundation nor the names of its
#      contributors may be used to endorse or promote products derived
#      from this software without specific prior written permission.

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

echo systemds2 > /dev/kmsg

dd if=/lib/systemd/system.conf.d/00-systemd-conf.conf of=/dev/null
dd if=/etc/fstab of=/dev/null
ls -l /etc/rc1.d
dd if=/lib/systemd/system/logrotate.service of=/dev/null
dd if=/lib/systemd/system/kmod-static-nodes.service of=/dev/null
dd if=/lib/systemd/system/init_early_boot.service of=/dev/null
dd if=/etc/selinux/mcs/policy/policy.31 of=/dev/null
dd if=/etc/selinux/mcs/contexts/files/file_contexts.subs_dist of=/dev/null
dd if=/etc/selinux/mcs/contexts/files/file_contexts.bin of=/dev/null
dd if=/etc/selinux/mcs/contexts/files/file_contexts.homedirs.bin of=/dev/null
dd if=/lib/systemd/system/rngd.service of=/dev/null
dd if=/lib/systemd/system/msm-bus.service of=/dev/null
ls -l /etc/systemd/system/basic.target.wants
ls -l /lib/systemd/system/multi-user.target.wants
ls -l /lib/systemd/system/runlevel1.target.wants
ls -l /etc/systemd/system/network-online.target.wants

echo systemdm2 > /dev/kmsg

dd if=/usr/lib/libwayland-server.so.0.1.0 of=/dev/null
dd if=/usr/lib/libwayland-server.so.0 of=/dev/null
dd if=/lib/libgcc_s.so.1 of=/dev/null
dd if=/usr/lib/libweston-desktop-8.so.0.0.0 of=/dev/null
dd if=/usr/lib/libweston-desktop-8.so.0 of=/dev/null
dd if=/usr/lib/libqseed3.so of=/dev/null
dd if=/usr/lib/libfribidi.so.0.4.0 of=/dev/null
dd if=/usr/lib/libfribidi.so.0 of=/dev/null
dd if=/lib/libz.so.1.2.11 of=/dev/null
dd if=/usr/lib/libsync.so.0.0.0 of=/dev/null
dd if=/usr/lib/libgthread-2.0.so.0.6200.6 of=/dev/null
dd if=/usr/share/fontconfig/conf.avail/60-latin.conf of=/dev/null
ls -l /data/misc
dd if=/usr/share/fonts/.uuid of=/dev/null
dd if=/usr/share/fonts/wqy-microhei.ttc of=/dev/null
dd if=/usr/share/fonts/ttf/.uuid of=/dev/null
dd if=/usr/share/fonts/ttf/LiberationMono-Bold.ttf of=/dev/null
dd if=/usr/share/fonts/ttf/LiberationMono-BoldItalic.ttf of=/dev/null
dd if=/usr/share/fonts/ttf/LiberationMono-Italic.ttf of=/dev/null
dd if=/usr/share/fonts/ttf/LiberationMono-Regular.ttf of=/dev/null
dd if=/usr/share/fonts/ttf/LiberationSans-Bold.ttf of=/dev/null
dd if=/usr/share/fonts/ttf/LiberationSans-BoldItalic.ttf of=/dev/null
dd if=/usr/share/fonts/ttf/LiberationSans-Italic.ttf of=/dev/null
dd if=/usr/share/fonts/ttf/LiberationSans-Regular.ttf of=/dev/null
dd if=/usr/share/fonts/ttf/LiberationSerif-Bold.ttf of=/dev/null
dd if=/usr/share/fonts/ttf/LiberationSerif-BoldItalic.ttf of=/dev/null
dd if=/usr/share/fonts/ttf/LiberationSerif-Italic.ttf of=/dev/null
dd if=/usr/share/fonts/ttf/LiberationSerif-Regular.ttf of=/dev/null
dd if=/usr/share/weston/pattern.png of=/run/p.bin

echo systemde2 > /dev/kmsg
