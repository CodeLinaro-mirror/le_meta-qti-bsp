inherit qcommon qprebuilt

SUMMARY = "QTI Screen shot SRC Plugin for GStreamer"
SECTION = "multimedia"
LICENSE = "LGPL-2.0 & BSD-3-Clause & MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/LGPL-2.0;md5=9427b8ccf5cf3df47c29110424c9641a \
                    file://${COREBASE}/meta/files/common-licenses/BSD-3-Clause;md5=550794465ba0ec5312d6919e203a55f9 \
                    file://${COREBASE}/meta/files/common-licenses/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"
FILESPATH =+ "${WORKSPACE}:"
SRC_DIR = "${WORKSPACE}/gstreamer/gst-plugins-qti-oss/gst-plugin-qscreencapsrc"

S      = "${WORKDIR}/gstreamer/gst-plugins-qti-oss/gst-plugin-qscreencapsrc"

LIBV = "1.0"
PREBUILT = "1"

DEPENDS = "glib-2.0 wayland-native"
DEPENDS += "gstreamer1.0 \
            gstreamer1.0-plugins-base \
            gstreamer1.0-plugins-bad \
            weston \
           "
DEPENDS += "virtual/libc"

CFLAGS += "-I${STAGING_INCDIR} \
           -I${STAGING_INCDIR}/drm \
           -I${STAGING_INCDIR}/weston \
           -I${STAGING_INCDIR}/pixman-1 \
           -I${STAGING_INCDIR}/../lib64/glib-2.0/include \
           -I${STAGING_INCDIR}/glib-2.0 \
           -I${STAGING_INCDIR}/glib-2.0/include \
           -I${STAGING_INCDIR}/glib-2.0/glib \
           -I${STAGING_INCDIR}/c++ \
           -I${STAGING_INCDIR}/c++/${TARGET_SYS}"

FILES_${PN} += "${libdir}/gstreamer-${LIBV}/*.so"
FILES_${PN}-dbg += "${libdir}/gstreamer-${LIBV}/.debug"
FILES_${PN}-dev += "${libdir}/gstreamer-${LIBV}/*.la"

#Skips check for .so symlinks
INSANE_SKIP_${PN} = "dev-so"
INSANE_SKIP_${PN} += "installed-vs-shipped"


