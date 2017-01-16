DESCRIPTION = "Sofia SIP Recipe using autotools"

DEFAULT_PREFERENCE = "-1"

PR = "r1"

HOMEPAGE = "http://sofia-sip.sourceforge.net/"

LICENSE = "LGPLv2.1+"
LIC_FILES_CHKSUM = "file://COPYING;md5=03068f550c635f6520e0f0252da412fc"


DEPENDS += "glib-2.0"

FILESPATH =+ "${WORKSPACE}:"
SRC_URI = "file://sofia-sip"
SRCREV      = "${AUTOREV}"





SRC_DIR = "${WORKSPACE}/sofia-sip"
S = "${WORKDIR}/sofia-sip"


inherit autotools gettext pkgconfig

FILES_${PN} =+ "${libdir}/sofia-sip/*"
FILES_${PN}-dbg += "${libdir}/sofia-sip/.debug/*"

