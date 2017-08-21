FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}-${PV}:"

SRC_URI += "file://root-home.patch \
           file://add-hash.patch \
           file://add-diag-user.patch \
           file://add-sdcard-diag-groups.patch \
           file://add-reboot-daemon-group.patch \
           file://add-qti-user-group.patch \
"

# Including the file depends on chipset
INCSUFFIX = "${@base_conditional('BASEMACHINE', '8x96auto', 'base-passwd_3.5.29_auto', "none", d)}"
include ${INCSUFFIX}.inc
INCSUFFIX = "${@base_conditional('BASEMACHINE', '8x96autofusion', 'base-passwd_3.5.29_auto', 'none',d)}"
include ${INCSUFFIX}.inc
INCSUFFIX = "${@base_conditional('BASEMACHINE', '8x96auto44', 'base-passwd_3.5.29_auto', 'none',d)}"
include ${INCSUFFIX}.inc
PR = "r1"
