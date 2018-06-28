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
# CONFIG_WIL6210 is not set
# CONFIG_CNSS2 is not set
# CONFIG_CNSS2_DEBUG is not set
CONFIG_CNSS=y
CONFIG_CNSS_ASYNC=y
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
# CONFIG_ACPI is not set
CONFIG_THERMAL=y
CONFIG_CPU_IDLE=y
# CONFIG_TOUCHSCREEN_SYNAPTICS_DSX_CORE_v21 is not set
# CONFIG_TOUCHSCREEN_SYNAPTICS_DSX_FW_UPDATE_v21 is not set
# CONFIG_TOUCHSCREEN_ATMEL_MXT is not set
# CONFIG_TOUCHSCREEN_ATMEL_MAXTOUCH_TS is not set
# CONFIG_MSM_IPC_ROUTER_SMD_XPRT is not set
# CONFIG_QCOM_REMOTEQDSS is not set
# CONFIG_MSM_IPC_ROUTER_MHI_XPRT is not set
KERNEL_EXTRACONFIGS
}

# make bootimge smaller patch
do_defconfig_tiny_patch () {
cat >> ${S}/arch/${ARCH}/configs/${KERNEL_CONFIG} <<KERNEL_EXTRACONFIGS
# CONFIG_CORESIGHT is not set
# CONFIG_KALLSYMS_ALL is not set
# CONFIG_FTRACE is not set
# CONFIG_ARM_PMU is not set
# CONFIG_HW_PERF_EVENTS is not set
# CONFIG_MMC_PERF_PROFILING is not set
# CONFIG_MMC_TEST is not set
# CONFIG_PROFILER is not set
# CONFIG_IPC_LOGGING is not set
# CONFIG_EVENT_TRACING is not set
CONFIG_DEBUG_FS=y
KERNEL_EXTRACONFIGS
}


do_deploy_rename () {
    mv ${DEPLOYDIR}/${MACHINE}-boot.img ${DEPLOYDIR}/boot.img
}
do_deploy[postfuncs] += "do_deploy_rename"

do_unpack_extra () {
    if [ ${@d.getVar("MACHINE", True)} == "8x96auto" ] || [ ${@d.getVar("MACHINE", True)} == "8x96autodvrs" ]; then
        if ${@bb.utils.contains('DISTRO_FEATURES','early-ethernet','true','false',d)}; then
            mkdir -p ${S}/drivers/net/ethernet/neutrino
            cp -f ${WORKSPACE}/vehiclenetwork/ethernet/* ${S}/drivers/net/ethernet/neutrino/
            rm ${S}/drivers/net/ethernet/neutrino/Makefile
            rm ${S}/drivers/net/ethernet/neutrino/Makefile.am
            rm ${S}/drivers/net/ethernet/neutrino/Kbuild
            mv ${S}/drivers/net/ethernet/neutrino/Makefile.builtin ${S}/drivers/net/ethernet/neutrino/Makefile
            echo "source \"drivers/net/ethernet/neutrino/Kconfig"\" >> ${S}/drivers/net/ethernet/Kconfig
            echo "obj-y += neutrino/" >> ${S}/drivers/net/ethernet/Makefile
        fi
    fi
}

addtask unpack_extra after do_unpack before do_patch

