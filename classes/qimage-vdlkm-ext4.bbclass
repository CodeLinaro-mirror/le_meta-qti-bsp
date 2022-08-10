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
do_makevdlkm() {
    make_ext4fs -B ${IMGDEPLOYDIR}/${IMAGE_BASENAME}/${VDLKMIMAGE_MAP_TARGET} \
                -s -l ${VDLKM_IMAGE_ROOTFS_SIZE} \
                ${IMGDEPLOYDIR}/${IMAGE_BASENAME}/${VDLKMIMAGE_TARGET} \
                ${IMAGE_ROOTFS}/lib/modules
}
# It must be before do_makesystem to remove /lib/modules
addtask do_makevdlkm after do_image before do_makesystem
