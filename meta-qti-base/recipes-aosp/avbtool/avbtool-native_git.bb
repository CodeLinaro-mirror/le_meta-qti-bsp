SUMMARY = "avbtool: Image signing tool"
DESCRIPTION = "Image signing tool used for generating images needed to support Android Verified Boot 2.0."
HOMEPAGE = "http://developer.android.com/"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=f0f3a517d46b5f0ca048b58f503b6dc1"

PR = "r0"

SRC_URI = "git://source.codeaurora.org/quic/la/platform/external/avb/;protocol=https;nobranch=1"

SRC_URI_append = " file://0001-avb-use-dm-mod.create-to-replace-of-dm-parameter.patch"

# Tagged by platform-tools-30.0.5.
SRCREV = "5282686a21c2a99d6f74876a11dc1ff61957e50e"

S = "${WORKDIR}/git"

inherit native

do_configure[noexec] = "1"
do_compile[noexec] = "1"

do_install(){
    install -d ${D}/${bindir}
    install -m 0755 ${S}/avbtool  ${D}/${bindir}
    # Keys for boot and dtbo image signing
    install -d ${D}${sysconfdir}/signing_tools/sigkeys/
    install -m 0755  ${S}/test/data/*.pem  ${D}${sysconfdir}/signing_tools/sigkeys/
}
