inherit autotools qcommon

DESCRIPTION = "display Library"
LICENSE = "BSD"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/\
${LICENSE};md5=3775480a712fc46a69647678acb234cb"

PR = "r8"

SRC_URI = " \
    ${CAF_LA_GIT}/platform/hardware/qcom/display.git;protocol=git;nobranch=1;tag=${CAF_TAG};destsuffix=display/display-hal \
"

S = "${WORKDIR}/display/display-hal/"

DEPENDS += "system-core"
DEPENDS += "libhardware"
DEPENDS += "native-frameworks"

EXTRA_OECONF = " --with-core-includes=${STAGING_INCDIR}"
EXTRA_OECONF += " --with-sanitized-headers=${STAGING_KERNEL_BUILDDIR}/usr/include"

LDFLAGS += "-llog -lhardware -lutils -lcutils"

CPPFLAGS += "-DTARGET_HEADLESS"
CPPFLAGS += "-DVENUS_COLOR_FORMAT"
CPPFLAGS += "-DPAGE_SIZE=4096"
CXXFLAGS += "-I${S}/libqdutils"
CXXFLAGS += "-I${S}/libqservice"
CXXFLAGS += "-I${S}/sdm/include"
CXXFLAGS += "-I${S}/include"
CXXFLAGS += "-I${S}/libgralloc"

do_install_append () {
    # libhardware expects to find /usr/lib/hw/gralloc.*.so
    install -d ${D}/usr/lib/hw
    ln -s /usr/lib/libgralloc.so ${D}/usr/lib/hw/gralloc.default.so
}

FILES_${PN}-dbg += "${libdir}/.debug/*"
FILES_${PN} += "${libdir}/*.so ${libdir}/hw/*.so"
SOLIBS = ".so"
FILES_SOLIBSDEV = ""
INSANE_SKIP_${PN} = "dev-so"