#Copyright (c) 2023 Qualcomm Innovation Center, Inc. All rights reserved.
#SPDX-License-Identifier: BSD-3-Clause-Clear

LXCRFSDIR = "${WORKDIR}/lxcrootfs"

do_lxcrfs_create[cleandirs] += "${LXCRFSDIR}"
DEPENDS += "mksh"
fakeroot do_lxcrfs_create() {
   fstree="\
      ${LXCRFSDIR}/selinux \
      ${LXCRFSDIR}/dev \
      ${LXCRFSDIR}/home \
      ${LXCRFSDIR}/root \
      ${LXCRFSDIR}/etc \
      ${LXCRFSDIR}/etc/init.d \
      ${LXCRFSDIR}/bin \
      ${LXCRFSDIR}/usr/bin \
      ${LXCRFSDIR}/sbin \
      ${LXCRFSDIR}/usr/sbin \
      ${LXCRFSDIR}/proc \
      ${LXCRFSDIR}/sys \
      ${LXCRFSDIR}/mnt \
      ${LXCRFSDIR}/mnt/legato \
      ${LXCRFSDIR}/mnt/flash \
      ${LXCRFSDIR}/tmp \
      ${LXCRFSDIR}/var/log \
      ${LXCRFSDIR}/var/run \
      ${LXCRFSDIR}/dev/pts \
      ${LXCRFSDIR}/lib \
      ${LXCRFSDIR}/legato \
      ${LXCRFSDIR}/app \
      ${LXCRFSDIR}/usr/lib"

   mkdir -p ${fstree} || return 1
   chmod 755 ${fstree} || return 1

   # root user defined
   cat <<EOF >> "${LXCRFSDIR}/etc/passwd"
root:x:0:0:root:/root:/bin/mksh
EOF

   cat <<EOF >> "${LXCRFSDIR}/etc/group"
root:x:0:root
EOF

   mknod       ${LXCRFSDIR}/dev/null c 1 3
   chmod 644   ${LXCRFSDIR}/dev/null

   cp ${IMAGE_ROOTFS}/bin/busybox      ${LXCRFSDIR}/bin/
   cp ${IMAGE_ROOTFS}/bin/busybox.suid ${LXCRFSDIR}/bin/
   cp ${IMAGE_ROOTFS}/bin/busybox.nosuid ${LXCRFSDIR}/bin/
   cp ${IMAGE_ROOTFS}/usr/lib/busybox/bin/* ${LXCRFSDIR}/bin/
   cp ${STAGING_BINDIR}/mksh ${LXCRFSDIR}/bin/

   # /etc/fstab must exist for "mount -a"
   touch "${LXCRFSDIR}/etc/fstab"

   touch "${LXCRFSDIR}/etc/shadow"

   #init create
   cp ${COREBASE}/meta-qti-auto/recipes-extended/lxc/files/lxc_init ${LXCRFSDIR}/sbin/init 
   chmod 755 ${LXCRFSDIR}/sbin/init

   if [ "${TARGET_ARCH}" = "arm" ]; then
      cp ${IMAGE_ROOTFS}/lib/ld-linux-armhf.so.3 ${LXCRFSDIR}/lib/ld-linux-armhf.so.3
   else
      cp ${IMAGE_ROOTFS}/lib/ld-linux-aarch64.so.1 ${LXCRFSDIR}/lib/ld-linux-aarch64.so.1
   fi

   cp ${IMAGE_ROOTFS}/usr/lib/busybox/sbin/logread    ${LXCRFSDIR}/sbin/
   cp ${IMAGE_ROOTFS}/usr/lib/busybox/sbin/syslogd    ${LXCRFSDIR}/sbin/
   cp ${IMAGE_ROOTFS}/usr/lib/busybox/usr/bin/logger  ${LXCRFSDIR}/usr/bin/
   cp ${IMAGE_ROOTFS}/usr/lib/busybox/usr/bin/logger  ${LXCRFSDIR}/usr/bin/

   cp ${IMAGE_ROOTFS}/lib/libz.so.1          ${LXCRFSDIR}/lib/libz.so.1
   cp ${IMAGE_ROOTFS}/lib/libc.so.6          ${LXCRFSDIR}/lib/libc.so.6
   cp ${IMAGE_ROOTFS}/lib/libm.so.6          ${LXCRFSDIR}/lib/libm.so.6
   cp ${IMAGE_ROOTFS}/lib/librt.so.1         ${LXCRFSDIR}/lib/librt.so.1
   cp ${IMAGE_ROOTFS}/lib/libpthread.so.0    ${LXCRFSDIR}/lib/libpthread.so.0
   cp ${IMAGE_ROOTFS}/lib/libdl.so.2         ${LXCRFSDIR}/lib/libdl.so.2
   cp ${IMAGE_ROOTFS}/lib/libresolv.so.2     ${LXCRFSDIR}/lib/libresolv.so.2
   cp ${IMAGE_ROOTFS}/lib/libpcre.so.1       ${LXCRFSDIR}/lib/libpcre.so.1
   cp ${IMAGE_ROOTFS}/lib/libselinux.so.1    ${LXCRFSDIR}/lib/libselinux.so.1
   cp ${IMAGE_ROOTFS}/usr/lib/libssl.so.3    ${LXCRFSDIR}/usr/lib/
   cp ${IMAGE_ROOTFS}/usr/lib/libcrypto.so.3 ${LXCRFSDIR}/usr/lib/
   cp ${IMAGE_ROOTFS}/lib/libsepol.so.1      ${LXCRFSDIR}/lib
   cp ${IMAGE_ROOTFS}/usr/lib/libsemanage.so.1 ${LXCRFSDIR}/usr/lib/
   cp ${IMAGE_ROOTFS}/lib/libaudit.so.1*     ${LXCRFSDIR}/lib
   cp ${IMAGE_ROOTFS}/usr/lib/libbz2.so.1*   ${LXCRFSDIR}/usr/lib/
   cp ${IMAGE_ROOTFS}/lib/libcap-ng.so.0*    ${LXCRFSDIR}/lib
   cp ${IMAGE_ROOTFS}/usr/lib/libvsomeip3*   ${LXCRFSDIR}/usr/lib/
   cp ${IMAGE_ROOTFS}/usr/lib/libboost_*     ${LXCRFSDIR}/usr/lib/
   cp ${IMAGE_ROOTFS}/usr/lib/libstdc\+\+.so.6* ${LXCRFSDIR}/usr/lib/
   cp ${IMAGE_ROOTFS}/lib/libgcc_s.so.1      ${LXCRFSDIR}/lib/

}

addtask do_lxcrfs_create after do_rootfs before do_build
