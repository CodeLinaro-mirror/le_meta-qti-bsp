FILESBBAPPENDPATH := "${THISDIR}"
FILESEXTRAPATHS =. "${FILESBBAPPENDPATH}/${BPN}-${PV}:${FILESBBAPPENDPATH}/${BPN}:"

SRC_URI:append = " ${@bb.utils.contains('MACHINE_FEATURES', 'qti-usermode-display', ' file://0001-DRM-front-end-display-DRM-front-end.patch', ' ', d)}"
EXTRA_OEMESON:append = " ${@bb.utils.contains('MACHINE_FEATURES', 'qti-usermode-display', ' -Denable_drm-fe=yes', ' ', d)}"

PACKAGE_ARCH = "${MACHINE_ARCH}"

