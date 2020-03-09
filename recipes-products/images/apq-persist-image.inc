# This include forces the generation of a persist image file

DEPENDS += "ext4-utils-native"
PERSIST_IMAGE_BASENAME = "${MACHINE}-persist"
OUTPUT_PERSIST_FILE = "${DEPLOY_DIR_IMAGE}/${PERSIST_IMAGE_BASENAME}.ext4"
PERSIST_IMAGE_ROOTFS_SIZE = "6536668"
PERSIST_DATA_FOLDER = "${TMPDIR}/rootfs/${PERSIST_IMAGE_BASENAME}"

# Generating persist image in EXT4 format
IMAGE_PREPROCESS_COMMAND += "${@base_contains('DISTRO_FEATURES', 'persist-image', 'create_persist_ext4_img;', '', d)}"

create_persist_ext4_img() {
   rm    -rf ${PERSIST_DATA_FOLDER}
   mkdir -p ${PERSIST_DATA_FOLDER}
   if [ ! -d ${PERSIST_DATA_FOLDER}/var/lib ]; then
       mkdir -p ${PERSIST_DATA_FOLDER}/var-lib
   fi

   if [ ! -d ${IMAGE_ROOTFS}/var/lib ]; then
       mkdir -p ${IMAGE_ROOTFS}/var/lib
   fi

   cp -rf ${IMAGE_ROOTFS}/var/lib/* ${PERSIST_DATA_FOLDER}/var-lib
   rm -rf ${PERSIST_DATA_FOLDER}/var-lib/opkg
   ln -sf /data/var/lib/opkg ${PERSIST_DATA_FOLDER}/var-lib/opkg

   # Tftp services need writable /usr/persist to run
   if ${@bb.utils.contains('IMAGE_FEATURES','read-only-rootfs','true','false',d)}; then
       if [ -d ${IMAGE_ROOTFS}/usr/persist/hlos_rfs ]; then
           mv ${IMAGE_ROOTFS}/usr/persist/hlos_rfs ${PERSIST_DATA_FOLDER}/
           ln -sf /persist/hlos_rfs  ${IMAGE_ROOTFS}/usr/persist/hlos_rfs
       fi
       if [ -d ${IMAGE_ROOTFS}/usr/persist/rfs ]; then
           mv ${IMAGE_ROOTFS}/usr/persist/rfs      ${PERSIST_DATA_FOLDER}/
           ln -sf /persist/rfs       ${IMAGE_ROOTFS}/usr/persist/rfs
       fi
   fi

   if [ -e "${WORKSPACE}/android_compat/device/qcom/${SOC_FAMILY}/WCNSS_qcom_wlan_nv.bin" ];then
       cp -rf "${WORKSPACE}/android_compat/device/qcom/${SOC_FAMILY}/WCNSS_qcom_wlan_nv.bin" ${PERSIST_DATA_FOLDER}
   fi

   if [ -e "${WORKSPACE}/android_compat/device/qcom/${SOC_FAMILY}/WCNSS_wlan_dictionary.dat" ]; then
       cp -rf "${WORKSPACE}/android_compat/device/qcom/${SOC_FAMILY}/WCNSS_wlan_dictionary.dat" ${PERSIST_DATA_FOLDER}
   elif [ -e "${WORKSPACE}/android_compat/device/qcom/${SOC_FAMILY}_32/WCNSS_wlan_dictionary.dat" ]; then
       cp -rf "${WORKSPACE}/android_compat/device/qcom/${SOC_FAMILY}_32/WCNSS_wlan_dictionary.dat" ${PERSIST_DATA_FOLDER}
   fi

   make_ext4fs -s -l ${PERSIST_SIZE_EXT4} ${OUTPUT_PERSIST_FILE} ${PERSIST_DATA_FOLDER}
   chmod 644 ${OUTPUT_PERSIST_FILE}
}
