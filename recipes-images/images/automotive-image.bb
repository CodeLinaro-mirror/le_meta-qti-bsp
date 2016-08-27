DESCRIPTION = "AGL Demo Platform image currently contains a simple HMI and \
demos."

require recipes-platform/images/agl-demo-platform.bb

#add packages on top of AGL master
IMAGE_INSTALL_append = " \
    weston \
    weston-examples \
    qtbase-examples \
    qtwebkit \
    qtwebkit-examples-examples \
    qtmultimedia \
    qtmultimedia-plugins \
    qtmultimedia-qmlplugins \
    lmbench \
    libcutils \
    liblog \
    system-core-adbd \
    system-core-usb \
    packagegroup-multimedia \
    lib32-lk \
    lib32-gensecimage \
    cdcdriver-noship \
    audio-nxp-auto-noship \
    init-audio \
    start-scripts-firmware-links \
    start-scripts-find-partitions \
    neutrino-eth \
    neutrino-flash \
"

IMAGE_ROOTFS_SIZE = "1048576"

IMAGE_ROOTFS_EXTRA_SPACE_append = "${@bb.utils.contains("DISTRO_FEATURES", "systemd", " + 4096", "" ,d)}"
