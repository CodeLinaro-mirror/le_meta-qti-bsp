FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"

SRC_URI:append = "\
    file://0001-fileio.c-fix-a-buffer-overflow-detected-issue.patch \
"
