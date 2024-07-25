SUMMARY = "External/Out of tree (OOT) device tree overlay"
DESCRIPTION = "External/out of tree (OOT) device tree overlay"
HOMEPAGE = "https://git.codelinaro.org"
LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/${LICENSE};md5=801f80980d171dd6425610833a22dbe6"

DEPENDS += "bison-native dtc-native"

SRC_URI = "\
           ${PATH_TO_REPO}/vendor/qcom/opensource/safelinux-system-cfg/devicetree/.git;protocol=${PROTO};usehead=1 \
           ${PATH_TO_REPO}/kernel/${RH_KERNEL_NAME}/.git;protocol=${PROTO};usehead=1 \
"
SRCREV = "${AUTOREV}"
KERNEL_DIR_SRC = "${SRC_DIR_ROOT}/kernel/${RH_KERNEL_NAME}"
KERNEL_DIR_DESTINATION = "${WORKDIR}/vendor/qcom/opensource/safelinux-system-cfg/devicetree/centos-stream-9"
KERNEL_WORKDIR = "${WORKDIR}/kernel/${RH_KERNEL_NAME}"

S = "${WORKDIR}/vendor/qcom/opensource/safelinux-system-cfg/devicetree"

inherit deploy

do_unpack[depends] += "virtual/kernel:do_configure"

do_compile:prepend() {
    # Copy only the required git metadata needed for "git log", so that we can build defconfigs
    GIT_METADATA_PATH_REFS=`realpath ${KERNEL_DIR_SRC}/.git/refs`
    GIT_METADATA_PATH_OBJECTS=`realpath ${KERNEL_DIR_SRC}/.git/objects`
    rm -rf ${KERNEL_DIR_DESTINATION}
    cp -rf ${KERNEL_WORKDIR} ${KERNEL_DIR_DESTINATION}
    rm -rf ${KERNEL_DIR_DESTINATION}/.git/objects ${KERNEL_DIR_DESTINATION}/.git/refs
    cp -rf ${GIT_METADATA_PATH_OBJECTS} ${GIT_METADATA_PATH_REFS} ${KERNEL_DIR_DESTINATION}/.git
}

do_compile() {
    make
}

OOT_DTBS ?= ""

do_install:append() {
    if [ -d ${S}/oot-dt-bindings/ ]; then
        install -d ${D}${includedir}/safelinux-system-cfg/oot-dt-bindings
        install -m 0644 ${S}/oot-dt-bindings/*.h ${D}${includedir}/safelinux-system-cfg/oot-dt-bindings/
    fi
}

do_deploy() {
    if [ -n "${OOT_DTBS}" ]; then
        install -d ${DEPLOYDIR}/build-artifacts/dtb

        for dtb in ${OOT_DTBS}; do
            if [ -f ${S}/$dtb ]; then
                install -m 0644 ${S}/$dtb ${DEPLOYDIR}/build-artifacts/dtb/
            fi
        done
    fi
}
addtask do_deploy after do_install
