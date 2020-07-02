INSANE_SKIP_${PN} += "installed-vs-shipped"

DEPENDS += " openssl-native"

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
CONFIG_MSM_AGL=y
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
CONFIG_HW_RANDOM=y
CONFIG_HW_RANDOM_MSM_LEGACY=y
CONFIG_RELAY=y
CONFIG_DEVMEM=y
CONFIG_DEVKMEM=y
#For BT
CONFIG_BT=y
CONFIG_MSM_BT_POWER=y
CONFIG_BTFM_SLIM=y
CONFIG_BTFM_SLIM_WCN3990=y
CONFIG_SLIMBUS=y
CONFIG_SLIMBUS_MSM_NGD=y
CONFIG_RFKILL=y
# CONFIG_SCSI_SCAN_ASYNC is not set
CONFIG_POSIX_MQUEUE=y
CONFIG_PANIC_TIMEOUT=60
CONFIG_CRYPTO_SHA256=y
CONFIG_CC_STACKPROTECTOR_STRONG=y
CONFIG_DEBUG_CREDENTIALS=y
CONFIG_RANDOMIZE_BASE=y

KERNEL_EXTRACONFIGS

if ${@bb.utils.contains('DISTRO_FEATURES', 'qti-perf', 'true', 'false', d)}; then
cat >> ${S}/arch/${ARCH}/configs/${KERNEL_CONFIG} <<KERNEL_EXTRACONFIGS_PERF
# CONFIG_AUDIT is not set
# CONFIG_KALLSYMS_ALL is not set
# CONFIG_KALLSYMS is not set
# CONFIG_FW_LOADER_USER_HELPER_FALLBACK is not set
# CONFIG_FW_LOADER_USER_HELPER is not set
# CONFIG_SERIAL_CORE_CONSOLE is not set
CONFIG_DEBUG_LIST=y
CONFIG_PROFILING=y
CONFIG_OPROFILE=y
CONFIG_SECURITY_DMESG_RESTRICT=y
CONFIG_SECURITY_KPTR_RESTRICT=2
KERNEL_EXTRACONFIGS_PERF
fi
}

SRC_URI_append_kernelpatchok += " \
    file://0001-ion-Add-hvenv-driver-for-ion.patch \
    file://0002-ION-Enable-ION-to-work-with-a-carveout-heap.patch \
    file://0001-Fix_the_compilation_error_for_ion_dummy_driver.patch \
    file://0003-amba-skip-clock-calls-on-msm-virtual-platform.patch \
    file://0003-ION-Add-qsecom-heap-to-hvenv-driver.patch \
    file://0011-Add-psci-workaround-for-gvm-suspend.patch \
    file://0012-unmask-virtio-net-irq-to-wake-up-system.patch \
    file://0001-kernel-misc-qseecom-Use-hvenv-driver-for-ghs-lv-gvm.patch \
    file://0001-ASoC-msm-Change-SEC_MI2S-to-fit-8x96autogvmga.patch \
    file://0001-Enable-CONFIG_PROFILING-CONFIG_OPROFILE-in-perf-buil.patch \
    file://0001-add-kernel.kptr_restrict-Kconfig-options.patch \
    file://0001-ion-use-system-heap-type-instead-of-carve-out-type.patch \
"

SRC_URI_append_kernelpatchok += "${@bb.utils.contains('DISTRO_FEATURES', 'qti-perf', 'file://0001-parallelize-audio-and-pcie-driver-to-improve-bootkpi.patch ', '', d)}"

do_deploy_rename () {
    mv ${DEPLOYDIR}/${MACHINE}-boot.img ${DEPLOYDIR}/boot.img
}
do_deploy[postfuncs] += "do_deploy_rename"

kernel_do_compile() {
        unset CFLAGS CPPFLAGS CXXFLAGS LDFLAGS MACHINE
        # The $use_alternate_initrd is only set from
        # do_bundle_initramfs() This variable is specifically for the
        # case where we are making a second pass at the kernel
        # compilation and we want to force the kernel build to use a
        # different initramfs image.  The way to do that in the kernel
        # is to specify:
        # make ...args... CONFIG_INITRAMFS_SOURCE=some_other_initramfs.cpio
        if [ "$use_alternate_initrd" = "" ] && [ "${INITRAMFS_TASK}" != "" ] ; then
                # The old style way of copying an prebuilt image and building it
                # is turned on via INTIRAMFS_TASK != ""
                copy_initramfs
                use_alternate_initrd=CONFIG_INITRAMFS_SOURCE=${B}/usr/${INITRAMFS_IMAGE}-${MACHINE}.cpio
        fi
        oe_runmake ${KERNEL_IMAGETYPE_FOR_MAKE} ${KERNEL_ALT_IMAGETYPE} CC="${KERNEL_CC}" LD="${KERNEL_LD}" ${KERNEL_EXTRA_ARGS} $use_alternate_initrd
        if test "${KERNEL_IMAGETYPE_FOR_MAKE}.gz" = "${KERNEL_IMAGETYPE}"; then
                gzip -9c < "${KERNEL_IMAGETYPE_FOR_MAKE}" > "${KERNEL_OUTPUT}"
        fi
}

