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
    alsaucm-conf \
    start-scripts-firmware-links \
    start-scripts-find-partitions \
    neutrino-eth \
    neutrino-flash \
    neutrino-firmware \
    iperf \
    ethtool \
    lib32-androidcompat \
    lib32-cameradbg \
    lib32-mm-camerasdk \
    lib32-mm-camera-lib \
    lib32-mm-camera-core \
    lib32-mm-3a-core \
    lib32-mm-camera \
"

IMAGE_ROOTFS_SIZE = "1048576"

IMAGE_ROOTFS_EXTRA_SPACE_append = "${@bb.utils.contains("DISTRO_FEATURES", "systemd", " + 4096", "" ,d)}"
