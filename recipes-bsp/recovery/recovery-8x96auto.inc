DEPENDS += "miniui-recovery"

SRC_URI += "file://0001-recovery-Enable-recovery-UI-on-LV-platform.patch"

CPPFLAGS += "-I${STAGING_INCDIR}/drm"

do_install_append() {
	install -d ${D}/res/images/
	install -m 0755 ${WORKSPACE}/poky/meta-qti-bsp/recipes-bsp/recovery/files/icon_installing.png -D ${D}/res/images
	install -m 0755 ${WORKSPACE}/poky/meta-qti-bsp/recipes-bsp/recovery/files/icon_error.png -D ${D}/res/images
	install -m 0755 ${WORKSPACE}/poky/meta-qti-bsp/recipes-bsp/recovery/files/progress_empty.png -D ${D}/res/images
	install -m 0755 ${WORKSPACE}/poky/meta-qti-bsp/recipes-bsp/recovery/files/progress_fill.png -D ${D}/res/images
	install -m 0755 ${WORKSPACE}/poky/meta-qti-bsp/recipes-bsp/recovery/files/stage_empty.png -D ${D}/res/images
	install -m 0755 ${WORKSPACE}/poky/meta-qti-bsp/recipes-bsp/recovery/files/stage_fill.png -D ${D}/res/images
}
