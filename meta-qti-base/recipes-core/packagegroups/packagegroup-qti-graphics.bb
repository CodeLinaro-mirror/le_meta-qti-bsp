SUMMARY = "QTI package group for vulkan loader"
LICENSE = "BSD-3-Clause-Clear"

inherit packagegroup

PACKAGES = "\
    packagegroup-qti-graphics \
    "

ALLOW_EMPTY:${PN} = "1"

RDEPENDS:${PN} += "\
    vulkan-loader \
    "
