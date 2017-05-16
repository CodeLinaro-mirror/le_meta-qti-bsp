INSANE_SKIP_${PN} += "installed-vs-shipped"

do_defconfig_patch () {
cat >> ${S}/arch/${ARCH}/configs/${KERNEL_CONFIG} <<KERNEL_EXTRACONFIGS
CONFIG_DEVTMPFS=y
CONFIG_SYSVIPC=y
#CONFIG_ION=y
#CONFIG_ION_DUMMY=y
CONFIG_MSM_GVM_QUIN=y
CONFIG_MSM_GVM=y
CONFIG_FHANDLE=y
CONFIG_SECURITY_SMACK=y
CONFIG_TMPFS_XATTR=y
CONFIG_DEFAULT_SECURITY="smack"
CONFIG_DEFAULT_SECURITY_SMACK=y
KERNEL_EXTRACONFIGS
}

SRC_URI_append_8x96autogvmquin += " \
    file://amba-pl011.c \
    file://0001-ion-Add-hvenv-driver-for-ion.patch \
    file://0002-ion-Enable-ion-to-work-with-a-carveout-heap.patch \
"

do_deploy_rename () {
    mv ${DEPLOY_DIR_IMAGE}/${MACHINE}-boot.img ${DEPLOY_DIR_IMAGE}/boot.img
}
addtask deploy_rename after do_deploy before do_packagedata

do_add_gitinfor() {
    cd ${S}/.git
    rm -rf ./*
    cd ${WORKSPACE}/kernel/msm-3.18/.git/
    cp config  description  HEAD  hooks  index  info  logs  objects  packed-refs  refs  rr-cache  svn  ${S}/.git -fpPRHL
}
addtask add_gitinfor after do_unpack before do_patch


do_configure_append_8x96autogvmquin() {
    cp -f ${WORKDIR}/amba-pl011.c ${S}/drivers/tty/serial/
}
