FILESEXTRAPATHS_prepend := "${THISDIR}:"
SRC_URI = "git://git.codelinaro.org/clo/le/platform/external/prelink-cross;protocol=git;branch=caf_migration/yocto/cross_prelink \
           file://prelink.conf \
           file://prelink.cron.daily \
           file://prelink.default \
           file://macros.prelink"
PR = "r1"
