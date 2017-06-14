FILESEXTRAPATHS_prepend := "${WORKSPACE}:"

SRC_URI = "\
           file://external/strongswan \
          "

SRC_DIR = "${WORKSPACE}/external/strongswan"
S = "${WORKDIR}/external/strongswan"
B = "${WORKDIR}/external/strongswan"

PV = "5.5.2"
PR = "r0"

do_install_append() {
  rm -f ${D}/lib/systemd/system/strongswan.service
}

SYSTEMD_SERVICE_${PN} = "${BPN}-swanctl.service"

FILES_{PN} += "/lib/systemd/system/strongswan-swanctl.service"
