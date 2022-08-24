SUMMARY = "The Cairo 2D vector graphics library"
DESCRIPTION = "Cairo is a multi-platform library providing anti-aliased \
vector-based rendering for multiple target backends. Paths consist \
of line segments and cubic splines and can be rendered at any width \
with various join and cap styles. All colors may be specified with \
optional translucence (opacity/alpha) and combined using the \
extended Porter/Duff compositing algebra as found in the X Render \
Extension."
HOMEPAGE = "http://cairographics.org"
BUGTRACKER = "http://bugs.freedesktop.org"
SECTION = "libs"

LICENSE = "(MPL-1.1 | LGPLv2.1) & GPLv3+"
LICENSE_${PN} = "MPL-1.1 | LGPLv2.1"
LICENSE_${PN}-dev = "MPL-1.1 | LGPLv2.1"
LICENSE_${PN}-doc = "MPL-1.1 | LGPLv2.1"
LICENSE_${PN}-gobject = "MPL-1.1 | LGPLv2.1"
LICENSE_${PN}-script-interpreter = "MPL-1.1 | LGPLv2.1"
LICENSE_${PN}-perf-utils = "GPLv3+"

LIC_FILES_CHKSUM = "file://COPYING;md5=e73e999e0c72b5ac9012424fa157ad77"

DEPENDS = "fontconfig glib-2.0 libpng pixman zlib"
UBUNTU_VERSION_cairo = "1.15.10"

FILESEXTRAPATHS_prepend := "${THISDIR}/files:"
SRC_URI = "git://gitlab.freedesktop.org/miso/cairo.git;protocol=https;nobranch=1;tag=${UBUNTU_VERSION_cairo}"
SRC_URI += "file://fix-double-free-and-failed-assertions-in-cairo_scaled_font_destroy.patch"

inherit autotools pkgconfig upstream-version-is-even gtk-doc multilib_script

MULTILIB_SCRIPTS = "${PN}-perf-utils:${bindir}/cairo-trace"

PACKAGECONFIG[xcb] = "--enable-xcb,--disable-xcb,libxcb"
PACKAGECONFIG[directfb] = "--enable-directfb=yes,,directfb"
PACKAGECONFIG[valgrind] = "--enable-valgrind=yes,--disable-valgrind,valgrind"
PACKAGECONFIG[egl] = "--enable-egl=yes,--disable-egl,virtual/egl"
PACKAGECONFIG[glesv2] = "--enable-glesv2,--disable-glesv2,virtual/libgles2"
PACKAGECONFIG[opengl] = "--enable-gl,--disable-gl,virtual/libgl"

EXTRA_OECONF += " \
    ${@bb.utils.contains('TARGET_FPU', 'soft', '--disable-some-floating-point', '', d)} \
    --enable-tee \
"

# We don't depend on binutils so we need to disable this
export ac_cv_lib_bfd_bfd_openr="no"
# Ensure we don't depend on LZO
export ac_cv_lib_lzo2_lzo2a_decompress="no"

S="${WORKDIR}/git"

do_configure_prepend () {
	touch ${S}/boilerplate/Makefile.am.features
	touch ${S}/src/Makefile.am.features
	touch ${S}/ChangeLog
}

do_install_append () {
	rm -rf ${D}${bindir}/cairo-sphinx
	rm -rf ${D}${libdir}/cairo/cairo-fdr*
	rm -rf ${D}${libdir}/cairo/cairo-sphinx*
	rm -rf ${D}${libdir}/cairo/.debug/cairo-fdr*
	rm -rf ${D}${libdir}/cairo/.debug/cairo-sphinx*
	[ ! -d ${D}${bindir} ] ||
		rmdir -p --ignore-fail-on-non-empty ${D}${bindir}
	[ ! -d ${D}${libdir}/cairo ] ||
		rmdir -p --ignore-fail-on-non-empty ${D}${libdir}/cairo
}

do_package_qa[noexec] = "1"

PACKAGES =+ "cairo-gobject cairo-script-interpreter cairo-perf-utils"

SUMMARY_cairo-gobject = "The Cairo library GObject wrapper library"
DESCRIPTION_cairo-gobject = "A GObject wrapper library for the Cairo API."

SUMMARY_cairo-script-interpreter = "The Cairo library script interpreter"
DESCRIPTION_cairo-script-interpreter = "The Cairo script interpreter implements \
CairoScript.  CairoScript is used by tracing utilities to enable the ability \
to replay rendering."

DESCRIPTION_cairo-perf-utils = "The Cairo library performance utilities"

FILES_${PN} = "${libdir}/libcairo.so.*"
FILES_${PN}-gobject = "${libdir}/libcairo-gobject.so.*"
FILES_${PN}-script-interpreter = "${libdir}/libcairo-script-interpreter.so.*"
FILES_${PN}-perf-utils = "${bindir}/cairo-trace* ${libdir}/cairo/*.la ${libdir}/cairo/libcairo-trace.so"
FILES_${PN}-dev += "${libdir}/cairo/*.so"
BBCLASSEXTEND = "native nativesdk"

