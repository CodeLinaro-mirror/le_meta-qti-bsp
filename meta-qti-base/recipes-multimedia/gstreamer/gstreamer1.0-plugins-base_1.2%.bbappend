# add depends of libion, libsync, libuhab for HY11 build error
DEPENDS += "libcutils libion libsync"
DEPENDS += "${@oe.utils.version_less_or_equal('${preferred-kernel}', '5.4', '', 'libdmabufheap', d)}"
DEPENDS += "${@bb.utils.contains('MACHINE_FEATURES', 'qti-hypervisor', 'libuhab', '', d)}"
