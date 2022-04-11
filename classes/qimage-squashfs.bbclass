# if A/B support is supported, generate OTA pkg by default.
GENERATE_AB_OTA_PACKAGE ?= "${@bb.utils.contains('COMBINED_FEATURES', 'qti-ab-boot', '1', '', d)}"

QIMGSQUASHFSCLASSES  = ""
QIMGSQUASHFSCLASSES += "${@bb.utils.contains('GENERATE_AB_OTA_PACKAGE', '1', 'ab-ota-squashfs', '', d)}"

inherit ${QIMGSQUASHFSCLASSES}

## native tools support
DEPENDS += " squashfs-tools-native "

CORE_IMAGE_EXTRA_INSTALL += "${@bb.utils.contains('COMBINED_FEATURES', 'qti-ab-boot', ' recovery-ab', '', d)}"

do_image_ext4[noexec] = "1"
do_image_squashfs[noexec] = "1"

# Default Image names
SYSTEMIMAGE_TARGET ?= "system.img"
SYSTEMIMAGE_MAP_TARGET ?= "system.map"
USERDATAIMAGE_TARGET ?= "userdata.img"
USERDATAIMAGE_MAP_TARGET ?= "userdata.map"
PERSISTIMAGE_TARGET ?= "persist.img"
PERSISTIMAGE_MAP_TARGET ?= "persist.map"
DTBOIMAGE_TARGET ?= "dtbo.img"

IMAGE_EXT4_SELINUX_OPTIONS = "${@bb.utils.contains('DISTRO_FEATURES', 'selinux', '-S ${SELINUX_FILE_CONTEXTS}', '', d)}"
IMAGE_SQUASHFS_SELINUX_OPTIONS = "${@bb.utils.contains('DISTRO_FEATURES', 'selinux', '-context-file ${SELINUX_FILE_CONTEXTS}', '', d)}"

ROOTFS_POSTPROCESS_COMMAND += "gen_buildprop;do_fsconfig;"
ROOTFS_POSTPROCESS_COMMAND += "gen_overlayfs;"

gen_buildprop() {
   mkdir -p ${IMAGE_ROOTFS}/cache
   echo ro.build.version.release=`cat ${IMAGE_ROOTFS}/etc/version ` >> ${IMAGE_ROOTFS}/build.prop
   echo ro.product.name=${BASEMACHINE}-${DISTRO} >> ${IMAGE_ROOTFS}/build.prop
   echo ${MACHINE} >> ${IMAGE_ROOTFS}/target
}

gen_overlayfs() {
    mkdir -p ${IMAGE_ROOTFS}/overlay
    mkdir -p ${IMAGE_ROOTFS}/overlay/etc
    mkdir -p ${IMAGE_ROOTFS}/overlay/.etc-work
    mkdir -p ${IMAGE_ROOTFS}/overlay/data
    mkdir -p ${IMAGE_ROOTFS}/overlay/.data-work
    mkdir -p ${IMAGE_ROOTFS}/overlay/cache
    mkdir -p ${IMAGE_ROOTFS}/overlay/.cache-work
}

do_fsconfig() {
    chmod go-r ${IMAGE_ROOTFS}/etc/passwd || :
    chmod -R o-rwx ${IMAGE_ROOTFS}/etc/init.d/ || :

    # For ext4, fs_config setting is covered by tool make_ext4fs with specific arg.
    # In squashfs case, mksquashfs doesn't support such arg, so handle manually.
    if [ x"${MACHINE_FSCONFIG_CONF_FULL_PATH}" == x"" ]; then
        bbnote "fsconfig file not available, skip"
        return
    fi

    cp ${MACHINE_FSCONFIG_CONF_FULL_PATH} ${WORKDIR}/rootfs-fsconfig.conf
    while read line
    do
        set -- ${line}
        if [ -z "$1" ]; then
            continue
        elif [ $# -lt 4 ]; then
            bbnote "invalid config: $@"
            continue
        elif [ ! -e "${IMAGE_ROOTFS}/$1" ]; then
            bbnote "${IMAGE_ROOTFS}/$1 not exists, skip"
            continue
        fi

        bbnote "CMD: chown $2:$3 ${IMAGE_ROOTFS}/$1"
        chown $2:$3 ${IMAGE_ROOTFS}/$1
        bbnote "CMD: chmod ${4} ${IMAGE_ROOTFS}/$1"
	chmod ${4} ${IMAGE_ROOTFS}/$1
    done < ${WORKDIR}/rootfs-fsconfig.conf
}

do_fsconfig_append_qti-distro-user() {
    # disable debugfs support #
    rm ${IMAGE_ROOTFS}/lib/systemd/system/sys-kernel-debug.mount
}

#############################
### Generate system.img #####
#############################

fakeroot do_makesystem() {
    mksquashfs ${IMAGE_ROOTFS} ${IMGDEPLOYDIR}/${IMAGE_BASENAME}/${SYSTEMIMAGE_TARGET} ${IMAGE_SQUASHFS_SELINUX_OPTIONS} -noappend -b 65536 -processors 1
}
addtask do_makesystem after do_rootfs before do_image_complete

#############################
### Generate userdata.img ###
#############################
do_makeuserdata[dirs] = "${IMGDEPLOYDIR}/${IMAGE_BASENAME}"

do_makeuserdata() {
    make_ext4fs -B ${IMGDEPLOYDIR}/${IMAGE_BASENAME}/${USERDATAIMAGE_MAP_TARGET} \
                -a /data ${IMAGE_EXT4_SELINUX_OPTIONS} \
                -s -b 4096 -l ${USERDATA_SIZE_EXT4} \
                ${IMGDEPLOYDIR}/${IMAGE_BASENAME}/${USERDATAIMAGE_TARGET} \
                ${IMAGE_ROOTFS}/overlay
}

addtask do_makeuserdata after do_rootfs before do_build

################################################
############ Generate persist image ############
################################################
PERSIST_IMAGE_ROOTFS_SIZE ?= "6536668"
do_makepersist[dirs] = "${IMGDEPLOYDIR}/${IMAGE_BASENAME}"

do_makepersist() {
    make_ext4fs ${PERSISTFS_CONFIG} ${MAKEEXT4_MOUNT_OPT} \
                -B ${IMGDEPLOYDIR}/${IMAGE_BASENAME}/${PERSISTIMAGE_MAP_TARGET} \
                -s -l ${PERSIST_IMAGE_ROOTFS_SIZE} \
                ${IMGDEPLOYDIR}/${IMAGE_BASENAME}/${PERSISTIMAGE_TARGET} \
                ${IMAGE_ROOTFS}/persist

    # Empty the /persist folder so that it doesn't end up
    # in system image as well
    rm -rf ${IMAGE_ROOTFS}/persist/*
}
# It must be before do_makesystem to remove /persist
addtask do_makepersist after do_rootfs before do_makesystem
