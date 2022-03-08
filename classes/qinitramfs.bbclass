# Copyright (c) 2022 Qualcomm Innovation Center, Inc. All rights reserved.
#
# Redistribution and use in source and binary forms, with or without
# modification, are permitted (subject to the limitations in the
# disclaimer below) provided that the following conditions are met:
#
#     * Redistributions of source code must retain the above copyright
#       notice, this list of conditions and the following disclaimer.
#
#     * Redistributions in binary form must reproduce the above
#       copyright notice, this list of conditions and the following
#       disclaimer in the documentation and/or other materials provided
#       with the distribution.
#
#     * Neither the name of Qualcomm Innovation Center, Inc. nor the names of its
#       contributors may be used to endorse or promote products derived
#       from this software without specific prior written permission.
#
# NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE
# GRANTED BY THIS LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT
# HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED
# WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
# MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
# IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR
# ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
# DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE
# GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
# INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER
# IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR
# OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN
# IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.


# This scripts is used to filter out not necessary packages
# from initramfs root file system.

set_devices () {
  [ -e "dev/null" ]    || mknod dev/null c 1 3
  [ -e "dev/zero" ]    || mknod dev/zero c 1 5
  [ -e "dev/urandom" ] || mknod dev/urandom c 1 9
  [ -e "dev/console" ] || mknod dev/console c 5 1
}

add_folder () {
  if ! [ -e $1 ]; then
    mkdir -p $1
  fi
}

remove_file () {
  if ! [ -d $1 ]; then
      rm -rf $1
  fi
}

remove_all () {
    rm -rf "$1"
}

configure_final_image () {
  cd ${IMAGE_ROOTFS}

  # To speed up the build, delete the known path directory
  known_list="
            ./lib/udev
            ./lib/systemd
            ./etc/udev
            ./usr/lib/opkg
            ./usr/share
            ./usr/bin/update-alternatives
            ./usr/libexec/udevadm
            ./bin/udevadm
            ./sbin/ldconfig
            ./sbin/udevd
            "

  for file in ${known_list}; do
      remove_all "${file}"
  done

  for file in $(find); do
    if echo "$file" | grep -E "/bin|usr/lib|./sbin" > /dev/null; then
      continue
    elif echo "$file" | grep "./usr/sbin" > /dev/null; then
      case $file in
        */cryptsetup) ;;
        */dmsetup) ;;
        */ubi*) ;;
        *) remove_file $file
          continue ;;
      esac
    elif echo "$file" | grep "./lib" > /dev/null; then
      case $file in
        */ld-*) ;;
        */libc*) ;;
        */libdl*) ;;
        */librt*) ;;
        */libpthread*) ;;
        */libuuid*) ;;
        */libdev*) ;;
        */libudev*) ;;
        */libcypt*) ;;
        */libz.so*) ;;
        */libm*) ;;
        */libresolv*) ;;
        */libblkid*) ;;
        */libmount*) ;;
        */libpcre*) ;;
        */libselinux*) ;;
        */libssl*) ;;
        *) remove_file $file
          continue ;;
      esac
    else
      case $file in
        ./init) ;;
        ./dev/console) ;;
        ./dev/tty*) ;;
        ./dev/null) ;;
        ./dev/urandom) ;;
        ./dev/zero) ;;
        ./etc/keys*) ;;
        *) remove_file $file
          continue ;;
      esac
    fi
  done

  # To make the boot up faster here prepare the device instead of
  # create them during boot up
  for folder in dev mnt proc run sys tmp var; do
    add_folder "${folder}"
  done

  set_devices
}

