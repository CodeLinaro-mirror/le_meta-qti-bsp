SUMMARY = "Sysctl configuration file for eth IPv6 address configuration"
DESCRIPTION = "Qualcomm Technologies, Inc. Configuration file to setup IPv6 address on ethernet interface"
HOMEPAGE = "https://www.codelinaro.org/"
LICENSE = "BSD-3-Clause-Clear"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/${LICENSE};md5=550794465ba0ec5312d6919e203a55f9"

SRC_URI = "\
    file://100-sysctl.conf \
"

do_install() {
        install -d ${D}${sysconfdir}/sysctl.d/
	install -m 0644 ${WORKDIR}/100-sysctl.conf ${D}${sysconfdir}/sysctl.d/100-sysctl.conf
}
