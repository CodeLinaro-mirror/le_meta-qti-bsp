PACKAGE_ARCH = "${MACHINE_ARCH}"

PACKAGECONFIG_append = " gstreamer"
#rb1.4FILESEXTRAPATHS_append := ":${THISDIR}/${PN}"
#rb1.4
#rb1.4python do_getpatches() {
#rb1.4    import os
#rb1.4
#rb1.4    cmd = "mkdir -p ${WORKSPACE}/poky/meta-qti-bsp/recipes-qt/qt5/qtmultimedia && (wget https://source.codeaurora.org/quic/le/AGL/meta-agl-demo/plain/recipes-qt/qt5/qtmultimedia/0001-GStreamer-support-cover-art.patch?h=automotivelinux/chinook -O ${WORKSPACE}/poky/meta-qti-bsp/recipes-qt/qt5/qtmultimedia/0001-GStreamer-support-cover-art.patch || pwd) && (wget https://source.codeaurora.org/quic/le/AGL/meta-agl-demo/plain/recipes-qt/qt5/qtmultimedia/0001-GStreamer-support-date-time-type-in-metadata.patch?h=automotivelinux/chinook -O ${WORKSPACE}/poky/meta-qti-bsp/recipes-qt/qt5/qtmultimedia/0001-GStreamer-support-date-time-type-in-metadata.patch || pwd)"
#rb1.4
#rb1.4    os.system(cmd)
#rb1.4}
#rb1.4
#rb1.4addtask getpatches before do_fetch
#rb1.4
#rb1.4SRC_URI_prepend = " \
#rb1.4    file://0001-GStreamer-support-date-time-type-in-metadata.patch \
#rb1.4    file://0001-GStreamer-support-cover-art.patch \
#rb1.4    "
