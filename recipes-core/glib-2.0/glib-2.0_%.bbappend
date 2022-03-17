FILESEXTRAPATHS_prepend := "${THISDIR}/files:"
# When cross compiling for ARM we don't want to include this
RRECOMMENDS_${PN}_remove_arm = "shared-mime-info"

SRC_URI += "file://CVE-2020-35457.patch \
           "
