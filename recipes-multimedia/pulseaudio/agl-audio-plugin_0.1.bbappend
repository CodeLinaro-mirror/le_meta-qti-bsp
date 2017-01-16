PACKAGE_ARCH = "${MACHINE_ARCH}"

FILESEXTRAPATHS_prepend := "${THISDIR}/agl-audio-plugin:"

FILESPATH =+ "${WORKSPACE}:"
SRC_URI = "file://audio/agl-audio-plugin"
SRC_DIR = "${WORKSPACE}/audio/agl-audio-plugin/"
S = "${WORKDIR}/agl-audio-plugin/"

