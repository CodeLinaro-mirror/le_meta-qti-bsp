inherit androidmk deploy

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/\
${LICENSE};md5=89aea4e17d99a7cacdbeed46a0096b10"


FILESPATH =+ "${WORKSPACE}:"
SRC_URI = "file://hardware/libhardware_legacy/"
SRC_URI += "file://0001-Add-support-for-non-Android-builds.patch"

SRC_DIR = "${WORKSPACE}/hardware/libhardware_legacy"

SRCREV = "${AUTOREV}"
S      = "${WORKDIR}/hardware/libhardware_legacy"

DEPENDS += "libcutils system-core libnetutils libhardware"

export TARGET_LIBRARY_SUPPRESS_LIST=""
CFLAGS += "-I${S}/include"
LDFLAGS += "-lcutils -llog"
EXTRA_OEMAKE += "-e MAKEFLAGS="

EXTRA_OEMAKE += "ANDROID_COMPAT=true"

do_install_append() {
	install -d ${D}${includedir}/hardware_legacy/
        install -m 0644 ${S}/include/hardware_legacy/power.h -D ${D}${includedir}/hardware_legacy/power.h
}
