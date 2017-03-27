require conf/distro/include/security_flags.inc
require conf/machine/include/basemachine.inc

# For new yocto UNINATIVE_CHECKSUM
UNINATIVE_CHECKSUM[x86_64] = "26d46c61ad88cc245e31c88549717c0092a838d111b93ec169d88b08cc027581"

SOC_FAMILY ?= "msm8996"
include conf/machine/include/soc-family.inc

# Use non-perf settings for the perf build.
BASEMACHINE = "${@d.getVar('MACHINE', True).replace('-perf', '')}"
MACHINEGROUP = "auto"

TMPDIR = "${TOPDIR}/tmp-glibc"

require conf/machine/include/arm/arch-armv8.inc

SERIAL_CONSOLE ?= "115200 ttyHSL0"

DISTRO_FEATURES_BACKFILL_remove = "gobject-introspection-data"
DISTRO_FEATURES_BACKFILL_CONSIDERED_remove = "gobject-introspection-data"
DISTRO_FEATURES_append = " emmc-boot "
DISTRO_FEATURES_append = " persist-image "

PAGE_SIZE ?= '4096'

# Max supported inodes
#EXTRA_IMAGECMD ?= "-N 2048"

# Sizes for EXT4 (in bytes)
#SYSTEM_SIZE_EXT4 ?= "3000000000"
USERDATA_SIZE_EXT4 ?= "5000000000"
CACHE_SIZE_EXT4 ?= "65536000"

KERNEL_DEFCONFIG = "msm-auto_defconfig"
KERNEL_PERF_DEFCONFIG = "msm-auto-perf_defconfig"

# Sizes for EXT4 (in bytes)
USERDATA_SIZE_EXT4 = "2000000000"
CACHE_SIZE_EXT4 = "80000000"

#
# Multilib configuration
#
# This sets any packages preprended with lib32- to be built with
# the armv7a tuning (32 bit) instead of 64 bit aarch.
require conf/multilib.conf
MULTILIBS = "multilib:lib32"
DEFAULTTUNE_virtclass-multilib-lib32 = "armv7a"

EXTRA_IMAGE_FEATURES = "debug-tweaks"

#Krogoth default GCC version
GCCVERSION = "5.3.0"

IMAGE_FSTYPES += "tar.bz2 ext4"

# Graphics driver essentials
PREFERRED_PROVIDER_virtual/wayland-egl  = "adreno"
PREFERRED_PROVIDER_virtual/egl          = "adreno"
PREFERRED_PROVIDER_virtual/libgles1     = "adreno"
PREFERRED_PROVIDER_virtual/libgles2     = "adreno"

PREFERRED_VERSION_weston = "1.9.0"
PREFERRED_VERSION_wayland = "1.9.0"
PREFERRED_VERSION_wayland-native = "1.9.0"
PREFERRED_VERSION_wayland-ivi-extension = "1.9.1"
PREFERRED_VERSION_libdrm = "2.4.62"
PREFERRED_VERSION_gtest = "1.7.0"

# Prefer GStreamer 1.8.x by default
PREFERRED_VERSION_gstreamer1.0              ?= "1.8.%"
PREFERRED_VERSION_gstreamer1.0-plugins-bad  ?= "1.8.%"
PREFERRED_VERSION_gstreamer1.0-plugins-base ?= "1.8.%"
PREFERRED_VERSION_gstreamer1.0-plugins-good ?= "1.8.%"
PREFERRED_VERSION_gstreamer1.0-plugins-ugly ?= "1.8.%"
PREFERRED_VERSION_gstreamer1.0-libav        ?= "1.8.%"
PREFERRED_VERSION_gstreamer1.0-omx          ?= "1.2.%"

LICENSE_FLAGS_WHITELIST = "commercial_gstreamer1.0-libav commercial_gstreamer1.0-omx"
LICENSE_FLAGS_WHITELIST += "commercial_gstreamer1.0-plugins-ugly commercial_opencore-amr"
LICENSE_FLAGS_WHITELIST += "commercial_libomxil"

# Smack
OVERRIDES .= ":smack"
DISTRO_FEATURES_append = " smack"

BB_SIGNATURE_HANDLER = "OEBasicHash"
BB_HASHBASE_WHITELIST = "TMPDIR FILE PATH PWD BB_TASKHASH BBPATH DL_DIR \
    SSTATE_DIR THISDIR FILESEXTRAPATHS FILE_DIRNAME HOME LOGNAME SHELL TERM \
    USER FILESPATH STAGING_DIR_HOST STAGING_DIR_TARGET COREBASE PRSERV_HOST \
    PRSERV_DUMPDIR PRSERV_DUMPFILE PRSERV_LOCKDOWN PARALLEL_MAKE \
    CCACHE_DIR EXTERNAL_TOOLCHAIN CCACHE CCACHE_DISABLE LICENSE_PATH SDKPKGSUFFIX \
    WARN_QA ERROR_QA WORKDIR STAMPCLEAN PKGDATA_DIR BUILD_ARCH SSTATE_PKGARCH \
    BB_WORKERCONTEXT"

