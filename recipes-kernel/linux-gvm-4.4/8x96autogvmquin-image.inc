INSANE_SKIP_${PN} += "installed-vs-shipped"

do_defconfig_patch () {
cat >> ${S}/arch/${ARCH}/configs/${KERNEL_CONFIG} <<KERNEL_EXTRACONFIGS
CONFIG_DEVTMPFS=y
CONFIG_SYSVIPC=y
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
KERNEL_EXTRACONFIGS
}

SRC_URI_append_kernelpatchok += " \
    file://0003-amba-skip-clock-calls-on-msm-virtual-platform.patch \
    file://0004-interrupt-workaround-for-virtual-plaform.patch \
    file://0009-cnss-Adapt-cnss-to-virtual-platform.patch \
    file://0010-Add-support-for-USB-pass-through-device-on-GVM.patch \
    file://0011-Add-psci-workaround-for-gvm-suspend.patch \
    file://0012-unmask-virtio-net-irq-to-wake-up-system.patch \
    file://0014-trigger-wdog-bite-for-panic-on-gvm.patch \
    file://0001-msm-add-dts-node-qrng.patch \
"

do_deploy_rename () {
    mv ${DEPLOYDIR}/${MACHINE}-boot.img ${DEPLOYDIR}/boot.img
}
do_deploy[postfuncs] += "do_deploy_rename"
