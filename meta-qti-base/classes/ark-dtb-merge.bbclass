#Copyright (c) 2024 Qualcomm Innovation Center, Inc. All rights reserved.
#SPDX-License-Identifier: BSD-3-Clause-Clear

DEPENDS += "dtc-native"

match_dtb_to_dtbo () {
    dtb=$1
    dtbo=$2

    dtbo_compatible=$(fdtget -t s $dtbo / compatible)
    dtbo_model=$(fdtget -t -s $dtbo model)

    dtb_compatible=$(fdtget -t s $dtb / compatible)
    dtb_model=$(fdtget -t -s $dtb model)

    if [ "$dtb_model" = "$dtbo_model" ]; then
        return 0
    fi

    for dtb in $dtb_compatible; do
        for dtbo in $dtbo_compatible; do
            if [ "$dtb" = "$dtbo" ]; then
                return 0
            fi
        done
    done
    return 1
}

merge_dtbos () {
    base_dtb=$1
    dtbo_files=$2
    out_dir=$3
    matched_dtbos=""

    for dtbo_file in $dtbo_files; do
        if match_dtb_to_dtbo $base_dtb $dtbo_file; then
            matched_dtbos="${matched_dtbos} ${dtbo_file}"
        fi
    done

    base_name=$(basename $base_dtb)
    base_dtb_name=$(echo "$base_name" | sed -e 's/\.[^.]*$//')
    out_dtb=${base_dtb_name}-overlay.dtb

    if [ "$matched_dtbos" != ""  ]; then
        # execute the command in verbose mode(-v)
        fdtoverlay -i $base_dtb -o ${out_dir}/${out_dtb} -v $matched_dtbos
        #exit in case of failure
        if [ $? -ne 0 ]; then
            exit 1
        fi
    else
        cp $base_dtb ${out_dir}/${out_dtb}
    fi
}

