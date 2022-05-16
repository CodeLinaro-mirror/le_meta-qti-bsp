SUMMARY = "QTI package group for Qdrive"

inherit packagegroup

PACKAGES = "\
    packagegroup-qti-qdrive \
    "

ALLOW_EMPTY:${PN} = "1"

RDEPENDS:${PN} += "\
    opkg \
    opkg-utils \
    libusb1 \
    usbutils \
    valgrind\
    sysstat \
    "
