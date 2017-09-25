INSANE_SKIP_${PN} += "installed-vs-shipped"

do_defconfig_patch () {
cat >> ${S}/arch/${ARCH}/configs/${KERNEL_CONFIG} <<KERNEL_EXTRACONFIGS
CONFIG_DEVTMPFS=y
CONFIG_SYSVIPC=y
CONFIG_ION=y
CONFIG_ION_DUMMY=y
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
KERNEL_EXTRACONFIGS
}

SRC_URI_append += " \
    file://0001-ion-Add-hvenv-driver-for-ion.patch \
    file://0002-ION-Enable-ION-to-work-with-a-carveout-heap.patch \
    file://0003-amba-skip-clock-calls-on-msm-virtual-platform.patch \
    file://0004-interrupt-workaround-for-virtual-plaform.patch \
    file://0005-Revert-soc-qcom-remove-debugfs-interface-from-ssr-se.patch \
    file://0006-drivers-char-diag-Enable-diag-over-IP.patch \
    file://0008-mmc-Fix-mmc-compilation-error-when-devfreq-isn-t-ena.patch \
    file://0009-cnss-Adapt-cnss-to-virtual-platform.patch \
"

do_deploy_rename () {
    mv ${DEPLOY_DIR_IMAGE}/${MACHINE}-boot.img ${DEPLOY_DIR_IMAGE}/boot.img
}
addtask deploy_rename after do_deploy before do_packagedata
