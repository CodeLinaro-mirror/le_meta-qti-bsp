BINUTILS_GIT_URI = "${CLO_LE_GIT}/binutils-gdb.git;protocol=https;branch=caf_migration/binutils-gdb/binutils-${BINUPV}-branch"
FILESEXTRAPATHS:prepend := "${THISDIR}/files:"
SRC_URI += "file://CVE-2023-45853.patch \
"