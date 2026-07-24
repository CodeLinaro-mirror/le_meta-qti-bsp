FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

SRC_URI += "${@'file://0001-fix-sanitizer-compile-error.patch file://0001-fix-rt-error.patch' if d.getVar('QTI_SANITIZER') else ''}"
