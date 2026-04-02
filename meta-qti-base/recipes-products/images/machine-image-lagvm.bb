require machine-image-pvm.bb

SUMMARY = "Machine image - single LAGVM"
DESCRIPTION = "Build the machine image with single LAGVM"
LICENSE = "BSD-3-Clause-Clear"

IMAGE_INSTALL += "\
    ${@bb.utils.contains('MACHINE_FEATURES', 'qti-vmm', 'packagegroup-qti-vmm', '', d)} \
"

modify_vm_config() {
    if [ -f ${IMAGE_ROOTFS}/etc/vm_config.xml ]; then
        sed -i '/<vm>/,/<\/vm>/ {
            /<vm>/ {
                x
                s/^/x/
                /^xx$/ {
                    :a
                    N
                    /<\/vm>/!ba
                    d
                }
                x
            }
        }' ${IMAGE_ROOTFS}/etc/vm_config.xml
        sed -i 's/NUM_VMS="2"/NUM_VMS="1"/' ${IMAGE_ROOTFS}/etc/vm_config.xml
    fi
}

ROOTFS_POSTPROCESS_COMMAND:append = " modify_vm_config;"

DEPLOY_NAME_BASE = "${PRODUCT}-lagvm-automotive"
USR_IMAGE_BASENAME = "${PRODUCT}-lagvm-usrfs"
PERSIST_IMAGE_BASENAME = "${PRODUCT}-lagvm-persist"
BOOTIMAGE_TARGET = "${PRODUCT}-lagvm-boot.img"
