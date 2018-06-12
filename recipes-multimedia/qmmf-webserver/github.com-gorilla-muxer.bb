DESCRIPTION = "github.com/gorilla/mux"

GO_IMPORT = "github.com/gorilla/mux"

inherit go

DEPENDS = "github.com-gorilla-context"

DEST_DIR :="${PN}-${PV}"
DEST_DIR_remove = "${BBEXTENDVARIANT}-"
SRC_URI = "git://github.com/gorilla/mux;protocol=https;destsuffix=${DEST_DIR}/src/${GO_IMPORT}"
SRCREV = "${AUTOREV}"
LICENSE = "BSD"
LIC_FILES_CHKSUM = "file://src/${GO_IMPORT}/LICENSE;md5=33fa1116c45f9e8de714033f99edde13"

FILES_${PN} += "${GOBIN_FINAL}/*"
