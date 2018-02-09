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
CONFIG_DEFAULT_SECURITY="smack"
CONFIG_DEFAULT_SECURITY_SMACK=y
CONFIG_MSM_HAB=y
CONFIG_MSM_SECURE_BUFFER=y
CONFIG_QCOM_SCM_QCPE=y
CONFIG_SND_SOC_MSM8996_VM=y
CONFIG_PTP_1588_CLOCK=y
CONFIG_MSM_VIRTCLK_FRONTEND_8996=y
CONFIG_CRYPTO_SHA256=y
KERNEL_EXTRACONFIGS
}

SRC_URI_append += " \
    file://0003-amba-skip-clock-calls-on-msm-virtual-platform.patch \
    file://0004-interrupt-workaround-for-virtual-plaform.patch \
    file://0006-drivers-char-diag-Enable-diag-over-IP.patch \
    file://0009-cnss-Adapt-cnss-to-virtual-platform.patch \
    file://0011-HAB-upgrade-to-the-internal-latest-dev-version.patch \
    file://0011-Add-psci-workaround-for-gvm-suspend.patch \
    file://0012-unmask-virtio-net-irq-to-wake-up-system.patch \
    file://0014-psci-Forbid-psci-reset-call.patch \
    file://0021-automotive-add-boot-time-marker-for-LV-GVM.patch \
    file://0001-enable-rootfs-mount-as-dm-verity-target-during-boot.patch \
"

do_deploy_rename () {
    mv ${DEPLOY_DIR_IMAGE}/${MACHINE}-boot.img ${DEPLOY_DIR_IMAGE}/boot.img
}
addtask deploy_rename after do_deploy before do_packagedata
