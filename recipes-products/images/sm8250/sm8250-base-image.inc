#compile abl
RDEPENDS += "${@bb.utils.contains('TARGET_ARCH', 'arm', 'lib64-edk2', 'edk2', d)}"

# Rootfs creation.
RDEPENDS += "packagegroup-core-boot"
RDEPENDS += "${CORE_IMAGE_EXTRA_INSTALL}"

# The MSM Linux minimal boot image files.
RDEPENDS += "base-files"
RDEPENDS += "kernel-modules"
RDEPENDS += "libcutils"
RDEPENDS += "liblog"

RDEPENDS += "system-core-adbd"
RDEPENDS += "system-core-usb"
RDEPENDS += "system-core-logd"
RDEPENDS += "coreutils"
RDEPENDS += "system-core-leprop"
RDEPENDS += "system-core-post-boot"
RDEPENDS += "systemd-machine-units"
RDEPENDS += "volatile-binds"
RDEPENDS += "binder"
RDEPENDS += "ffmpeg"
RDEPENDS += "x264"

RDEPENDS += "${@bb.utils.contains('DISTRO_FEATURES', 'ab-boot-support', 'start-script-ab-slot-util', '', d)}"

EXTRA_USERS_PARAMS := ""
EXTRA_USERS_PARAMS_remove = "%"

ROOTFS_POSTPROCESS_COMMAND := ""
ROOTFS_POSTPROCESS_COMMAND_remove += "purge_rootfs;"
ROOTFS_POSTPROCESS_COMMAND_remove += "symlink_modules;"
ROOTFS_POSTPROCESS_COMMAND_remove += "sync_hostfs;"
ROOTFS_POSTPROCESS_COMMAND_remove += "set_user_group;"
ROOTFS_POSTPROCESS_COMMAND_remove += "zap_empty_root_password;"
ROOTFS_POSTPROCESS_COMMAND_remove += "rootfs_update_timestamp;"
ROOTFS_POSTPROCESS_COMMAND_remove += "read_only_rootfs_hook;"
ROOTFS_POSTPROCESS_COMMAND_remove += "set_systemd_default_target;"
ROOTFS_POSTPROCESS_COMMAND_remove += "systemd_create_users;"
ROOTFS_POSTPROCESS_COMMAND_remove += "do_fsconfig;"

PACKAGE_INSTALL_remove += "run-postinsts"
PACKAGE_INSTALL_remove += "packagegroup-base-extended"
PACKAGE_INSTALL_remove += "packagegroup-core-boot"

IMAGE_PREPROCESS_COMMAND_remove += "prelink_image;"

ROOTFS_POSTUNINSTALL_COMMAND_remove += "write_image_manifest ;"
ROOTFS_POSTUNINSTALL_COMMAND_remove += "buildhistory_list_installed_image ;"
ROOTFS_POSTUNINSTALL_COMMAND_remove += "buildhistory_get_image_installed ;"

ROOTFS_POSTPROCESS_COMMAND_remove += "gen_buildprop;"
ROOTFS_POSTPROCESS_COMMAND_remove += "sort_passwd;"
ROOTFS_POSTPROCESS_COMMAND_remove += "rootfs_reproducible;"

