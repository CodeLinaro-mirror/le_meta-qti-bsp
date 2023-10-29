SUMMARY = "External/Out of tree (OOT) device tree overlay"
DESCRIPTION = "External/out of tree (OOT) device tree overlay"
HOMEPAGE = "https://git.codelinaro.org"
LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/${LICENSE};md5=801f80980d171dd6425610833a22dbe6"

SRC_URI = "${PATH_TO_REPO}/vendor/qcom/opensource/safelinux-system-cfg/devicetree/.git;protocol=${PROTO};usehead=1"
SRC_URI += "${PATH_TO_REPO}/kernel/rh-kernel-5.14/.git;protocol=${PROTO};usehead=1"
SRCREV = "${AUTOREV}"

S = "${WORKDIR}/vendor/qcom/opensource/safelinux-system-cfg/devicetree"

do_prepare_kernel_source() {
    for altfile in config hooks logs objects packed-refs refs rr-cache svn ; do
        rm -rf ${WORKDIR}/kernel/rh-kernel-5.14/.git/${altfile}
    done
    for altfile in config logs refs ; do
        cp -rf ${SRC_DIR_ROOT}/.repo/projects/kernel/rh-kernel-5.14.git/${altfile} ${WORKDIR}/kernel/rh-kernel-5.14/.git
    done
    for altfile in hooks objects ; do
        cp -rf ${SRC_DIR_ROOT}/.repo/project-objects/kernel/ark-5.14.git/${altfile} ${WORKDIR}/kernel/rh-kernel-5.14/.git
    done
    rm -rf ${WORKDIR}/vendor/qcom/opensource/safelinux-system-cfg/devicetree/centos-stream-9
    mv ${WORKDIR}/kernel/rh-kernel-5.14 ${WORKDIR}/vendor/qcom/opensource/safelinux-system-cfg/devicetree/centos-stream-9
}
addtask prepare_kernel_source after do_patch before do_compile

do_compile() {
    make
}

do_install() {
    install -d ${D}/sysroot-only
    install -m 0755 ${WORKDIR}/vendor/qcom/opensource/safelinux-system-cfg/devicetree/centos-stream-9/arch/arm64/boot/dts/qcom/sa8775p-ride.dtb.overlay ${D}/sysroot-only
}

FILES:${PN} += "sysroot-only/*"
