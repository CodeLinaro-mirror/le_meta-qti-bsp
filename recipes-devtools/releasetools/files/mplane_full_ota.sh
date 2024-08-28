#!/bin/sh
# Copyright (c) 2017, The Linux Foundation. All rights reserved.
#
# Redistribution and use in source and binary forms, with or without
# modification, are permitted provided that the following conditions are
# met:
#     * Redistributions of source code must retain the above copyright
#       notice, this list of conditions and the following disclaimer.
#     * Redistributions in binary form must reproduce the above
#       copyright notice, this list of conditions and the following
#       disclaimer in the documentation and/or other materials provided
#       with the distribution.
#     * Neither the name of The Linux Foundation nor the names of its
#       contributors may be used to endorse or promote products derived
#       from this software without specific prior written permission.
#
# THIS SOFTWARE IS PROVIDED "AS IS" AND ANY EXPRESS OR IMPLIED
# WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
# MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NON-INFRINGEMENT
# ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS
# BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
# CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
# SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR
# BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
# WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE
# OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN
# IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
#
# full_ota.sh      script to generate OTA upgrade pacakges.
# if sign is part of arguments, the testkey.pk8 located at OTA/build/target/product/security is taken as private key.
# OEMs can replaces this file with their own private key.

# Changes from Qualcomm Innovation Center, Inc. are provided under the following license:
# Copyright (c) 2024 Qualcomm Innovation Center, Inc. All rights reserved.
# SPDX-License-Identifier: BSD-3-Clause-Clear

set -o xtrace

if [ "$#" -lt 4 ]; then
    echo "Usage  : $0 target_files_zipfile rootfs_path ext4_or_ubi [-c fsconfig_file [-p prefix]][--system_path path]"
    echo "------------------------------------------------------------------"
    echo "example: $0 target_files_ubi.zip  machine_image/1.0-r0/rootfs ubi --system_path <path>"
    echo "example: $0 target_files_ext4.zip machine_image/1.0-r0/rootfs ext4 --system_path <path>"
    echo "example: $0 target_files_ext4.zip machine_image/1.0-r0/rootfs ext4  -p system/ -c fsconfig.conf --block --system_path <path>"
    echo "example: $0 target_files_ext4.zip machine_image/1.0-r0/rootfs ext4 --sign"
    exit 1
fi

export PATH=.:$PATH:/usr/bin
export OUT_HOST_ROOT=.

export LC_ALL=en_US.UTF-8
export LANG=en_US.UTF-8
export LANGUAGE=en_US.UTF-8
export FSCONFIGFOPTS=" "
block_based=" "
ubuntu=" "
python_version="python3"
system_path=" "
cache_location=" "
sign_ota_package=" "
mirror_sync=" "
install_only=" "
package_name=""
generate_package_name=false
vendor_code=""
ru_type=""
update_release=""
maintainance_release=""
build_type=""

