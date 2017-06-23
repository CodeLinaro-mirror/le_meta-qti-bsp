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
KERNEL_EXTRACONFIGS
}

SRC_URI_append += " \
    file://0001-ion-Add-hvenv-driver-for-ion.patch \
    file://0002-ION-Enable-ION-to-work-with-a-carveout-heap.patch \
    file://0003-amba-skip-clock-calls-on-msm-virtual-platform.patch \
"

do_deploy_rename () {
    mv ${DEPLOY_DIR_IMAGE}/${MACHINE}-boot.img ${DEPLOY_DIR_IMAGE}/boot.img
}
addtask deploy_rename after do_deploy before do_packagedata

do_add_gitinfor() {
    cd ${S}/.git
    rm -rf ./*
    cd ${WORKSPACE}/kernel/msm-4.4/.git/
    cp config  description  HEAD  hooks  index  info  logs  objects  packed-refs  refs  rr-cache  svn  ${S}/.git -fpPRHL
}
addtask add_gitinfor after do_unpack before do_patch
