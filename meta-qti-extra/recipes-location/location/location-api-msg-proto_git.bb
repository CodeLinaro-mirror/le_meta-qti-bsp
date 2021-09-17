inherit autotools-brokensep pkgconfig
require ../include/common-location-defines.inc

DESCRIPTION = "location api msg protobuf library"
PR = "r1"

LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/\
${LICENSE};md5=550794465ba0ec5312d6919e203a55f9"

FILESPATH =+ "${WORKSPACE}:"
SRC_URI = "${PATH_TO_REPO}/qcom-opensource/location/.git;protocol=${PROTO};destsuffix=qcom-opensource/location/location_api_msg_proto;subpath=location_api_msg_proto;usehead=1"
SRCREV = "${AUTOREV}"
S = "${WORKDIR}/qcom-opensource/location/location_api_msg_proto"

DEPENDS = "glib-2.0 protobuf protobuf-native loc-pla-hdr gps-utils"

FILES_${PN} += "${libdir}/*"

do_compile_prepend () {
    echo "Running location_api_msg_protobuf_gen.sh"
    cd ${S}
    ./location_api_msg_protobuf_gen.sh
    cd -
}
