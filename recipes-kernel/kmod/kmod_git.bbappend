#Package is fetching from the codelinaro
SRC_URI:remove = "git://git.kernel.org/pub/scm/utils/kernel/kmod/kmod.git;branch=master"
SRC_URI:prepend = "${CLO_LE_GIT}/kmod.git;branch=kmod/master;protocol=https"

FILESEXTRAPATHS:prepend := "${THISDIR}/files:"
