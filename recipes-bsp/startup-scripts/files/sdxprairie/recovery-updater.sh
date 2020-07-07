#!/bin/sh
# Copyright (c) 2020, The Linux Foundation. All rights reserved.
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

# The workspace for recovery-update
cache_work_dir="/cache/recoveryupgrade"
mkdir -p $cache_work_dir

# Redirect stdout to "log" in /cache/recoveryupgrade
exec > $cache_work_dir/log

# Redirect stderr also to the same log file
exec 2>&1

set -o xtrace

date=`date`
self_pid=`echo $$`

INIT_LOG="\nrecovery-updater firing up
          \nDATE&TIME -  $date
          \nProcess ID - $self_pid \n"

echo -e $INIT_LOG

# Cookie to indicate whether to perform recovery-upgrade or not
cookie="${cache_work_dir}/RECOVERY_UPGRADE_DONE"

mtd_file=/proc/mtd

recovery_mtd_block_number=`cat $mtd_file | grep -i \"recovery\" | sed 's/^mtd//' | awk -F ':' '{print $1}'`
recovery_mtd_device=/dev/mtd${recovery_mtd_block_number}

recoveryfs_mtd_block_number=`cat $mtd_file | grep -i \"recoveryfs\" | sed 's/^mtd//' | awk -F ':' '{print $1}'`
recoveryfs_mtd_device=/dev/mtd${recoveryfs_mtd_block_number}

# Change this location to suit your requirement
update_zip_dir=/data/ota_package

# ==============================================================================
# Helper functions

# Writes RECOVERY_UPGRADE_DONE cookie
WriteRecoveryUpgradeDoneCookie() {
  echo -e "Writing RECOVERY_UPGRADE_DONE cookie ..."
  echo "RECOVERY_UPGRADE_DONE" > $cookie
  cat $cookie
}

# Takes as argument any "exit code" from another command
# and terminates; In case of an abnormal exit code, If the
# second argument is 1, the cookie is written before terminating.
CheckExitCodeAndTerminate() {
  if [ $1 != 0  ];
  then
    echo -e "\nObserved an abnormal exit code. Exiting ..."
    # For an abnormal exit code, provide the option of writing the cookie
    if [ $2 == 1 ];
    then
      WriteRecoveryUpgradeDoneCookie
    fi
    exit 1
  fi
}

CheckFileinZip() {
  zip=$1
  file=$2

  unzip -p $zip $file > /tmp/raw_image

  # sanity check on length of raw-image
  raw_image_size=`stat -c "%s" /tmp/raw_image`
  [ $raw_image_size -gt 0 ]
}

# Unzip 'file' file from 'zip' zipfile and write
# the contents to 'output_stream'.
# Terminates the program (without writing the cookie)
# if any of the operations within encountered errors.
UpgradeMTDWithRawImage() {
  mtd_device=$1
  zip=$2
  file=$3

  # Extract the file from zip to tmpfs (/tmp)
  unzip -p $zip $file > /tmp/raw_image
  CheckExitCodeAndTerminate $? 0

  flash_erase $mtd_device 0 0
  CheckExitCodeAndTerminate $? 0

  nandwrite -p $mtd_device /tmp/raw_image
  CheckExitCodeAndTerminate $? 0
}

# End of Helper functions
# ==============================================================================

# Important: We assume that the only zip file in
# $update_zip_dir has to be the upgrade package
zipfile=`ls ${update_zip_dir}/*.zip | awk '{ print $1 }'`

# As a sanity check, count the number of *.zip
# in the said location. If > 1, exit!
# If = 0, exit but write the cookie as well
count_of_zipfiles=`ls -1 ${update_zip_dir}/*.zip | wc -l`
if [ $count_of_zipfiles -gt 1 ];
then
  echo -e "\nMore than one zip file present. Aborting!"
  echo -e "\nFound - $zipfile"
  CheckExitCodeAndTerminate 1 0
elif [ $count_of_zipfiles -eq 0 ];
then
  echo -e "\nNo zip file present in $update_zip_dir. Aborting!"
  # exit but write the cookie
  CheckExitCodeAndTerminate 1 1
fi

# Don't bother with recovery(fs) upgrade
# if a cookie already exists in /cache/recoveryupgrade.
# When an OTA upgrade is triggered, recovery module
# will clear this cookie signalling us to do an explicit upgrade.
if [ -e $cookie ]; then
  echo -e "\nA cookie is present in /cache/recoveryupgrade."
  echo -e "We must have already done a recovery-upgrade recently. Nothing to do!"
  # Recreate the cookie, just in case
  WriteRecoveryUpgradeDoneCookie
else
  echo -e "\nDidn't find cookie in /cache/recoveryupgrade."
  echo -e "Proceeding with recovery(fs) upgrade.."
  echo -e "Will be using recovery(fs) images from $zipfile.."

  if CheckFileinZip $zipfile recoveryfs.ubi; then
    echo -e "\n Found recoveryfs.ubi in $zipfile"
  else
    echo -e "\nDidn't find recoveryfs.ubi in $zipfile"
    echo -e "Not updating both recovery & recoveryfs.."
    CheckExitCodeAndTerminate 1 1
  fi

  if CheckFileinZip $zipfile boot.img; then
    echo -e "\n Found boot.img in $zipfile"
  else
    echo -e "\nDidn't find boot.img in $zipfile"
    echo -e "Not updating both recovery & recoveryfs.."
    CheckExitCodeAndTerminate 1 1
  fi

  UpgradeMTDWithRawImage $recoveryfs_mtd_device $zipfile recoveryfs.ubi
  UpgradeMTDWithRawImage $recovery_mtd_device $zipfile boot.img

  echo -e "\nRecovery(fs) upgrade completed!"
  WriteRecoveryUpgradeDoneCookie
fi

exit 0

