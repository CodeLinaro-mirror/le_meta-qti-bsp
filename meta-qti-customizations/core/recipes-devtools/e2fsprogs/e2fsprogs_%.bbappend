#Below Package is fetch from Codelinaro

FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"

SRC_URI:remove = "git://git.kernel.org/pub/scm/fs/ext2/e2fsprogs.git;branch=master"
SRC_URI:prepend = " ${CLO_LE_GIT}/e2fsprogs.git;protocol=https;branch=ext2/master "

SRCREV = "aad34909b6648579f42dade5af5b46821aa4d845"
