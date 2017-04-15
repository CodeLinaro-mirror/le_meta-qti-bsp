inherit androidmk deploy

LICENSE = "Zlib"
LIC_FILES_CHKSUM = "file://${SRC_DIR}/NOTICE"

FILESPATH =+ "${WORKSPACE}:"
SRC_URI = "file://external/nanopb-c/"

SRC_DIR = "${WORKSPACE}/external/nanopb-c"

SRCREV = "${AUTOREV}"
S      = "${WORKDIR}/external/nanopb-c"

EXTRA_OEMAKE += "-e MAKEFLAGS="

ALLOW_EMPTY_${PN} = "1"

do_install_append() {
	install -m 0644 \
		${LA_OUT_TARGET_INTERMEDIATES}/STATIC_LIBRARIES/libprotobuf-c-nano-enable_malloc_intermediates/libprotobuf-c-nano-enable_malloc.a \
		${STAGING_LIBDIR}/
	install -d ${D}${includedir}/
	install -m 0644 ${S}/*.h -D ${D}${includedir}/
}
