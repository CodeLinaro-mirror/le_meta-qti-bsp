SUMMARY = "QTI package group for os common libs"

inherit packagegroup

PACKAGES = "\
    packagegroup-qti-core-commonlibs \
    "

ALLOW_EMPTY:${PN} = "1"

RDEPENDS:${PN} += "\
    libcutils \
    libstdc++ \
    liblog \
    glib-2.0 \
    procps \
    "
