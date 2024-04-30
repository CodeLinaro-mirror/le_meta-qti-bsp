inherit core-image

# This class creates recoveryfs
DEPENDS += "virtual/kernel"
DEPENDS += "pkgconfig-native gtk-doc-native gettext-native virtual/mkbootimg-native"
DEPENDS += "bzip2 fsconfig-native applypatch-native bsdiff-native ext4-utils-native mtd-utils-native"

# Use busybox as login manager
IMAGE_LOGIN_MANAGER = "busybox-static"

# Include minimum init and init scripts
IMAGE_DEV_MANAGER = "udev"
IMAGE_INIT_MANAGER = "systemd"
IMAGE_INITSCRIPTS ?= ""

IMAGE_LINGUAS = ""

do_rootfs[nostamp] = "1"
do_build[nostamp]  = "1"

do_image_ext4[noexec] = "1"
do_image_ubi[noexec] = "1"
do_image_ubifs[noexec] = "1"
do_image_multiubi[noexec] = "1"

CORE_IMAGE_EXTRA_INSTALL += "\
            packagegroup-qti-recoveryfs \
            packagegroup-qti-core-recovery \
"

RM_WORK_EXCLUDE += "${PN}"

# Configs for generating recovery.ubi
RECOVERY_MKUBIFS_ARGS = "-m 4096 -e 253952 -c 233 -F"
RECOVERY_UBINIZE_CFG = "ubinize-recoveryfs.cfg"
RECOVOERY_UBIFS_IMAGE = "recoveryfs.ubifs"
RECOVOERY_UBI_IMAGE = "recoveryfs.ubi"
RECOVERY_SYSTEMRW_VOLUME_SIZE ?= "4MiB"

#configs for ext4
RECOVERYFS_SIZE_EXT4 ?= "100000000"
RECOVOERY_EXT4_IMAGE = "recoveryfs.img"

RECOVERY_UBI_SELINUX_OPTIONS = "${@bb.utils.contains('DISTRO_FEATURES', 'selinux', '--selinux=${SELINUX_FILE_CONTEXTS}', '', d)}"
RECOVERY_EXT4_SELINUX_OPTIONS = "${@bb.utils.contains('DISTRO_FEATURES', 'selinux', '-S ${SELINUX_FILE_CONTEXTS}', '', d)}"

# Update usb composition in recovery mode
RECOVERY_USBCOMPOSITION ?= "901D"
update_usb_composition() {
    if ${@bb.utils.contains('DISTRO_FEATURES','systemd','true','false',d)}; then
        if [ -e ${IMAGE_ROOTFS}/etc/usb/boot_hsusb_comp ]; then
            echo ${RECOVERY_USBCOMPOSITION} > ${IMAGE_ROOTFS}/etc/usb/boot_hsusb_comp
        fi
    fi
}

generate_public_key() {
    if ${@bb.utils.contains('DISTRO_FEATURES','ota-package-verification', 'true', 'false', d)}; then
        openssl pkcs8 -inform DER -nocrypt -in ${WORKSPACE}/OTA/build/target/product/security/testkey.pk8 -out ${TMPDIR}/work/x86_64-linux/releasetools-native/1.0-r0/releasetools/private.pem
        openssl rsa -in ${TMPDIR}/work/x86_64-linux/releasetools-native/1.0-r0/releasetools/private.pem -outform PEM -pubout > ${IMAGE_ROOTFS}/res/public.pem
    fi
}

