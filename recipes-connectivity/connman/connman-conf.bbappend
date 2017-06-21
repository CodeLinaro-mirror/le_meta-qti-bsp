# Including the file depends on chipset
INCSUFFIX = "${@base_conditional('MACHINE', '8x96autofusion', 'connman-conf', 'none',d)}"
FOLDERSUFFIX = "${@base_conditional('MACHINE', '8x96autofusion', 'connman-conf-8x96autofusion', 'connman-conf',d)}"

FILESEXTRAPATHS_append := ":${THISDIR}/${FOLDERSUFFIX}"

SRC_URI = "file://main.conf"

FILES_${PN} += "${sysconfdir}/connman/*"

do_install_append () {
  install -d ${D}${sysconfdir}/connman/
  install -m 0644 ${WORKDIR}/main.conf ${D}${sysconfdir}/connman/
}

# Including the file depends on chipset
include ${INCSUFFIX}-${MACHINE}.inc
