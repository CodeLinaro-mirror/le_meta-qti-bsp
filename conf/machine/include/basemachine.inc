BASEMACHINE ??= ""

# Add BASEMACHINE to machine overrides
MACHINEOVERRIDES =. "${@['', '${BASEMACHINE}:']['${BASEMACHINE}' != '']}"

# Define BASEMACHINE_ARCH
BASEMACHINE_ARCH = "${@['${MACHINE_ARCH}', '${BASEMACHINE}']['${BASEMACHINE}' != '']}"

# Add BASEMACHINE_ARCH to PACKAGE_ARCHS. 
# This variable is originally set in bitbake.conf
PACKAGE_ARCHS .= "${@['', ' ${BASEMACHINE_ARCH}']['${BASEMACHINE_ARCH}' != '${MACHINE_ARCH}']}"
PACKAGE_ARCHS[vardepsexclude] += "BASEMACHINE_ARCH"

# Default Image names
BOOTIMAGE_TARGET ?= "${MACHINE}-boot.img"
CACHEIMAGE_TARGET ?= "${MACHINE}-cache.ext4"
PERSISTIMAGE_TARGET ?= "${MACHINE}-persist.ext4"
RECOVERYIMAGE_TARGET ?= "${MACHINE}-recovery.ext4"
SYSTEMIMAGE_TARGET ?= "${MACHINE}-sysfs.ext4"
SYSTEMRWIMAGE_TARGET ?= "${MACHINE}-systemrw.ext4"
USERDATAIMAGE_TARGET ?= "${MACHINE}-usrfs.ext4"