# Need to copy ubinize.cfg file in the deploy directory
create_ubinize_config[dirs] = "${IMGDEPLOYDIR}"
create_ubinize_config() {
    echo \[recoveryfs_volume\] > "${RECOVERY_UBINIZE_CFG}"
    echo mode=ubi >> "${RECOVERY_UBINIZE_CFG}"
    echo image="${RECOVOERY_UBIFS_IMAGE}" >> "${RECOVERY_UBINIZE_CFG}"
    echo vol_id=0 >> "${RECOVERY_UBINIZE_CFG}"
    echo vol_type=dynamic >> "${RECOVERY_UBINIZE_CFG}"
    echo vol_name=rootfs >> "${RECOVERY_UBINIZE_CFG}"
    echo vol_flags = autoresize >> "${RECOVERY_UBINIZE_CFG}"

    if ${@bb.utils.contains('MACHINE_FEATURES','qti-sdx', 'true', 'false', d)}; then
        echo \[systemrw_volume\] >> "${RECOVERY_UBINIZE_CFG}"
        echo mode=ubi >> "${RECOVERY_UBINIZE_CFG}"
        echo vol_id=1 >> "${RECOVERY_UBINIZE_CFG}"
        echo vol_type=dynamic >> "${RECOVERY_UBINIZE_CFG}"
        echo vol_name=systemrw >> "${RECOVERY_UBINIZE_CFG}"
        echo vol_size="${RECOVERY_SYSTEMRW_VOLUME_SIZE}" >> "${RECOVERY_UBINIZE_CFG}"
    fi
}

fakeroot do_create_recoveryfs_ubi() {
    rm -rf ${IMAGE_ROOTFS}/usr/bin/thermal-engine
    rm -rf ${IMAGE_ROOTFS}/usr/lib/libarchive.so.13.6.2
    rm -rf ${IMAGE_ROOTFS}/usr/bin/diag-router
    rm -rf ${IMAGE_ROOTFS}/usr/lib/libsolv.so.1
    rm -rf ${IMAGE_ROOTFS}/lib/libext2fs.so.2.4
    rm -rf ${IMAGE_ROOTFS}/lib/libext2fs.so.2
    rm -rf ${IMAGE_ROOTFS}/usr/lib/libqmi_cci.so.1.0.0
    rm -rf ${IMAGE_ROOTFS}/usr/lib/libqmi_cci.so.1
    rm -rf ${IMAGE_ROOTFS}/usr/lib/libqmi_common_so.so.1.0.0
    rm -rf ${IMAGE_ROOTFS}/usr/lib/libqmi_common_so.so.1
    rm -rf ${IMAGE_ROOTFS}/usr/lib/libqmi_csi.so.1.0.0
    rm -rf ${IMAGE_ROOTFS}/usr/lib/libqmi_csi.so.1
    rm -rf ${IMAGE_ROOTFS}/usr/lib/libqmi_encdec.so.1.0.0
    rm -rf ${IMAGE_ROOTFS}/usr/lib/libqmi_encdec.so.1
    rm -rf ${IMAGE_ROOTFS}/usr/lib/libqmi_sap.so.1.0.0
    rm -rf ${IMAGE_ROOTFS}/usr/lib/libqmi_sap.so.1
    rm -rf ${IMAGE_ROOTFS}/usr/lib/libqrtr.so.1.0.0
    rm -rf ${IMAGE_ROOTFS}/usr/lib/libqrtr.so.1
    rm -rf ${IMAGE_ROOTFS}/sbin/fsck.ext2
    rm -rf ${IMAGE_ROOTFS}/sbin/fsck.ext3
    rm -rf ${IMAGE_ROOTFS}/sbin/fsck.ext4
    rm -rf ${IMAGE_ROOTFS}/sbin/fsck.util-linux
    rm -rf ${IMAGE_ROOTFS}/sbin/e2fsck
    rm -rf ${IMAGE_ROOTFS}/sbin/fsck
    rm -rf ${IMAGE_ROOTFS}/usr/bin/qmi_test_mt_client_init_instance
    rm -rf ${IMAGE_ROOTFS}/usr/bin/qmi_test_service_clnt_test_0000
    rm -rf ${IMAGE_ROOTFS}/usr/bin/qmi_test_service_clnt_test_0001
    rm -rf ${IMAGE_ROOTFS}/usr/bin/qmi_test_service_clnt_test_1000
    rm -rf ${IMAGE_ROOTFS}/usr/bin/qmi_test_service_clnt_test_1001
    rm -rf ${IMAGE_ROOTFS}/usr/bin/qmi_test_service_clnt_test_2000
    rm -rf ${IMAGE_ROOTFS}/usr/bin/qmi_test_service_clnt_test_3000
    rm -rf ${IMAGE_ROOTFS}/usr/bin/qmi_test_service_clnt_test_3001
    rm -rf ${IMAGE_ROOTFS}/usr/bin/qmi_test_service_clnt_test_4000
    rm -rf ${IMAGE_ROOTFS}/usr/bin/qmi_test_service_clnt_test_4001
    rm -rf ${IMAGE_ROOTFS}/usr/bin/qmi_test_service_start_svc
    rm -rf ${IMAGE_ROOTFS}/usr/bin/qmi_test_service_test
    rm -rf ${IMAGE_ROOTFS}/usr/bin/qrtr-cfg
    rm -rf ${IMAGE_ROOTFS}/usr/bin/qrtr-filter
    rm -rf ${IMAGE_ROOTFS}/usr/bin/qrtr-lookup
    rm -rf ${IMAGE_ROOTFS}/usr/bin/qrtr-ns
    rm -rf ${IMAGE_ROOTFS}/usr/bin/qrtr_test_client
    rm -rf ${IMAGE_ROOTFS}/usr/bin/qrtr_test_server
    mkfs.ubifs -r ${IMAGE_ROOTFS} ${RECOVERY_UBI_SELINUX_OPTIONS} -o ${RECOVOERY_UBIFS_IMAGE} ${RECOVERY_MKUBIFS_ARGS}
    ubinize -o ${RECOVOERY_UBI_IMAGE} ${UBINIZE_ARGS} ${RECOVERY_UBINIZE_CFG}
    chmod 644 ${RECOVOERY_UBI_IMAGE}
}

