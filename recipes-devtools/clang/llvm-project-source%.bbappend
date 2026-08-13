FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

SRC_URI += "${@'file://0040-Fix-vfork-due-to-link-error-and-support-39bit-VA.patch' if d.getVar('QTI_SANITIZER') else ''}"
SRC_URI += "${@'file://0041-fix-global-value-tag-mismatch-in-hwasan.patch' if d.getVar('QTI_SANITIZER') else ''}"
