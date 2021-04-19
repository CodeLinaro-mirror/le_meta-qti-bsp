SUMMARY = "QTI package group for AGL demo development tools"

inherit packagegroup

PACKAGES = "\
    packagegroup-qti-agl-demo-tools \
    "

ALLOW_EMPTY_${PN} = "1"

RDEPENDS_${PN} += "\
    curl \
    gdbserver \
    iputils \
    "
