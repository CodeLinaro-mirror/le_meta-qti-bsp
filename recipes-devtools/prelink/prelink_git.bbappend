FILESEXTRAPATHS_prepend := "${THISDIR}:"
SRC_URI = "${GIT_URI}/platform/external/prelink-cross;protocol=${PROTOCOL};branch=${BRANCH_PREFIX}yocto/cross_prelink \
           file://prelink.conf \
           file://prelink.cron.daily \
           file://prelink.default \
           file://macros.prelink"
PR = "r1"
