inherit autotools qcommon

DESCRIPTION = "encoders"
LICENSE = "BSD"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/\
${LICENSE};md5=3775480a712fc46a69647678acb234cb"

PR = "r0"

SRC_URI = " \
    ${CAF_LA_GIT}/platform/hardware/qcom/audio.git;protocol=git;nobranch=1;tag=${CAF_TAG};subpath=mm-audio;destsuffix=hardware/qcom/audio/mm-audio \
"

S = "${WORKDIR}/hardware/qcom/audio/mm-audio/"

EXTRA_OECONF_append += " --with-sanitized-headers=${STAGING_KERNEL_BUILDDIR}/usr/include"
EXTRA_OECONF_append += " --with-glib"

DEPENDS = "media"

FILES_${PN}-dbg  = "${libdir}/.debug/* ${bindir}/.debug/*"
FILES_${PN}      = "${libdir}/*.so ${libdir}/*.so.* ${sysconfdir}/* ${bindir}/* ${libdir}/pkgconfig/*"
FILES_${PN}-dev  = "${libdir}/*.la ${includedir}"
INSANE_SKIP_${PN} = "dev-so"
