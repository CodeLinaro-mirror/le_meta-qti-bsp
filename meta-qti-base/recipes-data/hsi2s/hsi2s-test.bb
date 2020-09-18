inherit qcommon

SUMMARY = "hsi2s-test"

PROVIDERS = "hsi2s-test"

DESCRIPTION = "HS-I2S test utility"

LICENSE = "GPL-2.0"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/${LICENSE};md5=801f80980d171dd6425610833a22dbe6"

SRC_DIR = "${SRC_DIR_ROOT}/vendor/qcom/opensource/hsi2s-kernel"
SRC_URI = "git://source.codeaurora.org/platform/vendor/qcom-opensource/hsi2s-kernel.git;protocol=https;destsuffix=vendor/qcom/opensource/hsi2s-kernel;nobranch=1"
SRCREV = "3be812645c890e0490117ac1160f8f1887321947"

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
