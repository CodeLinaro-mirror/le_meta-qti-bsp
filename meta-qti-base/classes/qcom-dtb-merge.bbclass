#Copyright (c) Qualcomm Technologies, Inc. and/or its subsidiaries.
#SPDX-License-Identifier: BSD-3-Clause-Clear

DEPENDS += "dtc-native"

match_dtb_to_dtbo () {
    dtb=$1
    dtbo=$2

    dtbo_compatible=$(fdtget -t s $dtbo / "compatible" | sed -e 's/\"//g' -e 's/[;\,]//g')
    dtbo_model=$(fdtget -t s $dtbo / "model" | sed -e 's/\"//g' -e 's/[;\,]//g')

    dtb_compatible=$(fdtget -t s $dtb / "compatible" | sed -e 's/\"//g' -e 's/[;\,]//g')
    dtb_model=$(fdtget -t s $dtb / "model" | sed -e 's/\"//g' -e 's/[;\,]//g')

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
    dtb_dir=$1
    dtbo_dir=$2
    out_dir=$3
    matched_dtbos=""

    dtb_files=$(find $dtb_dir -name "*.dtb*")
    dtbo_files=$(find $dtbo_dir -name "*.dtbo")

    if [ -z "$dtb_files" ]; then
        echo "ERR : Base DTB files NOT found"
        exit 1
    fi

    if [ -z "$dtbo_files" ]; then
        echo "WARN: Overlay DTB files not found"
        cp $dtb_dir/* $out_dir
        return 0
    fi

    for dtb_file in $dtb_files; do
        matched_dtbos=""
        for dtbo_file in $dtbo_files; do
            if match_dtb_to_dtbo $dtb_file $dtbo_file; then
                matched_dtbos="${matched_dtbos} ${dtbo_file}"
            fi
        done

        base_name=$(basename $dtb_file)
        base_dtb_name=$(echo "$base_name" | sed -e 's/\.[^.]*$//')
        out_dtb=${base_dtb_name}-overlay.dtb

        if [ "$matched_dtbos" != ""  ]; then
            # execute the command in verbose mode(-v)
            fdtoverlay -i $dtb_file -o ${out_dir}/${out_dtb} -v $matched_dtbos
            #exit in case of failure
            if [ $? -ne 0 ]; then
                exit 1
            fi
        else
            cp $dtb_file ${out_dir}/${out_dtb}
        fi
    done
}

merge_dtbos_single () {
    dtb_dir=$1
    dtbo_dir=$2
    out_dir=$3

    dtb_files=$(find $dtb_dir -name "*.dtb*")
    dtbo_files=$(find $dtbo_dir -name "*.dtbo")

    if [ -z "$dtb_files" ]; then
        echo "ERR : Base DTB files NOT found"
        exit 1
    fi

    if [ -z "$dtbo_files" ]; then
        echo "WARN: Overlay DTB files not found"
        cp $dtb_dir/* $out_dir
        return 0
    fi

    for dtb_file in $dtb_files; do
        dtbo_matched=""
        for dtbo_file in $dtbo_files; do
            if match_dtb_to_dtbo $dtb_file $dtbo_file; then
                dtbo_matched="true"
                dtbo_string=$(basename $dtbo_file)
                dtbo_string=$(echo "$dtbo_string" | sed -e 's/\.[^.]*$//')
                out_dtb=${dtbo_string}.dtb
                # execute the command in verbose mode(-v)
                fdtoverlay -i $dtb_file -o ${out_dir}/${out_dtb} -v $dtbo_file
                #exit in case of failure
                if [ $? -ne 0 ]; then
                    exit 1
                fi
            fi
        done
        if [ -z "$dtbo_matched" ]; then
            base_name=$(basename $dtb_file)
            base_dtb_name=$(echo "$base_name" | sed -e 's/\.[^.]*$//')
            out_dtb=${base_dtb_name}-overlay.dtb
            cp $dtb_file ${out_dir}/${out_dtb}
        fi
    done
}

merge_ddr_dtbos_single () {
    dtb_dir=$1
    dtbo_dir=$2
    out_dir=$3

    # prefix 0gvm: for pvm only; prefix 1gvm: for single LAGVM; no prefix: for multigvm LAGVM + LVGVM
    ddr_sizes="0gvm-64gb:0x700 0gvm-48gb:0x600 0gvm-36gb:0x500 0gvm-32gb:0x500 0gvm-24gb:0x400 0gvm-16gb:0x300 0gvm-12gb:0x200 0gvm-8gb:0x100"
    ddr_sizes+=" 1gvm-64gb:0x700 1gvm-48gb:0x600 1gvm-36gb:0x500 1gvm-32gb:0x500 1gvm-24gb:0x400 1gvm-16gb:0x300 1gvm-12gb:0x200 1gvm-8gb:0x100"
    ddr_sizes+=" 64gb:0x700 48gb:0x600 36gb:0x500 32gb:0x500 24gb:0x400 16gb:0x300 12gb:0x200 8gb:0x100"

    dtb_files=$(find $dtb_dir -name "*.dtb*")
    dtbo_files=$(find $dtbo_dir -name "*.dtbo")
    if [ -z "$dtb_files" ]; then
        echo "ERR : Base DTB files NOT found"
        exit 1
    fi

    if [ -z "$dtbo_files" ]; then
        echo "WARN: Overlay DTB files not found"
        cp $dtb_dir/* $out_dir
        return 0
    fi

    for dtb_file in $dtb_files; do
        for dtbo_file in $dtbo_files; do
            dtbo_string=$(basename $dtbo_file)
            dtbo_string=$(echo "$dtbo_string" | sed -e 's/\.[^.]*$//')
            input_dtb=$(basename "$dtb_file")

            for i in $ddr_sizes; do
               ddr_size=$(echo $i | sed 's,:.*,,g')
               ddr_type=$(echo $i | sed 's,.*:,,g')
               if [[ "$dtbo_file" == *"$ddr_size"* ]]; then
                  subtype="$ddr_type"
                  break
               fi
            done

            out_dtb=${input_dtb%.dtb}-${ddr_size}.dtb

            fdtoverlay -i $dtb_file -o ${out_dir}/${out_dtb} -v $dtbo_file
            #get board-id from dtb files and replace with updated value
            #OR operation of board id subtype and ddr type is performed.
            board_id=$(fdtget -t x ${out_dir}/${out_dtb} / qcom,board-id )
            updated_bid=$(echo "$board_id" | awk -v mask_hex="$subtype" '
            BEGIN {
                mask = strtonum(mask_hex)
            }
            {
                for (i = 1; i <= NF; i++) {
                    val = strtonum("0x" $i)
                    if (i % 2 == 0) {
                        val = or(val, mask)
                    }
                    printf "0x%X ", val
                }
            }')

            # execute the command in verbose mode(-v)
            fdtput -t x  ${out_dir}/${out_dtb} / qcom,board-id $updated_bid
            #exit in case of failure
            if [ $? -ne 0 ]; then
                exit 1
            fi
        done
    done
}
