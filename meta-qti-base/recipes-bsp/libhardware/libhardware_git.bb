DESCRIPTION = "hardware libhardware headers"
HOMEPAGE = "http://codeaurora.org/"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/\
${LICENSE};md5=89aea4e17d99a7cacdbeed46a0096b10"

DEPENDS += "libutils libcutils liblog system-core-headers"

SRCREV = "${AUTOREV}"
PR = "r6"

SRC_URI = "${PATH_TO_REPO}/hardware/libhardware/.git;protocol=${PROTO};destsuffix=hardware/libhardware;usehead=1"
# Get Add-gralloc1.h-from-p-keystone-qcom-branch
SRC_URI_append = " https://source.codeaurora.org/quic/la/platform/hardware/libhardware/plain/include/hardware/gralloc1.h?h=keystone/p-keystone-qcom-release;downloadfilename=gralloc1.h;md5sum=5171fc33c1299824ede5756a4da57507"

S = "${WORKDIR}/hardware/libhardware"

inherit autotools pkgconfig

do_install_append () {
    # remove headers, use libhardware-headers
    rm -rf ${D}${includedir}/hardware/
}