do_fsconfig() {
    chmod go-r ${IMAGE_ROOTFS}/etc/passwd
}

create_system_dir() {
    if [ ! -e ${IMAGE_ROOTFS}/system ]; then
        mkdir -p ${IMAGE_ROOTFS}/system
    fi
}

# Below is to generate sparse ext4 recovery image (OE by default supports raw ext4 images)
do_create_recoveryfs_ext4() {
    if ${@bb.utils.contains('COMBINED_FEATURES', 'qti-ab-boot', 'false', 'true', d)}; then
        make_ext4fs -l ${RECOVERYFS_SIZE_EXT4} ${RECOVOERY_EXT4_IMAGE} -a / ${RECOVERY_EXT4_SELINUX_OPTIONS} ${IMAGE_ROOTFS}
        # Create an unsparse image as well to be included as part of ota target-files
        #simg2img ${RECOVOERY_EXT4_IMAGE} recovery-unsparsed.ext4
    fi
}

do_create_recoveryfs_ubi[prefuncs] += "update_usb_composition"
do_create_recoveryfs_ubi[prefuncs] += "generate_public_key"
do_create_recoveryfs_ubi[prefuncs] += "create_system_dir"
do_create_recoveryfs_ubi[prefuncs] += "create_ubinize_config"
do_create_recoveryfs_ubi[dirs] = "${IMGDEPLOYDIR}"

do_create_recoveryfs_ext4[prefuncs] = "do_fsconfig"
do_create_recoveryfs_ext4[dirs] = "${IMGDEPLOYDIR}"

python () {
    if bb.utils.contains('IMAGE_FSTYPES', 'ubi', True, False, d):
        bb.build.addtask('do_create_recoveryfs_ubi', 'do_image_complete', 'do_image', d)
    if bb.utils.contains('IMAGE_FSTYPES', 'ext4', True, False, d):
        bb.build.addtask('do_create_recoveryfs_ext4', 'do_image_complete', 'do_image', d)
}

do_cleanup_sepolicy() {

        policy_version=33
        policy_type=mls
        policy_dir=${IMAGE_ROOTFS}/etc/selinux/${policy_type}/policy
        recovery_policy=${policy_dir}/recovery.policy.${policy_version}
        mv -f ${recovery_policy} ${policy_dir}/policy.${policy_version}
}

ROOTFS_POSTPROCESS_COMMAND += "${@bb.utils.contains('DISTRO_FEATURES', 'selinux', 'do_cleanup_sepolicy;', '', d)}"



