inherit systemd

DESCRIPTION = "Coresight DLKM Loader"
PR = "r0"

LICENSE          = "BSD-3-Clause-Clear"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/${LICENSE};md5=550794465ba0ec5312d6919e203a55f9"

FILESPATH =+ "${WORKSPACE}:"

SRC_URI += "file://load_coresight-dlkm.service"
SRC_URI += "file://load_coresight_dlkm"

SYSTEMD_SERVICE_${PN} += "load_coresight-dlkm.service"
FILES_${PN}+="${sysconfdir}/initscripts/load_coresight_dlkm"

do_configure[noexec] = "1"
do_compile[noexec] = "1"

do_install () {
      # Place coresight_load.service in systemd unitdir
      install -d ${D}${systemd_unitdir}/system/
      install -m 0644 ${WORKDIR}/load_coresight-dlkm.service  \
          -D ${D}${systemd_unitdir}/system/load_coresight-dlkm.service

      # Place load_coresight_dlkm script in initscripts dir
      install -m 0755 ${WORKDIR}/load_coresight_dlkm  \
          -D ${D}${sysconfdir}/initscripts/load_coresight_dlkm
}
