# Provides packages required to build
# QTI Generic Linux image.

inherit qimage populate_sdk_ext

IMAGE_FEATURES += "ssh-server-openssh"

# This image doesn't support abl generation
EXTRA_IMAGEDEPENDS:remove = "edk2"

KERN_MODS ?= "kernel-modules"
KERN_MODS:waipio = "gki-kernel-modules-second-stage"

CORE_IMAGE_EXTRA_INSTALL += "\
        glib-2.0 \
        ${KERN_MODS} \
        packagegroup-android-utils-base \
        packagegroup-filesystem-utils-base \
        packagegroup-startup-scripts-base \
        systemd-machine-units \
        ${@bb.utils.contains('DISTRO_FEATURES','selinux', 'packagegroup-selinux-minimal', '', d)} \
"
# Install display packages
CORE_IMAGE_EXTRA_INSTALL += " \
            libdrm \
            wayland \
            "

# Force default usb composition to 4EE7 as this image can't
# support diag, which is expected by all other compositions.
USBCOMPOSITION:forcevariable = "4EE7"

python copy_buildsystem:append() {
    # Create src directory in extensible SDK to copy the project sources
    bb.utils.mkdirhier(baseoutpath + '/src')
    # Enable the use of WORKSPACE variable on an extensible SDK
    with open(baseoutpath + '/conf/bblayers.conf', 'a') as f:
        f.write('WORKSPACE = "$' + '{TOPDIR}/src"\n')
}
