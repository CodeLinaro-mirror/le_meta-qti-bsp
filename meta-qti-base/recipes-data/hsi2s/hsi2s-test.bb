inherit autotools-brokensep

SUMMARY = "hsi2s-test"

PROVIDERS = "hsi2s-test"

DESCRIPTION = "HS-I2S test utility"

LICENSE = "GPL-2.0"
LIC_FILES_CHKSUM = "file://NOTICE;md5=434b8411d18d7f18ebe745bd3cc502ed"

SRC_DIR = "${SRC_DIR_ROOT}/vendor/qcom/opensource/hsi2s-kernel"
SRC_URI = "${PATH_TO_REPO}/vendor/qcom/opensource/hsi2s-kernel/.git;protocol=${PROTO};destsuffix=vendor/qcom/opensource/hsi2s-kernel;usehead=1"
SRCREV = "${AUTOREV}"

PR = "r0"
PV = "0.1"

S =  "${WORKDIR}/vendor/qcom/opensource/hsi2s-kernel"

TARGET_CC_ARCH += "${LDFLAGS}"

do_compile () {
	oe_runmake -C test/generic
}

do_install () {
	install -d ${D}${bindir}
	install -m 0755 ${S}/test/generic/hsi2s_test ${D}${bindir}
}
