PACKAGE_ARCH = "${MACHINE_ARCH}"

FILESEXTRAPATHS_prepend := "${THISDIR}/agl-audio-plugin:"

FILESPATH =+ "${WORKSPACE}:"
SRC_URI = "file://audio/agl-audio-plugin"
S = "${WORKDIR}/audio/agl-audio-plugin/"

