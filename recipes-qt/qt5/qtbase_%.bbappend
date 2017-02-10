PACKAGECONFIG += "sql-sqlite examples"

PACKAGE_ARCH = "${MACHINE_ARCH}"

FILESEXTRAPATHS_append := ":${THISDIR}/${PN}"

SRC_URI_append = "\
	file://use_EGL_GLESv2.patch \
	"
