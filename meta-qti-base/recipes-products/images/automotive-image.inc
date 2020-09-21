IMAGE_INSTALL += " \
    ${@bb.utils.contains('COMBINED_FEATURES', 'qti-core', 'packagegroup-qti-core-minimal', '', d)} \
    ${@bb.utils.contains('COMBINED_FEATURES', 'qti-data', 'packagegroup-qti-data', '', d)} \
    ${@bb.utils.contains('COMBINED_FEATURES', 'qti-extra', 'packagegroup-qti-extra', '', d)} \
    ${@bb.utils.contains('COMBINED_FEATURES', 'qti-security', 'packagegroup-qti-security', '', d)} \
    ${@bb.utils.contains('DISTRO_FEATURES', 'lxc', 'packagegroup-qti-lxc', '', d)} \
    ${@bb.utils.contains('COMBINED_FEATURES', 'qti-tools', 'packagegroup-qti-tools', '', d)} \
    ${@bb.utils.contains('COMBINED_FEATURES', 'qti-ros', 'packagegroup-qti-ros', '', d)} \
    ${@bb.utils.contains('COMBINED_FEATURES', 'qti-qdrive', 'packagegroup-qti-qdrive', '', d)} \
    "
