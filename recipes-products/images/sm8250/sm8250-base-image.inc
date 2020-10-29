#compile abl
RDEPENDS += "${@bb.utils.contains('TARGET_ARCH', 'arm', 'lib64-edk2', 'edk2', d)}"

# Rootfs creation.
IMAGE_INSTALL += "packagegroup-core-boot"
IMAGE_INSTALL += "${CORE_IMAGE_EXTRA_INSTALL}"

# The MSM Linux minimal boot image files.
IMAGE_INSTALL += "base-files"
IMAGE_INSTALL += "kernel-modules"
IMAGE_INSTALL += "libcutils"
IMAGE_INSTALL += "liblog"

IMAGE_INSTALL += "busybox"
IMAGE_INSTALL += "system-core-adbd"
IMAGE_INSTALL += "system-core-usb"
IMAGE_INSTALL += "system-core-logd"
IMAGE_INSTALL += "coreutils"
IMAGE_INSTALL += "system-core-leprop"
IMAGE_INSTALL += "system-core-post-boot"
IMAGE_INSTALL += "systemd-machine-units"
IMAGE_INSTALL += "volatile-binds"
IMAGE_INSTALL += "binder"
IMAGE_INSTALL += "start-scripts-misc-daemon"
IMAGE_INSTALL += "start-scripts-firmware-links"
IMAGE_INSTALL += "start-scripts-find-partitions"
IMAGE_INSTALL += "opencv"
IMAGE_INSTALL += "system-media"
IMAGE_INSTALL += "protobuf"

IMAGE_INSTALL += "${@bb.utils.contains('DISTRO_FEATURES', 'ab-boot-support', 'start-script-ab-slot-util', '', d)}"

