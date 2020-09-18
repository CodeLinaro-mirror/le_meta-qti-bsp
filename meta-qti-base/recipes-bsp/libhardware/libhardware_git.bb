inherit autotools pkgconfig

DESCRIPTION = "hardware libhardware headers"
HOMEPAGE = "http://codeaurora.org/"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/\
${LICENSE};md5=89aea4e17d99a7cacdbeed46a0096b10"

SRC_URI   =  "git://source.codeaurora.org/platform/hardware/libhardware.git;protocol=https;destsuffix=hardware/libhardware;nobranch=1"
# Get Add-gralloc1.h-from-p-keystone-qcom-branch
SRC_URI_append = " https://source.codeaurora.org/quic/le/platform/hardware/libhardware/plain/include/hardware/gralloc1.h?h=keystone/p-keystone-qcom-release;downloadfilename=gralloc1.h;md5sum=5171fc33c1299824ede5756a4da57507"

SRCREV = "cfe9d71b7aecad5477285bc3da499cde02197c43"
S = "${WORKDIR}/hardware/libhardware"

PR = "r6"

DEPENDS += "libutils libcutils liblog system-core-headers"

EXTRA_OECONF_append_apq8053 = " --enable-sensors"
EXTRA_OECONF_append_concam = " --enable-camera"
EXTRA_OECONF_append_sdm845 = " --enable-sensors"
EXTRA_OECONF_append_sdm845 = " --enable-camera"
EXTRA_OECONF_append_robot-som = " --enable-camera"

do_install_append () {
    # remove headers, use libhardware-headers
    rm -rf ${D}${includedir}/hardware/
}
