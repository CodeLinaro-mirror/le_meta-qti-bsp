inherit androidmk deploy update-rc.d

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/\
${LICENSE};md5=89aea4e17d99a7cacdbeed46a0096b10"

FILESPATH =+ "${WORKSPACE}:"
SRC_URI = "file://telephony/ril/"
SRC_URI += "file://ril-0001-RIL-Add-support-for-building-Android-RIL-for-non-And.patch"
SRC_URI += "file://start_rild"

FILES_${PN} += "/etc/init.d/*"

SRC_DIR = "${WORKSPACE}/telephony/ril"

SRCREV = "${AUTOREV}"
S      = "${WORKDIR}/telephony/ril"

DEPENDS += "libcutils nanopb-c protobuf system-core \
	native-frameworks libhardware libhardware-legacy libcap"

export TARGET_LIBRARY_SUPPRESS_LIST="libprotobuf-c-nano-enable_malloc"
CFLAGS += "-D__unused=__attribute__\(\(unused\)\) -Dstrlcat=strncat --std=c++11 -fpermissive"
LDFLAGS += "-lcutils -llog -lutils -lprotobuf-c-nano-enable_malloc"
EXTRA_OEMAKE += "-e MAKEFLAGS="

INITSCRIPT_NAME = "rild"
INITSCRIPT_PARAMS = "start 90 5 3 2 . stop 10 0 1 6 ."

do_compile_prepend() {
	# .pc folder has duplicated makefiles, cause make failure
	rm -rf ${S}/.pc
}

do_install_append() {
	install -d ${D}${includedir}/telephony
	install -m 0644 ${S}/include/telephony/*.h -D ${D}${includedir}/telephony/
	install -d ${D}${includedir}/hardware/ril/librilutils/proto
	install -m 0644 ${S}/librilutils/proto/sap-api.pb.h -D \
		${D}${includedir}/hardware/ril/librilutils/proto/sap-api.pb.h
	install -m 0755 ${WORKDIR}/start_rild -D ${D}${sysconfdir}/init.d/${INITSCRIPT_NAME}
}
