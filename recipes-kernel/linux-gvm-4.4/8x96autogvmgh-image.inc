INSANE_SKIP_${PN} += "installed-vs-shipped"

do_defconfig_patch () {
cat >> ${S}/arch/${ARCH}/configs/${KERNEL_CONFIG} <<KERNEL_EXTRACONFIGS
CONFIG_DEVTMPFS=y
CONFIG_SYSVIPC=y
CONFIG_ION=y
CONFIG_ION_MSM=y
CONFIG_ION_DUMMY=y
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
CONFIG_MMC=y
CONFIG_QSEECOM=y
KERNEL_EXTRACONFIGS
}

SRC_URI_append_kernelpatchok += " \
    file://0001-ion-Add-hvenv-driver-for-ion.patch \
    file://0002-ION-Enable-ION-to-work-with-a-carveout-heap.patch \
    file://0003-amba-skip-clock-calls-on-msm-virtual-platform.patch \
    file://0003-ION-Add-qsecom-heap-to-hvenv-driver.patch \
    file://0011-Add-psci-workaround-for-gvm-suspend.patch \
    file://0012-unmask-virtio-net-irq-to-wake-up-system.patch \
    file://0001-kernel-misc-qseecom-Use-hvenv-driver-for-ghs-lv-gvm.patch \
"

do_deploy_rename () {
    mv ${DEPLOYDIR}/${MACHINE}-boot.img ${DEPLOYDIR}/boot.img
}
do_deploy[postfuncs] += "do_deploy_rename"
