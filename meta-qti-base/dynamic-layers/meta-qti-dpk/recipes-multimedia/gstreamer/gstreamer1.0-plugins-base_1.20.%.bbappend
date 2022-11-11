# add these depends to fix HY11 build error
DEPENDS += "libcutils libion libsync"
DEPENDS += "${@bb.utils.contains('MACHINE_FEATURES', 'qti-hypervisor', 'libuhab', '', d)}"
