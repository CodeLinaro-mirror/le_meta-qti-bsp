INSANE_SKIP_${PN} += "installed-vs-shipped"

DEPENDS += " openssl-native"

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
KERNEL_EXTRACONFIGS
}

SRC_URI_append_8x96autocv2x += " \
    file://0001-esoc-Disable-access-check-for-ESOC-IOCTLs.patch \
"

SRC_URI_append_8x96mizar += " \
    git://source.codeaurora.org/quic/la/kernel/msm-4.4;nobranch=1;tag=8a1eab637309319aee4caa10b196eb7327b549ae;protocol=git;destsuffix=drivers/ntb;subpath=drivers/ntb \
    git://source.codeaurora.org/quic/la/kernel/msm-4.4;nobranch=1;tag=8a1eab637309319aee4caa10b196eb7327b549ae;protocol=git;destsuffix=include/linux/;subpath=include/linux \
    file://0001-defconfig-Enable-NTB-framework-and-IDT-hw-driver.patch \
    https://source.codeaurora.org/quic/la/kernel/msm-4.4/patch/?id=f083f557186d77de5f14bf90aec1bfe2503cf513;downloadfilename=0001-dmaengine-Add-helper-function-to-prep-for-error-repo.patch;md5sum=4d8b270cb5a0efc0192b8258e3bfa228;sha256sum=c07431e5ee692f60e82f7310bd1b47787d2c588236cf42a9eeba734453e0fe32 \
    https://source.codeaurora.org/quic/la/kernel/msm-4.4/patch/?id=f067025bc676ba8d18fba5f959598339e39b86db;downloadfilename=0001-dmaengine-add-support-to-provide-error-result-from-a.patch;md5sum=c84a235ce3b2ad450da1c27d4b790b86;sha256sum=948512ded510b2ab9121da4befd9acd3cb4e07690dd0560b7d9bdc09ba112223 \
    file://0001-defconfig-msm8996-Enable-NTB-for-Perf-build.patch \
    file://0001-ntb-ntb_perf-Disable-dma-routine.patch \
    file://0001-ntb-hw-idt-Fix-set-DMA-mask-bit-failure.patch \
    file://0001-dirvers-ntb-Fix-isr-routine-backport-issue.patch \
    file://0001-ntb_transport-Enable-ntb-tranport-layer-for-IDT-devi.patch \
    file://0001-net-ntb_netdev-Change-module-load-order.patch \
    file://0001-Revert-ARM-dts-msm-modify-HDMI-configuration-for-msm.patch \
    file://0001-hdmi-Map-corerct-mizar-physical-lanes-to-phy.patch \
"

do_deploy_rename () {
    mv ${DEPLOY_DIR_IMAGE}/${MACHINE}-boot.img ${DEPLOY_DIR_IMAGE}/boot.img
}

addtask deploy_rename after do_deploy before do_packagedata


do_unpack_extra () {
    if [ ${@d.getVar("MACHINE", True)} == "8x96auto" ]; then
        mkdir -p ${S}/drivers/net/ethernet/neutrino
        cp -f ${WORKSPACE}/vehiclenetwork/ethernet/* ${S}/drivers/net/ethernet/neutrino/
        rm ${S}/drivers/net/ethernet/neutrino/Makefile
        rm ${S}/drivers/net/ethernet/neutrino/Makefile.am
        rm ${S}/drivers/net/ethernet/neutrino/Kbuild
        mv ${S}/drivers/net/ethernet/neutrino/Makefile.builtin ${S}/drivers/net/ethernet/neutrino/Makefile
        echo "source \"drivers/net/ethernet/neutrino/Kconfig"\" >> ${S}/drivers/net/ethernet/Kconfig
        echo "obj-y += neutrino/" >> ${S}/drivers/net/ethernet/Makefile
    fi
    if [ ${@d.getVar("MACHINE", True)} == "8x96mizar" ]; then
      cp -rf ${WORKDIR}/drivers/ntb/* ${S}/drivers/ntb/
      cp -rf ${WORKDIR}/include/linux/ntb.h ${S}/include/linux/
    fi
}

addtask unpack_extra after do_unpack before do_patch

SRC_URI_append += " \
	file://0001-enable-rootfs-mount-as-dm-verity-target-during-boot.patch \
"
