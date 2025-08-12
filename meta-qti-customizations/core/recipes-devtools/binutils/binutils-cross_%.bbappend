BINUTILS_GIT_URI = "${CLO_LE_GIT}/binutils-gdb.git;protocol=https;branch=caf_migration/binutils-gdb/binutils-${BINUPV}-branch"
FILESEXTRAPATHS:prepend := "${THISDIR}/files:"
SRC_URI += "file://CVE-2023-45853.patch \
            file://CVE-2016-9840.patch \
            file://CVE-2022-37434-1.patch \
            file://CVE-2022-37434-2.patch \
"