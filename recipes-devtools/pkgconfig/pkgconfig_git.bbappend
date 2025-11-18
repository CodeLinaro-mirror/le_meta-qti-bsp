FILESEXTRAPATHS_prepend := "${THISDIR}/pkgconfig:"

SRC_URI_remove = "git://anongit.freedesktop.org/pkg-config "
SRC_URI_prepend = " git://gitlab.freedesktop.org/pkg-config/pkg-config.git;branch=master;protocol=https "
