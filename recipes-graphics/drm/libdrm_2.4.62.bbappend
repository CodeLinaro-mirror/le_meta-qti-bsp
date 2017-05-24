FILESPATH =+ "${WORKSPACE}:"
SRC_URI   = "file://display/libdrm"
SRCREV = "${AUTOREV}"
S      = "${WORKDIR}/libdrm"

do_packagedata_append() {
cp -rf ${S}/libdrm_macros.h ${STAGING_INCDIR}/libdrm/
}
