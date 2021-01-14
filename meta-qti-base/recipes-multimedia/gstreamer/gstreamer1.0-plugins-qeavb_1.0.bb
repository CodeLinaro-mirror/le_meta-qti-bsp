inherit qcommon

SUMMARY = "avb plugins for GStreamer"
SECTION = "multimedia"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/BSD-3-Clause;md5=550794465ba0ec5312d6919e203a55f9"

SRC_URI   =  "${PATH_TO_REPO}/gstreamer/gst-plugins-qti-oss/.git;protocol=${PROTO};destsuffix=gstreamer/gst-plugins-qti-oss;usehead=1"
SRC_DIR = "${SRC_DIR_ROOT}/gstreamer/gst-plugins-qti-oss/gst-plugins-qeavb"
S      = "${WORKDIR}/gstreamer/gst-plugins-qti-oss/gst-plugins-qeavb"
PR = "r1"
LV = "1.0.0"
LIBV = "1.0"
SRCREV="${AUTOREV}"

DEPENDS = "glib-2.0"

DEPENDS += "gstreamer1.0 \
            gstreamer1.0-plugins-base \
           "
DEPENDS += "virtual/libc"

CFLAGS += "-I${STAGING_INCDIR} \
           -I${STAGING_INCDIR}/glib-2.0 \
           -I${STAGING_INCDIR}/../lib/glib-2.0/include \
           -I${STAGING_INCDIR}/glib-2.0/include \
           -I${STAGING_INCDIR}/glib-2.0/glib \
           -I${STAGING_INCDIR}/c++ \
           -I${STAGING_INCDIR}/c++/${TARGET_SYS} \
           -I${STAGING_INCDIR}/gstreamer-1.0"
CFLAGS += "-I${STAGING_KERNEL_BUILDDIR}/usr/include"

do_configure[depends] += "virtual/kernel:do_shared_workdir"

install_config_file() {
  mkdir -p ${D}${sysconfdir}/xdg/
}
do_install[postfuncs] += " install_config_file "
FILES_${PN} += "${libdir}/gstreamer-${LIBV}/*.so"
FILES_${PN}-dbg += "${libdir}/gstreamer-${LIBV}/.debug"
FILES_${PN}-dev += "${libdir}/gstreamer-${LIBV}/*.la"

#Skips check for .so symlinks
INSANE_SKIP_${PN} = "dev-so"


