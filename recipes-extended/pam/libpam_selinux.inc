inherit enable-selinux

RDEPENDS_${PN}-runtime += "${@target_selinux(d, 'pam-plugin-selinux')}"
PACKAGECONFIG[selinux] = "--enable-selinux,--disable-selinux,libselinux,"