copy_edge_rootfs() {
    chmod +w ${IMAGE_ROOTFS}/ -R
    rm -rf ${IMAGE_ROOTFS}/*
    if [ -e ${WORKSPACE}/poky/meta-qti-internal/centos-noship/centos8-1905.tar.xz ]
    then
        tar -xf ${WORKSPACE}/poky/meta-qti-internal/centos-noship/centos8-1905.tar.xz -C ${IMAGE_ROOTFS}/
    elif [ -e ${WORKSPACE}/centos ]
    then
        cp -rf ${WORKSPACE}/centos/* ${IMAGE_ROOTFS}/
    else
        bberror "Error: RootFS is missing"
    fi

    mkdir -p ${IMAGE_ROOTFS}/packages/aarch64
    mkdir -p ${IMAGE_ROOTFS}/packages/noarch
    mkdir -p ${IMAGE_ROOTFS}/packages/sm8250

    mkdir -p ${IMAGE_ROOTFS}/systemrw
    mkdir -p ${IMAGE_ROOTFS}/persist
    mkdir -p ${IMAGE_ROOTFS}/firmware
    mkdir -p ${IMAGE_ROOTFS}/data
    mkdir -p ${IMAGE_ROOTFS}/cache
    mkdir -p ${IMAGE_ROOTFS}/dsp
    mkdir -p ${IMAGE_ROOTFS}/mnt/sdcard
    touch ${IMAGE_ROOTFS}/etc/default/usb

    # find ${IMAGE_ROOTFS} . -name "*.4.18*" |xargs rm -rf
    rm -rf {IMAGE_ROOTFS}/usr/lib/modules/4.18.0-80.el8.aarch64
    rm -rf {IMAGE_ROOTFS}/usr/lib/modules/4.18.0-79.el8+2.aarch64
    rm -rf {IMAGE_ROOTFS}/usr/share/doc/kernel-keys/4.18.0-80.el8.aarch64
    rm -rf {IMAGE_ROOTFS}/usr/share/licenses/kernel-core/COPYING-4.18.0
    rm -rf {IMAGE_ROOTFS}/usr/src/kernels/4.18.0-80.el8.aarch64
    rm -rf {IMAGE_ROOTFS}/usr/src/kernels/4.18.0-80.el8.aarch64+debug
    rm -rf {IMAGE_ROOTFS}/var/lib/pcp/testsuite/linux/procpid-4.18.13-root-006.tgz
    rm -rf {IMAGE_ROOTFS}/etc/ld.so.conf.d/kernel-4.18.0-80.el8.aarch64.conf
    rm -rf ${IMAGE_ROOTFS}/usr/lib/firmware/*
    rm -rf ${IMAGE_ROOTFS}/var/lib/pcp/testsuite
    rm -rf ${IMAGE_ROOTFS}/var/lib/nfs/rpc_pipefs
    rm -rf ${IMAGE_ROOTFS}/tmp/*

    install -m 0755 ${TMPDIR}/work/aarch64-oe-linux/busybox/1.29.3-r0/package/usr/bin/busybox.nosuid -D ${IMAGE_ROOTFS}/usr/bin/start-stop-daemon
    install -m 0644 ${TMPDIR}/work/aarch64-oe-linux/systemd/1_239-r0/package/etc/tmpfiles.d/platform.conf -D ${IMAGE_ROOTFS}/etc/tmpfiles.d/platform.conf
    if [ -e ${IMAGE_ROOTFS}/packages/install.sh ]
    then
        echo "/bin/bash /packages/install.sh" >> ${IMAGE_ROOTFS}/etc/rc.d/rc.local
        chmod +x ${IMAGE_ROOTFS}/etc/rc.d/rc.local
    fi
}

ROOTFS_POSTPROCESS_COMMAND =+ "copy_edge_rootfs;"

copy_edge_bsp_rpm() {
    cp ${DEPLOY_DIR_RPM}/noarch/volatile-binds-1.0-r0.noarch.rpm ${IMAGE_ROOTFS}/packages/noarch/
    cp ${DEPLOY_DIR_RPM}/sm8250/systemd-machine-units-1.0-r19.sm8250.rpm ${IMAGE_ROOTFS}/packages/sm8250/
    cp ${DEPLOY_DIR_RPM}/aarch64/av-frameworks-git-r1.aarch64.rpm ${IMAGE_ROOTFS}/packages/aarch64/
    cp ${DEPLOY_DIR_RPM}/aarch64/binder-git-r0.aarch64.rpm ${IMAGE_ROOTFS}/packages/aarch64/
    cp ${DEPLOY_DIR_RPM}/aarch64/libbase0-1.0-r1.aarch64.rpm ${IMAGE_ROOTFS}/packages/aarch64/
    cp ${DEPLOY_DIR_RPM}/aarch64/libcamera-metadata0-1.0-r0.aarch64.rpm ${IMAGE_ROOTFS}/packages/aarch64/
    cp ${DEPLOY_DIR_RPM}/aarch64/libcutils0-1.0-r1.aarch64.rpm ${IMAGE_ROOTFS}/packages/aarch64/
    cp ${DEPLOY_DIR_RPM}/aarch64/libext4-utils0-git-r1.aarch64.rpm ${IMAGE_ROOTFS}/packages/aarch64/
    cp ${DEPLOY_DIR_RPM}/aarch64/libhardware0-git-r6.aarch64.rpm ${IMAGE_ROOTFS}/packages/aarch64/
    cp ${DEPLOY_DIR_RPM}/aarch64/libion0-1.0-r1.aarch64.rpm ${IMAGE_ROOTFS}/packages/aarch64/
    cp ${DEPLOY_DIR_RPM}/aarch64/liblog0-1.0-r1.aarch64.rpm ${IMAGE_ROOTFS}/packages/aarch64/
    cp ${DEPLOY_DIR_RPM}/aarch64/libmincrypt0-git-r0.aarch64.rpm ${IMAGE_ROOTFS}/packages/aarch64/
    cp ${DEPLOY_DIR_RPM}/aarch64/libselinux0-git-r0.aarch64.rpm ${IMAGE_ROOTFS}/packages/aarch64/
    cp ${DEPLOY_DIR_RPM}/aarch64/libsparse0-1.0-r0.aarch64.rpm ${IMAGE_ROOTFS}/packages/aarch64/
    cp ${DEPLOY_DIR_RPM}/aarch64/libsync0-1.0-r1.aarch64.rpm ${IMAGE_ROOTFS}/packages/aarch64/
    cp ${DEPLOY_DIR_RPM}/aarch64/libui0-git-r1.aarch64.rpm ${IMAGE_ROOTFS}/packages/aarch64/
    cp ${DEPLOY_DIR_RPM}/aarch64/libutils0-1.0-r1.aarch64.rpm ${IMAGE_ROOTFS}/packages/aarch64/
    cp ${DEPLOY_DIR_RPM}/aarch64/start-script-ab-slot-util-1.0-r0.aarch64.rpm ${IMAGE_ROOTFS}/packages/aarch64/
    cp ${DEPLOY_DIR_RPM}/aarch64/system-core-git-r19.aarch64.rpm ${IMAGE_ROOTFS}/packages/aarch64/
    cp ${DEPLOY_DIR_RPM}/aarch64/system-core-adbd-git-r19.aarch64.rpm ${IMAGE_ROOTFS}/packages/aarch64/
    cp ${DEPLOY_DIR_RPM}/aarch64/system-core-logd-git-r19.aarch64.rpm ${IMAGE_ROOTFS}/packages/aarch64/
    cp ${DEPLOY_DIR_RPM}/aarch64/system-core-leprop-git-r19.aarch64.rpm ${IMAGE_ROOTFS}/packages/aarch64/
    cp ${DEPLOY_DIR_RPM}/aarch64/system-core-post-boot-git-r19.aarch64.rpm ${IMAGE_ROOTFS}/packages/aarch64/
    cp ${DEPLOY_DIR_RPM}/aarch64/system-core-usb-git-r19.aarch64.rpm ${IMAGE_ROOTFS}/packages/aarch64/
    cp ${DEPLOY_DIR_RPM}/aarch64/ffmpeg-4.2.2-r0.aarch64.rpm ${IMAGE_ROOTFS}/packages/aarch64/
    cp ${DEPLOY_DIR_RPM}/aarch64/ffmpeg-dev-4.2.2-r0.aarch64.rpm ${IMAGE_ROOTFS}/packages/aarch64/
    cp ${DEPLOY_DIR_RPM}/aarch64/libx264-152-r2854+git0+e9a5903edf-r0.aarch64.rpm ${IMAGE_ROOTFS}/packages/aarch64/
    cp ${DEPLOY_DIR_RPM}/aarch64/libx264-bin-r2854+git0+e9a5903edf-r0.aarch64.rpm ${IMAGE_ROOTFS}/packages/aarch64/
}

ROOTFS_POSTPROCESS_COMMAND += "copy_edge_bsp_rpm;"

do_makesystem() {
    cp ${THISDIR}/${BASEMACHINE}/sm8250-fsconfig.conf ${WORKDIR}/rootfs-fsconfig.conf
    make_ext4fs -C ${WORKDIR}/rootfs-fsconfig.conf -B ${DEPLOY_DIR_IMAGE}/system.map -s -a / ${IMAGE_EXT4_SELINUX_OPTIONS} -l ${SYSTEM_SIZE_EXT4} ${DEPLOY_DIR_IMAGE}/${SYSTEMIMAGE_TARGET} ${IMAGE_ROOTFS}
    make_ext4fs -s -a /systemrw ${IMAGE_EXT4_SELINUX_OPTIONS} -b 4096 -l ${SYSTEMRW_SIZE_EXT4} ${DEPLOY_DIR_IMAGE}/${SYSTEMRWIMAGE_TARGET}
}
