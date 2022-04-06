FILESEXTRAPATHS_prepend := "${THISDIR}:"
SRC_URI = "${CLO_LE_GIT}/platform/external/prelink-cross;protocol=${CLO_PROTOCOL};nobranch=1;name=prelink \
           file://prelink.conf \
           file://prelink.cron.daily \
           file://prelink.default \
           file://macros.prelink"

SRCREV_prelink = "a853a5d715d84eec93aa68e8f2df26b7d860f5b2"

PR = "r1"
