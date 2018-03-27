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
#CONFIG_SECURITY_SMACK=y
CONFIG_TMPFS_XATTR=y
# CONFIG_RT_GROUP_SCHED is not set
#CONFIG_DEFAULT_SECURITY="smack"
#CONFIG_DEFAULT_SECURITY_SMACK=y
CONFIG_ION_MSM=y
CONFIG_CMA=y
CONFIG_DMA_CMA=y
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
CONFIG_WIRELESS=y
CONFIG_WIRELESS_EXT=y
CONFIG_WEXT_CORE=y
CONFIG_WEXT_PROC=y
CONFIG_WEXT_SPY=y
CONFIG_WEXT_PRIV=y
CONFIG_CFG80211=y
CONFIG_NL80211_TESTMODE=y
CONFIG_CFG80211_INTERNAL_REGDB=y
CONFIG_CNSS=y
CONFIG_WCNSS_MEM_PRE_ALLOC=y
CONFIG_CNSS_CRYPTO=y
CONFIG_ATH_CARDS=y
CONFIG_CNSS_ASYNC=y
CONFIG_CLD_LL_CORE=y
CONFIG_MSM_HAB=y
CONFIG_MSM_SECURE_BUFFER=y
CONFIG_QCOM_SCM_QCPE=y
CONFIG_QSEECOM=y
CONFIG_MSM_PIL_SSR_GENERIC=y
CONFIG_MSM_ADSPRPC=y
CONFIG_MSM_VIRTCLK_FRONTEND_8996=y
KERNEL_EXTRACONFIGS
}

SRC_URI_append_kernelpatchok += " \
    file://0003-amba-skip-clock-calls-on-msm-virtual-platform.patch \
    file://0004-interrupt-workaround-for-virtual-plaform.patch \
    file://0009-cnss-Adapt-cnss-to-virtual-platform.patch \
    file://0010-Add-support-for-USB-pass-through-device-on-GVM.patch \
    file://0011-HAB-upgrade-to-the-internal-latest-dev-version.patch \
    file://0011-Add-psci-workaround-for-gvm-suspend.patch \
    file://0012-unmask-virtio-net-irq-to-wake-up-system.patch \
    file://0014-trigger-wdog-bite-for-panic-on-gvm.patch \
    file://0021-automotive-add-boot-time-marker-for-LV-GVM.patch \
    file://0022-cnss-change-pcie-fw-path-for-dual-wifi-support.patch \
"

do_deploy_rename () {
    mv ${DEPLOY_DIR_IMAGE}/${MACHINE}-boot.img ${DEPLOY_DIR_IMAGE}/boot.img
}
addtask deploy_rename after do_deploy before do_packagedata
