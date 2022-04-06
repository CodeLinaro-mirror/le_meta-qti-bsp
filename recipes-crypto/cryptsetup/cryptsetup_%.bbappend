FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

FILES_${PN} += " /usr/sbin"

PACKAGECONFIG_remove_class-target = " \
    udev \
"

SRC_URI += " \
            file://0001-Disable-direct-IO-use-case.patch \
           "
