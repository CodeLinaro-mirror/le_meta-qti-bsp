require machine-image-lagvm.bb

SUMMARY = "Machine image"
DESCRIPTION = "Build the full machine image depend on different parameters"
LICENSE = "BSD-3-Clause"

IMAGE_INSTALL:append = " \
    qcrosvm-lvgvm \
    vhost-user-q-lvgvm \
    vhost-user-scmi-lvgvm \
    gvm-net-config-lvgvm \
    dspfirmware-mount-lvgvm \
"

ROOTFS_POSTPROCESS_COMMAND:remove = "modify_vm_config;"

BOOTIMAGE_TARGET = "${PRODUCT}-boot.img"
VBMETAIMAGE_TARGET = "${PRODUCT}-vbmeta.img"
