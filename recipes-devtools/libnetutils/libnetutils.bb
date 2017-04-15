inherit androidmk deploy

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/\
${LICENSE};md5=89aea4e17d99a7cacdbeed46a0096b10"

FILESPATH =+ "${WORKSPACE}:"
SRC_URI = "file://system/core/libnetutils/"
SRC_URI += "file://0001-libnetutils-Add-support-for-building-in-non-Android-.patch"

SRC_DIR = "${WORKSPACE}/system/core/libnetutils"

SRCREV = "${AUTOREV}"
S      = "${WORKDIR}/system/core/libnetutils"

DEPENDS += "system-core libcutils liblog"

export TARGET_LIBRARY_SUPPRESS_LIST=""
LDFLAGS += "-lcutils -llog"
