inherit pkgconfig qprebuilt autotools-brokensep systemd

DESCRIPTION = "Sensing-hub Library"
LICENSE = "BSD-3-Clause-Clear"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta-qti-bsp/files/common-licenses/${LICENSE};md5=3771d4920bd6cdb8cbdf1e8344489ee0"


DEPENDS += "protobuf"
DEPENDS += "protobuf-native"

FILESPATH =+ "${WORKSPACE}/vendor/qcom/opensource/:"
SRC_URI  = "file://sensing-hub/"
S = "${WORKDIR}/sensing-hub"

EXTRA_OECONF += " --with-common-includes"
EXTRA_OECONF += " --with-systemd"

do_install:append() {
    install -d ${D}/usr/include
    install -m 0755 ${S}/session/1.0/inc/* -D ${D}/usr/include/
    install -m 0755 ${S}/common/inc/* -D ${D}/usr/include/
}

#Disable the split of debug information into -dbg files
INHIBIT_PACKAGE_DEBUG_SPLIT = "1"

#Skips check for .so symlinks
INSANE_SKIP:${PN} = "dev-so"

# need to export these variables for python-config to work
FILES:${PN} += "${includedir}/*"
FILES:${PN} += "/usr/lib/*"
FILES:${PN} += "/usr/bin/*"
FILES:${PN}-dev  = "${libdir}/*.la ${includedir}"

SOLIBS = ".so"
FILES:SOLIBSDEV = ""
PACKAGE_ARCH    ?= "${MACHINE_ARCH}"

