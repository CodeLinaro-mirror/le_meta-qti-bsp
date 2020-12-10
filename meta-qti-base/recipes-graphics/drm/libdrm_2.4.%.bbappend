FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"
SRC_URI_append = " file://0001-DRM-front-end-display-DRM-front-end.patch"

EXTRA_OEMESON_append = "${@bb.utils.contains('MACHINE_FEATURES', 'qti-hypervisor', ' -Denable_drm-fe=yes', '', d)}"

PACKAGE_ARCH = "${MACHINE_ARCH}"
