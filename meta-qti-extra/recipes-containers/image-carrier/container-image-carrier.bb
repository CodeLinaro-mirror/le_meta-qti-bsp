SUMMARY = "Package the ext4 image as a file"
LICENSE = "BSD-3-Clause-Clear"

do_install[depends] += "container-image:do_image_complete"

C_IMAGE_NAME = "container-image-${PRODUCT}.ext4"

do_install() {
    install -d ${D}/usr/share/
    install -m 0644 ${DEPLOY_DIR_IMAGE}/${C_IMAGE_NAME} \
                    ${D}/usr/share/infotainment-system.img
}

FILES:${PN} += "/usr/share/infotainment-system.img"

