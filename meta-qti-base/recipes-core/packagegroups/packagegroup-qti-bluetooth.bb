SUMMARY = "QTI package group for bluetooth"

inherit packagegroup

PACKAGES = "\
    packagegroup-qti-bluetooth \
    "

ALLOW_EMPTY:${PN} = "1"

RDEPENDS:${PN} += "\
    synergy-opensource \
    csrspp-tty \
    ${@bb.utils.contains('PREFERRED_VERSION_linux-msm', '5.15', 'bt-dlkm', '', d)} \
    "
