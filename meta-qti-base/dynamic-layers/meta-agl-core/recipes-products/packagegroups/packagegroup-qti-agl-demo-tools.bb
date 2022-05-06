SUMMARY = "QTI package group for AGL demo development tools"

inherit packagegroup

PACKAGES = "\
    packagegroup-qti-agl-demo-tools \
    "

ALLOW_EMPTY:${PN} = "1"

RDEPENDS:${PN} += "\
    curl \
    gdbserver \
    iputils \
    "
