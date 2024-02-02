inherit autotools pkgconfig

LICENSE = "BSD-3-Clause-Clear"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta-qti-bsp/files/common-licenses/\
BSD-3-Clause-Clear;md5=3771d4920bd6cdb8cbdf1e8344489ee0"

DEPENDS += "jsoncpp"

FILESEXTRAPATHS:prepend = "${WORKSPACE}/:"
SRC_URI   = "file://dsmf/server"

S = "${WORKDIR}/dsmf/server"
