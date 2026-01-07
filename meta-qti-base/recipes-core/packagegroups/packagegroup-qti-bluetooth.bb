SUMMARY = "QTI package group for bluetooth"

inherit packagegroup

PACKAGES = "\
    packagegroup-qti-bluetooth \
    "

ALLOW_EMPTY:${PN} = "1"

RDEPENDS:${PN} += "\
    synergy-opensource \
    csrspp-tty \
    bt-dlkm \
    "
RDEPENDS:${PN}:remove:quin-gvm-gen4-5 = "\
    synergy-opensource \
    csrspp-tty \
    "

RDEPENDS:${PN}:remove:gvm-gen4-5 = "\
    synergy-opensource \
    csrspp-tty \
    "
