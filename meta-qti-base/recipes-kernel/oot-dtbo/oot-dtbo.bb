SUMMARY = "Security devicetree"
DESCRIPTION = "To provide devicetree attributes for gvm kernel"
LICENSE = "GPLv2.0-with-linux-syscall-note"
LIC_FILES_CHKSUM = "file://COPYING;md5=6bc538ed5bd9a7fc9398086aedcd7e46"

SRC_URI = "${PATH_TO_REPO}/kernel_platform/qcom/opensource/devicetree/.git;protocol=${PROTO};destsuffix=kernel_platform/qcom/opensource/devicetree;usehead=1 \
           ${PATH_TO_REPO}/vendor/qcom/opensource/platform-kernel/.git;protocol=${PROTO};destsuffix=vendor/qcom/opensource/platform-kernel;usehead=1 "
SRCREV = "${AUTOREV}"

S = "${WORKDIR}/kernel_platform/qcom/opensource/devicetree/qcom"

EXT_MODULE = "${WORKDIR}/kernel_platform/qcom/opensource/devicetree/qcom"

DT_MAKE = "${WORKDIR}/vendor/qcom/opensource/platform-kernel/qclinux/devicetree-make"

CLEANBROKEN = "1"

inherit qti-techpack

do_compile[depends] += "soc-repo:do_configure"

do_configure:append() {
    find ${S} -maxdepth 1 -name "Makefile" -delete
    if [ -f ${DT_MAKE}/Makefile ]; then
        cp ${DT_MAKE}/* ${S}/
    else
        bbfatal "Makefile not found in devicetree-make"
    fi
}

TECHPACK_MODULE_OUT = "${S}"

do_deploy () {
    install -d ${DEPLOYDIR}/build-artifacts/dtb
    install -d ${DEPLOYDIR}/build-artifacts/dtbo

    for dtb in ${TECHPACK_DTBS}; do
        if [ -f ${S}/$dtb ]; then
            install -m 0644 ${S}/$dtb ${DEPLOYDIR}/build-artifacts/dtb/
        fi
    done

    for dtbo in ${TECHPACK_DTBOS}; do
        if [ -f ${S}/$dtbo ]; then
            install -m 0644 ${S}/$dtbo ${DEPLOYDIR}/build-artifacts/dtbo/
        fi
    done
}

TECHPACK_DTBS = "sa8797p-gunyah-vm-lv-qam.dtb"
TECHPACK_DTBOS = "sa8797p-gunyah-vm-lv-qam-overlay.dtbo"
