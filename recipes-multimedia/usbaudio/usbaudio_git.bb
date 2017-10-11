inherit autotools pkgconfig qcommon

DESCRIPTION = "usbaudio"
LICENSE = "BSD"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/\
${LICENSE};md5=3775480a712fc46a69647678acb234cb"

SRC_URI   = " \
    ${CAF_LA_GIT}/platform/hardware/libhardware.git;protocol=git;nobranch=1;tag=${CAF_TAG};subpath=modules/usbaudio;destsuffix=hardware/libhardware/modules/usbaudio \
"

S = "${WORKDIR}/hardware/libhardware/modules/usbaudio/"

PR = "r0"

DEPENDS = "tinyalsa system-media libhardware"

FILES_${PN} += "${libdir}/*.so"
INSANE_SKIP_${PN} = "dev-deps"
