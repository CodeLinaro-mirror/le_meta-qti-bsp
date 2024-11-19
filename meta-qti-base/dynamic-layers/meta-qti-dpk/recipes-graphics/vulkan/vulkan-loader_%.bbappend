FILESEXTRAPATHS:prepend := "${THISDIR}/files:"
SRC_URI += "file://0001-Add-EXTRASYSCONFDIR-macro-definition.patch"

EXTRA_OECMAKE += "-DEXTRASYSCONFDIR=/vendor/etc/"

