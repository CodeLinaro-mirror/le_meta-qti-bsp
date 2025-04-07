# Add supprot for kernel unit test framework
RDEPENDS:${PN} += "\
    ${@bb.utils.contains('DISTRO_FEATURES', 'ktf', 'ktf', '', d)} \
    googletest \
    libprocinfo \
    libmeminfo \
    ${@bb.utils.contains('MACHINE_FEATURES', 'qti-umd', 'scmi-test', '', d)} \
    "
