FILESEXTRAPATHS_prepend := "${THISDIR}/connman:"
SRC_URI += "file://0004-resolve-connman-test-script-import-gobject-failed.patch"

# Including the file depends on chipset
INCSUFFIX = "${@base_conditional('MACHINE', '8x96autofusion', 'connman_1.31', 'none',d)}"
include ${INCSUFFIX}-${MACHINE}.inc