IMAGE_INSTALL += "${@bb.utils.contains('DISTRO_FEATURES', 'ab-boot-support', 'recovery-ab', '', d)}"
IMAGE_INSTALL += "${@bb.utils.contains('DISTRO_FEATURES', 'ab-boot-support', 'recovery-ab-bin', '', d)}"

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
    if [ -e ${WORKSPACE}/centos/centos8-1905.tar.xz ]
    then
        tar -xf ${WORKSPACE}/centos/centos8-1905.tar.xz -C ${IMAGE_ROOTFS}/
    else
        bberror "Error: RootFS is missing"
    fi

    mkdir -p ${IMAGE_ROOTFS}/packages/aarch64
    mkdir -p ${IMAGE_ROOTFS}/packages/noarch
    mkdir -p ${IMAGE_ROOTFS}/packages/sm8250
    mkdir -p ${IMAGE_ROOTFS}/packages/qcaudio
    mkdir -p ${IMAGE_ROOTFS}/packages/opencv

    mkdir -p ${IMAGE_ROOTFS}/systemrw
    mkdir -p ${IMAGE_ROOTFS}/persist
    mkdir -p ${IMAGE_ROOTFS}/firmware
    mkdir -p ${IMAGE_ROOTFS}/data
    mkdir -p ${IMAGE_ROOTFS}/cache
    mkdir -p ${IMAGE_ROOTFS}/dsp
    mkdir -p ${IMAGE_ROOTFS}/mnt/sdcard
    touch ${IMAGE_ROOTFS}/etc/default/usb

    mkdir -p ${IMAGE_ROOTFS}/vendor
    ln -sf /dsp ${IMAGE_ROOTFS}/vendor/dsp

    # find ${IMAGE_ROOTFS} . -name "*.4.18*" |xargs rm -rf
    rm -rf ${IMAGE_ROOTFS}/usr/lib/modules/4.18.0-80.el8.aarch64
    rm -rf ${IMAGE_ROOTFS}/usr/lib/modules/4.18.0-79.el8+2.aarch64
    rm -rf ${IMAGE_ROOTFS}/usr/share/doc/kernel-keys/4.18.0-80.el8.aarch64
    rm -rf ${IMAGE_ROOTFS}/usr/share/licenses/kernel-core/COPYING-4.18.0
    rm -rf ${IMAGE_ROOTFS}/usr/src/kernels/4.18.0-80.el8.aarch64
    rm -rf ${IMAGE_ROOTFS}/usr/src/kernels/4.18.0-80.el8.aarch64+debug
    rm -rf ${IMAGE_ROOTFS}/var/lib/pcp/testsuite/linux/procpid-4.18.13-root-006.tgz
    rm -rf ${IMAGE_ROOTFS}/etc/ld.so.conf.d/kernel-4.18.0-80.el8.aarch64.conf
    rm -rf ${IMAGE_ROOTFS}/usr/lib/firmware/*
    rm -rf ${IMAGE_ROOTFS}/var/lib/pcp/testsuite
    rm -rf ${IMAGE_ROOTFS}/etc/systemd/system/multi-user.target.wants/pmie.service
    rm -rf ${IMAGE_ROOTFS}/etc/systemd/system/multi-user.target.wants/pmcd.service
    rm -rf ${IMAGE_ROOTFS}/etc/systemd/system/multi-user.target.wants/pmlogger.service
    rm -rf ${IMAGE_ROOTFS}/var/lib/nfs/rpc_pipefs
    rm -rf ${IMAGE_ROOTFS}/tmp/*

    if [ -d ${BASE_WORKDIR}/${BASEMACHINE}-oe-linux/linux-msm/4.19-r0/image/usr/lib/modules ]
    then
        cp -rf ${BASE_WORKDIR}/${BASEMACHINE}-oe-linux/linux-msm/4.19-r0/image/usr/lib/modules/* ${IMAGE_ROOTFS}/usr/lib/modules/
    fi

    # if A/B OTA supported, add recovery bin and libs to system rootfs
    cp -rf ${TMPDIR}/work/aarch64-oe-linux/recovery-ab/git-r7/package/* ${IMAGE_ROOTFS}/
    install -m 0755 ${TMPDIR}/work/aarch64-oe-linux/busybox/1.29.3-r0/package/usr/bin/busybox.nosuid -D ${IMAGE_ROOTFS}/usr/bin/start-stop-daemon
    install -m 0644 ${TMPDIR}/work/aarch64-oe-linux/systemd/1_239-r0/package/etc/tmpfiles.d/platform.conf -D ${IMAGE_ROOTFS}/etc/tmpfiles.d/platform.conf
    install -d ${IMAGE_ROOTFS}/etc/udev/scripts
    install -m 0744 ${TMPDIR}/work/aarch64-oe-linux/busybox/1.29.3-r0/package/etc/udev/scripts/automountsdcard.sh -D ${IMAGE_ROOTFS}/etc/udev/scripts/automountsdcard.sh
    install -m 0644 ${TMPDIR}/work/aarch64-oe-linux/systemd/1_239-r0/package/etc/udev/rules.d/mountpartitions.rules -D ${IMAGE_ROOTFS}/etc/udev/rules.d/mountpartitions.rules

    # Copy kernel header files
    cp -rf ${STAGING_KERNEL_BUILDDIR}/usr/include/linux/ion.h ${IMAGE_ROOTFS}/usr/include/linux/
    cp -rf ${STAGING_KERNEL_BUILDDIR}/usr/include/linux/msm_ion.h ${IMAGE_ROOTFS}/usr/include/linux/
    cp -rf ${STAGING_KERNEL_BUILDDIR}/usr/include/linux/videodev2.h ${IMAGE_ROOTFS}/usr/include/linux/
    cp -rf ${STAGING_KERNEL_BUILDDIR}/usr/include/linux/v4l2-controls.h ${IMAGE_ROOTFS}/usr/include/linux/
    cp -rf ${STAGING_KERNEL_BUILDDIR}/usr/include/media ${IMAGE_ROOTFS}/usr/include/
}

ROOTFS_POSTPROCESS_COMMAND =+ "copy_edge_rootfs;"

copy_edge_bsp_rpm() {
    cp ${DEPLOY_DIR_RPM}/noarch/volatile-binds-1.0-r0.noarch.rpm ${IMAGE_ROOTFS}/packages/noarch/
    cp ${DEPLOY_DIR_RPM}/sm8250/systemd-machine-units-1.0-r19.sm8250.rpm ${IMAGE_ROOTFS}/packages/sm8250/
    cp ${DEPLOY_DIR_RPM}/sm8250/start-scripts-find-partitions-1.0-r4.sm8250.rpm ${IMAGE_ROOTFS}/packages/aarch64/
    cp ${DEPLOY_DIR_RPM}/sm8250/start-scripts-firmware-links-1.0-r5.sm8250.rpm ${IMAGE_ROOTFS}/packages/aarch64/
    cp ${DEPLOY_DIR_RPM}/sm8250/start-scripts-misc-daemon-1.0-r2.sm8250.rpm ${IMAGE_ROOTFS}/packages/aarch64/
    cp ${DEPLOY_DIR_RPM}/aarch64/binder-git-r0.aarch64.rpm ${IMAGE_ROOTFS}/packages/aarch64/
    cp ${DEPLOY_DIR_RPM}/aarch64/libbase0-1.0-r1.aarch64.rpm ${IMAGE_ROOTFS}/packages/aarch64/
    cp ${DEPLOY_DIR_RPM}/aarch64/libcutils0-1.0-r1.aarch64.rpm ${IMAGE_ROOTFS}/packages/aarch64/
    cp ${DEPLOY_DIR_RPM}/aarch64/libext4-utils0-git-r1.aarch64.rpm ${IMAGE_ROOTFS}/packages/aarch64/
    cp ${DEPLOY_DIR_RPM}/sm8250/libhardware0-1.0-r7.sm8250.rpm ${IMAGE_ROOTFS}/packages/sm8250/
    cp ${DEPLOY_DIR_RPM}/aarch64/libion0-1.0-r1.aarch64.rpm ${IMAGE_ROOTFS}/packages/aarch64/
    cp ${DEPLOY_DIR_RPM}/aarch64/libion-dev-1.0-r1.aarch64.rpm ${IMAGE_ROOTFS}/packages/aarch64/
    cp ${DEPLOY_DIR_RPM}/aarch64/liblog0-1.0-r1.aarch64.rpm ${IMAGE_ROOTFS}/packages/aarch64/
    cp ${DEPLOY_DIR_RPM}/aarch64/libmincrypt0-git-r0.aarch64.rpm ${IMAGE_ROOTFS}/packages/aarch64/
    if ${@bb.utils.contains('DISTRO_FEATURES','selinux','false','ture',d)}
    then
        cp ${DEPLOY_DIR_RPM}/aarch64/libselinux0-git-r0.aarch64.rpm ${IMAGE_ROOTFS}/packages/aarch64/
    fi
    cp ${DEPLOY_DIR_RPM}/aarch64/libsparse0-1.0-r0.aarch64.rpm ${IMAGE_ROOTFS}/packages/aarch64/
    cp ${DEPLOY_DIR_RPM}/aarch64/libsync0-1.0-r1.aarch64.rpm ${IMAGE_ROOTFS}/packages/aarch64/
    cp ${DEPLOY_DIR_RPM}/aarch64/libutils0-1.0-r1.aarch64.rpm ${IMAGE_ROOTFS}/packages/aarch64/
    cp ${DEPLOY_DIR_RPM}/aarch64/libcutils-dev-1.0-r1.aarch64.rpm ${IMAGE_ROOTFS}/packages/aarch64/
    cp ${DEPLOY_DIR_RPM}/aarch64/start-script-ab-slot-util-1.0-r0.aarch64.rpm ${IMAGE_ROOTFS}/packages/aarch64/
    cp ${DEPLOY_DIR_RPM}/aarch64/system-core-git-r19.aarch64.rpm ${IMAGE_ROOTFS}/packages/aarch64/
    cp ${DEPLOY_DIR_RPM}/aarch64/system-core-adbd-git-r19.aarch64.rpm ${IMAGE_ROOTFS}/packages/aarch64/
    cp ${DEPLOY_DIR_RPM}/aarch64/system-core-logd-git-r19.aarch64.rpm ${IMAGE_ROOTFS}/packages/aarch64/
    cp ${DEPLOY_DIR_RPM}/aarch64/system-core-leprop-git-r19.aarch64.rpm ${IMAGE_ROOTFS}/packages/aarch64/
    cp ${DEPLOY_DIR_RPM}/aarch64/system-core-post-boot-git-r19.aarch64.rpm ${IMAGE_ROOTFS}/packages/aarch64/
    cp ${DEPLOY_DIR_RPM}/aarch64/system-core-usb-git-r19.aarch64.rpm ${IMAGE_ROOTFS}/packages/aarch64/
    cp ${DEPLOY_DIR_RPM}/aarch64/libjpeg8-1.2.1-r2.aarch64.rpm ${IMAGE_ROOTFS}/packages/aarch64/
    cp ${DEPLOY_DIR_RPM}/aarch64/mtdev-1.1.5-r0.aarch64.rpm ${IMAGE_ROOTFS}/packages/aarch64/
    cp ${DEPLOY_DIR_RPM}/aarch64/libevdev-1.5.9-r0.aarch64.rpm ${IMAGE_ROOTFS}/packages/aarch64/
    cp ${DEPLOY_DIR_RPM}/aarch64/libopencv*.rpm ${IMAGE_ROOTFS}/packages/opencv/
    cp ${DEPLOY_DIR_RPM}/aarch64/python3-opencv-3.4.3-r0.aarch64.rpm ${IMAGE_ROOTFS}/packages/opencv/
    cp ${DEPLOY_DIR_RPM}/aarch64/opencv*.rpm ${IMAGE_ROOTFS}/packages/opencv/
    cp ${DEPLOY_DIR_RPM}/aarch64/opencv*.rpm ${IMAGE_ROOTFS}/packages/opencv/
    cp ${DEPLOY_DIR_RPM}/aarch64/libprotobuf22-3.11.4-r0.aarch64.rpm ${IMAGE_ROOTFS}/packages/aarch64/
    cp ${DEPLOY_DIR_RPM}/aarch64/libprotobuf-compiler-3.11.4-r0.aarch64.rpm ${IMAGE_ROOTFS}/packages/aarch64/
    cp ${DEPLOY_DIR_RPM}/aarch64/libprotobuf-dev-3.11.4-r0.aarch64.rpm ${IMAGE_ROOTFS}/packages/aarch64/
    cp ${DEPLOY_DIR_RPM}/aarch64/libprotobuf-lite22-3.11.4-r0.aarch64.rpm ${IMAGE_ROOTFS}/packages/aarch64/
    cp ${DEPLOY_DIR_RPM}/aarch64/system-media-git-r2.aarch64.rpm ${IMAGE_ROOTFS}/packages/aarch64/
    cp ${DEPLOY_DIR_RPM}/aarch64/hdf5-1.8.21-r0.aarch64.rpm ${IMAGE_ROOTFS}/packages/aarch64/
    cp ${DEPLOY_DIR_RPM}/aarch64/hdf5-dev-1.8.21-r0.aarch64.rpm ${IMAGE_ROOTFS}/packages/aarch64/
    cp ${DEPLOY_DIR_RPM}/aarch64/libcamera-metadata0-1.1-r0.aarch64.rpm ${IMAGE_ROOTFS}/packages/aarch64/
}

ROOTFS_POSTPROCESS_COMMAND += "copy_edge_bsp_rpm;"

copy_edge_kernel_certs() {
    if [ -d ${STAGING_KERNEL_BUILDDIR}/certs ]
    then
        chmod a+r ${STAGING_KERNEL_BUILDDIR}/certs/*
        cp -rf ${STAGING_KERNEL_BUILDDIR}/certs ${DEPLOY_DIR_IMAGE}/
    fi
}

IMAGE_POSTPROCESS_COMMAND += "copy_edge_kernel_certs;"

do_makesystem() {
    cp ${THISDIR}/${BASEMACHINE}/sm8250-fsconfig.conf ${WORKDIR}/rootfs-fsconfig.conf
    make_ext4fs -C ${WORKDIR}/rootfs-fsconfig.conf -B ${DEPLOY_DIR_IMAGE}/system.map -s -a / ${IMAGE_EXT4_SELINUX_OPTIONS} -l ${SYSTEM_SIZE_EXT4} ${DEPLOY_DIR_IMAGE}/${SYSTEMIMAGE_TARGET} ${IMAGE_ROOTFS}
    make_ext4fs -s -a /systemrw ${IMAGE_EXT4_SELINUX_OPTIONS} -b 4096 -l ${SYSTEMRW_SIZE_EXT4} ${DEPLOY_DIR_IMAGE}/${SYSTEMRWIMAGE_TARGET}
}
