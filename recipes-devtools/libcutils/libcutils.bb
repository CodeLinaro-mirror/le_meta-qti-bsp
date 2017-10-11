inherit autotools pkgconfig qcommon

DESCRIPTION = "Build Android libcutils"
HOMEPAGE = "http://developer.android.com/"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/\
${LICENSE};md5=89aea4e17d99a7cacdbeed46a0096b10"

PR = "r2"

DEPENDS += "liblog"

BBCLASSEXTEND = "native"

SRC_URI = "\
    ${CAF_LA_GIT}/platform/system/core.git;protocol=git;nobranch=1;tag=${CAF_TAG};subpath=${BPN};destsuffix=system/core/${BPN} \
    ${CAF_LA_GIT}/platform/system/core.git;protocol=git;nobranch=1;tag=${CAF_TAG};subpath=include;destsuffix=system/core/include \
"

S = "${WORKDIR}/system/core/${BPN}"

EXTRA_OECONF += " --with-core-includes=${WORKDIR}/system/core/include"
EXTRA_OECONF += " --with-host-os=${HOST_OS}"
EXTRA_OECONF += " --disable-static"
EXTRA_OECONF += "${@base_conditional('BASEMACHINE', 'apq8017', ' LE_PROPERTIES_ENABLED=true', '', d)}"
EXTRA_OECONF += "${@base_conditional('BASEMACHINE', 'apq8009', ' LE_PROPERTIES_ENABLED=true', '', d)}"
EXTRA_OECONF += "${@base_conditional('BASEMACHINE', 'apq8053', ' LE_PROPERTIES_ENABLED=true', '', d)}"

EXTRA_OECONF += "${@base_conditional('BASEMACHINE', 'apq8096', ' LE_PROPERTIES_ENABLED=true', '', d)}"
EXTRA_OECONF += "${@base_conditional('BASEMACHINE', 'apq8098', ' LE_PROPERTIES_ENABLED=true', '', d)}"

FILES_${PN}-dbg    = "${libdir}/.debug/libcutils.*"
FILES_${PN}        = "${libdir}/libcutils.so.* ${libdir}/pkgconfig/*"
FILES_${PN}-dev    = "${libdir}/libcutils.so ${libdir}/libcutils.la ${includedir}"
