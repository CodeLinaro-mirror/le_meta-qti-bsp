BASEMACHINE ?= "mdm9650"
require conf/machine/include/basemachine.inc

SOC_FAMILY ?= "mdm9x50"
include conf/machine/include/soc-family.inc

# Pull in security flags
require ${COREBASE}/meta-qti-bsp-prop/conf/distro/include/security_flags.inc

# Enable floating-point ABI to Hard-float
DEFAULTTUNE = "cortexa8hf-neon"

# Defines floating-point ABI value suitable to pass to gcc compile flag
# permissable values:hard & softfp.hard allows generation of floating-point
# instructions and uses FPU-specific calling conventions.softfp allows the
# generation of code using hardware floating-point instructions, but still
# uses the soft-float calling conventions. if below line is commented out
# the build defaults to use softfp floating-point ABI.
ARM_FLOAT_ABI = "hard"

include conf/machine/include/tune-cortexa8.inc

PREFERRED_PROVIDER_virtual/kernel = "linux-msm"
PREFERRED_VERSION_linux-msm = "5.4"
PREFERRED_VERSION_linux-msm-headers = "5.4"
PREFERRED_VERSION_powermanager-daemon = "1.0"

SERIAL_CONSOLES ?= "115200;ttyMSM0"
KERNEL_IMAGETYPE = "zImage"
KERNEL_BASE = "0x80000000"
KERNEL_TAGS_OFFSET = "0x82000000"

KERNEL_DYNAMIC_DEFCONFIG = "True"

KERNEL_CONFIG ?= "vendor/mdm9650_auto_defconfig"
KERNEL_CONFIG_qti-distro-debug ?= "vendor/mdm9650_auto-debug_defconfig"

KERNEL_CMD_PARAMS = "noinitrd ro rootwait earlycon=msm_serial_dm,0x78b1000 console=ttyMSM0,115200,n8 androidboot.hardware=qcom androidboot.console=ttyMSM0 lpm_levels.sleep_disabled=1 service_locator.enable=1 firmware_class.path=/lib/firmware/updates msm_rtb.filter=0x237"

KERNEL_DTB_NAMES    = "\
                        vendor/qcom/mdm9650-ttp.dtb \
                        vendor/qcom/mdm9650-mtp.dtb \
"

# Bootloader configuration
EXTRA_IMAGEDEPENDS += "lk"

# MACHINE_FEATURES ##
# qti-security:           Machine supports QTI security solution
# qti-audio:              Machine supports QTI audio solution
# qti-location:           Machine supports Location solution
# qti-data-modem:         Machine supports QTI data modem solution
# nand-boot:              Machine supports boot from NAND flash

MACHINE_FEATURES += "qti-sdx qti-recovery dm-verity-none nand-boot qwesd qti-security no-rpmb-listener-support no-ssd-listener-support qti-data-modem qti-audio"

MACHINE_MNT_POINTS = " \
    /firmware \
    /persist \
    /cache \
    /data \
    /systemrw \
"

# Enable SDK feature
SDK_IMAGE ?= "1"

# Formats of root filesystem images.
IMAGE_FSTYPES += "ubi"

MACHINE_SUPPORTS_PDMAPPER = "False"
MACHINE_SUPPORTS_INIT_RAMDISK = "False"
MACHINE_SUPPORTS_ANDROID_PROPERTIES = "False"

# Disable autosuspend for USB peripheral mode
MACHINE_SUPPORTS_USB_AUTOSUSPEND = "False"

# File indicating firmware residing places on device.
MACHINE_FILESMAP_CONF = "mdm9650-filesmap"

USBCOMPOSITION = "901D"

# On msm-5.4 or above ufs driver supports BSG Device.
UFS_BSG_DEV_USAGE = "True"

PREFERRED_VERSION_protobuf = "3.11.4"
PREFERRED_VERSION_protobuf-native = "3.11.4"
PREFERRED_VERSION_python3-protobuf = "3.11.3"
PREFERRED_VERSION_python3-protobuf-native = "3.11.3"
PREFERRED_VERSION_protobuf-c = "1.3.3"
PREFERRED_VERSION_iproute2 = "5.5.0"

# y2038: Support for Y2038 safe time on 32 bit systems
TARGET_CFLAGS += "-D_TIME_BITS=64 -D_FILE_OFFSET_BITS=64"
TARGET_CPPFLAGS += "-D_TIME_BITS=64 -D_FILE_OFFSET_BITS=64"

DISTRO_FEATURES_remove = "qti-telux"

DEFAULT_ENFORCING = "permissive"
