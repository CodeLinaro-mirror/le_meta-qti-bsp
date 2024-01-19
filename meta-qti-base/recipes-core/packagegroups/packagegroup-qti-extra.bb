SUMMARY = "QTI package group for extra functions. TBD"

PACKAGE_ARCH = "${TUNE_PKGARCH}"

inherit packagegroup

PACKAGES = "\
    packagegroup-qti-extra \
    "

ALLOW_EMPTY:${PN} = "1"

DSP_DRIV ?= ""
DSP_DRIV:monaco = "${@bb.utils.contains('MACHINE_FEATURES', 'qti-hypervisor', '', 'dsp-kernel', d)}"

RDEPENDS:${PN} += "\
    python3 \
    resize-service \
    openssl \
    libxml2 \
    libnl \
    coreutils \
    powerapp-reboot \
    powerapp-shutdown \
    sec-config \
    libsensors \
    ${DSP_DRIV} \
    "

RDEPENDS:${PN}:remove:qti-dpk = "resize-service"
