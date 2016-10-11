FILESEXTRAPATHS_append := ":${THISDIR}/systemd-225"

#re-use non-perf settings
BASEMACHINE = "${@d.getVar('MACHINE', True).replace('-perf', '')}"

SRC_URI_append_msm8996 += "file://${BASEMACHINE}/70-net-setup-link.rules \
                           file://${BASEMACHINE}/DWC_ETH_QOS.conf"

do_install_append_msm8996 () {
  install -m 0644 ${WORKDIR}/${BASEMACHINE}/70-net-setup-link.rules ${D}${sysconfdir}/udev/rules.d/
  install -m 0644 ${WORKDIR}/${BASEMACHINE}/DWC_ETH_QOS.conf ${D}${sysconfdir}/modules-load.d/
}
