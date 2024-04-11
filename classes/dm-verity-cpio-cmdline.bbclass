#Copyright (c) 2023-2024 Qualcomm Innovation Center, Inc. All rights reserved.
#SPDX-License-Identifier: BSD-3-Clause-Clear
DEPENDS += "openssl-native"

CONFLICT_MACHINE_FEATURES += " dm-verity-bootloader dm-verity-none dm-verity-initramfs dm-verity-initramfs-v2 dm-verity-initramfs-v3"

VERITY_SALT = "aee087a5be3b982978c923f566a94613496b417f2af592639bc80d141e34dfe7"
BLOCK_SIZE = "4096"
SECTOR_SIZE = "512"
FEC_ROOTS = "2"

VERITY_CREATE = "dm-mod.create="

populate_verity_cpio_cmdline () {
    # Re-parse the values that have already been generated in system_verity_metadata.txt to avoid duplicate verity metadata creation.
    data_blocks=`awk -F ':' '{ if ($1 == "Data blocks") print $2 }' ${WORKDIR}/system_verity_metadata.txt | sed "s/^[ \t]*//"`
    data_sectors=`expr ${BLOCK_SIZE} / ${SECTOR_SIZE} \* ${data_blocks}`
    echo "$data_sectors"
    hash_start_block=`expr ${data_blocks} + 1`
    fec_offset=`awk -F ':' '{ if ($1 == "fec_offset") print $2 }' ${WORKDIR}/system_verity_metadata.txt | sed "s/^[ \t]*//"`
    fec_start_block=`expr ${fec_offset} / ${BLOCK_SIZE}`
    fec_blocks=`expr ${fec_start_block} - 1`
    root_hash=`awk -F ':' '{ if ($1 == "Root hash") print $2 }' ${WORKDIR}/system_verity_metadata.txt | sed "s/^[ \t]*//"`

    # Sign the root hash
    echo -n "${root_hash}" > ${WORKDIR}/roothash.txt
    openssl smime -sign -nocerts -noattr -binary -in ${WORKDIR}/roothash.txt -inkey ${KERNEL_PREBUILT_PATH}/dist/verity_key.pem -signer ${KERNEL_PREBUILT_PATH}/dist/verity_cert.pem -outform der -out ${WORKDIR}/verity_sig.txt

    root_hash_sig_key_value=`od -tx1 -An ${WORKDIR}/verity_sig.txt | tr -d ' \n'`

    echo -ne "${VERITY_CREATE}\"verity,,,ro,0 ${data_sectors} verity 1 \
/dev/vda /dev/vda ${BLOCK_SIZE} ${BLOCK_SIZE} ${data_blocks} ${hash_start_block} \
sha256 ${root_hash} ${VERITY_SALT} 10 use_fec_from_device /dev/vda fec_start ${fec_start_block} fec_blocks ${fec_blocks} fec_roots ${FEC_ROOTS} \
root_hash_sig_key_value ${root_hash_sig_key_value}\"" \
> ${WORKDIR}/verity-cmdline
}
do_makesystem[postfuncs] += "populate_verity_cpio_cmdline"

