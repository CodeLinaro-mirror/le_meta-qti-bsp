#Remove of bash from rpm
RDEPENDS:${PN}:remove = "bash"

FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"

SRC_URI:remove = "git://github.com/rpm-software-management/rpm;branch=rpm-4.17.x;protocol=https"
SRC_URI:prepend = " ${CLO_LE_GIT}/rpm.git;protocol=https;branch=rpm/rpm-4.17.x "
