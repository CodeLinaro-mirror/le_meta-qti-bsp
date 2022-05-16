# Add supprot for kernel unit test framework
RDEPENDS:${PN} += "\
    ${@bb.utils.contains('DISTRO_FEATURES', 'ktf', 'ktf', '', d)} \
    gtest \
    "
