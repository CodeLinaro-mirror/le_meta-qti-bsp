SUMMARY = "Sysctl configuration file for eth IPv6 address configuration"
DESCRIPTION = "Qualcomm Technologies, Inc. Configuration file to setup IPv6 address on ethernet interface"
HOMEPAGE = "https://www.codelinaro.org/"
LICENSE = "BSD-3-Clause-Clear"
LIC_FILES_CHKSUM = "file://${COREBASE}/../meta-qti-bsp/meta-qti-base/files/common-licenses/${LICENSE};md5=b796c0007db682166a1721da80267bb2"

SRC_URI = "\
    file://100-sysctl.conf \
"

do_install() {
        install -d ${D}${sysconfdir}/sysctl.d/
	install -m 0644 ${WORKDIR}/100-sysctl.conf ${D}${sysconfdir}/sysctl.d/100-sysctl.conf
}
