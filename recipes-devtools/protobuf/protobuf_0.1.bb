inherit androidmk deploy

LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://${SRC_DIR}/LICENSE"

FILESPATH =+ "${WORKSPACE}:"
SRC_URI = "file://external/protobuf/"

SRC_DIR = "${WORKSPACE}/external/protobuf"

SRCREV = "${AUTOREV}"
S      = "${WORKDIR}/external/protobuf"

EXTRA_OEMAKE += "-e MAKEFLAGS="

export TARGET_LIBRARY_SUPPRESS_LIST="libz"
export HOST_LIBRARY_SUPPRESS_LIST="libz libz-host"
export HOST_LDFLAGS += "-lz"
export LDFLAGS += "-lz"

DEPENDS += "zlib zlib-native"
