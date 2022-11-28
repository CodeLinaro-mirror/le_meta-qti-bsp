# add depends of libion, libsync, libuhab for HY11 build error
DEPENDS += "libcutils libion libsync"
DEPENDS += "${@bb.utils.contains('PREFERRED_VERSION_linux-msm', '5.15', 'libdmabufheap', '', d)}"
DEPENDS += "${@bb.utils.contains('MACHINE_FEATURES', 'qti-hypervisor', 'libuhab', '', d)}"
