FILESEXTRAPATHS_prepend := "${WORKSPACE}:"

SRC_URI = "\
           file://external/strongswan \
          "

SRC_DIR = "${WORKSPACE}/external/strongswan"
S = "${WORKDIR}/external/strongswan"
B = "${WORKDIR}/external/strongswan"

PV = "5.5.2"
PR = "r0"

FILE_DIRNAME = "${WORKSPACE}/poky/meta-qti-bsp/recipes-security/strongswan"

do_install_append() {
  rm -f ${D}/lib/systemd/system/strongswan.service
  patch  ${D}${sysconfdir}/strongswan.conf ${FILE_DIRNAME}/strongswan.patch
  patch  ${D}${sysconfdir}/strongswan.d/charon.conf ${FILE_DIRNAME}/charon.patch
}

SYSTEMD_SERVICE_${PN} = "${BPN}-swanctl.service"

FILES_{PN} += "/lib/systemd/system/strongswan-swanctl.service"
