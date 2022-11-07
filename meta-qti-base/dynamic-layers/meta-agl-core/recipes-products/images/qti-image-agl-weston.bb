SUMMARY = "A very basic Wayland image with a terminal"
LICENSE = "BSD-3-Clause"
# NOTE:
#   qti-image-{minimal,weston} use is avoided here to avoid mixing
#   core-image-minimal / packagegroup-core-boot with the AGL
#   replacements for those.  For packagegroup-core-boot, additional
#   bbappends are required to packagegroup-qti-core-boot and the
#   AGL packagegroup-agl-core-boot to get an image that matches AGL
#   behavior while still including QTI additions.
#
require recipes-platform/images/agl-image-weston.bb
require recipes-products/images/qti-image-weston-prop.bb

# Pull in to reuse the COMBINED_FEATURES logic for packagegroups
# from machine-image
require recipes-products/images/automotive-image.inc

# Remove splash feature added by agl-image-weston, as psplash does
# not work
IMAGE_FEATURES:remove = "splash"

# Switch to OpenSSH instead of Dropbear to support QtCreator use
# for demo development
IMAGE_FEATURES:remove = "ssh-server-dropbear"
IMAGE_FEATURES += "ssh-server-openssh"

# enable sparse image to reduce the image size and faster the flash speed.
IMAGE_FEATURES += "sparse-image"

# Disable root password for demo development
EXTRA_IMAGE_FEATURES += "debug-tweaks"

IMAGE_INSTALL += "\
    packagegroup-qti-agl-demo-tools \
    "

#
# Add hook to honor debug-tweaks/empty-root-password, since otherwise
# the password patched into base-passwd will be present.
#

ROOTFS_POSTPROCESS_COMMAND += "${@bb.utils.contains_any("IMAGE_FEATURES", [ 'debug-tweaks', 'empty-root-password' ], "clear_root_password ; ", "",d)}"

clear_root_password () {
        if [ -e ${IMAGE_ROOTFS}/etc/shadow ]; then
                sed -i 's%^root:[^:]\+:%root::%' ${IMAGE_ROOTFS}/etc/shadow
        fi
}

#
# Add hook to enable internal SFTP server in OpenSSH.
# NOTE: Done here instead of a openssh bbappend to avoid affecting
#       other potential openssh package users.
#

ROOTFS_POSTPROCESS_COMMAND += "${@bb.utils.contains("IMAGE_FEATURES", "ssh-server-openssh", "openssh_enable_internal_sftp; ", "",d)}"

openssh_enable_internal_sftp () {
        for f in sshd_config sshd_config_readonly; do
                if [ -e ${IMAGE_ROOTFS}/etc/ssh/sshd_config ]; then
                        sed -i 's%^Subsystem[ \t]\+sftp[ \t]\+/usr/libexec/sftp-server%Subsystem\tsftp\tinternal-sftp%' ${IMAGE_ROOTFS}/etc/ssh/$f
                fi
        done
}

# Introducing selinux-image.bbclass is to label selinux contexts when build.
inherit ${@bb.utils.contains('DISTRO_FEATURES', 'selinux', 'selinux-image', '', d)}

IMAGE_INSTALL += "\
    ${@bb.utils.contains('DISTRO_FEATURES', 'selinux', 'packagegroup-selinux-minimal packagegroup-selinux-policycoreutils checkpolicy secilc auditd', '', d)} \
"

# Add the libgomp support.
IMAGE_INSTALL += "libgomp"
