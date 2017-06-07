PR = "r157"

FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}-${PV}:"

SRC_URI += "file://umountfs"
SRC_URI += "file://bsp_paths.sh"
SRC_URI += "file://set_core_pattern.sh"
SRC_URI += "file://check_for_firmware_corruption.sh"

do_install_append() {
        update-rc.d -f -r ${D} mountnfs.sh remove
        update-rc.d -f -r ${D} urandom remove

        install -m 0755 ${WORKDIR}/bsp_paths.sh  ${D}${sysconfdir}/init.d
        update-rc.d -r ${D} bsp_paths.sh start 15 2 3 4 5 .

  if [ "${MACHINE}" == "8x96autofusion" ]; then
        install -m 0755 ${WORKDIR}/check_for_firmware_corruption.sh  ${D}${sysconfdir}/init.d
        update-rc.d -r ${D} check_for_firmware_corruption.sh start 99 2 3 4 5 .
  fi

        install -m 0755 ${WORKDIR}/set_core_pattern.sh  ${D}${sysconfdir}/init.d
        update-rc.d -r ${D} set_core_pattern.sh start 01 S 2 3 4 5 S .
        echo "test ! -x /sbin/restorecon || /sbin/restorecon -F /tmp" >> ${D}${sysconfdir}/init.d/populate-volatile.sh
}

MASKED_SCRIPTS += " \
  bsp_paths \
  set_core_pattern"
# Including the file depends on chipset
INCSUFFIX = "${@base_conditional('MACHINEGROUP', 'auto', 'initscripts_auto', 'none',d)}"
include ${INCSUFFIX}.inc
