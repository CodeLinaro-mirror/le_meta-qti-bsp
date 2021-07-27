require recipes-platform/images/agl-demo-platform.inc
require recipes-products/images/qti-image-weston-prop.bb

# Pull in to reuse the COMBINED_FEATURES logic for packagegroups
# from machine-image
require recipes-products/images/automotive-image.inc

# Remove splash feature added by agl-image-ivi.inc, as psplash does
# not work
IMAGE_FEATURES_remove = "splash"

# screen is GPLv3, remove
IMAGE_INSTALL_remove = "screen"

#
# Add hook to honor debug-tweaks/empty-root-password, since otherwise
# the password patched into base-passwd will be present.
#

ROOTFS_POSTPROCESS_COMMAND += '${@bb.utils.contains_any("IMAGE_FEATURES", [ 'debug-tweaks', 'empty-root-password' ], "clear_root_password ; ", "",d)}'

clear_root_password () {
        if [ -e ${IMAGE_ROOTFS}/etc/shadow ]; then
                sed -i 's%^root:[^:]\+:%root::%' ${IMAGE_ROOTFS}/etc/shadow
        fi
}

