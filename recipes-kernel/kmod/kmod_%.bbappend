# Package is fetch from Codelinaro to avoid downtime
SRC_URI:remove = "git://git.kernel.org/pub/scm/utils/kernel/kmod/kmod.git;branch=master;protocol=https"
SRC_URI:prepend = "${CLO_LE_GIT}/kmod.git;branch=kmod/master;protocol=https "

FILESEXTRAPATHS:prepend := "${THISDIR}/files:"
