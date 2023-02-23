include conf/machine/include/basemachine.inc

SOC_FAMILY ?= "sa-common"
include conf/machine/include/soc-family.inc
PRODUCT = "${MACHINE}"
PREFERRED_PROVIDER_virtual/kernel = "linux-ark"
PREFERRED_PROVIDER_virtual/kernel-headers = "linux-ark-headers"
PREFERRED_VERSION_linux-ark = "5.14"

# Prefer Wayland/Weston version
PREFERRED_VERSION_weston = "8.0.0"
PREFERRED_VERSION_wayland = "1.18.0"
PREFERRED_VERSION_wayland-native = "1.18.0"
PREFERRED_VERSION_wayland-protocols = "1.20"
PREFERRED_VERSION_wayland-ivi-extension = "2.2.0"
preferred-kernel = "${@oe.utils.ifelse(d.getVar('PREFERRED_PROVIDER_virtual/kernel') == 'linux-ark', 'PREFERRED_VERSION_linux-ark', 'PREFERRED_VERSION_linux-msm')}"
kernel-version = "${@oe.utils.ifelse(d.getVar('PREFERRED_PROVIDER_virtual/kernel') == 'linux-ark', '5.14', '5.4')}"
# Prefer gbm
PREFERRED_PROVIDER_virtual/libgbm = "gbm"

# Prefer adreno
PREFERRED_PROVIDER_virtual/wayland-egl  = "adreno"
PREFERRED_PROVIDER_virtual/egl          = "adreno"
PREFERRED_PROVIDER_virtual/libgles1     = "adreno"
PREFERRED_PROVIDER_virtual/libgles2     = "adreno"

BBMASK += "meta-qti-bsp/meta-qti-base/recipes-wlan"
BBMASK += "meta-qti-bsp/meta-qti-base/recipes-kernel/edk2/edk2_4.0.bb"

# mesa provides these packags and others like virtual/libgl.
# This is to enable mesa, adreno and gbm work together, and pass
# yocot-check-layer script check.
MULTI_PROVIDER_WHITELIST += "virtual/wayland-egl virtual/egl virtual/libgles1 virtual/libgles2 virtual/libgbm"

SERIAL_CONSOLE ?= "${@['', '115200 ttyMSM0'][d.getVar('VARIANT', True) == ('' or 'debug')]}"
SERIAL_CONSOLES = "115200;ttyMSM0"

IMAGE_FSTYPES ?= "ext4"
PAGE_SIZE ?= '4096'
IMAGE_ROOTFS_ALIGNMENT = "4096"
EXTRA_IMAGECMD_ext4 += " -b 4096 "

IMAGE_CLASSES += "qimage"

