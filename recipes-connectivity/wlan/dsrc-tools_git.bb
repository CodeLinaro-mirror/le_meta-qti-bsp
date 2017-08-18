inherit autotools-brokensep qcommon

DESCRIPTION = "dsrc-tools"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://${WORKDIR}/wlan/tools/NOTICE;md5=b61e09613da94fcbb21267d3e642f4a4"

DEPENDS = "libnl"

PR = "r0"

FILESPATH =+ "${WORKSPACE}:"
SRC_URI = "file://wlan/tools/"
S = "${WORKDIR}/wlan/tools"

CFLAGS += "-I${STAGING_INCDIR}/libnl3"

EXTRA_OEMAKE = "HAVE_LIBNL3=1 all dsrc_config"

do_compile_prepend() {
    cd ${S}/dsrc
}

do_install() {
    install -d ${D}${bindir}
    install -m 0755 ${S}/dsrc/bin/dsrc* ${D}${bindir}
    install -m 0644 ${S}/dsrc/bin/dcc.dat ${D}${bindir}
}

