SUMMARY = "A small ext4 image to be packed into system"
LICENSE = "BSD-3-Clause-Clear"

inherit image

IMAGE_LINGUAS = ""
DEPENDS += "sectool5-native"

EXTRA_IMAGECMD:ext4 = "-i 4096 -b 4096"

# default value for rootfs size
IMAGE_ROOTFS_SIZE ?= "266240"

# Add libgomp header
TOOLCHAIN_TARGET_TASK:append = " libgomp-dev libgomp-staticdev"

DEPENDS += "ext4-utils-native"

IMAGE_INSTALL = "\
	packagegroup-qti-agl-demo-tools \
	packagegroup-qti-aosp \
        system-prop \
        systemd \
        busybox \
        weston-examples \
        wayland-ivi-extension \
        alsa-lib \
        alsa-utils \
        packagegroup-qti-audio \
        packagegroup-qti-multimedia \
        libselinux \
        libselinux-bin \
        libxkbcommon \
        xkeyboard-config \
        refpolicy-mcs \
"

IMAGE_FSTYPES = "ext4"
