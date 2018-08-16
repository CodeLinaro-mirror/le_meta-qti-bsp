PACKAGECONFIG += "sql-sqlite examples"

PACKAGE_ARCH = "${MACHINE_ARCH}"

FILESEXTRAPATHS_append := ":${THISDIR}/${PN}"

SRC_URI_append = "\
	file://use_EGL_GLESv2.patch \
	"
DEPENDS_class-target += " virtual/egl gbm-headers adreno-native virtual/kernel glib-2.0 wayland gbm adreno-headers wayland-native libcutils"
DEPENDS_class-target += "${@base_conditional('BASEMACHINE', '8x96autogvmquin', 'libuhab', '', d)}"
