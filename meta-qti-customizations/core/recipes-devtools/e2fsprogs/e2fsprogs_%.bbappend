#Below Package is fetch from Codelinaro

FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"

SRC_URI:remove = "git://git.kernel.org/pub/scm/fs/ext2/e2fsprogs.git;branch=master;protocol=https"
SRC_URI:prepend = " ${CLO_LE_GIT}/e2fsprogs.git;protocol=https;branch=ext2/master "

SRCREV = "25ad8a431331b4d1d444a70b6079456cc612ac40"

SRC_URI += "file://CVE-2014-9114.patch \
"
