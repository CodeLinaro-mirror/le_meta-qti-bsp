FILESEXTRAPATHS_prepend := "${WORKSPACE}:"
SRC_URI = "\
           file://external/strongswan \
          "

SRC_DIR = "${WORKSPACE}/external/strongswan"
S = "${WORKDIR}/strongswan"
B = "${WORKDIR}/strongswan"

PV = "5.5.2"
PR = "r0"

SYSTEMD_SERVICE_${PN} = "${BPN}-swanctl.service ${BPN}.service"

FILES_{PN} += "/lib/system/system/strongswan-swanctl.service"
