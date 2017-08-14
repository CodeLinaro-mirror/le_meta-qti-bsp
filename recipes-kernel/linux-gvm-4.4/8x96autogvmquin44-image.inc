INSANE_SKIP_${PN} += "installed-vs-shipped"

do_defconfig_patch () {
cat >> ${S}/arch/${ARCH}/configs/${KERNEL_CONFIG} <<KERNEL_EXTRACONFIGS
CONFIG_DEVTMPFS=y
CONFIG_SYSVIPC=y
CONFIG_ION=y
CONFIG_MSM_GVM_QUIN=y
CONFIG_MSM_GVM=y
CONFIG_DIAG_CHAR=y
CONFIG_FHANDLE=y
CONFIG_SECURITY_SMACK=y
CONFIG_TMPFS_XATTR=y
CONFIG_DEFAULT_SECURITY="smack"
CONFIG_DEFAULT_SECURITY_SMACK=y
CONFIG_ION_MSM=y
CONFIG_CMA=y
CONFIG_MSM_PIL_SSR_GENERIC=y
CONFIG_SLIMBUS=y
CONFIG_SLIMBUS_MSM_NGD=y
CONFIG_SOUNDWIRE=y
CONFIG_SOUNDWIRE_WCD_CTRL=y
CONFIG_SWITCH=y
CONFIG_WCD9XXX_CODEC_UTIL=y
CONFIG_WCD9335_CODEC=y
CONFIG_SND_SOC_MSM8996=y
CONFIG_MSM_ADSP_LOADER=y
KERNEL_EXTRACONFIGS
}

SRC_URI_append += " \
    file://0003-amba-skip-clock-calls-on-msm-virtual-platform.patch \
    file://0004-interrupt-workaround-for-virtual-plaform.patch \
    file://0005-Revert-soc-qcom-remove-debugfs-interface-from-ssr-se.patch \
    file://0006-drivers-char-diag-Enable-diag-over-IP.patch \
    file://0007-disable-scm-for-pil-new.patch \
"

do_deploy_rename () {
    mv ${DEPLOY_DIR_IMAGE}/${MACHINE}-boot.img ${DEPLOY_DIR_IMAGE}/boot.img
}
addtask deploy_rename after do_deploy before do_packagedata
