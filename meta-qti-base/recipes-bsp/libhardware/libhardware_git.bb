SUMMARY = "Android libhardware library"
DESCRIPTION = "This library provides access to the Android libhardware HAL(Hardware Abstraction Layer)."
HOMEPAGE = "http://www.codeaurora.org/"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://NOTICE;md5=9645f39e9db895a4aa6e02cb57294595"

DEPENDS += "libutils libcutils liblog system-core-headers"

SRCREV = "${AUTOREV}"
SRC_URI = "\
    ${PATH_TO_REPO}/hardware/libhardware/.git;protocol=${PROTO};destsuffix=hardware/libhardware;usehead=1 \
    https://source.codeaurora.org/quic/la/platform/hardware/libhardware/plain/include/hardware/gralloc1.h?h=keystone/p-keystone-qcom-release;downloadfilename=gralloc1.h;md5sum=5171fc33c1299824ede5756a4da57507 \
"

S = "${WORKDIR}/hardware/libhardware"

inherit autotools pkgconfig

do_install_append () {
    # remove headers, use libhardware-headers
    rm -rf ${D}${includedir}/hardware/
}
