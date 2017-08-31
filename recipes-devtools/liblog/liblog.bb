inherit autotools-brokensep pkgconfig qcommon

DESCRIPTION = "Build Android liblog"
HOMEPAGE = "http://developer.android.com/"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/\
${LICENSE};md5=89aea4e17d99a7cacdbeed46a0096b10"

PR = "r2"

SRC_URI = " \
    ${CAF_LA_GIT}/platform/system/core.git;protocol=git;nobranch=1;tag=${CAF_TAG};subpath=${BPN};destsuffix=system/core/${BPN} \
    ${CAF_LA_GIT}/platform/system/core.git;protocol=git;nobranch=1;tag=${CAF_TAG};subpath=include;destsuffix=system/core/include \
"
SRC_URI  += "file://50-log.rules"

S = "${WORKDIR}/system/core/${BPN}"

BBCLASSEXTEND = "native"

EXTRA_OECONF  = " --with-core-includes=${WORKDIR}/system/core/include"
EXTRA_OECONF += " --disable-static"
EXTRA_OECONF_append_class-target = " --with-logd-logging"

do_install_append() {
    if [ "${CLASSOVERRIDE}" = "class-target" ]; then
       install -m 0644 -D ${WORKDIR}/50-log.rules ${D}${sysconfdir}/udev/rules.d/50-log.rules
    fi
}

FILES_${PN}-dbg = "${libdir}/.debug/* ${bindir}/.debug/*"
FILES_${PN}     = "${libdir}/pkgconfig/* ${libdir}/* ${sysconfdir}/*"
FILES_${PN}-dev = "${libdir}/*.so ${libdir}/*.la ${includedir}/*"
