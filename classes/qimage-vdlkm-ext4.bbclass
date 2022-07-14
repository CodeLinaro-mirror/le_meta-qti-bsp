# Copyright (c) 2022 Qualcomm Innovation Center, Inc. All rights reserved.
#
# Redistribution and use in source and binary forms, with or without
# modification, are permitted (subject to the limitations in the
# disclaimer below) provided that the following conditions are met:
#
#    * Redistributions of source code must retain the above copyright
#      notice, this list of conditions and the following disclaimer.
#
#    * Redistributions in binary form must reproduce the above
#      copyright notice, this list of conditions and the following
#      disclaimer in the documentation and/or other materials provided
#       with the distribution.
#
#    * Neither the name of Qualcomm Innovation Center, Inc. nor the names of its
#      contributors may be used to endorse or promote products derived
#           from this software without specific prior written permission.
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

## Generate vendor_dlkm image

# Convert human readable partition sizes into bytes
VDLKM_IMAGE_ROOTFS_SIZE = "${@get_size_in_bytes(d.getVar('VDLKM_SIZE_EXT4') or '32MB')}"

VDLKMIMAGE_TARGET ?= "vendor_dlkm.img"
VDLKMIMAGE_MAP_TARGET ?= "vendor_dlkm.map"

# Copy systemd modules-load.d configs from /etc to /lib/modules
# so that both modules and configs will be part of vdlkm image.
create_vdlkm_modules_load_d() {
    install -d ${IMAGE_ROOTFS}/lib/modules/modules-load.d/
    for conf in ${IMAGE_ROOTFS}${sysconfdir}/modules-load.d/*.conf; do
        install -D -m 0644 $conf ${IMAGE_ROOTFS}/lib/modules/modules-load.d/
        rm -f $conf
    done
}
ROOTFS_POSTPROCESS_COMMAND += "create_vdlkm_modules_load_d;"

do_makevdlkm[dirs] = "${IMGDEPLOYDIR}/${IMAGE_BASENAME}"

python do_makevdlkm() {
    import math
    import os
    import subprocess
    vdlkmimage_map_target = d.getVar('IMGDEPLOYDIR') + '/' + d.getVar('IMAGE_BASENAME') + '/' + d.getVar('VDLKMIMAGE_MAP_TARGET')
    vdlkmimage_target =  d.getVar('IMGDEPLOYDIR') + '/' + d.getVar('IMAGE_BASENAME') + '/' + d.getVar('VDLKMIMAGE_TARGET')
    vdlkmimage_files = d.getVar('IMAGE_ROOTFS') + '/lib/modules'
    vdlkm_image_rootfs_size = d.getVar('VDLKM_IMAGE_ROOTFS_SIZE')

    #Compute the least required size to generate unsparsed vdlkm image using make_ext4fs tool
    vdlkm_image_rootfs_size_actual = 0
    for dirpath, dirnames, filenames in os.walk(vdlkmimage_files):
        for f in filenames:
            fp = os.path.join(dirpath, f)
            if not os.path.islink(fp):
                vdlkm_image_rootfs_size_actual += os.path.getsize(fp)
    vdlkm_image_rootfs_size_limit_percentage = math.ceil(vdlkm_image_rootfs_size_actual / int(vdlkm_image_rootfs_size) * 100)

    while vdlkm_image_rootfs_size_limit_percentage < 100:
        try:
            vdlkm_image_rootfs_size_effective = math.ceil(int(vdlkm_image_rootfs_size) * vdlkm_image_rootfs_size_limit_percentage / 100)
            cmd = 'make_ext4fs -B ' + vdlkmimage_map_target + ' -l ' + str(vdlkm_image_rootfs_size_effective) + ' ' + vdlkmimage_target + ' '+ vdlkmimage_files
            subprocess.check_output(cmd, shell=True)
        except:
            vdlkm_image_rootfs_size_limit_percentage += 1
            continue
        else:
            break

    vdlkm_image_rootfs_size_effective = math.ceil(int(vdlkm_image_rootfs_size) * vdlkm_image_rootfs_size_limit_percentage / 100)

    # Align the effective size of unsparsed vdlkm image to block size
    block_size = 4096
    if (vdlkm_image_rootfs_size_effective % block_size) != 0:
        vdlkm_image_rootfs_size_effective +=  block_size - (vdlkm_image_rootfs_size_effective % block_size)

    # Generate unsparsed vdlkm image
    cmd = 'make_ext4fs -B ' + vdlkmimage_map_target + ' -l ' + str(vdlkm_image_rootfs_size_effective) + ' ' + vdlkmimage_target + ' '+ vdlkmimage_files
    subprocess.check_output(cmd, shell=True)
    bb.note("Unsparsed vdlkm image size: " + str(vdlkm_image_rootfs_size_effective))
}

# It must be before do_makesystem to remove /lib/modules
addtask do_makevdlkm after do_image before do_makesystem
