################################################
### Generate userdata.img ###
################################################
USERDATAIMAGE_TARGET ?= "userdata.img"
USERDATAIMAGE_MAP_TARGET ?= "userdata.map"
USERDATA_DIR ??= "data"
USERDATA_IMAGE_ROOTFS_SIZE = "${@get_size_in_bytes(d.getVar('USERDATA_SIZE') or '1GB')}"

DEPENDS += "${@bb.utils.contains('USERDATA_FSTYPE', 'f2fs', 'f2fs-tools-android-native', '', d)} "

do_makeuserdata[dirs] = "${IMGDEPLOYDIR}/${IMAGE_BASENAME}"

do_makeuserdata() {
    cp ${MACHINE_FSCONFIG_CONF_FULL_PATH} ${WORKDIR}/rootfs-fsconfig.conf
    if ${@bb.utils.contains('USERDATA_FSTYPE', 'f2fs', 'true', 'false', d)}; then
        bbnote "Generating ${USERDATAIMAGE_TARGET} with f2fs file system"
        dd if=${IMAGE_ROOTFS}/${USERDATA_DIR} of=${IMGDEPLOYDIR}/${IMAGE_BASENAME}/${USERDATAIMAGE_TARGET} \
	      seek=${USERDATA_IMAGE_ROOTFS_SIZE} count=0 bs=1
        make_f2fs -O encrypt -S ${USERDATA_IMAGE_ROOTFS_SIZE} -f -R root:root \
	      ${IMGDEPLOYDIR}/${IMAGE_BASENAME}/${USERDATAIMAGE_TARGET}
    else
        bbnote "Generating ${USERDATAIMAGE_TARGET} with ext4 file system"
        make_ext4fs -C ${WORKDIR}/rootfs-fsconfig.conf \
                    -B ${IMGDEPLOYDIR}/${IMAGE_BASENAME}/${USERDATAIMAGE_MAP_TARGET} \
                    -a /data ${IMAGE_EXT4_SELINUX_OPTIONS} \
                    ${SPARSE_SYSTEMIMAGE_FLAG} -b 4096 -l ${USERDATA_IMAGE_ROOTFS_SIZE} \
                    ${IMGDEPLOYDIR}/${IMAGE_BASENAME}/${USERDATAIMAGE_TARGET} \
                    ${IMAGE_ROOTFS}/${USERDATA_DIR}
    fi
}

addtask do_makeuserdata after do_image before do_build
