# Copyright (c) Qualcomm Technologies, Inc. and/or its subsidiaries.
# SPDX-License-Identifier: BSD-3-Clause-Clear


VBMETAIMAGE_TARGET ?= "vbmeta.img"
VBMETASYSTEMIMAGE_TARGET ?= "vbmeta_system.img"

DEPENDS +=  "avbtool-native"
AVBSIGN_KEY = "${STAGING_DIR_NATIVE}${sysconfdir}/avb/sigkeys/testkey_rsa4096.pem"

# Function to sign boot, vendor_boot, dtbo, vendor_dlkm images using avbtool.
avbsign_images[dirs] = "${DEPLOY_DIR_IMAGE}/${IMAGE_BASENAME}"
avbsign_images() {

    #sign boot image
    avbtool.py add_hash_footer --image ${BOOTIMAGE_TARGET} \
                               --partition_name boot \
                               --key ${AVBSIGN_KEY} \
                               --algorithm SHA256_RSA4096 \
                               --partition_size 0x4600000 \
                               --prop com.android.build.boot.os_version:4.0.26 \
                               --prop com.android.build.boot.security_patch:2025-05-01 \
                               --rollback_index 0

    #sign vendor boot image
    avbtool.py add_hash_footer --image ${VBOOTIMAGE_TARGET} \
                               --partition_name vendor_boot \
                               --key ${AVBSIGN_KEY} \
                               --algorithm SHA256_RSA4096 \
                               --partition_size 0x1800000 \
                               --prop com.android.build.vendor_boot.os_version:4.0.26 \
                               --prop com.android.build.vendor_boot.security_patch:2025-05-01 \
                               --rollback_index 0

    # sign dtbo image
    avbtool.py add_hash_footer --image ${DTBOIMAGE_TARGET} \
                               --partition_name dtbo \
                               --key ${AVBSIGN_KEY} \
                               --algorithm SHA256_RSA4096 \
                               --partition_size 0x7d0000 \
                               --prop com.android.build.dtbo.os_version:4.0.26 \
                               --prop com.android.build.dtbo.security_patch:2025-05-01 \
                               --rollback_index 0

    # sign vendor dlkm image
     avbtool.py add_hash_footer --image ${VDLKMIMAGE_TARGET} \
                                --partition_name vendor_dlkm \
                                --key ${AVBSIGN_KEY} \
                                --algorithm SHA256_RSA4096 \
                                --partition_size 0x6400000 \
                                --prop com.android.build.vendor_dlkm.os_version:4.0.26 \
                                --prop com.android.build.vendor_dlkm.security_patch:2025-05-01 \
                                --rollback_index 0

    #sign system image
    avbtool.py add_hash_footer --image ${SYSTEMIMAGE_TARGET} \
                               --partition_name system \
                               --key ${AVBSIGN_KEY} \
                               --algorithm SHA256_RSA4096 \
                               --partition_size 0x3FB70000 \
                               --prop com.android.build.system.os_version:4.0.26 \
                               --prop com.android.build.system.security_patch:2025-05-01 \
                               --rollback_index 0
}

# Generate vbmeta and vbmeta_system images.
do_makevbmeta_images[dirs] = "${DEPLOY_DIR_IMAGE}/${IMAGE_BASENAME}"
do_makevbmeta_images[prefuncs] += " avbsign_images"
do_makevbmeta_images() {
    # vbmeta image
    avbtool.py make_vbmeta_image --output ${VBMETAIMAGE_TARGET} \
                                 --key ${AVBSIGN_KEY} \
                                 --algorithm SHA256_RSA4096 \
                                 --include_descriptors_from_image ${BOOTIMAGE_TARGET} \
                                 --include_descriptors_from_image ${VBOOTIMAGE_TARGET} \
                                 --include_descriptors_from_image ${DTBOIMAGE_TARGET} \
                                 --include_descriptors_from_image ${VDLKMIMAGE_TARGET}

    # vbmeta_system image
    avbtool.py make_vbmeta_image --output ${VBMETASYSTEMIMAGE_TARGET} \
                                 --key ${AVBSIGN_KEY} \
                                 --algorithm SHA256_RSA4096 \
                                 --include_descriptors_from_image ${SYSTEMIMAGE_TARGET}

}
addtask makevbmeta_images after do_image_complete before do_build