if [ "$#" -gt 4 ]; then
    IFS=' ' read -a allopts <<< "$@"
    for i in $(seq 3 $#); do
       if [ "${allopts[${i}]}" = "--block" ]; then
           block_based="${allopts[${i}]}"
       elif [ "${allopts[${i}]}" = "--ubuntu" ]; then
           ubuntu="${allopts[${i}]}"
       elif [ "${allopts[${i}]}" = "--system_path" ]; then
           i=$((i+1))
           system_path="${allopts[${i}]}"
       elif [ "${allopts[${i}]}" = "--sign" ]; then
           sign_ota_package="${allopts[${i}]}"
       elif [ "${allopts[${i}]}" = "--mirror_sync" ]; then
           mirror_sync="${allopts[${i}]}"
       elif [ "${allopts[${i}]}" = "--install_only" ]; then
           install_only="${allopts[${i}]}"
       elif [ "${allopts[${i}]}" = "--package_name" ]; then
           generate_package_name=true
       elif [ "${allopts[${i}]}" = "--vendor_code" ]; then
           i=$((i+1))
           vendor_code="${allopts[${i}]}"
       elif [ "${allopts[${i}]}" = "--ru_type" ]; then
           i=$((i+1))
           ru_type="${allopts[${i}]}"
       elif [ "${allopts[${i}]}" = "--update_release" ]; then
           i=$((i+1))
           update_release="${allopts[${i}]}"
       elif [ "${allopts[${i}]}" = "--maintainance_release" ]; then
           i=$((i+1))
           maintainance_release="${allopts[${i}]}"
       elif [ "${allopts[${i}]}" = "--build_type" ]; then
           i=$((i+1))
           build_type="${allopts[${i}]}"
       else
           FSCONFIGFOPTS=$FSCONFIGFOPTS${allopts[${i}]}" "
       fi
    done
fi


if [ "${block_based}" = "--block" ]; then
    # python2 is needed for block based OTA.
    python_version="python2"
fi

no_of_target_files=$1

fs_type=$((no_of_target_files+3))
rootfs=$((no_of_target_files+2))
rootfs=${!rootfs}
fs_type=${!fs_type}

# Specify MMC or MTD type device and support MMC device with Squash file system. MTD by default
[[ $fs_type = "ext4" || $fs_type = "emmc_sqsh" ]] && device_type="MMC" || device_type="MTD"
all_target_files_paths=""
for n in $(seq 2 $((no_of_target_files+1)))
do
  target_file_path=${!n}
  echo "argument $n : $target_file_path"
  all_target_files_paths=$all_target_files_paths$target_file_path$" "

  #generate_package_name will not be true when this mplane_full_ota.sh is being
  #called from ab-ota-ext4.bbclass as we update the target-file-ext4.zip only one time.
  #when we call it from lint-script then we pass --package_name and
  #where we generate the full ota with all the images.
  if [ "$generate_package_name" = false ]; then
  # Setup temp folder to unzip target files
    target_files=target_files_full_ota_$fs_type
    rm -rf $target_files
    unzip -qo $target_file_path -d $target_files
    mkdir -p $target_files/META
    mkdir -p $target_files/SYSTEM
    mkdir -p $target_files/BOOT/RAMDISK
    touch $target_files/BOOT/RAMDISK/empty

    # Generate selabel rules only if file_contexts is packed in target-files
    if grep "selinux_fc" $target_files/META/misc_info.txt
    then
      zipinfo -1 $target_file_path |  awk 'BEGIN { FS="SYSTEM/" } /^SYSTEM\// {print "system/" $2}' | fs_config ${FSCONFIGFOPTS} -C -S $target_files/BOOT/RAMDISK/file_contexts -D $rootfs > $target_files/META/filesystem_config.txt
    else
      zipinfo -1 $target_file_path |  awk 'BEGIN { FS="SYSTEM/" } /^SYSTEM\// {print "system/" $2}' | fs_config ${FSCONFIGFOPTS} -D $rootfs > $target_files/META/filesystem_config.txt
    fi

    cd $target_files && zip -q $target_file_path META/*filesystem_config.txt SYSTEM/build.prop BOOT/RAMDISK/empty && cd ..
    rm -rf $target_files
  fi

done


if $generate_package_name; then
    package_name=$vendor_code$ru_type"ORU"$(date +%y%q)$update_release$maintainance_release$build_type$(date +%Y%m%d)".zip"
else
    package_name="update_$fs_type.zip"
fi

$python_version ./ota_from_target_files $block_based $mirror_sync $install_only $ubuntu -n -v -d $device_type -p . -m linux_embedded --no_signing --system_mount_path $system_path $1 $all_target_files_paths $package_name > ota_debug.txt 2>&1

if [[ $? = 0 ]]; then
    if [ "${sign_ota_package}" = "--sign" ]; then
        # Pipe the contents of OTA zip to openssl to generate the signature of the OTA zip
        unzip -p $package_name | openssl dgst -sha256 -sign private.pem -out update.sig
        if [[ $? = 0 ]]; then
            zip -q -u $package_name update.sig
            echo "OTA zip signing is successful"
        else
            echo "OTA zip signing is failed"
            rm $package_name # delete the half-baked ota zip if any;
        fi
    else
        echo "$package_name generation was successful"
    fi
else
    echo "$package_name generation failed"
    rm $package_name # delete the half-baked ota zip if any;
fi

