inherit autotools-brokensep qcommon

DESCRIPTION = "wlan dsrc tools"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/${LICENSE};md5=550794465ba0ec5312d6919e203a55f9"


DEPENDS = "libnl"

PARALLEL_MAKE = ""
PR = "r1"
PV = "1.0"

FILESPATH =+ "${WORKSPACE}:"
SRC_URI = "file://wlan/tools/ \
          "
S = "${WORKDIR}/wlan/tools"

CFLAGS += "-I${STAGING_INCDIR}/libnl3"

EXTRA_OEMAKE = "HAVE_LIBNL3=1 all dsrc_config wlan_ts"

do_compile_prepend() {
    cd ${S}/dsrc
}

do_install() {
    install -d ${D}/usr/sbin/
    install -m 0755 ${S}/dsrc/bin/dsrc_* ${D}/usr/sbin/
    install -m 0755 ${S}/dsrc/bin/wlan_ts ${D}/usr/sbin/
    install -m 0755 ${S}/dsrc/bin/dcc.dat ${D}/usr/sbin/
}
