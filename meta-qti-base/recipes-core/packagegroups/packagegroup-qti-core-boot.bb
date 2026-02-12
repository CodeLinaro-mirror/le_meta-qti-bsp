DESCRIPTION = "The minimal set of packages required to boot the system"

inherit packagegroup

PACKAGES = "\
    packagegroup-qti-core-boot \
    "

ALLOW_EMPTY:${PN} = "1"

RDEPENDS:${PN} += "\
    ${@bb.utils.contains_any('PREFERRED_VERSION_linux-msm', '6.1 6.12', 'platformdlkm ', '', d)} \
    ${@bb.utils.contains('DISTRO_FEATURES', 'ab-boot-support', 'abctl ab-status-updater', '', d)} \
    packagegroup-core-boot \
    "

RDEPENDS:${PN}:remove:gvm-gen5 = "platformdlkm"
