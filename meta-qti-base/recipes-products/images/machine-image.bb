require machine-image-lagvm.bb

SUMMARY = "Machine image"
DESCRIPTION = "Build the full machine image depend on different parameters"
LICENSE = "BSD-3-Clause"

IMAGE_INSTALL:append:sa7255-ivi = " \
    ${@bb.utils.contains('MACHINE_FEATURES', 'qti-vmm', 'qcrosvm-lvgvm vhost-user-q-lvgvm gvm-net-config-lvgvm dspfirmware-mount-lvgvm', '', d)} \
"

IMAGE_INSTALL:append:sa8255-ivi = " \
    ${@bb.utils.contains('MACHINE_FEATURES', 'qti-vmm', 'qcrosvm-lvgvm vhost-user-q-lvgvm vhost-user-scmi-lvgvm gvm-net-config-lvgvm dspfirmware-mount-lvgvm', '', d)} \
"

IMAGE_INSTALL:append:sa8775-flex = " \
    ${@bb.utils.contains('MACHINE_FEATURES', 'qti-vmm', 'qcrosvm-lvgvm vhost-user-q-lvgvm vhost-user-scmi-lvgvm gvm-net-config-lvgvm dspfirmware-mount-lvgvm', '', d)} \
"

ROOTFS_POSTPROCESS_COMMAND:remove = "modify_vm_config;"

BOOTIMAGE_TARGET = "${PRODUCT}-boot.img"
VBMETAIMAGE_TARGET = "${PRODUCT}-vbmeta.img"
