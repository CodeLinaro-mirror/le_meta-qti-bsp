LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/${LICENSE};md5=0835ade698e0bcf8506ecda2f7b4f302"

DESCRIPTION = "avbtool: Image signing tool"
PR = "r1"

FILESPATH =+ "${WORKSPACE}/external/avb/:"
SRC_URI = "file://avbtool"

do_install() {
       install -d ${D}${datadir}/avb_py_tool
       install -m 0555 ${WORKDIR}/avbtool ${D}${datadir}/avb_py_tool/
}

#don't run these functions
do_configure[noexec] = "1"
do_compile[noexec] = "1"

FILES_${PN} += " ${datadir}/avb_py_tool/*"
BBCLASSEXTEND = "nativesdk"
