SUMMARY = "QTI package group for bluetooth"

inherit packagegroup

PACKAGES = "\
    packagegroup-qti-bluetooth \
    "

ALLOW_EMPTY:${PN} = "1"

RDEPENDS:${PN} += "\
    dspfirmware-mount-bt \
    synergy-opensource \
    csrspp-tty \
    bt-dlkm \
    "
