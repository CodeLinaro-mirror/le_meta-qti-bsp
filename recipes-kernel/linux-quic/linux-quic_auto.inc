INSANE_SKIP_${PN} += "installed-vs-shipped"

do_defconfig_patch () {
cat >> ${S}/arch/${ARCH}/configs/${KERNEL_CONFIG} <<KERNEL_EXTRACONFIGS
CONFIG_DEVTMPFS=y
CONFIG_SYSVIPC=y
CONFIG_VT=y
CONFIG_INET_LRO=y
# CONFIG_FB is not set
# CONFIG_FB_MSM is not set
# CONFIG_MSM_DBA is not set
# CONFIG_MSM_DBA_ADV7533 is not set
# CONFIG_FB_MSM_MDSS is not set
# CONFIG_FB_MSM_MDSS_WRITEBACK is not set
# CONFIG_FB_MSM_MDSS_HDMI_PANEL is not set
# CONFIG_FB_MSM_MDSS_XLOG_DEBUG is not set
# CONFIG_MSM_KGSL is not set
CONFIG_DRM=y
CONFIG_SYNC=y
CONFIG_SW_SYNC=y
CONFIG_POSIX_MQUEUE=y
CONFIG_MSM_BA_V4L2=y
CONFIG_MSM_DBA=y
CONFIG_MSM_DBA_ADV7533=y
CONFIG_FHANDLE=y
CONFIG_NET_IPIP=y
CONFIG_NET_IPVTI=y
CONFIG_IP_NF_SECURITY=m
CONFIG_IP6_NF_SECURITY=m
CONFIG_EXT2_FS_SECURITY=y
CONFIG_EXT3_FS_SECURITY=y
CONFIG_EXT4_FS_SECURITY=y
CONFIG_SECURITY=y
CONFIG_SECURITY_SMACK=y
CONFIG_TMPFS_XATTR=y
# CONFIG_SECURITY_SELINUX is not set
# CONFIG_PFT is not set
# CONFIG_PFK is not set
CONFIG_DEFAULT_SECURITY="smack"
CONFIG_DEFAULT_SECURITY_SMACK=y
CONFIG_CRYPTO_GCM=y
CONFIG_CRYPTO_SHA512=y
CONFIG_CRYPTO_SHA256=y
KERNEL_EXTRACONFIGS
}

do_deploy_rename () {
    mv ${DEPLOY_DIR_IMAGE}/${MACHINE}-boot.img ${DEPLOY_DIR_IMAGE}/boot.img
}
addtask deploy_rename after do_deploy before do_packagedata

do_add_gitinfor() {
    cd ${S}/.git
    rm -rf ./*
    cd ${WORKSPACE}/kernel/msm-3.18/.git/
    cp config  HEAD  hooks  index  logs  objects  refs  rr-cache  svn  ${S}/.git -fpPRHL
}
addtask add_gitinfor after do_unpack before do_patch


