FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}-${PV}:"

SRC_URI += "file://root-home.patch \
           file://add-hash.patch \
           file://add-diag-user.patch \
           file://add-sdcard-diag-groups.patch \
           file://add-reboot-daemon-group.patch \
"

# Including the file depends on chipset
INCSUFFIX = "${@base_conditional('MACHINEGROUP', 'auto', 'base-passwd_3.5.29_auto', "none", d)}"
include ${INCSUFFIX}.inc
