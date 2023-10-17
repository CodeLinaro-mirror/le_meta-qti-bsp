BINUTILS_GIT_URI = "${CLO_LE_GIT}/binutils-gdb.git;protocol=https;branch=caf_migration/binutils-gdb/binutils-${BINUPV}-branch"
FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

SRC_URI += "file://CVE-2021-46174.patch \
"
