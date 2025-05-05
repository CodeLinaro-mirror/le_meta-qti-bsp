HOMEPAGE = "https://github.com/AndroidBootloader/platform_external_avb"
DESCRIPTION = "avbtool: Image signing tool"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${S}/LICENSE;md5=f0f3a517d46b5f0ca048b58f503b6dc1"

BBCLASSEXTEND =+ "native"

SRC_URI = "git://github.com/AndroidBootloader/platform_external_avb.git;branch=master;protocol=https"
SRCREV = "${AUTOREV}"

S = "${WORKDIR}/git"

do_configure[noexec] = "1"
do_compile[noexec] = "1"

do_install() {
   install -d ${D}${bindir}
  install -m 0755 ${S}/avbtool.py ${D}${bindir}/avbtool.py
}
