
include conf/machine/include/basemachine.inc

SOC_FAMILY ?= ""
include conf/machine/include/soc-family.inc

include conf/machine/include/arm/arch-arm64.inc

PREFERRED_PROVIDER_virtual/kernel = "linux-msm"
PREFERRED_VERSION_linux-msm = "4.14"

# Prefer Wayland/Weston version
PREFERRED_VERSION_weston = "5.0.0"
PREFERRED_VERSION_wayland = "1.18.0"
PREFERRED_VERSION_wayland-native = "1.18.0"
PREFERRED_VERSION_wayland-protocols = "1.20"
PREFERRED_VERSION_wayland-ivi-extension = "2.2.0"

# Prefer GStreamer 1.16.2 by default
PREFERRED_VERSION_gstreamer1.0              ?= "1.16.2"
PREFERRED_VERSION_gstreamer1.0-plugins-bad  ?= "1.16.2"
PREFERRED_VERSION_gstreamer1.0-plugins-base ?= "1.16.2"
PREFERRED_VERSION_gstreamer1.0-plugins-good ?= "1.16.2"
PREFERRED_VERSION_gstreamer1.0-plugins-ugly ?= "1.16.2"
PREFERRED_VERSION_gstreamer1.0-libav        ?= "1.16.2"
PREFERRED_VERSION_gstreamer1.0-omx          ?= "1.16.2"

# Prefer gbm
PREFERRED_PROVIDER_virtual/libgbm = "gbm"

# Prefer adreno
PREFERRED_PROVIDER_virtual/wayland-egl  = "adreno"
PREFERRED_PROVIDER_virtual/egl          = "adreno"
PREFERRED_PROVIDER_virtual/libgles1     = "adreno"
PREFERRED_PROVIDER_virtual/libgles2     = "adreno"

MULTI_PROVIDER_WHITELIST += "virtual/wayland-egl virtual/egl virtual/libgles1 virtual/libgles2 virtual/libgbm"

SERIAL_CONSOLE ?= "${@['', '115200 ttyMSM0'][d.getVar('VARIANT', True) == ('' or 'debug')]}"
SERIAL_CONSOLES = "115200;ttyMSM0"

PAGE_SIZE ?= '4096'
IMAGE_ROOTFS_ALIGNMENT = "4096"
EXTRA_IMAGECMD_ext4 += " -b 4096 "

IMAGE_CLASSES += "qimage"

# Temporary disable these paths for yocto2.6 upgrade.
WARN_TO_ERROR_QA_remove += "unknown-configure-option"

GCCVERSION = "9.%"
