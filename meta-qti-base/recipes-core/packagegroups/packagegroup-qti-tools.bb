SUMMARY = "QTI package group for test"

inherit packagegroup

PACKAGES = "\
    packagegroup-qti-tools \
    "

ALLOW_EMPTY:${PN} = "1"

RDEPENDS:${PN} += "\
    file \
    pciutils \
    usbutils \
    util-linux \
    libgpiod-tools \
    "
