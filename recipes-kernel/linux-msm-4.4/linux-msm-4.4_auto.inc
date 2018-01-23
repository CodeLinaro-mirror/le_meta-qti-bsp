INSANE_SKIP_${PN} += "installed-vs-shipped"

do_defconfig_patch () {
cat >> ${S}/arch/${ARCH}/configs/${KERNEL_CONFIG} <<KERNEL_EXTRACONFIGS
# CONFIG_DEBUG_STACK_USAGE is not set
# CONFIG_IRQSOFF_TRACER is not set
CONFIG_DEVTMPFS=y
CONFIG_DEVTMPFS_MOUNT=y
CONFIG_FHANDLE=y
CONFIG_SYSVIPC=y
CONFIG_VT=y
# CONFIG_FB_MSM is not set
CONFIG_NET_IPIP=y
CONFIG_NET_IPVTI=y
CONFIG_DRM=y
CONFIG_DRM_SDE_HDMI=y
CONFIG_MSM_DBA=y
CONFIG_MSM_DBA_ADV7533=y
CONFIG_REGULATOR_FAN53555=y
# CONFIG_QCOM_KGSL is not set
CONFIG_IP_NF_SECURITY=m
CONFIG_IP6_NF_SECURITY=m
CONFIG_EXT2_FS_SECURITY=y
CONFIG_EXT3_FS_SECURITY=y
CONFIG_EXT4_FS_SECURITY=y
CONFIG_SECURITY=y
CONFIG_SECURITY_SMACK=y
CONFIG_TMPFS_XATTR=y
# CONFIG_RT_GROUP_SCHED is not set
# CONFIG_SECURITY_SELINUX is not set
# CONFIG_PFT is not set
# CONFIG_PFK is not set
CONFIG_DEFAULT_SECURITY="smack"
CONFIG_DEFAULT_SECURITY_SMACK=y
CONFIG_CRYPTO_USER_API=y
CONFIG_CRYPTO_USER_API_HASH=y
CONFIG_CRYPTO_USER_API_SKCIPHER=y
CONFIG_CRYPTO_USER_API_RNG=y
CONFIG_CRYPTO_USER_API_AEAD=y
CONFIG_CRYPTO_GCM=y
CONFIG_CRYPTO_SHA512=y
CONFIG_CRYPTO_SHA256=y
CONFIG_POSIX_MQUEUE=y
CONFIG_VIDEO_ADV7481=y
KERNEL_EXTRACONFIGS
}

SRC_URI_append_8x96auto += " \
    file://0008-ARM-dts-msm-DRAM-diet-for-APQ8096.patch \
"
SRC_URI_append_8x96autodvrs += " \
    file://0009-ARM-dts-msm-DRAM-diet-for-dvrs.patch \
"

do_deploy_rename () {
    mv ${DEPLOY_DIR_IMAGE}/${MACHINE}-boot.img ${DEPLOY_DIR_IMAGE}/boot.img
}
addtask deploy_rename after do_deploy before do_packagedata

do_unpack_extra () {
    if [ ${@d.getVar("MACHINE", True)} == "8x96auto" ] || [ ${@d.getVar("MACHINE", True)} == "8x96autodvrs" ]; then
        mkdir -p ${S}/drivers/net/ethernet/neutrino
        cp -f ${WORKSPACE}/vehiclenetwork/ethernet/* ${S}/drivers/net/ethernet/neutrino/
        rm ${S}/drivers/net/ethernet/neutrino/Makefile
        rm ${S}/drivers/net/ethernet/neutrino/Makefile.am
        rm ${S}/drivers/net/ethernet/neutrino/Kbuild
        mv ${S}/drivers/net/ethernet/neutrino/Makefile.builtin ${S}/drivers/net/ethernet/neutrino/Makefile
        echo "source \"drivers/net/ethernet/neutrino/Kconfig"\" >> ${S}/drivers/net/ethernet/Kconfig
        echo "obj-y += neutrino/" >> ${S}/drivers/net/ethernet/Makefile

        mkdir -p ${S}/drivers/net/ethernet/neutrino_app
        cp -f ${WORKSPACE}/vehiclenetwork/ethernet/DWC_ETH_QOS_app.c ${S}/drivers/net/ethernet/neutrino_app/
        cp -f ${WORKSPACE}/vehiclenetwork/ethernet/Makefile_app ${S}/drivers/net/ethernet/neutrino_app/Makefile
        cp -f ${WORKSPACE}/vehiclenetwork/ethernet/Kconfig_app ${S}/drivers/net/ethernet/neutrino_app/Kconfig
        echo "source \"drivers/net/ethernet/neutrino_app/Kconfig"\" >> ${S}/drivers/net/ethernet/Kconfig
        echo "obj-y += neutrino_app/" >> ${S}/drivers/net/ethernet/Makefile

    fi
}

addtask unpack_extra after do_unpack before do_patch

SRC_URI_append += " \
	file://0001-enable-rootfs-mount-as-dm-verity-target-during-boot.patch \
"
