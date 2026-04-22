#!/bin/sh
# Copyright (c) 2018, The Linux Foundation. All rights reserved.
#
# Redistribution and use in source and binary forms, with or without
# modification, are permitted provided that the following conditions are
# met:
#   * Redistributions of source code must retain the above copyright
#     notice, this list of conditions and the following disclaimer.
#   * Redistributions in binary form must reproduce the above
#     copyright notice, this list of conditions and the following
#     disclaimer in the documentation and/or other materials provided
#     with the distribution.
#   * Neither the name of The Linux Foundation nor the names of its
#     contributors may be used to endorse or promote products derived
#     from this software without specific prior written permission.
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

log() {
    echo "$1" > /dev/kmsg
}


FindAndMountUBI () {
   partition=$1
   dir=$2
   extra_opts=$3

   mtd_block_number=$(cat $mtd_file | grep -i $partition | sed 's/^mtd//' | awk -F ':' '{print $1}')
   echo "MTD : Detected block device : $dir for $partition"
   mkdir -p $dir

   ubiattach_output=$(ubiattach -m "$mtd_block_number" -d 1 /dev/ubi_ctrl 2>&1)
   ubiattach_ret=$?

   if [ $ubiattach_ret -ne 0 ]; then
      log "ubiattach failed for mtd_block_number=$mtd_block_number with exit code $ubiattach_ret"
      echo "$ubiattach_output" | while read -r line; do log "ubiattach error: $line"; done
   fi

   device=/dev/ubi1_0

   MAX_ATTEMPTS=20
   ATTEMPT=0

   while [ $ATTEMPT -lt $MAX_ATTEMPTS ];
    do
        if [ -c $device ]; then

            if test -x /sbin/restorecon; then
                restorecon_output=$(/sbin/restorecon "$device" 2>&1)
                restorecon_ret=$?

                if [ $restorecon_ret -ne 0 ]; then
                    log "restorecon failed with exit code $restorecon_ret"
                    echo "$restorecon_output" | while read -r line; do log "restorecon error: $line"; done
                fi
            else
                log "restorecon not found or not executable"
            fi

            # Retry logic for mount
            COUNT=0
            MAX_MOUNT_RETRIES=5
            RETRY_DELAY=0.5

            while [ $COUNT -lt $MAX_MOUNT_RETRIES ]; do
                mount_output=$(mount -t ubifs "$device" "$dir" -o bulk_read$extra_opts 2>&1)
                mount_ret=$?

                if [ $mount_ret -eq 0 ]; then
                    break 2  # Exit both retry and outer loop
                else
                    if grep -s " /firmware " /proc/self/mounts; then
                        log "$device already mounted on $dir"
                        break 2
                    fi

                    log "Mount of $device failed with exit code $mount_ret"
                    echo "$mount_output" | while read -r line; do log "mount error: $line"; done

                    COUNT=$((COUNT + 1))

                    if [ $COUNT -lt $MAX_MOUNT_RETRIES ]; then
                        log "Retrying in $RETRY_DELAY seconds..."
                        sleep $RETRY_DELAY
                    fi
                fi
            done

            # If all retries failed
            if [ $COUNT -eq $MAX_MOUNT_RETRIES ]; then
              log "Mount of $device failed after $MAX_MOUNT_RETRIES attempts"
              exit $mount_ret
            fi
        else
            log "$device not found, sleeping for 10ms"
            sleep 0.010
        fi
        ATTEMPT=$((ATTEMPT + 1))
   done

   if [ $ATTEMPT -ge $MAX_ATTEMPTS ]; then
     log "Exceeded maximum attempts ($MAX_ATTEMPTS) to find device $device"
     exit 1
   fi
}

mtd_file=/proc/mtd
if [ -x /sbin/restorecon ]; then
    firmware_selinux_opt=",context=system_u:object_r:firmware_t:s0"
else
    firmware_selinux_opt=""
fi
eval FindAndMountUBI modem${SLOT_SUFFIX} /firmware $firmware_selinux_opt

exit 0
