# /etc/os-release add Yocto information
OS_RELEASE_FIELDS:append = " ${@bb.utils.contains('DISTRO_FEATURES', 'qti-dpk', '', 'OE_VERSION MACHINE_FEATURES DISTRO_FEATURES', d)}"
