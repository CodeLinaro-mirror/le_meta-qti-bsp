INSANE_SKIP_${PN} += "installed-vs-shipped"

do_defconfig_patch () {
cat >> ${S}/arch/${ARCH}/configs/${KERNEL_CONFIG} <<KERNEL_EXTRACONFIGS
# CONFIG_DEBUG_STACK_USAGE is not set
# CONFIG_IRQSOFF_TRACER is not set
CONFIG_DEVTMPFS=y
CONFIG_DEVTMPFS_MOUNT=y
CONFIG_FHANDLE=y
CONFIG_SYSVIPC=y
CONFIG_VT=y
# CONFIG_FB_MSM is not set
CONFIG_NET_IPIP=y
CONFIG_NET_IPVTI=y
CONFIG_DRM=y
CONFIG_DRM_SDE_HDMI=y
CONFIG_MSM_DBA=y
CONFIG_MSM_DBA_ADV7533=y
CONFIG_REGULATOR_FAN53555=y
# CONFIG_QCOM_KGSL is not set
CONFIG_IP_NF_SECURITY=m
CONFIG_IP6_NF_SECURITY=m
CONFIG_EXT2_FS_SECURITY=y
CONFIG_EXT3_FS_SECURITY=y
CONFIG_EXT4_FS_SECURITY=y
CONFIG_SECURITY=y
CONFIG_SECURITY_SMACK=y
CONFIG_TMPFS_XATTR=y
# CONFIG_RT_GROUP_SCHED is not set
# CONFIG_SECURITY_SELINUX is not set
# CONFIG_PFT is not set
# CONFIG_PFK is not set
CONFIG_DEFAULT_SECURITY="smack"
CONFIG_DEFAULT_SECURITY_SMACK=y
CONFIG_CRYPTO_USER_API=y
CONFIG_CRYPTO_USER_API_HASH=y
CONFIG_CRYPTO_USER_API_SKCIPHER=y
CONFIG_CRYPTO_USER_API_RNG=y
CONFIG_CRYPTO_USER_API_AEAD=y
CONFIG_CRYPTO_GCM=y
CONFIG_CRYPTO_SHA512=y
CONFIG_CRYPTO_SHA256=y
CONFIG_POSIX_MQUEUE=y
CONFIG_VIDEO_ADV7481=y
CONFIG_CAN_KVASER_USB=y
CONFIG_CIFS=y
CONFIG_USB_ACM=y
CONFIG_CNSS_GENL=y
KERNEL_EXTRACONFIGS

cat >> ${S}/arch/${ARCH}/configs/${KERNEL_CONFIG} <<KERNEL_EXTRACONFIGS
# CONFIG_MSM_RMNET_MHI is not set
# CONFIG_MSM_MHI is not set
# CONFIG_MSM_MHI_UCI is not set
# CONFIG_MSM_MHI_DEBUG is not set
# CONFIG_RMNET_DATA is not set
# CONFIG_RMNET_DATA_FC is not set
# CONFIG_RMNET_DATA_DEBUG_PKT is not set
KERNEL_EXTRACONFIGS
}

do_deploy_rename () {
    mv ${DEPLOY_DIR_IMAGE}/${MACHINE}-boot.img ${DEPLOY_DIR_IMAGE}/boot.img
}

addtask deploy_rename after do_deploy before do_packagedata

FILESEXTRAPATHS_append := ":${WORKSPACE}/kernel"
SRC_URI += "file://eap-qti-kernel"

do_unpack_extra () {
    cp -r ${WORKDIR}/eap-qti-kernel/* ${S}/
    cp ${WORKDIR}/eap-qti-kernel/devicetree/* ${S}/arch/${ARCH}/boot/dts/
    echo "source \"drivers/qti/Kconfig"\" >> ${S}/drivers/Kconfig
    echo "obj-y += qti/" >> ${S}/drivers/Makefile
    echo "header-y += qrtr.h" >> ${S}/include/uapi/linux/Kbuild
    echo "header-y += msm_rmnet.h" >> ${S}/include/uapi/linux/Kbuild
    echo "header-y += rmnet_data.h" >> ${S}/include/uapi/linux/Kbuild
    echo "#include \"external-modem-pcie.dtsi\"" >> ${S}/arch/arm64/boot/dts/qcom/msm8996-cv2x.dtsi
    echo "&mhi_0 { qcom,pci-domain = <2>; };" >> ${S}/arch/arm64/boot/dts/qcom/msm8996-cv2x.dtsi
}

addtask unpack_extra after do_unpack before do_patch

do_patch_extra () {
    cat ${WORKDIR}/eap-qti-kernel/defconfig >> ${S}/arch/${ARCH}/configs/${KERNEL_CONFIG}
    echo "# CONFIG_DRM_MSM_EARLY_CARD is not set" >> ${S}/arch/${ARCH}/configs/${KERNEL_CONFIG}
}

addtask patch_extra after do_patch before do_configure
