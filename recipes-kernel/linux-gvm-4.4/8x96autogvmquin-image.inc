INSANE_SKIP_${PN} += "installed-vs-shipped"

do_defconfig_patch () {
cat >> ${S}/arch/${ARCH}/configs/${KERNEL_CONFIG} <<KERNEL_EXTRACONFIGS
CONFIG_DEVTMPFS=y
CONFIG_SYSVIPC=y
CONFIG_HOTPLUG_SIZE_BITS=28
CONFIG_MEMORY_HOTPLUG=y
CONFIG_MEMORY_HOTREMOVE=y
CONFIG_ION=y
CONFIG_ION_MSM=y
CONFIG_CMA=y
CONFIG_DMA_CMA=y
CONFIG_MSM_GVM_QUIN=y
CONFIG_MSM_GVM=y
CONFIG_FHANDLE=y
CONFIG_SECURITY_SMACK=y
CONFIG_TMPFS_XATTR=y
# CONFIG_RT_GROUP_SCHED is not set
CONFIG_DEFAULT_SECURITY="smack"
CONFIG_DEFAULT_SECURITY_SMACK=y
CONFIG_MSM_HAB=y
CONFIG_MSM_SECURE_BUFFER=y
CONFIG_QCOM_SCM_QCPE=y
CONFIG_QSEECOM=y
CONFIG_SND_SOC_MSM8996_VM=y
CONFIG_PTP_1588_CLOCK=y
CONFIG_MSM_VIRTCLK_FRONTEND_8996=y
CONFIG_CRYPTO_SHA256=y
CONFIG_ARM64_CRYPTO=y
CONFIG_CRYPTO_SHA1_ARM64_CE=y
CONFIG_CRYPTO_SHA2_ARM64_CE=y
CONFIG_CRYPTO_GHASH_ARM64_CE=y
CONFIG_CRYPTO_AES_ARM64_CE_CCM=y
CONFIG_CRYPTO_AES_ARM64_CE_BLK=y
CONFIG_CRYPTO_AES_ARM64_NEON_BLK=y
CONFIG_CRYPTO_CRC32_ARM64=y
CONFIG_HW_RANDOM=y
CONFIG_HW_RANDOM_MSM_LEGACY=y
CONFIG_POSIX_MQUEUE=y
# CONFIG_CNSS2 is not set
# CONFIG_CNSS2_DEBUG is not set
# CONFIG_WCNSS_MEM_PRE_ALLOC is not set
# CONFIG_CNSS_CRYPTO is not set
KERNEL_EXTRACONFIGS
}

SRC_URI_append_kernelpatchok += " \
    file://0003-amba-skip-clock-calls-on-msm-virtual-platform.patch \
    file://0010-Add-support-for-USB-pass-through-device-on-GVM.patch \
    file://0011-Add-psci-workaround-for-gvm-suspend.patch \
    file://0012-unmask-virtio-net-irq-to-wake-up-system.patch \
    file://0014-trigger-wdog-bite-for-panic-on-gvm.patch \
    file://0021-automotive-add-boot-time-marker-for-LV-GVM.patch \
"

do_deploy_rename () {
    mv ${DEPLOYDIR}/${MACHINE}-boot.img ${DEPLOYDIR}/boot.img
}
do_deploy[postfuncs] += "do_deploy_rename"
