DEPENDS += "libcutils libion"
DEPENDS += "${@bb.utils.contains('MACHINE_FEATURES', 'qti-hypervisor', 'libuhab', '', d)}"
