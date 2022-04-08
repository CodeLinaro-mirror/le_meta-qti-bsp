PACKAGE_ARCH = "${MACHINE_ARCH}"

FILESEXTRAPATHS_append := ":${THISDIR}/${PN}"

python do_getpatches() {
    import os

    cmd = "mkdir -p ${WORKSPACE}/poky/meta-qti-bsp/recipes-qt/qt5/qtmultimedia && (wget https://git.codelinaro.org/clo/le/AGL/meta-agl-demo/-/raw/caf_migration/automotivelinux/chinook/recipes-qt/qt5/qtmultimedia/0001-GStreamer-support-cover-art.patch -O ${WORKSPACE}/poky/meta-qti-bsp/recipes-qt/qt5/qtmultimedia/0001-GStreamer-support-cover-art.patch || pwd) && (wget https://git.codelinaro.org/clo/le/AGL/meta-agl-demo/-/raw/caf_migration/automotivelinux/chinook/recipes-qt/qt5/qtmultimedia/0001-GStreamer-support-date-time-type-in-metadata.patch -O ${WORKSPACE}/poky/meta-qti-bsp/recipes-qt/qt5/qtmultimedia/0001-GStreamer-support-date-time-type-in-metadata.patch || pwd)"

    os.system(cmd)
}

addtask getpatches before do_fetch

SRC_URI_prepend = " \
    file://0001-GStreamer-support-date-time-type-in-metadata.patch \
    file://0001-GStreamer-support-cover-art.patch \
    "
