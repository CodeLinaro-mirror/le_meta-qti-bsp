inherit native

DESCRIPTION = "Tool from Android to validate and merge dtbo files before creating dtbo.img"

LICENSE = "GPL-2.0-or-later"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/${LICENSE};md5=fed54355545ffd980b814dab4a3b312c"

BBCLASSEXTEND = "native"

DEFAULT_PREFERENCE = "-1"

DEPENDS = "flex-native bison-native"

FILESEXTRAPATHS:prepend := "${KERNEL_PLATFORM_PATH}/external:"

SRC_URI = "file://dtc/"

S = "${WORKDIR}/dtc"

EXTRA_OEMAKE = "PKG_CONFIG=false"

do_compile() {
    oe_runmake -C ${S}
}

do_install() {
    # Install  bin
    install -d ${D}${bindir}/merge_dtbs/

    install -m 0755 ${S}/fdtget  ${D}${bindir}/merge_dtbs/
    install -m 0755 ${S}/fdtoverlaymerge  ${D}${bindir}/merge_dtbs/
    install -m 0755 ${S}/fdtdump  ${D}${bindir}/merge_dtbs/
    install -m 0755 ${S}/fdtoverlay  ${D}${bindir}/merge_dtbs/
    install -m 0755 ${S}/fdtput ${D}${bindir}/merge_dtbs/
    # Copy libfdt libs
    install -d ${D}${bindir}/merge_dtbs/lib/
    install -m 0644 ${S}/libfdt/libfdt-1.6.0.so ${D}${bindir}/merge_dtbs/lib/
    install -m 0644 ${S}/libfdt/libfdt.a ${D}${bindir}/merge_dtbs/lib/
    install -m 0644 ${S}/libfdt/libfdt.so.1 ${D}${bindir}/merge_dtbs/lib/

}
