include conf/machine/include/basemachine.inc

SOC_FAMILY ?= "sa-common"
include conf/machine/include/soc-family.inc

PREFERRED_PROVIDER_virtual/kernel = "linux-msm"
PREFERRED_PROVIDER_virtual/kernel-headers = "linux-msm-headers"
PREFERRED_VERSION_linux-msm ?= "5.4"
PREFERRED_VERSION_linux-msm-headers ?= "5.4"
preferred-kernel = "PREFERRED_VERSION_linux-msm"

PREFERRED_VERSION_edk2 ?= "3.0"

# Prefer camx
PREFERRED_VERSION_camx ?= "0.1"
PREFERRED_VERSION_chi-cdk ?= "0.1"
PREFERRED_VERSION_cameradlkm ?= "0.1"

# Prefer gbm
PREFERRED_PROVIDER_virtual/libgbm = "gbm"

# Prefer adreno
PREFERRED_PROVIDER_virtual/wayland-egl  = "adreno"
PREFERRED_PROVIDER_virtual/egl          = "adreno"
PREFERRED_PROVIDER_virtual/libgles1     = "adreno"
PREFERRED_PROVIDER_virtual/libgles2     = "adreno"

# mesa provides these packags and others like virtual/libgl.
# This is to enable mesa, adreno and gbm work together, and pass
# yocot-check-layer script check.
BB_MULTI_PROVIDER_ALLOWED += "virtual/wayland-egl virtual/egl virtual/libgles1 virtual/libgles2 virtual/libgbm"

SERIAL_CONSOLES = "115200;ttyMSM0"

IMAGE_FSTYPES ?= "ext4"
PAGE_SIZE ?= '4096'
IMAGE_ROOTFS_ALIGNMENT = "4096"
EXTRA_IMAGECMD:ext4 += " -b 4096 "

IMAGE_CLASSES += "qimage"

IMAGE_CLASSES += "${@oe.utils.version_less_or_equal('PREFERRED_VERSION_linux-msm', '5.14', '', 'qimage-boot', d)}"
