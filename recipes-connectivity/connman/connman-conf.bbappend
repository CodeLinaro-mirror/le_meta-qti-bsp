# Including the file depends on chipset
INCSUFFIX = "${@base_conditional('MACHINE', '8x96autofusion', 'connman-conf', 'none',d)}"
FOLDERSUFFIX = "${@base_conditional('MACHINE', '8x96autofusion', 'connman-conf-8x96autofusion', 'connman-conf',d)}"

FILESEXTRAPATHS_append := ":${THISDIR}/${FOLDERSUFFIX}"

SRC_URI = "file://main.conf"

FILES_${PN} += "${sysconfdir}/connman/*"

do_install_append () {
  install -d ${D}${sysconfdir}/connman/
  install -m 0644 ${WORKDIR}/main.conf ${D}${sysconfdir}/connman/
  # For early_ethernet, eth is configure in early_init. Just let connman ignore it.
  if ${@bb.utils.contains('DISTRO_FEATURES', 'early-ethernet', 'true', 'false', d)}; then
      sed -i '/^NetworkInterfaceBlacklist/s/$/,eth0/' ${D}${sysconfdir}/connman/main.conf
  fi
}

# Including the file depends on chipset
include ${INCSUFFIX}-${MACHINE}.inc
