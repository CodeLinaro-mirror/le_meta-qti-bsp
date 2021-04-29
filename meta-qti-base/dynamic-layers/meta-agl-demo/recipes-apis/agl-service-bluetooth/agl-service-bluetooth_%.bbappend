FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

SRC_URI += "file://0001-agl-service-bluetooth-run-without-bluez.patch"

AGLWGT_CMAKE_CONFIGURE_ARGS_append = " -DMOCK_DATA=1"

