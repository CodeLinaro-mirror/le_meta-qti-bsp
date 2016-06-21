DESCRIPTION = "Linux kernel"
SECTION     = "kernel"
LICENSE     = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=d7810fab7487fb0aad327b76f1be7cd7"

FILESEXTRAPATHS_prepend := "${WORKSPACE}/:"
SRC_URI   =  "file://kernel"

S =  "${WORKDIR}/kernel"
